package com.hit.service.impl;

import com.hit.mapper.CommentMapper;
import com.hit.pojo.Result;
import com.hit.service.SpiderService;
import com.hit.vo.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpiderServiceImpl implements SpiderService {

    private static final String URL = "https://bbs.hupu.com";//虎扑论坛基本url

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Result getHotTitle() {//获取热榜标题
        try {
            Document document = Jsoup.connect(URL)
                    .ignoreContentType(true)
                    .get();//使用Jsoup库连接到虎扑热帖榜主页，获取网页内容并解析为一个Document对象
            Elements list = document.getElementsByClass("list-title");//获取热帖榜六大主板块信息
            return Result.success(getItemList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("热帖标题获取失败");
    }

    @Override
    public Comment getComments(String hupuId) {
        Comment comment = new Comment();
        comment.setHupuId(hupuId);
        long nowMills = System.currentTimeMillis();
        List<Review> storedCommentList = commentMapper.getComments(hupuId, nowMills);
        if (storedCommentList.size() == 0) {
            List<Review> commentList = new ArrayList<>();
            int count = 0;
            int pageId = 1;
            int lastPage = 1;
            try {
                Document document = Jsoup.connect("https://bbs.hupu.com/" + hupuId + ".html")
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                        .get();
                Elements paginationItems = document.select(".hupu-rc-pagination-item a");
                String lastPageStr = null;
                for (Element paginationItem : paginationItems) {
                    lastPageStr = paginationItem.text();
                }
                lastPage = Integer.parseInt(lastPageStr);
                Map<String, String> topicIdList = new HashMap<>();
                topicIdList.put("/cba", "173");
                topicIdList.put("/topic-daily", "1");
                Elements topicLinks = document.select(".post-user_post-user-comp-info-bottom-from__6aulb a.post-user_post-user-comp-info-bottom-link__BMF8U");
                String topicStr = null;
                for (Element topicLink : topicLinks) {
                    topicStr = topicLink.attr("href");
                }
                if (topicIdList.get(topicStr) != null) {
                    comment.setTopicId(topicIdList.get(topicStr));
                } else {
                    comment.setTopicId("1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (count < 50 && pageId <= lastPage) {
                try {
                    Document document = Jsoup.connect("https://bbs.hupu.com/" + hupuId + "-" + pageId + ".html")
                            .ignoreContentType(true)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                            .get();
//                Elements comments = document.select(".post-wrapper_gray__HNv4A .post-reply-list-content .thread-content-detail p");
                    Elements comments = document.select(".post-wrapper_gray__HNv4A .post-reply-list");
                    for (Element commentElement : comments) {
                        String commentId = commentElement.select("span").attr("id");
                        Elements commentDetails = commentElement.select(".post-reply-list-content .thread-content-detail p");
                        if (count >= 50) {
                            break;
                        }
                        for (Element detail : commentDetails) {
                            if (detail.select("img").isEmpty()) {
                                Review review = new Review();
                                review.setReview(detail.text().trim());
                                review.setPid(commentId);
                                commentList.add(review);
                                count++;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                pageId++;
            }
            comment.setReviews(commentList);
            for (Review review : commentList) {
                commentMapper.insert(hupuId, comment.getTopicId(), review.getPid(), review.getReview(), nowMills);
            }
        } else {
            comment.setReviews(storedCommentList);
            comment.setTopicId(commentMapper.selectTopicId(hupuId));
        }
        return comment;
    }

    private List<HuPuList> getItemList(Elements list) {//获取板块内帖子的概略信息
        List<HuPuList> hupuL = new ArrayList<>();//存储热帖榜所有板块总概略信息
        for (Element listItem : list) {
            HuPuList hupuListObj = new HuPuList();//存储当前板块的概略信息
            Element parent = listItem.parent();//获取父元素
            Element ele = parent;
            List<HuPu> huPuList = new ArrayList<>();//存储当前板块帖子的概略信息
            for (int i = 0; i < 10; i++) {
                HuPu huPu = new HuPu();//存储当前帖子的概略信息
                Element item = ele.nextElementSibling();//获取下一个兄弟元素
                String href = item.getElementsByTag("a").get(0).attr("href");
                String regex = "\\d+亮\\d+回复";
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                java.util.regex.Matcher matcher = pattern.matcher(item.text());
                String text_removed = item.text();
                if (matcher.find()) {
                    String match = matcher.group();
                    String[] parts = match.split("亮|回复");
                    text_removed = item.text().substring(0, matcher.start()).trim();
                    huPu.setLightNum(Integer.parseInt(parts[0]));
                    huPu.setRepliesNum(Integer.parseInt(parts[1]));
                }
                huPu.setTitle(text_removed.replaceAll("^\\[.*?\\]\\s*", ""));
                huPu.setUrl(URL + href);
                huPu.setHupuId(href.replaceAll("[^0-9]", ""));
                huPu.setLabel(parent.text());
                ele = item;
                huPuList.add(huPu);
            }
            hupuListObj.setCategory(parent.text());//板块类别
            hupuListObj.setHuPuList(huPuList);//板块帖子概略信息
            hupuL.add(hupuListObj);
        }
        return hupuL;
    }


}

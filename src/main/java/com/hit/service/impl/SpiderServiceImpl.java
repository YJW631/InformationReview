package com.hit.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hit.pojo.Result;
import com.hit.service.SpiderService;
import com.hit.vo.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpiderServiceImpl implements SpiderService {

    private static final String URL = "https://bbs.hupu.com";//虎扑论坛基本url

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
                String text_removed= item.text();
                if (matcher.find()) {
                    String match = matcher.group();
                    String[] parts = match.split("亮|回复");
                    text_removed = item.text().substring(0, matcher.start()).trim();
                    huPu.setLightNum(Integer.parseInt(parts[0]));
                    huPu.setRepliesNum(Integer.parseInt(parts[1]));
                }
                huPu.setTitle(text_removed.replaceAll("^\\[.*?\\]\\s*", ""));
                huPu.setUrl(URL+href);
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

    public HuPuInfo getInfo(String id) {
        try {
            Document document = Jsoup.connect(URL + id)
                    .ignoreContentType(true)
                    .get();
            Element script = document.getElementById("__NEXT_DATA__");
            String jsonStr = script.html();
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            JSONObject props = jsonObject.getJSONObject("props");
            JSONObject pageProps = props.getJSONObject("pageProps");
            JSONObject detail = pageProps.getJSONObject("detail");

            JSONObject thread = detail.getJSONObject("thread"); // 内容 对象
            String tid = thread.getString("tid");
            String title = thread.getString("title");
            String lights2 = thread.getString("lights");
            String replies2 = thread.getString("replies");
            String recommend = thread.getString("recommend");
            String read = thread.getString("read");
            String client = thread.getString("client");
            String videoCover = thread.getString("videoCover");
            String video = thread.getString("video");
            String time = thread.getString("createdAtFormat");
            String authorJsonStr = thread.getString("author");
            String createdAt = thread.getString("createdAt");
            Author author = JSONObject.parseObject(authorJsonStr, Author.class);
            String location = thread.getString("location");
            String content = thread.getString("content");

            String lights = detail.getString("lights");// 热帖 数组
            List<RepliesList> lightsList = JSONArray.parseArray(lights, RepliesList.class);
            String replies = detail.getString("replies");// 帖子 对象
            Replies repliesObject = JSONObject.parseObject(replies, Replies.class);

            HuPuInfo huPuInfo = new HuPuInfo(videoCover, video, time, createdAt, author, location, content, lightsList, repliesObject, tid, title, lights2, replies2, recommend, read, client);
            return huPuInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RepliesList> getRepInfoList(String tid, String pid) {

        try {
            Document document = Jsoup.connect("https://bbs.hupu.com/api/v2/reply/reply?tid=" + tid + "&pid=" + pid + "&maxpid=0")
                    .ignoreContentType(true)
                    .get();
            String jsonStr = document.body().text();
            String string = JSONObject.parseObject(jsonStr).getJSONObject("data").getString("list");
            List<RepliesList> repliesLists = JSONArray.parseArray(string, RepliesList.class);
            return repliesLists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

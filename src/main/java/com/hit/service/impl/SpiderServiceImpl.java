package com.hit.service.impl;

import com.hit.mapper.CommentMapper;
import com.hit.mapper.ImageMapper;
import com.hit.mapper.UserReviewMapper;
import com.hit.pojo.Result;
import com.hit.service.SpiderService;
import com.hit.vo.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SpiderServiceImpl implements SpiderService {

    private static final String URL = "https://bbs.hupu.com";//虎扑论坛基本url

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserReviewMapper userReviewMapper;

    @Autowired
    private ImageMapper imageMapper;

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

    @Override
    public Map<String, Integer> getInterestedField(String userId) {
        Map<String, Integer> topicCounts = new HashMap<>();
        Map<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", "smidV2=20241007171107e73b259f8108ec775fdf2070bd5c8495003e5347cf014a620; _c_WBKFRo=XjsBqH2JbLwvpVhBYItSD6GxaxVcdMtqfyxJtGlT; _HUPUSSOID=775436eb-a6f8-47c4-a0f5-71a81b2e2df8; _CLT=918ebe7bb324d8673460f7af1d701a5c; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22191dbf254e55ef-04cd14b802cf3-26001e51-1327104-191dbf254e61afe%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTkxZGJmMjU0ZTU1ZWYtMDRjZDE0YjgwMmNmMy0yNjAwMWU1MS0xMzI3MTA0LTE5MWRiZjI1NGU2MWFmZSJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%22%2C%22value%22%3A%22%22%7D%2C%22%24device_id%22%3A%22191dbf254e55ef-04cd14b802cf3-26001e51-1327104-191dbf254e61afe%22%7D; Hm_lvt_b241fb65ecc2ccf4e7e3b9601c7a50de=1729583383,1730560081,1730612124,1730648682; Hm_lvt_4fac77ceccb0cd4ad5ef1be46d740615=1729583383,1730560081,1730612124,1730648682; acw_tc=1a0c579917307127249322771e013c0f53894c2ae7158d60446fe6e2d67957; csrfToken=VCtKQ1_WTN78TpwPWqSsGEJO; Hm_lvt_ac423772552f47d0bb3ae6d55e13262c=1730611016,1730626315,1730639152,1730712726; HMACCOUNT=034F24E62CCFF380; g=100397067%7C6JmO5omRSlIxMjI2MzYxMzUx; u=100397067|6JmO5omRSlIxMjI2MzYxMzUx|e314|5ad2033eb0281e5cbcf73a811dd2dde7|4ecab7e3645b6584|aHVwdV8zYmJiZDA0MDUwZWEyYzhi; us=b334174e74061b087c32ba84d636964ea09e9b8f1868d8d0e5a27031513d0d30e0a3fbac5edd79b21ccd6bb42f864f646c6ae9eff4bf16a17c0429c10632011a; ua=25451658; tfstk=fQM-aJcDaEYo1AIAM6O0ta9wDg-DivnzD4o1Ky4lOq3xY2BlEJ2uOkaxP_43qDaKJq4VtkwS-2QKrqD7PvnFpkiI83zH4Imr4JyBSF2pI0orlLNklYB7hSi4Xw1CmDILXJyBSEbe40YULVuCBPTKcnE3x_1SRuiXDkELdTNCFs_buraQd9a5ltZT2gaIRJtxcrrIGjj8-gadpX-yDFivmVBCdY9UTPQQuor-HrP85SaAd0HY27Us2YfTBrZjnxFgiMYzhclmPoe9eNr-Gce7vv-19yFKU-UxWd6U0bg-hWHDYQoxpzFsFSIJV0y8A2NKgFBg28cY6YhyYZiSSzh_UDj9rmFtM5rbGMC7EfmiL5MWeNz0_k3YsbLOeVsr7ADTzR_gWkfWDnCFT7Zq20N1w8r9qGrYSnfcT6P00oUMDnCFT7Z4DPxDI65Uio5..; Hm_lpvt_ac423772552f47d0bb3ae6d55e13262c=1730713103");
        try {
            Document document = Jsoup.connect("https://my.hupu.com/" + userId)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                    .cookies(cookies)
                    .get();
            Elements fields = document.select(".list-font-item .font-item-Topic");
            for (Element field : fields) {
                String topic = field.text();
                topicCounts.put(topic, topicCounts.getOrDefault(topic, 0) + 1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return topicCounts;
    }

    @Override
    public List<UserReview> getUserComment(String userId) {
        List<UserReview> userReviewList = new ArrayList<>();
        List<UserReview> storedUserReviewList = userReviewMapper.getUserReview(userId);
        if (storedUserReviewList.size() == 0) {
            int count = 0;
            System.setProperty("webdriver.chrome.driver", "D:\\Code\\Java\\code\\project\\src\\main\\java\\com\\hit\\chromedriver\\chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            try {
                driver.get("https://my.hupu.com/271809050780808?tabKey=2");
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                WebElement usernameInput = driver.findElement(By.id("bbs-login-form_username"));
                usernameInput.sendKeys("15004169279");
                WebElement passwordInput = driver.findElement(By.id("bbs-login-form_password"));
                passwordInput.sendKeys("irp123456");
                WebElement smartButton = driver.findElement(By.id("SM_BTN_1"));
                smartButton.click();
                WebElement agreementCheckbox = driver.findElement(By.id("bbs-login-form_policy"));
                if (!agreementCheckbox.isSelected()) {
                    agreementCheckbox.click(); // 勾选复选框
                }
                WebElement loginButton = driver.findElement(By.cssSelector(".submit-btn"));
                loginButton.click();
                TimeUnit.SECONDS.sleep(5);
                WebElement loginButton1 = driver.findElement(By.cssSelector(".submit-btn"));
                loginButton1.click();
                TimeUnit.SECONDS.sleep(5);
                driver.get("https://my.hupu.com/"+userId+"?tabKey=2");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                for (int i = 0; i < 5; i++) {
                    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    Thread.sleep(2000);
                }
                List<WebElement> fields = driver.findElements(By.cssSelector(".list-item-reply"));
                for (WebElement field : fields) {
                    if(count<50){
                        String topic = field.getText().trim();
                        UserReview userReview=new UserReview();
                        userReview.setUserId(userId);
                        userReview.setReview(topic);
                        userReviewList.add(userReview);
                        userReviewMapper.insert(userReview);
                    }
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            return storedUserReviewList;
        }
        return userReviewList;
    }

    @Override
    public List<String> getImages(String hupuId) {
        int count = 0;
        int pageId = 1;
        int lastPage = 1;
        List<String> imageList=new ArrayList<>();
        List<String> storedImageList=imageMapper.getImageUrls(hupuId);
        if (storedImageList.size()!=0){
            return storedImageList;
        }else {
            try {
                Document document = Jsoup.connect("https://bbs.hupu.com/628756283.html")
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                        .get();
                Elements paginationItems = document.select(".hupu-rc-pagination-item a");
                String lastPageStr = null;
                for (Element paginationItem : paginationItems) {
                    lastPageStr = paginationItem.text();
                }
                lastPage = Integer.parseInt(lastPageStr);
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
                    Document document = Jsoup.connect("https://bbs.hupu.com/"+hupuId+"-" + pageId + ".html")
                            .ignoreContentType(true)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                            .get();
                    Elements imgDivs  = document.select(".post-wrapper_gray__HNv4A div.thread-content-detail img");
                    for (Element img : imgDivs) {
                        String imgUrl = img.attr("src");
                        if (count >= 50) {
                            break;
                        }
                        if (imgUrl.contains("?")) {
                            imgUrl = imgUrl.substring(0, imgUrl.indexOf('?'));
                        }
                        imageList.add(imgUrl);
                        imageMapper.insert(hupuId,imgUrl);
                        count++;
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
            return imageList;
        }
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

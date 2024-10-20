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

    private static final String URL = "https://bbs.hupu.com";

    private List<HuPuList> getItemList(Elements list) {
        List<HuPuList> hupuL = new ArrayList<>();
        for (Element listItem : list) {
            HuPuList hupuListObj = new HuPuList();
            Element parent = listItem.parent();
            Element ele = parent;
            List<HuPu> huPuList = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                HuPu huPu = new HuPu();
                Element item = ele.nextElementSibling();
                String href = item.getElementsByTag("a").get(0).attr("href");
                huPu.setTitle(item.text());
                huPu.setUrl(href);
                ele = item;
                huPuList.add(huPu);
            }
            hupuListObj.setCategory(parent.text());
            hupuListObj.setHuPuList(huPuList);
            hupuL.add(hupuListObj);
        }
        return hupuL;
    }

    @Override
    public Result getHotTitle() {
        try {
            Document document = Jsoup.connect(URL)
                    .ignoreContentType(true)
                    .get();
            Elements list = document.getElementsByClass("list-title");
            List<HuPuList> itemList = getItemList(list);
            for (HuPuList hu : itemList) {
                System.out.println(hu);
            }
            return Result.success(getItemList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("热帖标题获取失败");
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

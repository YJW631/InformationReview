package com.hit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
    public static void main(String[] args) {
        try {
            // 获取网页内容
            String url = "https://bbs.hupu.com/";  // 替换为你需要爬取的网址
            Document doc = Jsoup.connect(url).get();

            // 选择所有的 <a> 标签，并且 class 属性是 "false"
            Elements links = doc.select("a.false");

            // 提取每个链接的 href 属性
            for (Element link : links) {
                String href = link.attr("href");
                String text = link.text();

                // 如果 href 是相对路径，拼接成完整路径
                if (!href.startsWith("http")) {
                    href = url + href;
                }

                System.out.println("Link: " + href);
                System.out.println("Text: " + text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

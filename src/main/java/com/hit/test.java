package com.hit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        try {
            Map<String, String> cookies = new HashMap<>();
            cookies.put("Cookie", "smidV2=20241007171107e73b259f8108ec775fdf2070bd5c8495003e5347cf014a620; _c_WBKFRo=XjsBqH2JbLwvpVhBYItSD6GxaxVcdMtqfyxJtGlT; _HUPUSSOID=775436eb-a6f8-47c4-a0f5-71a81b2e2df8; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22191dbf254e55ef-04cd14b802cf3-26001e51-1327104-191dbf254e61afe%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTkxZGJmMjU0ZTU1ZWYtMDRjZDE0YjgwMmNmMy0yNjAwMWU1MS0xMzI3MTA0LTE5MWRiZjI1NGU2MWFmZSJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%22%2C%22value%22%3A%22%22%7D%2C%22%24device_id%22%3A%22191dbf254e55ef-04cd14b802cf3-26001e51-1327104-191dbf254e61afe%22%7D; csrfToken=IJLO3xnjSttQTy7nm4a-t1rS; Hm_lvt_ac423772552f47d0bb3ae6d55e13262c=1730559919,1730561797,1730564244,1730611016; HMACCOUNT=034F24E62CCFF380; Hm_lvt_4fac77ceccb0cd4ad5ef1be46d740615=1729583383,1730560081,1730612124; HMACCOUNT=034F24E62CCFF380; Hm_lvt_b241fb65ecc2ccf4e7e3b9601c7a50de=1729583383,1730560081,1730612124; acw_tc=ac11000117306129457154702e0074ac735abba12024c910c2811dbe5091b8; PHPSESSID=h7gpmq2fogsdkhti0vg0e1e1m2; Hm_lpvt_b241fb65ecc2ccf4e7e3b9601c7a50de=1730613053; Hm_lpvt_4fac77ceccb0cd4ad5ef1be46d740615=1730613053; u=100397067|6JmO5omRSlIxMjI2MzYxMzUx|bc72|6c6666abfdd8bade14a28e4e65642969|23a8f7cd341adb65|aHVwdV8zYmJiZDA0MDUwZWEyYzhi; us=cf19b768b33dc96b90efceb295d1518d23f68fc273a39c29e52b9d63197674f7ab0f62d42180afa49b7a0849579e21465493d51db7c12c388c8b364ac3bd47a8; ua=25450192; _CLT=918ebe7bb324d8673460f7af1d701a5c; tfstk=fyqtC44h76fgPuppJE_noc_fQxWh-oeZp5yWmjcMlWFL3SKMjmmilxGL1FcmI-GYMWceoxi_iSdYsWqs1oe2DxwbgOlcQw2aQmofZ_mAqRyNawcAWlHXdpwEeE96K-pxemofZ6AVQRfq07Ps6hWTd9HmnFT_GAwCpxHxchgXfeOITXGjcAGbdMMZ3FtX5SwCpxlIGjsJBjvsMnUVp1f0ECEBcnZ10mGR0XMEIkHsWbgI9nwTvA3tNREMy0hIdkqKr2vc_Dwz-5MK2GdrByw-fxEVFEhxkloK97IW0vz_6kgYxt7apohtPlgpBOwZXxNjky5JqAngJ4ET7tRQC7cTPcyDe12Kyy3rd29v5c4uEou7fGdrt4kY944OwhF54qru2foWZbHD59BpuEusLResPFnZeOvrpbXH-E8q-vkKZ9BpuEusLvhlKgY2u2DF.; Hm_lpvt_ac423772552f47d0bb3ae6d55e13262c=1730613363");
            Document document = Jsoup.connect("https://my.hupu.com/279261059918343")
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                    .cookies(cookies)
                    .get();
            System.out.println(document.toString());
        } catch (IOException e) {
            e.printStackTrace(); // 更改为打印堆栈跟踪
        }
    }
}

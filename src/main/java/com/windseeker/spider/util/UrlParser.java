package com.windseeker.spider.util;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UrlParser {

    private static String baseUrl="https://cl.dg53.xyz";

    public static List<String> topicUrls(String htmlPage) {
        List<String> urls = new ArrayList<>();
        //<a href="htm_data/2102/16/4345783.html" target="_blank" id="">他哥极力反对 但也不耽误玩啊 黑丝户外[10P]</a></h3>
        String[] split = htmlPage.split("<h3><a href");
        Arrays.stream(split).filter(s -> s.contains("=\"htm_data/"))
                .forEach(s -> {

                    String innerUrl = s.split("\"")[1];
                    String url = baseUrl + "/" + innerUrl;
                    System.out.println(url);
                    urls.add(url);
                    //1.创建对应的文件夹
                    //2.拼接url

                });

        return urls;
    }
}

package com.windseeker.spider.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Windseeker
 */
public class HtmlGetter {


    public static String htmlPage(String url) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        HttpUriRequest request = new HttpGet(url);
        request.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36");
        HttpResponse res = client.execute(request);
        HttpEntity entity = res.getEntity();
        InputStreamReader isr = new InputStreamReader(entity.getContent(), "GBK");
        BufferedReader br = new BufferedReader(isr);
        String str = "";
        StringBuilder builder = new StringBuilder();
        while ((str = br.readLine()) != null) {
            builder.append(str);
        }
        return builder.toString();
    }
}

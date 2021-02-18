package com.windseeker.spider;

import com.windseeker.spider.util.HtmlGetter;
import com.windseeker.spider.util.UrlParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class SpiderApplication {

    private static String flagPage = "/thread0806.php?fid=16";
    private static String basePage = "https://cl.dg53.xyz";
    private static String basePath = "D:\\cl";

    public static void main(String[] args) throws Exception {
       // List<String> allUrls = new ArrayList<>();

      /*  //287
        for (int i = 1; i < 287; i++) {
            System.out.println("page:==================" + i);
            String s = HtmlGetter.htmlPage(basePage + flagPage + "&search=&page=" + i);
            List<String> urls = UrlParser.topicUrls(s);
            allUrls.addAll(urls);
        }
        IOUtils.writeLines(allUrls,"\r\n",new FileWriter("D:\\cl\\urls.txt"));*/
        InputStream is=new FileInputStream("D:\\cl\\urls.txt");
        List<String> allUrls = IOUtils.readLines(is, Charset.defaultCharset());
        is.close();
        //每个url是一个帖子，读取帖子页面的内容，找到图片的链接，下载到本地。
        allUrls.parallelStream().forEach(url -> {
            try {
                //1. 创建文件夹
                String topicContent = HtmlGetter.htmlPage(url);
                String topicName = topicContent.split("<h4 class=\"f16\">")[1].split("</h4>")[0];
                String folderName = basePath + File.separator + topicName;
                File file = new File(folderName);
                if (!file.exists()) {
                    file.mkdir();
                }
                //2.开始解析其中的图片链接
                List<String> pics = new ArrayList<>();
                String totalLinks = topicContent.split("<h4 class=\"f16\">")[1].split("class=\"t_like\"")[0];
                String[] imgs = totalLinks.split("<img");
                for (int i = 1; i < imgs.length; i++) {
                    String imageTag = imgs[i];
                    String picUrl = imageTag.split("ess-data='")[1].split("'")[0];
                    pics.add(picUrl);
                }
                for (int i = 0; i < pics.size(); i++) {
                    savePic(folderName, i + 1, pics.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void savePic(String folderName, int num, String pic) throws Exception {

        String[] suffixes=pic.split("\\.");
        String suffix =suffixes[suffixes.length-1];
        URL url = new URL(pic);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        //new一个文件对象用来保存图片，默认保存当前工程根目录
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(folderName + File.separator + num + "." + suffix);
        //写入数据
        outStream.write(data);
        //关闭输出流
        outStream.close();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

}

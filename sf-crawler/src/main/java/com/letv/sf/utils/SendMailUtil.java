package com.letv.sf.utils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal static utilities for  sending mail
 */
public class SendMailUtil {

    private static final Logger logger = LoggerFactory.getLogger(SendMailUtil.class);

    private static final String COMMON_SERVER = "http://10.180.92.210:9110";

    public static void sendEmail(String to, String cc, String title, String content, boolean isHtml) {
        Map<String, Object> paramap = new HashMap<String, Object>();
        paramap.put("to", to);
        paramap.put("copyto", cc);
        paramap.put("title", title);
        paramap.put("html", content);
        paramap.put("isHtml", isHtml);
        post(paramap);
    }

    public static void post(Map<String, Object> requestParamsMap) {
        String requestUrl = COMMON_SERVER + "/bigdata/common_service/v0/send_mail";
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        // BufferedReader bufferedReader = null;
        StringBuffer responseResult = new StringBuffer();
        StringBuffer params = new StringBuffer();
        HttpURLConnection httpURLConnection = null;
        // 组织请求参数
        Iterator it = requestParamsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            params.append(element.getKey());
            params.append("=");
            params.append(element.getValue());
            params.append("&");
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }

        try {
            URL realUrl = new URL(requestUrl);
            // 打开和URL之间的连接
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Length", String
                    .valueOf(params.length()));
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(params.toString());
            // flush输出流的缓冲
            printWriter.flush();

            // 根据ResponseCode判断连接是否成功
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                logger.error("code = " + responseCode);
            } else {
                // 定义BufferedReader输入流来读取URL的ResponseData
                bufferedReader = new BufferedReader(new InputStreamReader(
                        httpURLConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseResult.append(line);
                }
                logger.info("返回 = " + responseResult.toString());
            }

        } catch (Exception e) {
            logger.error("HTTP请求异常" + e);
        } finally {
            httpURLConnection.disconnect();
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                logger.error("I/O流关闭异常" + ex);
            }
        }
    }

    private static boolean inToday(long time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date d1 = cal.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dtStr1 = df.format(d1);
        Date d = df.parse(dtStr1);
        long t1 = d.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        d1 = cal.getTime();
        dtStr1 = df.format(d1);
        d = df.parse(dtStr1);
        long t2 = d.getTime();
        if (time >= t1 && time < t2) {
            return true;
        }
        return false;
    }

}

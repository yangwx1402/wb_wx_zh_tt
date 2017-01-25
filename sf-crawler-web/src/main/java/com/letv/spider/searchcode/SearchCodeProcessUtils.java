package com.letv.spider.searchcode;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.letv.spider.dao.DaoFactory;
import com.letv.spider.entity.CrawlerSlotConfig;
import com.letv.spider.http.WeiboCrawler;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yangyong3 on 2017/1/20.
 */
public class SearchCodeProcessUtils {

    public static String saveImg(InputStream is, String img_file) throws IOException {
        FileOutputStream fos = new FileOutputStream(img_file);
        byte[] buffer = new byte[100];
        while (is.read(buffer) != -1) {
            fos.write(buffer);
        }
        fos.flush();
        fos.close();
        return img_file;
    }

    public static void main(String[] args) throws Exception {
        CrawlerSlotConfig config = DaoFactory.crawlerSlotDao.getSlotById(300);
        WeiboCrawler weiboCrawler = new WeiboCrawler(config);
        String codeUrl = "http://s.weibo.com/ajax/pincode/pin?type=sass" + System.currentTimeMillis();
        WebResponse response = weiboCrawler.get(codeUrl);
        saveImg(response.getContentAsStream(), "E:\\code.png");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine();
        String postUrl = "http://s.weibo.com/ajax/pincode/verified?__rnd=" + System.currentTimeMillis();
        Map<String, String> param = new HashMap<String, String>();
        param.put("type","sass");
        param.put("pageid","weibo");
        param.put("_t","0");
        param.put("secode",code);
        WebResponse response1 = weiboCrawler.post(postUrl,param);
        System.out.println(IOUtils.toString(response1.getContentAsStream(),"utf-8"));
    }
}

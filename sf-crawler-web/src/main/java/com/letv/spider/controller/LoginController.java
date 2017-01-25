package com.letv.spider.controller;

import com.letv.spider.dao.CookieDao;
import com.letv.spider.thread.CommandRunner;
import com.letv.spider.utils.CookieReader;
import com.letv.spider.utils.CrawlerThreadPool;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/13.
 */
@Controller
@RequestMapping("/user")
public class LoginController {

    private static final CrawlerThreadPool threadPool = new CrawlerThreadPool();

    private static final int code_sleep = 15 * 1000;

    private static final CookieDao dao = new CookieDao();
    @Autowired
    private Environment env;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(ModelMap map) {
        return "login";
    }

    @RequestMapping(value = "/logining", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView logining(@RequestParam String username, @RequestParam String password) throws InterruptedException, IOException, ParseException {
        long now = System.currentTimeMillis();
        String img_png = env.getProperty("spider.sf.imgpngpath") + File.separator + "img_code_" + now + ".png";
        String img_txt = env.getProperty("spider.sf.imgtxtpath") + File.separator + "img_txt_" + now + ".txt";
        String result_png = env.getProperty("spider.sf.resultpng") + File.separator + "result_" + now + ".png";
        String cookieFile = env.getProperty("spider.sf.cookiepath") + File.separator + "cookie_" + now + ".txt";
        String command = "casperjs " + env.getProperty("spider.sf.scriptpath") + "" + File.separator + "weibo_login.js " + " " + username + " " + password + " " + img_png + " " + img_txt + " " + result_png + " " + cookieFile;
        Thread thread = new Thread(new CommandRunner(command));
        threadPool.execute(thread);
        //这里就直接输出验证码给显神看
        File file = new File(img_png);
        int count = 0;
        while (!file.exists()) {
            System.out.println("wait 验证码文件" + img_png);
            count++;
            Thread.sleep(1000);
            if (count >= 20)
                break;
        }
        ModelAndView view = new ModelAndView();
        view.addObject("date_time", now);
        view.addObject("username", username);
        view.addObject("password", password);
        if (file.exists()) {
            view.setViewName("code");
            return view;
        } else {
            String cookie = CookieReader.readCookie(cookieFile);
            dao.updateCookie(username, password, cookie);

            view.setViewName("result");
            return view;
        }
    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validate(@RequestParam String code, @RequestParam String datetime, @RequestParam String username, @RequestParam String password) throws InterruptedException, IOException, ParseException {
        String img_txt = env.getProperty("spider.sf.imgtxtpath") + File.separator + "img_txt_" + datetime + ".txt";
        FileUtils.writeStringToFile(new File(img_txt), code, "utf-8", false);
        System.out.println("写入验证码到" + img_txt);
        ModelAndView view = new ModelAndView();
        view.addObject("date_time", datetime);
        view.setViewName("result");

        String cookieFile = env.getProperty("spider.sf.cookiepath") + File.separator + "cookie_" + datetime + ".txt";
        File file = new File(cookieFile);
        int count = 0;
        while (!file.exists()) {
            System.out.println("wait read cookiefile");
            count++;
            Thread.sleep(1000);
            //最多等待60秒
            if (count >= 60)
                break;
        }
        System.out.println("wait finished wait time is " + count + " seconds");
        String cookie = CookieReader.readCookie(cookieFile);
        dao.updateCookie(username, password, cookie);
        return view;
    }
}

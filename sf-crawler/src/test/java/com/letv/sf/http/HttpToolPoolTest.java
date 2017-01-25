package com.letv.sf.http;

import com.letv.sf.entity.common.HttpToolGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2017/1/11.
 */
public class HttpToolPoolTest {

    public static class PutThread implements Runnable {

        public void run() {
            List<HttpToolGroup> list = new ArrayList<HttpToolGroup>();
            for (int i = 0; i < 20; i++) {
                HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType("weibo");
                System.out.println("Thread group = " + group);
                list.add(group);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (HttpToolGroup group : list)
                HttpToolFactory.returnTool(group);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new PutThread()).start();
        Thread.sleep(1000);
        for (int i = 0; i < 20; i++) {
            System.out.println(HttpToolFactory.getHttpToolGroupByType("weibo"));
        }
    }
}

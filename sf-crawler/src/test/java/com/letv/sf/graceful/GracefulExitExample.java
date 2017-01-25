package com.letv.sf.graceful;

/**
 * Created by yangyong3 on 2016/12/23.
 */
public class GracefulExitExample {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("this programe is exit ");
            }
        });
        System.out.println("bussniess");
        Thread.sleep(10000);
    }
}

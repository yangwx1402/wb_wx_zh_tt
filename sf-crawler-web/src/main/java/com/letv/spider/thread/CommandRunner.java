package com.letv.spider.thread;

import com.letv.spider.utils.CommandTools;

import java.io.IOException;

/**
 * Created by yangyong3 on 2017/1/18.
 */
public class CommandRunner implements Runnable {

    private String command;

    public CommandRunner(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        try {
            System.out.println("source /etc/profile &"+command);
            System.out.println(CommandTools.process(command));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

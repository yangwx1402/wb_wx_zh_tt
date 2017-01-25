package com.letv.spider.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by young.y1ang on 2016/11/29.
 */
public class DataSourceFactory {
    private static DataSource dataSource;
    private synchronized static void init() throws Exception {
        if(dataSource==null) {
            dataSource = new DruidDataSource();
            String path = DataSourceFactory.class.getResource("/").getPath() + File.separator + "druid.properties";
            Properties properties = new Properties();
            properties.load(new FileInputStream(path));
            dataSource = DruidDataSourceFactory.createDataSource(properties);

        }
    }
    public static DataSource getDataSource() throws Exception {
       if(dataSource==null){
           init();
       }
       return dataSource;
    }

    public static void destory(){

    }


    public static void main(String[] args) throws Exception {
        System.out.println(DataSourceFactory.getDataSource().getConnection().createStatement());
    }
}

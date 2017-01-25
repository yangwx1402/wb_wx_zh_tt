package com.letv.sf.dao;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class TableNameFactory {

    private static TableNameGenerator generator = new DefaultTableNameGenerator();

    public static  String getTableName(String tableName,Long condition){
         return generator.getTableName(tableName,condition);
    }
}

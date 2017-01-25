package com.letv.sf.dao;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class DefaultTableNameGenerator implements TableNameGenerator {
    public String getTableName(String tableName, Long condition) {
        return tableName;
    }
}

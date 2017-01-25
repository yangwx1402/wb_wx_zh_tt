package com.letv.sf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public abstract class AbstractDao {

    protected static final String WEIBO_MID = "WBMID_";

    protected static final String WEIBO_USER_UID = "WBUID_";

    protected static final String WEIBO_COMMENT_CID = "WBCID_";


    protected void close(ResultSet rs, Statement stmt, Connection con) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}

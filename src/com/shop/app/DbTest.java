package com.shop.app;

import java.sql.Connection;
import com.shop.util.DBUtil;

public class DbTest {
    public static void main(String[] args) throws Exception {
        try (Connection con = DBUtil.getConnection()) {
            System.out.println("CONNECTED: " + (con != null && !con.isClosed()));
        }
    }
}

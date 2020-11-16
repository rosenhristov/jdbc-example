package com.dataart.databaseaccess.utils;

import java.sql.Connection;

public class Utils {

    public static Connection getConnection(String dbms) {
        if (dbms.equals("postgres")) {
            return getConnection("postgresql", "localhost", 5432, "postgres", "7840", "bank");
        }
        return null;
    }

    public static Connection getConnection(String dbms, String host, int port,
                                           String userName, String password, String dbName) {
        return new JDBCConnector(dbms, host, port, userName, password, dbName)
                .getConnection();
    }
}

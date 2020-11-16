package com.dataart.databaseaccess.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnector {

    private String dbms;
    private String host;
    private int port;
    private String userName;
    private String password;
    private String dbName;

    public JDBCConnector(String dbms, String host, int port, String userName, String password, String dbName) {
        this.dbms = dbms;
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.dbName = dbName;
    }


    public Connection getConnection() {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        try {
            if (this.dbms.equals("postgresql")) {
                conn = DriverManager.getConnection(
                        new StringBuilder("jdbc:")
                                .append(this.dbms).append("://")
                                .append(this.host).append(":")
                                .append(this.port).append("/")
                                .append(dbName)
                                .toString(),
                        connectionProps);
            } else if (this.dbms.equals("derby")) {
                conn = DriverManager.getConnection(
                        "jdbc:" + this.dbms + ":" +
                                this.dbName +
                                ";create=true",
                        connectionProps);
            }
            if (conn == null) {
                throw new SQLException("Connection was not established successfully.");
            }
            System.out.println("Connected to database");
        } catch (SQLException e) {
                System.out.println(e.getMessage());
        }
        return conn;
    }
}

package com.example.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.core.env.Environment;

public class DatabaseConnector {

    public Connection getConnection(Environment env) throws SQLException, IOException {

        String userName = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);

        conn = DriverManager.getConnection(env.getProperty("spring.datasource.url"), connectionProps);

        return conn;
    }
}

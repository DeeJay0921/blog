package com;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
    static String driverClass = null; // 数据库类型
    static String url = null; // 数据库地址
    static String userName = null;
    static String password = null;

    static { // 默认static代码块  读取properties配置
        try {
            Properties properties = new Properties(); // 获取Properties对象
            InputStream setting = new FileInputStream("src/jdbc.properties"); // 获取文件流
            properties.load(setting);// properties对象加载读取到的配置
            driverClass = properties.getProperty("driverClass"); // 从配置中读取特定的配置
            url = properties.getProperty("url");
            userName = properties.getProperty("userName");
            password = properties.getProperty("password");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    注册驱动并且并且获取连接Connection
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, userName, password);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return connection;
        }
    }


    //     释放资源
    public static void close(ResultSet res, Statement statement, Connection connection) {
        closeRes(res);
        closeStatement(statement);
        closeConnection(connection);
    }

    public static void closeRes(ResultSet res) {
        try {
            if(res != null) {
                res.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            res = null;
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            if(statement != null) {
                statement.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            statement = null;
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if(connection != null) {
                connection.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            connection = null;
        }
    }
}

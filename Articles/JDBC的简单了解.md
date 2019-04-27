# JDBC
> Java DataBase Connectivity  java数据库连接

是一种数据库访问规则规范。

## 简单使用
基本步骤：
- 注册driver
- 建立连接
- 创建statement对象进行交互
- 执行sql 获取结果
- 释放资源

代码举例：
```
package com;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Driver driver;
        Connection connection;
        Statement statement;
        ResultSet res;
        try {
            driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/base1?serverTimezone=GMT&useSSL=false", "root", "Admin@123");
            statement = connection.createStatement();
            String sql = "select * from product";
            res =  statement.executeQuery(sql);
            while(res.next()) {
                System.out.println("pname: " + res.getString("pname")
                        + "price: " + res.getDouble("price"));
            }
            res.close();
            statement.close();
            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

maven依赖添加地址：
```
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.15</version>
        </dependency>
```

### 释放资源代码整理
将释放资源的逻辑可以提出来，写出一个工具类
```
package com;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCUtils {

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

```
优化之后，上面的例子就可以写成这样：
```
package com;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Driver driver;
        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;
        try {
            driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/base1?serverTimezone=GMT&useSSL=false", "root", "Admin@123");
            statement = connection.createStatement();
            String sql = "select * from product";
            res =  statement.executeQuery(sql);
            while(res.next()) {
                System.out.println("pname: " + res.getString("pname")
                        + "price: " + res.getDouble("price"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(res, statement, connection);// 在finally里去close()
        }
    }
}

```
### 关于注册driver的优化

看下源码可以发现其内部有个static代码块:
```
    static {
        try {
            DriverManager.registerDriver(new Driver()); // 已经注册了一个Driver
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
```
内部已经进行了driver的注册，所以没必要重复注册第二个driver，按照文档中的示例，可以采用`Class.forName("com.mysql.jdbc.Driver").newInstance()`（其实没必要去newInstance()已经有driver,这个操作浪费空间）;

优化过后的代码如下所示：

```
package com;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Driver driver;
        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;
        try {
//            driver = new com.mysql.cj.jdbc.Driver(); // 改用动态加载一个driver类
            Class.forName("com.mysql.cj.jdbc.Driver");
//            DriverManager.registerDriver(driver); // com.mysql.cj.jdbc.Driver内部已经注册过driver了  没必要重复注册
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/base1?serverTimezone=GMT&useSSL=false", "root", "Admin@123");
            statement = connection.createStatement();
            String sql = "select * from product";
            res =  statement.executeQuery(sql);
            while(res.next()) {
                System.out.println("pname: " + res.getString("pname")
                        + "price: " + res.getDouble("price"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(res, statement, connection);
        }
    }
}

```

> 关于Class.forName(className), 在一些应用中，无法事先知道使用者将加载什么类(比如本例中使用jdbc可能是其他数据库)，而必须让使用者指定类名称以加载类，可以使用 Class 的静态 forName() 方法实现动态加载类。详见[Class.forName](https://www.cnblogs.com/sunzn/p/3187868.html)

### jdbc.properties

上述例子中的数据库配置等信息都是写在代码中的，我们一般是要从配置文件中读取的，所以我们创建一个名为jdbc.properties的文件,如下:
```
driverClass=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/base1?serverTimezone=GMT&useSSL=false
userName=root
password=Admin@123
```
然后我们修改JDBCUtils里面的代码：
```
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

```


### JDBC对数据库的CRUD
- insert
```
 @Test
    public void testInsert() {
        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;
        try{
            connection =  JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "insert into product values(null, '泡椒凤爪', 3, null, 5);";
            int result = statement.executeUpdate(sql); // 执行insert update  delete的时候使用executeUpdate return int
            if(result > 0) { // result为影响的行数
                System.out.println("insert successfully!");
            }else {
                System.out.println("insert failed");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(res, statement, connection);
        }
    }
```

update和delete同理

### 使用junit进行单元测试

1. 添加junit依赖
```
<!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
```
2. 添加一个类，定义测试方法，给其加上@Test注解

```
import com.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDemo {

    @Test
    public void testQuery() {
        Connection connection = null;
        Statement statement = null;
        ResultSet res = null;
        try{
            connection =  JDBCUtils.getConnection();
            statement = connection.createStatement();
            String sql = "select * from product;";
            res = statement.executeQuery(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(res, statement, connection);
        }
    }
}

```




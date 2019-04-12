# 数据库概述

- 什么是数据库: 数据库就是一个文件系统，只不过需要通过命令(sql)来操作这个文件系统
- 数据库的作用：存储数据，数据的仓局，带有不同访问权限的人可以有不同的操作
- 常见的关系型数据库：mysql, oracle, mariadb(mysql分支，和mysql非常相似),db2, sqlserver
- 常见的非关系型数据库：MongoDB, redis
- 关系型数据库：主要描述实体与实体的关系
	
# mysql的简单使用

### 安装和卸载

安装：[安装教程](https://blog.csdn.net/huzhongxi/article/details/81988988#%E4%B8%8B%E8%BD%BDzip%E5%AE%89%E8%A3%85%E5%8C%85)

卸载：卸载mysqlServer，删除mysql目录下所有文件，删除mysql所有存储数据。


### mysql的一些语句

DDL(定义): create drop alter(修改)
DML(操作): insert update delete
DCL(数据控制语句):  定义访问权限，取消访问权限，安全设置，grant
DQL(查询)：select from子句 where子句

### 常用的crud操作
#### 创建数据库
- 登录
```
mysql -uroot -proot  //(账号密码为root root)
```
- 创建一个数据库DeeJay
```
create database DeeJay1;
```
> 创建数据库的时候要指定字符集的话可以这么写：` create database DeeJay1 character set utf8;`
`create database DeeJay1 character set utf8 collate 校对规则;`
- 查看数据库
  - 查看所有数据库
    ```
    show databases;
    ```
    输出如下：
    ```
    mysql> show databases;
    +--------------------+
    | Database           |
    +--------------------+
    | deejay1            |
    | information_schema |
    | mysql              |
    | performance_schema |
    | sys                |
    +--------------------+
    5 rows in set (0.02 sec)
    ```
    可以看到还有一些默认库。
    - 查看数据库定义的语句：
    ```
    show create database 数据库名;
    ```
    ```
    mysql> show create database DeeJay1;
    +----------+------------------------------------------------------------------+
    | Database | Create Database                                                  |
    +----------+------------------------------------------------------------------+
    | DeeJay1  | CREATE DATABASE `DeeJay1` /*!40100 DEFAULT CHARACTER SET utf8 */ |
    +----------+------------------------------------------------------------------+
    1 row in set (0.01 sec)
    ```

- 修改数据库的操作
    - 修改数据库的字符集
    ```
    alter database 数据库名 character set 字符集;
    ```
    ```
    mysql> alter database DeeJay1 character set gbk;
    Query OK, 1 row affected (0.13 sec)

    mysql> show create database DeeJay1;
    +----------+-----------------------------------------------------------------+
    | Database | Create Database                                                 |
    +----------+-----------------------------------------------------------------+
    | DeeJay1  | CREATE DATABASE `DeeJay1` /*!40100 DEFAULT CHARACTER SET gbk */ |
    +----------+-----------------------------------------------------------------+
    1 row in set (0.00 sec)
    ```
- 删除数据库
```
drop database 数据库名;
```
```
mysql> drop database DeeJay1;
Query OK, 0 rows affected (0.19 sec)

mysql> show create database DeeJay1;
ERROR 1049 (42000): Unknown database 'deejay1'
```
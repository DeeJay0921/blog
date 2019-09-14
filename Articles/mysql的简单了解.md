---
title: mysql的简单了解
date: 2019/04/14 00:00:01
tags: 
- mysql
categories: 
- DataBase
---
mysql的简单了解
<!--more-->

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

### 常用的数据库crud操作
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
- 其他数据库操作
    - 切换数据库（选中数据库）
    ```
    use 数据库名;
    ```
    - 查看当前正在使用的数据库
    ```
    select database();
    ```
    ```
    mysql> create database base1;
    Query OK, 1 row affected (0.15 sec)
    
    mysql> select database();
    +------------+
    | database() |
    +------------+
    | NULL       |
    +------------+
    1 row in set (0.00 sec)
    
    mysql> use base1;
    Database changed
    mysql> select database();
    +------------+
    | database() |
    +------------+
    | base1      |
    +------------+
    1 row in set (0.00 sec)
    ```
### 常用的表的crud操作
#### 创建表
```
create table 表名(
    列名 列的类型 约束,
    列名2 列2的类型 约束
);
```
> 列的类型(和java做下对比)：

java | sql
-- | --
int | int 
char/String | char/varchar
double | double
float | float
boolean | boolean
date | date(YYYY-MM-DD)/time(hh:mm:ss)/datetime(YYYY-MM-DD hh:mm:ss 默认为null)/timestamp(YYYY-MM-DD hh:mm:ss 默认为当前时间)

*char: 固定长度  varchar:可变长度*
数据库中的列类型还有`text`(主要用来存储文本)和`blob`(主要用来存放二进制)

> 列的约束：
主键约束： primary key
唯一约束： unique
非空约束： not null

创建表举例：
1. 分析实体： 学生
2. 学生ID
3. 姓名
4. 性别
5. 年龄

```
mysql> create table student(studentId int primary key, name varchar(25), gender boolean, age int);
Query OK, 0 rows affected (0.72 sec)
```
#### 查看表
- 查看所有的表
```
show tables;
```
```
mysql> show tables;
+-----------------+
| Tables_in_base1 |
+-----------------+
| student         |
+-----------------+
1 row in set (0.07 sec)
```
- 查看表的定义
```
show create table 表名;
```
```
mysql> show create table student;
+---------+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Table   | Create Table
                                                                                                                   |
+---------+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| student | CREATE TABLE `student` (
  `studentId` int(11) NOT NULL,
  `name` varchar(25) DEFAULT NULL,
  `gender` tinyint(1) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`studentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 |
+---------+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
1 row in set (0.03 sec)
```
- 查看表结构
```
desc 表名;
```
```
mysql> desc student;
+-----------+-------------+------+-----+---------+-------+
| Field     | Type        | Null | Key | Default | Extra |
+-----------+-------------+------+-----+---------+-------+
| studentId | int(11)     | NO   | PRI | NULL    |       |
| name      | varchar(25) | YES  |     | NULL    |       |
| gender    | tinyint(1)  | YES  |     | NULL    |       |
| age       | int(11)     | YES  |     | NULL    |       |
+-----------+-------------+------+-----+---------+-------+
4 rows in set (0.04 sec)
```
#### 修改表

```
alter table 表名 修改的类型 列名 列的类型 列的约束;
```
> 修改的类型: 添加列(add), 修改列(modify), 修改列名(change), 删除列(drop), 修改表名(rename)
- 添加列(add)

```
mysql> alter table student add score int not null; // 给student添加一列成绩，为int型，并且不能为null
Query OK, 0 rows affected (0.23 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> desc student;
+-----------+-------------+------+-----+---------+-------+
| Field     | Type        | Null | Key | Default | Extra |
+-----------+-------------+------+-----+---------+-------+
| studentId | int(11)     | NO   | PRI | NULL    |       |
| name      | varchar(25) | YES  |     | NULL    |       |
| gender    | tinyint(1)  | YES  |     | NULL    |       |
| age       | int(11)     | YES  |     | NULL    |       |
| score     | int(11)     | NO   |     | NULL    |       |
+-----------+-------------+------+-----+---------+-------+
5 rows in set (0.00 sec)
```

- 修改列(modify)
```
mysql> alter table student modify gender int;// 修改student的gender列的类型为int          
Query OK, 0 rows affected (0.75 sec)                       
Records: 0  Duplicates: 0  Warnings: 0                     
                                                           
mysql> desc student;                                       
+-----------+-------------+------+-----+---------+-------+ 
| Field     | Type        | Null | Key | Default | Extra | 
+-----------+-------------+------+-----+---------+-------+ 
| studentId | int(11)     | NO   | PRI | NULL    |       | 
| name      | varchar(25) | YES  |     | NULL    |       | 
| gender    | int(11)     | YES  |     | NULL    |       | 
| age       | int(11)     | YES  |     | NULL    |       | 
| score     | int(11)     | NO   |     | NULL    |       | 
+-----------+-------------+------+-----+---------+-------+ 
5 rows in set (0.00 sec)                                   
```

- 修改列名(change)
```
mysql> alter table student change name studentname varchar(25);
Query OK, 0 rows affected (0.12 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> desc student;
+-------------+-------------+------+-----+---------+-------+
| Field       | Type        | Null | Key | Default | Extra |
+-------------+-------------+------+-----+---------+-------+
| studentId   | int(11)     | NO   | PRI | NULL    |       |
| studentname | varchar(25) | YES  |     | NULL    |       |
| gender      | int(11)     | YES  |     | NULL    |       |
| age         | int(11)     | YES  |     | NULL    |       |
| score       | int(11)     | NO   |     | NULL    |       |
+-------------+-------------+------+-----+---------+-------+
5 rows in set (0.00 sec)
```
- 删除列(drop)
```
mysql> alter table student drop score;
Query OK, 0 rows affected (0.68 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> desc student;
+-------------+-------------+------+-----+---------+-------+
| Field       | Type        | Null | Key | Default | Extra |
+-------------+-------------+------+-----+---------+-------+
| studentId   | int(11)     | NO   | PRI | NULL    |       |
| studentname | varchar(25) | YES  |     | NULL    |       |
| gender      | int(11)     | YES  |     | NULL    |       |
| age         | int(11)     | YES  |     | NULL    |       |
+-------------+-------------+------+-----+---------+-------+
4 rows in set (0.00 sec)
```
- 修改表名(rename), 修改表的字符集
这两种方法一般不常用，慎用。
```
rename table student to normalstudent;
```
```
alter table student character set gbk;
```

#### 删除表
```
drop table 表名;
```
```
mysql> drop table student;
Query OK, 0 rows affected (0.24 sec)

mysql> show tables;
Empty set (0.00 sec)
```

### sql对表中数据的CRUD操作
现有如下结构的一个表:
```
mysql> desc student;
+--------+-------------+------+-----+---------+-------+
| Field  | Type        | Null | Key | Default | Extra |
+--------+-------------+------+-----+---------+-------+
| id     | int(11)     | NO   | PRI | NULL    |       |
| name   | varchar(25) | YES  |     | NULL    |       |
| ismale | tinyint(1)  | YES  |     | NULL    |       |
| age    | int(11)     | YES  |     | NULL    |       |
+--------+-------------+------+-----+---------+-------+
4 rows in set (0.00 sec)
```
#### 插入数据
```
insert into 表名(列名1,列名2,列名3) values(值1, 值2, 值3);
```
```
mysql> insert into student(id, name, ismale, age) values(1, "zhangsan", true, 20);
Query OK, 1 row affected (0.08 sec)

mysql> select * from student;
+----+----------+--------+------+
| id | name     | ismale | age  |
+----+----------+--------+------+
|  1 | zhangsan |      1 |   20 |
+----+----------+--------+------+
1 row in set (0.00 sec)
```
对于上述的表中所有列都插入数据的情况，可以简写为：
```
insert into 表名 values(值1, 值2, 值3);
```
```
mysql> insert into student values(2, "lisi", true, 18);
Query OK, 1 row affected (0.08 sec)

mysql> select * from student;
+----+----------+--------+------+
| id | name     | ismale | age  |
+----+----------+--------+------+
|  1 | zhangsan |      1 |   20 |
|  2 | lisi     |      1 |   18 |
+----+----------+--------+------+
2 rows in set (0.00 sec)
```
> 也可以灵活指定想插入的列。

批量插入：
```
insert into 表名 values(值1, 值2, 值3),(值1, 值2, 值3),(值1, 值2, 值3);
```
> 单条插入和批量插入的效率问题： 批量会比较快，但是如果某一条数据有问题的话会都失败。
```
mysql> insert into student values(3, "Yang", true, 23),(4, "Wen", false, 25);
Query OK, 2 rows affected (0.06 sec)
Records: 2  Duplicates: 0  Warnings: 0

mysql> select * from student;
+----+----------+--------+------+
| id | name     | ismale | age  |
+----+----------+--------+------+
|  1 | zhangsan |      1 |   20 |
|  2 | lisi     |      1 |   18 |
|  3 | Yang     |      1 |   23 |
|  4 | Wen      |      0 |   25 |
+----+----------+--------+------+
4 rows in set (0.00 sec)
```

#### 删除记录
```sql
delete from 表名 [where 条件]
```
```text
mysql> delete from student where name='zhangsan';
Query OK, 1 row affected (0.06 sec)

mysql> select * from student;
+----+------+--------+------+
| id | name | ismale | age  |
+----+------+--------+------+
|  2 | lisi |      1 |   18 |
|  3 | Yang |      1 |   23 |
|  4 | Wen  |      0 |   25 |
+----+------+--------+------+
3 rows in set (0.00 sec)
```
> `delete from student;`如果没有where条件，执行这个语句的话会将数据一条一条全部删除。

> delete删除数据和truncate删除数据的区别:delete属于DML,是一条一条删除表中数据, truncate属于DDL，先去删除表再去重建表。
> 如果数据较少那么delete较快，如果数据多，那么truncate较快。 
#### 修改表记录
```sql
update 表名 set 列名=列的值,列名2=值2 [where 条件]
```
```text
mysql> update student set name="zhangsan" where id=2; // 将id为2的那一列的name改为zhangsan
Query OK, 1 row affected (0.06 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from student;
+----+----------+--------+------+
| id | name     | ismale | age  |
+----+----------+--------+------+
|  2 | zhangsan |      1 |   18 |
|  3 | Yang     |      1 |   23 |
|  4 | Wen      |      0 |   25 |
+----+----------+--------+------+
3 rows in set (0.00 sec)
```
> `update student set name="zhangsan",ismale=false;`如果后面不跟where条件，那么表中所有的列的name和ismale列都会改。

#### 查询记录
```
select [distinct] [ * ] [列名1,列名2] from 表名 [where 条件];
```
> distinct: 去除重复的数据 [distinct详细用法](https://www.cnblogs.com/shiluoliming/p/6604407.html)


首先创建一个商品分类的表：
```sql
create table category(
  cid int primary key auto_increment,
  cname varchar(25),
  cdesc varchar(31)
);
```
然后插入数据：
```sql
insert into category values(null, "手机数码", "电子产品");
insert into category values(null, "鞋靴箱包", "江南皮鞋厂打造");
insert into category values(null, "香烟酒水", "芙蓉王，茅台");
insert into category values(null, "酸奶饼干", "哇哈哈");
insert into category values(null, "馋嘴零食", "瓜子花生");
```
现在这个表的结构为：
```text
mysql> select * from category;
+-----+--------------+-----------------------+
| cid | cname        | cdesc                 |
+-----+--------------+-----------------------+
|   1 | 手机数码     | 电子产品              |
|   2 | 鞋靴箱包     | 江南皮鞋厂打造        |
|   3 | 香烟酒水     | 芙蓉王，茅台          |
|   4 | 酸奶饼干     | 哇哈哈                |
|   5 | 馋嘴零食     | 瓜子花生              |
+-----+--------------+-----------------------+
5 rows in set (0.00 sec)
```
对于指定列的查询：
```text
mysql> select cname,cdesc from category;
+--------------+-----------------------+
| cname        | cdesc                 |
+--------------+-----------------------+
| 手机数码     | 电子产品              |
| 鞋靴箱包     | 江南皮鞋厂打造        |
| 香烟酒水     | 芙蓉王，茅台          |
| 酸奶饼干     | 哇哈哈                |
| 馋嘴零食     | 瓜子花生              |
+--------------+-----------------------+
5 rows in set (0.00 sec)
```
再来创建一张商品的表，商品和商品分类的关系为所属关系:
```sql
create table product(
  pid int primary key auto_increment,
  pname varchar(25),
  price double, // 价格
  pdate timestamp, // 生产日期
  cno int // 商品分类的id
);
```
插入一些数据：
```sql
insert into product values(null, "小米mix2s", 2700, current_timestamp, 1);
insert into product values(null, "华为p30", 4788, current_timestamp, 1);
insert into product values(null, "阿迪王", 99, current_timestamp, 2);
insert into product values(null, "老村长", 88, current_timestamp, 3);
insert into product values(null, "劲酒", 35, current_timestamp, 3);
insert into product values(null, "小熊饼干", 3, current_timestamp, 4);
insert into product values(null, "卫龙辣条", 1, current_timestamp, 5);
insert into product values(null, "旺旺雪饼", 2, current_timestamp, 5);
```
当前product表为：
```text
mysql> select * from product;
+-----+--------------+-------+---------------------+------+
| pid | pname        | price | pdate               | cno  |
+-----+--------------+-------+---------------------+------+
|   4 | 小米mix2s    |  2700 | 2019-04-14 13:14:31 |    1 |
|   5 | 华为p30      |  4788 | 2019-04-14 13:14:32 |    1 |
|   6 | 阿迪王       |    99 | 2019-04-14 13:14:32 |    2 |
|   7 | 老村长       |    88 | 2019-04-14 13:14:32 |    3 |
|   8 | 劲酒         |    35 | 2019-04-14 13:14:32 |    3 |
|   9 | 小熊饼干     |     3 | 2019-04-14 13:14:32 |    4 |
|  10 | 卫龙辣条     |     1 | 2019-04-14 13:14:32 |    5 |
|  11 | 旺旺雪饼     |     1 | 2019-04-14 13:14:32 |    5 |
+-----+--------------+-------+---------------------+------+
8 rows in set (0.00 sec)
```
- 简单查询
    - 查询所有商品: 
        ```
        select * from product;
        ```
    - 查询商品名称和价格
        ```
        select pname,price from product;
        ```
    - 别名查询. `as`关键字， as可以省略
        - 表别名, `select p.pname, p.price from product as p`（主要用于多表查询）
            ```sql
             select p.pname, p.price from product as p;
            ```
        - 列别名, `select pname as 商品名称, price as 商品价格 from product;`
            ```text
            mysql> select pname as 商品名称, price as 商品价格 from product;
            +--------------+--------------+
            | 商品名称     | 商品价格     |
            +--------------+--------------+
            | 小米mix2s    |         2700 |
            | 华为p30      |         4788 |
            | 阿迪王       |           99 |
            | 老村长       |           88 |
            | 劲酒         |           35 |
            | 小熊饼干     |            3 |
            | 卫龙辣条     |            1 |
            | 旺旺雪饼     |            1 |
            +--------------+--------------+
            8 rows in set (0.00 sec)
            ```
            可以看到，输出的列别名已经发生了更改。
    - 去掉重复的值
        - 查询商品所有的价格:
            ```text
            mysql> select price from product;
            +-------+
            | price |
            +-------+
            |  2700 |
            |  4788 |
            |    99 |
            |    88 |
            |    35 |
            |     3 |
            |     1 |
            |     1 |
            +-------+
            8 rows in set (0.00 sec)
            ```
            输出的值有些是重复的，那么我们可以引入distinct来去掉重复的值：
            ```text
            mysql> select distinct price from product;
            +-------+
            | price |
            +-------+
            |  2700 |
            |  4788 |
            |    99 |
            |    88 |
            |    35 |
            |     3 |
            |     1 |
            +-------+
            7 rows in set (0.00 sec)
            ```
    - select查询运算
    
        假设我们现在所有的商品都要打8折，那么可以这样查询输出:
        ```
        mysql> select *,price*0.8 from product;
        +-----+--------------+-------+---------------------+------+--------------------+
        | pid | pname        | price | pdate               | cno  | price*0.8          |
        +-----+--------------+-------+---------------------+------+--------------------+
        |   4 | 小米mix2s    |  2700 | 2019-04-14 13:14:31 |    1 |               2160 |
        |   5 | 华为p30      |  4788 | 2019-04-14 13:14:32 |    1 |             3830.4 |
        |   6 | 阿迪王       |    99 | 2019-04-14 13:14:32 |    2 |               79.2 |
        |   7 | 老村长       |    88 | 2019-04-14 13:14:32 |    3 |               70.4 |
        |   8 | 劲酒         |    35 | 2019-04-14 13:14:32 |    3 |                 28 |
        |   9 | 小熊饼干     |     3 | 2019-04-14 13:14:32 |    4 | 2.4000000000000004 |
        |  10 | 卫龙辣条     |     1 | 2019-04-14 13:14:32 |    5 |                0.8 |
        |  11 | 旺旺雪饼     |     1 | 2019-04-14 13:14:32 |    5 |                0.8 |
        +-----+--------------+-------+---------------------+------+--------------------+
        8 rows in set (0.00 sec)
        ```
        可以加一个列别名:
        ```text
        mysql> select *,price*0.8 as 8折折后价 from product;
        +-----+--------------+-------+---------------------+------+--------------------+
        | pid | pname        | price | pdate               | cno  | 8折折后价          |
        +-----+--------------+-------+---------------------+------+--------------------+
        |   4 | 小米mix2s    |  2700 | 2019-04-14 13:14:31 |    1 |               2160 |
        |   5 | 华为p30      |  4788 | 2019-04-14 13:14:32 |    1 |             3830.4 |
        |   6 | 阿迪王       |    99 | 2019-04-14 13:14:32 |    2 |               79.2 |
        |   7 | 老村长       |    88 | 2019-04-14 13:14:32 |    3 |               70.4 |
        |   8 | 劲酒         |    35 | 2019-04-14 13:14:32 |    3 |                 28 |
        |   9 | 小熊饼干     |     3 | 2019-04-14 13:14:32 |    4 | 2.4000000000000004 |
        |  10 | 卫龙辣条     |     1 | 2019-04-14 13:14:32 |    5 |                0.8 |
        |  11 | 旺旺雪饼     |     1 | 2019-04-14 13:14:32 |    5 |                0.8 |
        +-----+--------------+-------+---------------------+------+--------------------+
        8 rows in set (0.00 sec)
        ```
        上述的输出，其实**并不改变库内的数据，仅仅是在查询结果上做了运算**。
    - where关键字进行条件查询
        
        指定条件，确定要查询记录
        - 查询价格大于60的商品：`select * from where price > 60;`
        - where后的关系运算符：`> >= < <= = != <>` <>和!=都为不等于，区别在于<>是标准sql语法而!=是非标准的sql语法
        - where后的逻辑运算符： `and, or, not`，判断某一列是否为空: `is null`, `is not null`
        - 查询价格大于10并且小于100的商品: ` select * from product where price < 100 and price > 10;`
        也可以使用`between...and...`,`select * from product where price between 10 and 100;`
        - like 模糊查询
            - `_`代表的是一个字符
            - `%`代表的是多个字符
            - 查询出商品名称中带有"小"的所有商品：`select * from product where pname like "%小%";`
                ```text
                mysql> select * from product where pname like "%小%";
                +-----+--------------+-------+---------------------+------+
                | pid | pname        | price | pdate               | cno  |
                +-----+--------------+-------+---------------------+------+
                |   4 | 小米mix2s    |  2700 | 2019-04-14 13:14:31 |    1 |
                |   9 | 小熊饼干     |     3 | 2019-04-14 13:14:32 |    4 |
                +-----+--------------+-------+---------------------+------+
                2 rows in set (0.00 sec)
                ```
            - 查询名字第二个字是'熊'的商品:`select * from product where pname like "_熊%";`
                ```
                mysql> select * from product where pname like "_熊%";
                +-----+--------------+-------+---------------------+------+
                | pid | pname        | price | pdate               | cno  |
                +-----+--------------+-------+---------------------+------+
                |   9 | 小熊饼干     |     3 | 2019-04-14 13:14:32 |    4 |
                +-----+--------------+-------+---------------------+------+
                1 row in set (0.00 sec)
                ```
        - in 在某个范围中获得值
            - 查询出商品分类id在1,4,5里面的所有商品`select * from product where cno in (1,4,5);`
    - 排序查询：order by 关键字. 关键字: asc(ascend升序,为默认值), desc(descend降序).
        - 查询所有商品进行按价格降序排列: `select * from product order by price desc;`
        - 查询商品名称中含有"小"的所有商品并且进行按价格降序排列: `select * from product where pname like "%小%" order by price desc;`

- 复杂查询
    - 聚合函数：
    
    函数 | 作用
    sum() | 求和
    count() | 统计数量
    max() | 最大值
    min() | 最小值
    avg() | 平均值
    
    - 获得所有商品价格的总和：`select sum(price) from product;`
    - 获得所有商品价格的平均值：`select avg(price) from product;`
    - 获得所有商品的个数：`select count(*) from product;`
    
    > 需要注意的是：**where后面不能接聚合函数！**
    
    由于where后面不能加聚合函数，所以如果想要查询到价格大于平均价格的所有商品时。需要这么写（子查询）:
    ```sql
    select * from product where price > (select avg(price) from product);
    ```
    ```text
    mysql> select * from product where price > (select avg(price) from product);
    +-----+-------------+-------+---------------------+------+
    | pid | pname       | price | pdate               | cno  |
    +-----+-------------+-------+---------------------+------+
    |   4 | 小米mix2s   |  2700 | 2019-04-14 13:14:31 |    1 |
    |   5 | 华为p30     |  4788 | 2019-04-14 13:14:32 |    1 |
    +-----+-------------+-------+---------------------+------+
    2 rows in set (0.00 sec)
    ```
    - 分组： group by
         - 根据cno字段去进行分组，统计各个种类商品的个数：
         ```sql
        select cno as 商品种类,count(*) as 商品个数 from product group by cno;
        ```
        ```text
        mysql> select cno as 商品种类,count(*) as 商品个数 from product group by cno;
        +--------------+--------------+
        | 商品种类     | 商品个数     |
        +--------------+--------------+
        |            1 |            2 |
        |            2 |            1 |
        |            3 |            2 |
        |            4 |            1 |
        |            5 |            2 |
        +--------------+--------------+
        5 rows in set (0.00 sec)
        ```
        - 根据cno分组，分组统计每组商品的平均价格，并且商品平均价格>60
        直接根据cno分组，分组统计每组商品的平均价格可以这样写：
        ```sql
        select cno as 商品种类, avg(price) as 平均价格 from product group by cno;
        ```
        ```text
        mysql> select cno as 商品种类, avg(price) as 平均价格 from product group by cno;
        +--------------+--------------+
        | 商品种类     | 平均价格     |
        +--------------+--------------+
        |            1 |         3744 |
        |            2 |           99 |
        |            3 |         61.5 |
        |            4 |            3 |
        |            5 |            1 |
        +--------------+--------------+
        5 rows in set (0.00 sec)
        ```
        还有过滤掉平均价格不大于60的商品，要使用`having`关键字：
        ```sql
        select cno as 商品种类, avg(price) as 平均价格 from product group by cno having avg(price) > 60;
        ```
        ```text
        mysql> select cno as 商品种类, avg(price) as 平均价格 from product group by cno having avg(price) > 60;
        +--------------+--------------+
        | 商品种类     | 平均价格     |
        +--------------+--------------+
        |            1 |         3744 |
        |            2 |           99 |
        |            3 |         61.5 |
        +--------------+--------------+
        3 rows in set (0.00 sec)
        ```
        > having关键字，可以接聚合函数，并且出现在分组之后
        
        > where关键字，不可以接聚合函数，并且出现在分组之前
        
#### sql语句的编写顺序和执行顺序

- 编写顺序： 
select ... from ... where ... group by ... having ... order by ...
- 执行顺序：
from ... where ... group by ... having ... select ... order by ...

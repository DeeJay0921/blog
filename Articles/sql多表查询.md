# sql会创建多表以及多表的关系
## 需求：
[上篇文章](https://www.jianshu.com/p/3f3315a453d7)中的商品表和分类表之间存在着所属关系，在数据库如何表示这种关系
## 分析：
### 多表之间的关系如何来维护
- 外键约束：假设现在在product表中插入一条cno为99的数据，但是cno为99的列在category中并不存在。这时候就需要**外键约束**，`foreign key`
    - 给product这个表中的cno添加一个外键约束: `alter table product add foreign key(cno) references category(cid);` 添加外键约束之后，再向product中插入category中没有的种类会失败。
> 添加外键约束之后，如果想删除category中的某一列，也会报错，因为product中有记录在引用。首先得去product表中删除引用该种类的所有记录，才能去删除category中的相应列。

### 建立数据库的原则
通常情况下，一个应用一个数据库。

### 多表之间的建表原则
#### 一对多
例子中提到的product和category就是一对多的关系，一个种类对应多个商品
- 建表原则： 在**多的一方(product)添加一个外键(cno)指向一的一方(category)的主键(cid)**
#### 多对多
例如：老师和学生，学生和课程等
student表 id name gender age
subject表 id name teacher
student表中的学生选了不固定的subject表里的科目，这种关系为多对多
- 建表原则： 需要多建一张中间表，将多对多的关系拆为一对多的关系，中间表**至少要有2个外键**，这2个外键分别指向原来的那张表。
在上述中间表中，至少需要2个外键，即学生的id和科目的id，其中对于中间表的学生id来说，student表中和中间表的学生id为一对多(即category和product的关系，student为一，中间表学生id为多)，而对于中间表中课程编号，跟subject，也是一样的(subject的id对中间表的科目编号也为一对多)。
#### 一对一
例如公民和身份证的关系：
People表： id name income
IDcard表：sid 头像 性别
- 可以直接合并2张表
- 可以在一张表中新增列，作为外键，当初一对多的情况来处理，这个外键指向另外一张表。
- 将2张表的主键建立起连接，让2张表主键相等。

> 实际用途不多，比如说拆表操作(将表中的一些列拆分出来)


> 关于主键约束和唯一约束的区别：
主键约束：默认不能为空且唯一，并且不能有多个主键
唯一约束：默认为空且唯一，可以有多个唯一约束键
外键都是指向另一张表的**主键**,而唯一的约束不可以作为其他表的外键


### 多表查询

在一个商城应用中，有几个表之前的数据相互关联：user表 order表 product表 category表 以及orderItem表

#### 多表查询的几种类型：
- 交叉连接查询 笛卡尔积
对于上述的product表以及category表，如果想要一次查询2张表的内容，我们可以输入：
```
select * from product,category;
```
```
笛卡尔积
```
> 上述例子中查出来的结果称为笛卡尔积即2张表的乘积，没什么实际意义。其实我们需要的就是cno等于cid的那些数据，所以需要做一下过滤。
```
select * from  product,category where cno=cid;
```

> 对于上述查询，经常会发现cno和cid属于哪个表不明确，可以通过起个别名的方式来指明是哪个表: `select * from product as P,category as C where p.cno=c.cid;`
- 内连接查询
对于上述例子，使用where进行条件过滤的为隐式内连接，
    - 隐式内连接
    ```
    select * from product p,category c where p.cno=c.cid;
    ```
    - 显式内链接（使用inner join）
    ```
    select * from product p inner join category on p.cno=c.cid;
    ```
> 上述2种方式查询出来的结果都是一样的，但是区别在于，隐式内连接是在查询出的结果基础上进行的where条件过滤，但是显式内连接是带着条件去查询结果的，**执行效率是比较高的**。
    
- 左外连接（使用 left join）
左(外)连接，左表的记录将会全部表示出来，而右表**只会显示符合搜索条件的记录**。右表记录不足的地方均为NULL。
对于上述例子，我们先给product表插入一条没有对应cno的数据。
```
insert into product values(null, "一条没有cno的记录", 100, null, null);
```
然后执行左外连接查询：
```
select * from product p left join category c on p.cno=c.cid;
```
可以看到右表的这一条的输出都为null，而左表的数据都输出了。
- 右外连接（使用right join）
和左外连接相对的，右外连接查询会将右表的所有数据查询出来，如果左表没有对应数据的话会用null代替

> left join和right join分别为left outer join和right outer join的缩写 inner/outer在语句中可以省略

> 简单理解： 内连接查出来的是2个表的交集，左外和右外其实查询了左表或者右表的全部，并且如果有相对应的左表记录的话也会显示。

- 全连接（使用union /union all）
全连接指的是将2个表合并。
语句：
`(select colum1,colum2...columN from tableA ) union (select colum1,colum2...columN from tableB );`
` (select colum1,colum2...columN from tableA ) union all (select colum1,colum2...columN from tableB );`

>  通过union连接的SQL它们分别单独取出的列数必须相同；不要求合并的表列名称相同时，以第一个sql 表列名为准；使用union 时，完全相等的行，将会被合并，由于合并比较耗时，一般不直接使用 union 进行合并，而是通常采用union all 进行合并；

> 被union 连接的sql 子句，单个子句中不用写order by ，因为不会有排序的效果。但可以对最终的结果集进行排序；
`(select id,name from A order by id) union all (select id,name from B order by id); //没有排序效果`
` (select id,name from A ) union all (select id,name from B ) order by id; //有排序效果`


#### 分页查询(使用limit)
假设规定pageSize为3
```
select * from product limit 0,3; // 第一页
```
```
select * from product limit 3,3; // 第二页
```
```
select * from product limit 6,3; // 第一页
```
即每次查询根据page和pageSize计算一个起始的索引位置即可：
```
select * from product limit (page-1)*pageSize,pageSize;
```

#### 子查询
即sql语句中再嵌套sql语句

1. 如果要查询分类名为“手机数码”的商品，需要动态查询出在category中的cid
```
select * from product where cno=(select cid from category where cname="手机数码");
```

2. 如果要查询出商品名称(pname)和商品分类名称(cname)的信息
选用左右链接查询都可达到目的，在这介绍下子查询的写法：
```
select pname,(select cname from category as c where p.cno=c.cid) from product as p;
```

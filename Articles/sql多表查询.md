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

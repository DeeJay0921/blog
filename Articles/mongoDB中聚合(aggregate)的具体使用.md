---
title: mongoDB中聚合(aggregate)的具体使用
date: 2018/05/30 00:00:01
cover: https://cdn.tutsplus.com/net/uploads/2014/02/mongodb-retina-preview.png
tags: 
- mongoDB
categories: 
- DataBase
---
mongoDB中聚合(aggregate)的具体使用
<!--more-->

最近在学习mongoDB的使用，本文来介绍一下其中aggregate的具体使用

先来看一个分组的例子，本例中$group是一个管道操作符，获得的结果可以接着输出到下一个管道，而内部的$sum是一个表达式操作符。

## 用$group 举个例子
    将document分组，用作统计结果
    ```
        db.Ubisoft.aggregate([ // aggregate方法接收的是一个数组
            {
                $group: {
                    _id: '$time', 
                    num: {$sum: 1}
                }
            }
        ])
        // 这里的_id字段表示你要基于哪个字段来进行分组(即制定字段值相同的为一组)，这里的$time就表示要基于time字段来进行分组

        // 下面的num字段的值$sum: 1表示的是获取满足time字段相同的这一组的数量乘以后面给定的值(本例为1，那么就是同组的数量)。
    ```

那么看完这个例子之后，mongoDB中还有其他的一些管道操作符和表达式操作符：

### 管道操作符

常用管道 | 含义
-- | -- 
$group | 将collection中的document分组，可用于统计结果
$match | 过滤数据，只输出符合结果的文档
$project | 修改输入文档的结构(例如重命名，增加、删除字段，创建结算结果等)
$sort | 将结果进行排序后输出
$limit | 限制管道输出的结果个数
$skip | 跳过制定数量的结果，并且返回剩下的结果
$unwind | 将数组类型的字段进行拆分

### 表达式操作符

常用表达式 | 含义
-- | -- 
$sum | 计算总和，{$sum: 1}表示返回总和×1的值(即总和的数量),使用{$sum: '$制定字段'}也能直接获取制定字段的值的总和
$avg | 平均值
$min | min
$max | max
$push | 将结果文档中插入值到一个数组中
$first | 根据文档的排序获取第一个文档数据
$last | 同理，获取最后一个数据


了解完这些操作符之后，继续拿`$group`来试试看：
我们现在有一个名为Ubisoft的一个collection，内部的文档为：
```
/* 1 */
{
    "_id" : ObjectId("5b0cf67270e4fa02d31de42e"),
    "name" : "rainbowSix Siege",
    "time" : 400.0
}

/* 2 */
{
    "_id" : ObjectId("5b0cf69270e4fa02d31de42f"),
    "name" : "Assassin's creed",
    "time" : 20.0
}

/* 3 */
{
    "_id" : ObjectId("5b0cf6ad70e4fa02d31de430"),
    "name" : "ghost Recon",
    "time" : 0.0
}

/* 4 */
{
    "_id" : ObjectId("5b0d14c870e4fa02d31de436"),
    "name" : "farCry",
    "time" : 0.0
}
```

我们现在来试试其他的表达式操作符：
```
   db.Ubisoft.aggregate([
       {
           $group: {
               _id: '$time',
               gameName: {$push: '$name'}
           }
       }
   ]) 
```
返回结果为：
```
/* 1 */
{
    "_id" : 20.0,
    "gameName" : [ 
        "Assassin's creed"
    ]
}

/* 2 */
{
    "_id" : 0.0,
    "gameName" : [ 
        "ghost Recon", 
        "farCry"
    ]
}

/* 3 */
{
    "_id" : 400.0,
    "gameName" : [ 
        "rainbowSix Siege"
    ]
}
```
可以看到time字段相同的document被分为了一组，而且使用`$push`表达式，将我们制定的document的name字段的值也放到了一个数组中作为我们在mongoDB语句中制定的gameName的值。

*另外$group中可以制定_id:null, 即可以把所有的document分为一组，可以用于计算平均值之类的操作*

*我们可以用`$指定字段`来表示选定的document的field,另外可以使用`$$ROOT`来表示选定的document的所有内容（例如:`chosenDocument: {$push: '$$ROOT'}`）*

上述例子基本介绍了表达式操作符的用法。


接着来看`$match`

## $match

```
    db.Ubisoft.aggregate([
        {
            $match: {
                time: {$gte: 20} //选取time字段 >=20的document
            }
        }
    ])
```

这就拿到了所有time>=20的document,然后可以通过再接个管道来进行其他操作，比如说我们再接一个`$group`来进行分组，显示筛选出来的所有time>=20的document的个数。
```
    db.Ubisoft.aggregate([
        {
            $match: {
                time: {$gte: 20}
            }
        },
        {
            $group: {
                _id: null, // _id: null表示全选
                totalNum: {$sum: 1}
            }
        }
    ])
```

输出结果为： 
```
/* 1 */
{
    "_id" : null,
    "totalNum" : 2.0
}
```

可以看到time>=20的document的个数为2

## $project  投影

修改输入文档的结构(例如重命名，增加、删除字段，创建结算结果等)

$project和直接使用find()的写法一样：

```
db.Ubisoft.aggregate([
    {
        $project: {
            _id: 0,  //不显示_id字段
        }
    }
])
```
和我们直接写`db.Ubisoft.find({},{'_id': 0})`写法一样

输出结果为： 

```
/* 1 */
{
    "name" : "rainbowSix Siege",
    "time" : 400.0
}

/* 2 */
{
    "name" : "Assassin's creed",
    "time" : 20.0
}

/* 3 */
{
    "name" : "ghost Recon",
    "time" : 0.0
}

/* 4 */
{
    "name" : "farCry",
    "time" : 0.0
}
```

可以看到没有_id字段了。

那么我们现在如果想拿到所有time>=20的document的name字段的话，可以把管道搭配起来用：

```
db.Ubisoft.aggregate([
    {
        $match: {
            time: {$gte: 20}
        }
    },
    {
        $project: {
            _id: 0, // _id不显示
            name: 1 // name是要显示的
        }
    },
    {
        $group: {
            _id: null,
            name: {$push: '$name'}
        }
    }
])
```

输出结果为： 
```
/* 1 */
{
    "_id" : null,
    "name" : [ 
        "rainbowSix Siege", 
        "Assassin's creed"
    ]
}
```

## $sort

`$sort`和我们find()中排序的写法也是一样的。

现在我们想将所有的document按照time降序来排列的话：

和`db.Ubisoft.find().sort({time: -1})`写法是一样的:

```
db.Ubisoft.aggregate([
    {
        $sort: {
            time: -1
        }
    }
])
```

同理，`$sort`也可以和其他管道搭配使用

## $limit $skip

和limit()以及skip()的写法也是一样的。

`db.Ubisoft.find().skip(1).limit(2)`

使用聚合可以写成：
```
db.Ubisoft.aggregate([
    {
        $skip: 1
    },
    {
        $limit: 2
    }
])
```

limit和skip搭配使用可以达到分页的效果。

*注意先写skip在写limit*

## $unwind

`$unwind`管道可以document中的数组类型的字段进行拆分，每条包含数组中的一个值。

- 基本使用

在Ubisoft这个集合里新增如下一条document:
```
/* 5 */
{
    "_id" : ObjectId("5b0e242ed85f6f9cc56da7cc"),
    "name" : "gameList",
    "list" : [ 
        "dota2", 
        "csgo", 
        "ow"
    ]
}
```

我们针对这个document中的list字段来进行`$unwind`

```
db.Ubisoft.aggregate([
    {
        $unwind: '$list' // 指定list字段
    }
])
```

输出结果为：
```
/* 1 */
{
    "_id" : ObjectId("5b0e242ed85f6f9cc56da7cc"),
    "name" : "gameList",
    "list" : "dota2"
}

/* 2 */
{
    "_id" : ObjectId("5b0e242ed85f6f9cc56da7cc"),
    "name" : "gameList",
    "list" : "csgo"
}

/* 3 */
{
    "_id" : ObjectId("5b0e242ed85f6f9cc56da7cc"),
    "name" : "gameList",
    "list" : "ow"
}
```

可以看到unwind是将文档中的数组字段进行拆分，如果有其他文档的list字段也是数组，也会一并拆分。

- 特殊情况下的unwind(空数组，null,非数组，无指定字段)

针对特殊情况，新建一个colletion,内容为：
```
/* 1 */
{
    "_id" : ObjectId("5b0e27fdd85f6f9cc56da7ce"),
    "list" : null
}

/* 2 */
{
    "_id" : ObjectId("5b0e2827d85f6f9cc56da7cf"),
    "list" : []
}

/* 3 */
{
    "_id" : ObjectId("5b0e2834d85f6f9cc56da7d0"),
    "list" : "notArray"
}

/* 4 */
{
    "_id" : ObjectId("5b0e2844d85f6f9cc56da7d1")
}
```

来进行`$unwind`，
```
db.unwind.aggregate([
    {
        $unwind: '$list'
    }
])
```
输出结果为： 
```
/* 1 */
{
    "_id" : ObjectId("5b0e2834d85f6f9cc56da7d0"),
    "list" : "notArray"
}
```

可以看到`[]`,`null`,以及无指定字段的数据都丢失了，

为了不丢失数据，我们可以写成：
```
db.unwind.aggregate([
    {
        $unwind: {
            path: '$list', // path是指定字段
            preserveNullAndEmptyArrays: true //该属性为true即保留
        }
    }
])
```
这次输出结果就保留了null以及空数组，值得关注的就是`preserveNullAndEmptyArrays`这个属性，为true的时候就保留。


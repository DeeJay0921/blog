---
title: Node之Stream
date: 2018/04/23 00:00:01
cover: https://stackabuse.com/content/images/2019/10/xintroduction-to-nodejs-streams-cover.png.pagespeed.ic.3z_2F_1ytW.png
tags: 
- Node
- Stream
categories: 
- Node
---
Node之Stream
<!--more-->

最近在学习Node，本篇文章针对与Node中的stream模块做了一些个人学习过程中的理解与介绍。

## Node中Stream的几种类型

Node中stream类型有：`Readable`,`Writable`,`Duplex`,`Transform`以及`"classic"`。
```
const Stream = require('stream')

const Readable = Stream.Readable
const Writable = Stream.Writable
const Duplex = Stream.Duplex
const Transform = Stream.Transform
```

## stream的优势

来看一个简单的例子：
```
const fs = require('fs')
const stream = require('stream')

fs.readFile(`./test.txt`,(err,chunk) => {
    if(err) return
    console.log(chunk.toString())
})

```
上述例子中，我们想读取目标文件的内容，是先将**所有内容**都读取到内存中，然后再进行处理。这么做的坏处是：
1. 如果目标文件过大，内存会装不下
2. 用户在读取文件完成之前是根本拿不到内容的，体验也会很差。

再来看如果使用stream的例子：
```
fs.createReadStream(`./test.txt`)
    .pipe(process.stdout)
```
使用了stream之后，程序不必把目标文件的所有内容都放到内存中等读取完之后再进行输出，而是可以读一点消费一点到`process.stdout`(或者是其他Writeable流)。节省了内存也不需要过长的等待。

下面来看几种基本类型的流。

### Readable 可读流

##### Readable使用push()推送数据到流中
```
const fs = require('fs')
const stream = require('stream')

let rs = new stream.Readable
rs.push(`hello `)
rs.push(`Readable stream \n`)
rs.push(null) // push(null)告诉rs输出数据结束了

rs.pipe(process.stdout)
```
通过调用push()将数据放入Readable流中供下游消耗，当全部数据都生产出来后，**必须**调用`push(null)`来结束可读流，并且当调用了`push(null)`之后，就不能再调用`push(chunk)`来添加数据了。

##### 使用_read()函数
可读流可以通过push将数据传给流。但是直接push会导致就算没有数据消耗方的时候，数据也会都存在缓存中。更多时候我们会写一个`_read()`函数来进行**按需推送数据**。

```
let n = 97
rs._read = () => {
    rs.push(String.fromCharCode(n))
    n += 1
    if ( n > 'z'.charCodeAt(0)) {
        rs.push(`\n`)
        rs.push(null)
    }   
}

rs.pipe(process.stdout)
```
我们在`_read()`中将abcde...z推送到了流中，但是只有当数据消耗者(本例中`process.stdout`)出现时，数据才会真正实现推送。我们可以通过Readable流的**`rs._readableState.buffer`**来进行查看。如果设置了_read()的话，BufferList中head,tail都是null。说明确实是按需推送的。
此外，在`_read()`中还可以**异步**push数据到流中。

##### Readable流的消耗
Readable流实际上存在2种模式，flowing和paused，具体可以查看[文档](http://nodejs.cn/api/stream.html#stream_two_modes)
对于大部分开发情况，都应该使用flowing为true的情况(我们在开发时，一般是使用rs.pipe(dist)，所以一般都是flowing模式)
一般不建议使用`readable事件`和`read(n)`方法（适用于paused的readable stream）,而是使用`data事件`或者`rs.pipe()`。
```
let rs = new stream.Readable({objectMode: true})
let n = 97
rs._read = function () {
    if(n >= `z`.charCodeAt()) {
        rs.push(`\n`)
        rs.push(null)
    }else {
        setTimeout(() => {
            rs.push(String.fromCharCode(n))
            n += 1
        },200)
    }
}
rs.pipe(process.stdout)
rs.on('data', (chunk) => {
    console.log(`\tdata: ${chunk}`)
})
rs.on('end', () => {
    console.log(`end!\n`)
})
console.log(rs._readableState.buffer)
console.log(rs._readableState.flowing)
```

### Writeable 可写流

Writeable stream作为下游来接受数据,只能流进，不能流出。

##### Writeable stream使用write(chunk)写入数据

```
let ws = fs.createWriteStream(`./test.txt`)
ws.write('hello')
ws.write(`Writeable stream\n`)
ws.end() // end()中还可以传想write的参数  但是end之后不可以继续write
```
使用`write(chunk)`可以写入数据到Writeable stream中，写完之后要调用`end()`方法，`end()`方法中还可以最后传入想传的数据，但是调用了`end()`之后就不可以再次调用`write(chunk)`

关于back-pressure,`ws.write(chunk)`返回false的时候，我们可以监听`drain事件`
##### 定义_write()函数

定义一个`_write()`可以将上游的数据释放到当前可写流中。

```
let ws = new stream.Writable
ws._write = (chunk,encoding,next) => {
    console.log(chunk) // 输出的是buffer
    next()
}
process.stdin.pipe(ws)
```
`_write()`中的next()是一个callback,可以选择传递一个err对象给callback.例如`next(err)`

### Duplex流和Transform流

Duplex 流是同时实现了 Readable 和 Writable 接口的流。

Transform 流是一种 Duplex 流。它的输出与输入是通过某种方式关联的。和所有 Duplex 流一样，变换流同时实现了 Readable 和 Writable 接口。

一般我们通过使用封装好的`through2模块`来使用transform流

`sudo npm install through2`

```
const through = require(`through2`)
let ws = fs.createWriteStream(`./test.txt`)
process.stdin
    .pipe(through( function (chunk,enc,next) {
        chunk = chunk.toString().toUpperCase()
        this.push(chunk)
        next()
    }))
    .pipe(ws)
```
上述例子就是使用了一个封装的transform流来进行数据的处理，处理之后又可以输出给ws使用。这样以来,stdin中的输入会在transform流中转换为大写然后传给ws。

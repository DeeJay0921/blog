---
title: Java-File类详解及IO介绍及使用
date: 2019/09/02 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- File
- IO
categories: 
- Java
---
Java-File类详解及IO介绍及使用
<!--more-->

[我是一个CPU：这个世界慢！死！了！ ](https://www.itcodemonkey.com/article/2110.html)，可以感受到CPU，内存和硬盘之间的速度比较。

## 文件的本质
OS（操作系统）的目的就是可以让上层程序可以通过一种统一的方式去拿到无论什么磁盘里的文件（即**一段字节流**）。
一切文件的本质：
- 一段字节流
    - 文本文件（txt,代码，html等）
    - 二进制文件
- 每个程序负责解释文件中的字节流

即文件本身就是一段字节流，不管是图片、音频还是文本等等，文件的展示形式取决于用什么软件打开，例如播放器可以解析mp3文件等，但是本质上这个文件就是一段字节流。

## 文件和IO

程序和文件之间的双向流读取和输出，和网络连接时，从机器到远程主机上的双向的读取输出流十分相似，所以Java针对这种流抽象出了InputStream和OutputStream。
### InputStream/OutputStream
- 抽象的输入/输出流，无论是：
    - 从文件中读取字节流
    - 从网络中读取字节流
    - 从任何地方读取字节流

> 输入和输出都是以程序为基准来判定的

> 对于文件读取，永远建议使用绝对路径。

## Java中的File类
- File并不代表一个“文件”，它只代表一个“路径”
- 抽象的“文件”路径： 可以指文件也可以指文件夹
- File的常见方法
`isFile()` `isDirectory()` `exists()` `getAbsolutePath()`等等
- 绝对路径和相对路径
相对路径相对的是JVM当前的工作目录，永远建议使用绝对路径。
- 读写文件

## 使用FileInputStream/FileOutputStream读写数据

举个简单的例子，现有一个多行文本，需要提供一个方法将其内容按照一行一行的写为一个`List`，另外的方法将其生成的多行文本的`List`写入文件：

```
public class FileAccessor {
    public static List<String> readFile1(File file) {
//        使用 FileInputStream 读取数据
        List<String> lines = new ArrayList<>();
        if(file.exists()) {
            try(InputStream inputStream = new FileInputStream(file)){
                int readRes = inputStream.read();
                StringBuffer allChars = new StringBuffer();
                while(readRes != -1) { // -1表示读取到文件末尾
                    allChars.append((char) readRes); // 将文件一次读完
                    readRes = inputStream.read();
                }
                String[] split = allChars.toString().split("\r\n"); // 使用换行符将其分隔开
                lines.addAll(Arrays.asList(split));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    public static void writeLinesToFile1(List<String> lines, File file) {
        try(OutputStream outputStream = new FileOutputStream(file)) {
            for (String line : lines) {
                outputStream.write(line.getBytes()); // 直接将一整行转换为byte[]进行写入,无需再一个字节一个字节的写入
                outputStream.write("\r\n".getBytes()); // 每行末尾加上换行符
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String rootPath = System.getProperty("user.dir"); // 获取当前工作目录
        File targetText = new File(rootPath, "text.txt");
        List<String> lines = readFile1(targetText);
        System.out.println("lines = " + lines);
        File anotherText = new File(rootPath, "anotherText.txt");
        writeLinesToFile1(lines, anotherText);
    }
}
```


## NIO
- NIO是Java7之后引入的，解释为：1.new IO 2.Non-blocking IO非阻塞的IO
- NIO中的Path其实就是旧版的File，可以通过toFile方法进行转换
- NIO的Files工具类，提供了walkFileTree等方法

NIO解决的问题是，对于旧版的流式读取，因为其是通过字节流的形式传输的，一个字节一个字节的进行读写，没法插队等等，速度较慢。NIO改为了按块读写，中间块和块是也是没有顺序的，是比旧版IO快的。
#### IO太慢的解决方法
使用BufferedReader/BufferedWriter

### 不用重复造轮子
[FileUtils](http://commons.apache.org/proper/commons-io/javadocs/api-2.5/org/apache/commons/io/FileUtils.html)

[IOutils](http://commons.apache.org/proper/commons-io/javadocs/api-2.5/org/apache/commons/io/IOUtils.html)

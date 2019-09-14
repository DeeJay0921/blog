---
title: Java中的IO流的简单介绍
date: 2018/07/21 00:00:01
tags: 
- Java
- IO
categories: 
- Java
---
Java中的IO流的简单介绍
<!--more-->


IO流用来处理设备之间的数据传输。

- 输出流: FileWriter
- 输入流: FileReader

### java.io.FileWriter的简单使用

- 构造方法
`FileWriter(String fileName)`
- 成员方法
`void write(String str)`
`void flush()`
`void close()`

- 简单使用举例：
  1. 创建对象
  2. 写入内容
  3. flush刷新
  4. close释放
```
import java.io.FileWriter;

public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("D:\\test\\a.txt"); //创建了a.txt文件,然后创建了输出流对象，最后把输出流对象指向了这个a.txt
    
        // 调用输出流对象的写数据的方法
        // 写一个字符串数据
        fw.write("hello IO流"); // 此时内容没有写到文件，而是写到了内存缓冲区。
        fw.flush();// 刷新缓冲区  此时内容写到了文件中
        
        // 释放资源
        fw.close(); // 通知系统释放和该资源相关的文件
    }
}
```
`FileWriter fw = new FileWriter("D:\\test\\a.txt"); `这个语句中使用的是绝对路径，也可以使用相对路径，相对于项目的根目录来进行创建文件。


- 关于`flush()`和`close()`
在进行`close()`的时候，会先检查一下缓冲区，有内容的话会先flush一次，所以其实写入内容少的时候，直接close()也是可以的。
flush() 是单纯的刷新缓冲区。close()是先刷新缓冲区，然后释放。
flush()完之后可以继续write() 但是close()之后不可以write()

#### FilerWriter写数据的方法`write()`
`void write(String str)`
`void write(String str, int index, int len)`写一个字符串中的部分内容
`void write(int ch)`
`void write(char[] chs)`
`void write(char[] chs, int index, int len)`'

```
public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("b.txt"); //相对路径  在项目跟目录下创建了b.txt
        fw.write("Hello IO"); // void write(String str)
        fw.close(); // b.txt: Hello IO
    }
}
```
```
public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("b.txt"); 
        fw.write("Hello IO", 6, 2); // void write(String str, int index, int len)
        fw.close(); // b.txt: IO
    }
}
```
```
public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("b.txt"); 
        fw.write('a'); // void write(int ch)
        fw.write(98);
        fw.close(); // b.txt: ab
    }
}
```
```
public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("b.txt"); 
        char[] chs = {'a','b','c','d'};
        fw.write(chs); // void write(char[] chs)
        fw.close(); // b.txt: abcd
    }
}
```

```
public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("b.txt"); 
        char[] chs = {'a','b','c','d'};
        fw.write(chs,1 , 2); // void write(char[] chs, int index, int len)
        fw.close(); // b.txt: bc
    }
}
```

#### FileWriter写数据的相关问题
- 如何实现换行
加上`\n`即可实现换行，但是windows下的记事本识别的换行符是`\r\n`,所以写成`\r\n`即可
```
public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("b.txt"); 
        for(int i = 0; i < 10; i ++) {
            fw.write("Line " + i);
            fw.write("\r\n"); // 加入\r\n
        }
        fw.close(); // b.txt: 换行 Line 0 到 Line 9
    }
}
```
- 如何实现数据的追加写入
如果要实现追加数据，那么就要使用构造方法`FileWriter(String fileName, boolean append)`
```
// 我们现在b.txt的内容为Line 0 到Line 9, 追加一个Line 10
public class FileWriterDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建输出流对象
        FileWriter fw = new FileWriter("b.txt",true); // FileWriter(String fileName, boolean append)
        fw.write("Line 10");
        fw.close(); // b.txt: 追加了Line 10
    }
}
```


### java.io.FileReader
构造方法   `FileReader(String fileName)`
读数据方法： `int read()` 返回的是读到的字符的值，如果已经读完，那么返回的是-1
```
public class FileReaderDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建读取流对象
        FileReader fr = new FileReader("b.txt");
        
        int readCh = fr.read(); // 返回的是读到的字符对应的值 如果目标文件已经读取完了，那么返回的是 -1
        
        while(readCh != -1) {
            System.out.print( (char)(readCh) ); // 类型转换为char型输出， 使用print()可以看到完整的文件，如果println()就换行了
            readCh = fr.read(); // 继续读下一个字符
        }
        fr.close();
    }
}
```
`int read(char[] cbuf)`: 读取一个字符数组的数据,返回值是实际读取到的字符数组的长度

关于`int read(char[] cbuf)`,如果指定字符数组的长度，文件末尾读取数据时可能最后一次读取到的数据凑不够这个指定的长度，那么返回的就是实际读取到的长度，但是那个字符数组中，只有前面实际读到的字符被替换了，没有读到的字符还是上一次读取时候的值。

一样的，当`int read(char[] cbuf)`返回值为-1时，表示已经读取完
```
public class FileReaderDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        // 创建读取流对象
        FileReader fr = new FileReader("b.txt");
        char[] chs = new char[1024]; // 长度一般写1024及其整数倍
        int length = fr.read(chs);
        
        while(length != -1) {
            System.out.print( new String(chs, 0, length) ); // 转为String时要注意给定length, 否则如果出现这次长度凑不齐1024时，chs后面字符不会替换的情况
            length = fr.read(chs); // 继续读下一个字符数组长度的字符
        }
        fr.close();
    }
}
```

### 文件的复制

可以读写就可以进行文件的复制了

```
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileCopyDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        FileReader fr = new FileReader("b.txt");
        FileWriter fw = new FileWriter("c.txt");
        
        int readCh = fr.read();
        
        while(readCh != -1) {
            fw.write(readCh);
            readCh = fr.read(); 
        }
        // 释放资源
        fw.close();
        fr.close();
    }
}
```
```

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileCopyDemo {
    public static void main(String[] args)  throws IOException { // 抛出IO异常
        FileReader fr = new FileReader("b.txt");
        FileWriter fw = new FileWriter("c.txt");
        
        char[] chs = new char[1024];
        int length = fr.read(chs);
        
        while(length != -1) {
            fw.write(chs, 0 , length); // void write(char[] chs, int index, int len)
            length = fr.read(chs); 
        }
        // 释放资源
        fw.close();
        fr.close();
    }
}
```
### 缓冲流的基本使用(BufferedWriter,BufferedReader)

#### java.io.BufferedWriter
将文本写入字符输出流，缓冲各个字符，从而提供单个字符，数组和字符串的高效输入。

构造方法：`BufferedWriter(Writer out)`  `BufferedWriter(Writer out, int size)`


```
import java.io.IOException;
import java.io.BuffedWriter;
import java.io.BuffedReader;
import java.io.FileWriter;
import java.io.FileReader;


public class BuffedDemo {
    public static void main(String[] args) {
        BufferedWriter bw = new BufferedWriter( new FileWriter("b.txt") ); // 构造方法接受的是一个Writer类型的参数
        
        bw.write("Hello BufferedWriter");
        bw.flush();
        bw.close();
        
    }
}
```
#### java.io.BufferedReader

从字符串输入流中读取文本，缓冲各个字符。从而效率更高、

构造方法：`BufferedReader(Reader in)`  `BufferedReader(Reader in, int size)`

```
public class BuffedDemo {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader( new FileReader("b.txt") );
        
        // 一次读写一个字符
        int ch = br.read();
        while(ch != -1) {
            System.out.print( (char)(ch) );
            ch = br.read();
        }
        
        // 一次读写一个字符数组
        char[] chs = new char[1024];
        int length = br.read(chs);
        while(length != -1) {
            System.out.print( new String(chs, 0, length) );
            length = br.read(chs);
        }
        br.close();
    }
}
```


####　缓冲流的特殊功能
- BufferedWriter  `void newLine()` //写一个换行符，这个换行符由系统决定

我们之前实现换行是通过`write("\r\n");`来实现的。现在通过使用缓冲流，可以直接`newLine();`
```
public class BuffedDemo {
    public static void main(String[] args) {
        BufferedWriter bw = new BufferedWriter( new FileWriter("b.txt") ); 
        
        for(int i = 0; i < 10; i++) {
            bw.write("Line: " + i);
            // bw.write("\r\n");
            bw.newLine();
        }
        bw.flush();
        bw.close();
        
    }
}
```

- BufferedReader  `String readLine()`  // 一次读取一行数据，但是不读取换行符

对于`String readLine()`如果已经读完了所有行的话，那么返回的是一个`null`，所以可以这么写:

```
public class BuffedDemo {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader( new FileReader("b.txt") );
        
        String line = br.readLine();
        while(line != null) {
            System.out.println(line); // 依次输出每一行
            line = br.readLine();
        }
        
        br.close();
    }
}
```






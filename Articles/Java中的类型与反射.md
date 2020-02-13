---
title: Java中的类型与反射
date: 2020/01/10 11:41:01
cover: https://img.php.cn/upload/article/000/000/040/5e40da5630604732.jpg
tags: 
- Java
categories: 
- Java
---
本文主要介绍了Java的类与Class定义以及反射及动态调用
<!--more-->

# 类型与反射

## Java的类与Class

`RTTI(Run-Time Type Identification)` 运行时类型识别

在`Java`中，在任何一个时刻，任何一个对象都清楚的知道自己是什么类型的，通过`obj.getClass()`来获取信息

比如说我是一个接口类型的对象，但是调用`obj.getClass()`也可以获取到当前对象究竟是这个接口的哪个具体的实现类

一个`Class`对象就是一个类的说明书, 在开发时，源代码`.java`文件经过`compile`变为字节码`.class`文件

而`JVM`可以运行这个`.class`字节码文件,得到一些`Class`对象，存放在堆中（`Java7`之前称为永久代PermGen，`Java8`之后称为元空间MetaSpace）

每当新建一个对象的时候，JVM会在堆里开辟一个空间，然后根据对应的编译好的`.class`文件来照着其内部实现去创建一个对象

所以说`Class`对象就是一个类的说明书，在此种情况下，多次创建对象，这些对象都是按照同一份说明书创建出来的

而我们经常使用的`static`变量，则是归属于这个说明书的，所以才能做到只有一个

而 `instanceof`方法的本质，就是去调用当前对象的`getClass()`方法，去和目标类做对比

相似的，当发生强制类型转换时，`JVM`也清楚的知道当前对象和要转换为的类型，所以如果不能转换的话，就会抛出`ClassCastException`

## 类加载与`ClassLoader`

### `Class`对象的声明周期

在第一次被使用时加载，且如果有继承关系的话，比如有这样`WhiteCat extends Cat extends Animal`的继承关系

那么我们直接`new WhiteCat()`时，要加载`WhiteCat`类，必须要先去加载`Cat`，要加载`Cat`，就必须先去加载`Animal`

所以加载顺序为`Animal -> Cat -> WhiteCat`

> 如果手动执行编译和运行命令的话，在命令行输入`-verbose:class`可以清晰的看到所以类加载的顺序和信息

完成一个类的加载过程后，JVM中就会将这个类对应的`Class`对象放在堆中的Metaspace中，然后就可以根据这个`Class`对象创建对应的实例对象

### `Class`与`ClassLoader`

上述的加载动作，就是通过`ClassLoader`来做的

- ClassLoader负责从外部系统中加载一个类
    - 这个类对应的Java文件并不一定需要存在（可以凭空的创建一个`.class`文件而不需要经过`.java`文件编译得到`.class`文件）
    - 这个类（即字节码）并不一定需要存在（完全可以通过网络下载等方式得到一个字节流，也完全可以动态生成得到一个字节码）
    
- ClassLoader的双亲委派加载模型

每一个`ClassLoader`对象都有一个`parent`，每当加载一个外部类的时候，当前`ClassLoader`对象总是会去询问其父亲是否加载过该类

如果父亲没加载过，自己才会去尝试加载，因为有这种机制，最底层的类，比如`Java.lang.String`等类都是由最顶端的启动类加载的

这样一来，如果有人也写了一个`Java.lang.String`，那么在加载时，当前的加载对象会在其父亲处拿到一开始加载的`JVM`自己的类

保证了安全性

- Java语言规范与Java虚拟机规范

了解到上述的类加载过程以及`ClassLoader`的特点之后，其实可以发现，`JVM`其实不需要任何`Java`代码就可以运行

Java代码和JVM唯一的联系就是字节码，而字节码可以不通过Java代码编译生成

Java语言规范(Java Language Specification，简称JLS) 表明了当前代码是不是合法的Java代码

Java虚拟机规范(Java Virtual Machine Specification,简称JVMS) 则表明了字节码要如何在虚拟机上跑 

这种分离提供了在JVM上运行其他语言的可能（例如Kotlin等）

## 反射

我们平常写的新建对象，比如`new String(); new ArrayList();`等，都是在编译时期就已经确定好要创建什么样的对象了

那么现在有如下场景：

```java
import java.lang.reflect.InvocationTargetException;

public class WhiteCat extends Cat {
    public static void main(String[] args) {
        genarateObjByArg("java.lang.String");
        genarateObjByArg("java.util.ArrayList");
    }

    public static void genarateObjByArg (String className) {
        // 根据参数传递的名字，创建一个对应的对象
        // 比如传入 Java.lang.String 就创建一个String对象 而传入一个Cat 就创建一个Cat对象
        try {
            Class klass = Class.forName(className); // class为关键字 所以可以命名为klass clazz等
            Object unknownTypeObj = klass.getConstructor().newInstance();
            System.out.println(unknownTypeObj.getClass());
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
```

上述例子达成了一个需求，就是这段代码在执行的时候我们并不知道`genarateObjByArg`方法里面会生成什么类型的对象

而是**动态的去创建了一个指定类型的对象**。

分析一下上述代码，`Class.forName(className)`这个方法的注释为：

```
Returns the {@code Class} object associated with the class or interface with the given string name.
```

即根据指定的`string`返回`MetaSpace`中对应的`Class`对象

拿到这个`Class`对象之后，就可以使用其内部的成员和方法，比如获取一个无参的构造器，`getConstructor()`从而创建一个实例对象

这种方式实现，并没有通过传统的`new Xxx`去创建一个新对象，而是通过一个参数去动态的创建任何能创建的对象

相似的，也可以通过参数动态的决定调用当前对象的哪个方法以及获取对象的成员：

```java
import java.lang.reflect.InvocationTargetException;

public class Cat extends Animal {
    private String catName = "MiaoMiao";

    public void catchMouse() {
        System.out.println("catchMouse");
    }

    public void meow() {
        System.out.println("meow~");
    }

    public static void invokeMethodByArg(String methodName) {
        Cat cat = new Cat();
        try {
            cat.getClass().getMethod(methodName).invoke(cat); // 根据参数动态去决定调用当前cat对象的哪个方法
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void getFieldByArg(String fieldName) {
        Cat cat = new Cat();
        try {
            System.out.println(cat.getClass().getField(fieldName).get(cat)); // 根据参数动态去获取当前cat对象的成员
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        invokeMethodByArg("meow");
        getFieldByArg("catName");
    }
}
```

从上面的例子可以体会出反射的强大

反射在各种框架代码中都有大量应用，比如`Spring`的源码中就有一个`ReflectionUtils`类，内部就有着和上面例子相似的工具方法

### 反射常用`API`

- Class
    - `Class.forName()`
- Method
    - `Method.invoke()`
- Field
    - `Field.get()`
    

### 反射的使用举例

在下例中使用反射完成了一个`JavaBean`到`Map`的互相转换:

```java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapBeanConverter {
    // 传入一个遵守Java Bean约定的对象，读取它的所有属性，存储成为一个Map
    // 例如，对于一个DemoJavaBean对象 { id = 1, name = "ABC" }
    // 应当返回一个Map { id -> 1, name -> "ABC", longName -> false }
    public static Map<String, Object> beanToMap(Object bean) {
        // 读取传入参数bean的Class  通过反射获得它包含的所有名为getXXX/isXXX，且无参数的方法（即getter方法）
        Method[] methods = bean.getClass().getDeclaredMethods();
        Map<String, Object> map = new HashMap<>();
        try {
            for (Method method : methods) {
                if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                    if (method.getParameterCount() == 0) {
                        // 通过反射调用这些方法并将获得的值存储到Map中返回
                        map.put(getFieldNameByMethodName(method.getName()), method.invoke(bean));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String getFieldNameByMethodName(String methodName) {
        String fieldName = null;
        if (methodName.startsWith("get")) {
            fieldName = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            fieldName = methodName.substring(2);
        }
        if (fieldName != null) {
            fieldName = fieldName.replace(fieldName.charAt(0), Character.toLowerCase(fieldName.charAt(0)));
        }
        return fieldName;
    }

    // 传入一个遵守Java Bean约定的Class和一个Map，生成一个该对象的实例
    // 传入参数DemoJavaBean.class和Map { id -> 1, name -> "ABC"}
    // 应当返回一个DemoJavaBean对象 { id = 1, name = "ABC" }
    public static <T> T mapToBean(Class<T> klass, Map<String, Object> map) {
        T t = null;
        try {
            t = klass.newInstance(); // 使用反射创建klass对象的一个实例
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Set<String> keySet = map.keySet(); // 遍历map中的所有键值对，寻找klass中名为setXXX，且参数为对应值类型的方法（即setter方法）
        for (String key : keySet) {
            String methodName = "set" + key.replace(key.charAt(0), Character.toUpperCase(key.charAt(0)));
            try {
                // 使用反射调用setter方法对该实例的字段进行设值
                klass.getMethod(methodName, map.get(key).getClass()).invoke(t, map.get(key));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static void main(String[] args) {
        DemoJavaBean bean = new DemoJavaBean();
        bean.setId(100);
        bean.setName("AAAAAAAAAAAAAAAAAAA");
        System.out.println(beanToMap(bean));

        Map<String, Object> map = new HashMap<>();
        map.put("id", 123);
        map.put("name", "ABCDEFG");
        System.out.println(mapToBean(DemoJavaBean.class, map));
    }

    static class DemoJavaBean {
        Integer id;
        String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isLongName() {
            return name.length() > 10;
        }

        @Override
        public String toString() {
            return "DemoJavaBean{"
                    + "id="
                    + id
                    + ", name='"
                    + name
                    + '\''
                    + ", longName="
                    + isLongName()
                    + '}';
        }
    }
}

```
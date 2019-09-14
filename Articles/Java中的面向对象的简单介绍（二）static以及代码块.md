---
title: Java中的面向对象的简单介绍（二）static以及代码块
date: 2018/07/18 00:00:01
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java中的面向对象的简单介绍（二）static以及代码块
<!--more-->

### static 关键字
- 用途
可以用于修饰成员变量和成员方法。
- 特点
    - 修饰的成员变量/方法，可以被**同类所有的对象所共享**。
来看例子：现有Person类，其创建出来的2个对象p1,p2，当中的成员属性school都是一样的，但是每次创建的时候都需要重新赋值，对于这种情况，可以使用`static`修饰成员变量。
    ```
    public class Person {
	    String name;
	    String school;
	
	    public void show() {
		     System.out.println(name + "---" + school);
	    }
    }

    public class StaticIntro {
	    public static void main(String[] args) {
	    	Person p1 = new Person();
	      	p1.name = "Yang";
		    p1.scholl = "XiDian";
		    p1.show(); // Yang---XiDian
		
	    	Person p2 = new Person();
	    	p2.name = "DeeJay";
	    	p2.scholl = "XiDian";
	    	p2.show(); // DeeJay---XiDian
	    }
    }
    ```

    ```
		public class Person {
			String name;
			static String school;

			public void show() {
				System.out.println(name + "---" + school);
			}
		}


		public class StaticIntro {
			public static void main(String[] args) {
				Person p1 = new Person();
				p1.name = "Yang";
				p1.scholl = "XiDian";
				p1.show(); // Yang---XiDian

				Person p2 = new Person();
				p2.name = "DeeJay";
				//p2.scholl = "XiDian"; // 不需要再次重复的进行成员变量的赋值，static修饰的成员变量是所有对象共享的，p1有了p2就有了
				p2.show(); // DeeJay---XiDian
			}
		}
    ```
    - 可以使用类名调用。
在上述例子中，其实static修饰的成员属性，是可以通过类名调用的。
    ```
		public class Person {
			String name;
			static String school;

			public void show() {
				System.out.println(name + "---" + school);
			}
		}


		public class StaticIntro {
			public static void main(String[] args) {
				Person p1 = new Person();
				p1.name = "Yang";
				Person.scholl = "XiDian"; // 直接使用类名来进行赋值
				p1.show(); // Yang---XiDian

				Person p2 = new Person();
				p2.name = "DeeJay";
				//p2.scholl = "XiDian"; // 不需要再次重复的进行成员变量的赋值，static修饰的成员变量是所有对象共享的，p1有了p2就有了
				p2.show(); // DeeJay---XiDian
			}
		}
    ```
    static是**随着类的加载就已经加载了的，要比创建对象更早**。
- 使用static的注意事项
    - 静态方法**只能**访问静态成员(包括静态的成员方法和静态的成员变量)
    ```
		public class Person {
			String name;
			static String school;

			public static void showSchool () { // 静态的方法 
				System.out.println(school); // 内部访问类中静态的成员属性
				sleep(); // 内部访问类中静态的成员方法
			}
			
			public static void sleep() {
				System.out.println("sleeping~");
			}
		}


		public class StaticIntro {
			public static void main(String[] args) {
				Person.school = "XiDian";
				Person.showSchool(); // XiDian
			}
		}
    ```
    注意**不可以调用没有static修饰的成员**
    - 非静态方法**既可以访问静态也可以访问非静态**（对象已经创建了，当然都能访问）
    ```
		public class Person {
			String name;
			static String school;

			public static void showSchool () { // 静态的方法 
				System.out.println(school); // 内部访问类中静态的成员属性
				sleep(); // 内部访问类中静态的成员方法
			}
			
			public static void sleep() {
				System.out.println("sleeping~");
			}
			
			public void showInfo () { // 非static在对象创建好之后才存在  这时候的成员都可以通过对象访问(不管静态非静态) 内部也有this 
				System.out.println(name + "---" + school);
			}
		}


		public class StaticIntro {
			public static void main(String[] args) {
				Person.school = "XiDian";
				Person.showSchool(); // XiDian
				
				Person p1 = new Person();
				p1.name = "Yang";
				p1.showInfo(); // Yang---XiDian
			}
		}
    ```

    - 另外，`static`修饰的方法中，是**没有**`this`的（对象都没创建，当然没this）

- static的优点
    - 对对象的共享数据提供单独空间的存储，节省空间，没有必要对每一个对象都存储一份
    - 可以直接通过类名进行调用，不用在堆中创建对象
- static的缺点
    - 访问出现局限性(static只能访问static)
- 应用场景
    - 比如说Math工具类，所有的方法都是static的，不需要创建对象(其实构造方法已经被私有了，想创建对象都不行)就可以通过类来进行操作。


### 通过static，自定义一个工具类

```
public class MyArray {
	private MyArray () {} // 私有化构造方法  即禁止创建对象
	
	// 返回int[] 中的最大值
	public static int max (int[] arr) {
		int max = arr[0];
		for(int i = 0; i < arr.length; i ++) {
			if(max < arr[i]) {
				max = arr[i];
			}
			return max;
		}
	}
	
	// 返回当前元素在int[] 中的索引
	public static int getIndex(int[] arr, int element) {
		int index = -1;
		for(int i = 0; i < arr.length; i ++) {
			if(element == arr[i]) {
				index = i;
			}
		}
		return index;
	}
}
```
然后就可以通过类名来进行使用这个工具类了。

### 代码块 
`{}`中的代码被称为代码块。
代码块的分类：
- 局部代码块
存在于方法中，控制变量的生命周期(作用域)
- 构造代码块
提取构造方法中的共性，每次创建对象都会执行，并且**优先于构造方法执行**
    ```
		public class  TestDemo {
			String name;

			public TestDemo () {
				System.out.println("无参空构造");
			}

			public TestDemo (String name) {
				this.name = name;
				System.out.println("有参构造");
			}

			{
				System.out.println("构造代码块");
			}
		}

		public class Test {
			public static void main(String[] args) {
				TestDemo t1 = new TestDemo(); // 构造代码块 无参空构造
				TestDemo t2 = new TestDemo("DeeJay"); // 构造代码块 有参构造
			}
		}
    ```
- 静态代码块
和构造代码块相似，但是如果创建多个对象，只执行一次`static {}`，因为他是跟对象的加载而加载的，跟对象没关系。一般`static {}`用来进行一些加载类的初始化问题。
- 同步代码块(多线程)

注意点： 因为类的加载先于对象的创建，所以**静态代码块也先于构造代码块执行**。

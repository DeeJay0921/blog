# Java8函数式编程

## 为什么要使用函数式编程

有如下优点

1. 减少工作量
2. 提高效率
3. 减少bug

使用函数式编程提高了生产力

## 使用函数式编程的例子

假设现有如下场景：

```java
public class User {
    /** 用户ID，数据库主键，全局唯一 */
    private final Integer id;

    /** 用户名 */
    private final String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 过滤ID为偶数的用户
    public static List<User> filterUsersWithEvenId(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.id % 2 == 0) {
                results.add(user);
            }
        }
        return results;
    }

    // 过滤姓张的用户
    public static List<User> filterZhangUsers(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.name.startsWith("张")) {
                results.add(user);
            }
        }
        return results;
    }

    // 过滤姓王的用户
    public static List<User> filterWangUsers(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.name.startsWith("王")) {
                results.add(user);
            }
        }
        return results;
    }
    // 你可以发现，在上面三个函数中包含大量的重复代码。
    public static List<User> filter(List<User> users, Predicate<User> predicate) {
        return null;
    }
}
```

分析上面的代码，我们可以看到3个过滤方法，内部有着大量的重复逻辑，唯一不同的地方只有`if`语句中的条件判断。

如果我们想对上述代码进行优化，由于不同的点是一个条件判断，如果想将这个条件判断当成一个参数传入方法的话，

不使用函数式编程的话，只能使用`java.util.function.Predicate`接口来进行提取。

来看使用`Predicate`接口进行优化的例子：

```java
public class User {
    // 省略上面代码...
    
    // 过滤ID为偶数的用户
    static  class isEvenId implements Predicate<User> {
        @Override
        public boolean test(User user) {
            return user.id % 2 == 0;
        }
    }
    public static List<User> filterUsersWithEvenId(List<User> users) {
        return filter(users, new isEvenId());
    }

    // 过滤姓张的用户
    static class isZhangUser implements Predicate<User> {
        @Override
        public boolean test(User user) {
            return user.name.startsWith("张");
        }
    }
    public static List<User> filterZhangUsers(List<User> users) {
        return filter(users, new isZhangUser());
    }

    // 过滤姓王的用户
    static class isWangUser implements Predicate<User> { // 实现一个Predicate 内部传入判断条件
        @Override
        public boolean test(User user) {
            return user.name.startsWith("王");
        }
    }

    public static List<User> filterWangUsers(List<User> users) {
        return filter(users, new isWangUser());
    }

    public static List<User> filter(List<User> users, Predicate<User> predicate) {
    //  公共代码提取到公共过滤方法中
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (predicate.test(user)) { // 在这传入不同的条件
                results.add(user);
            }
        }
        return results;
    }
}
```

可以看到，虽然是经过了优化，但是每次传入新条件的时候，总是要声明一个`Predicate`的实现类（也可以不用显式声明，使用匿名内部类也可）

而在Java8之前，没有什么好的办法去进一步优化上述逻辑。

在Java8之后，上述的匿名内部类可以使用`lambda`表达式代替

## `lambda`表达式及方法引用(method reference)

### 基本使用

继续拿上面的例子来讲`lambda`表达式，假设我们现在将其中一个`Predicate`实现类变为了匿名内部类，大概像这样：

```java
public class User {
    // 省略相关代码
    public static List<User> filterUsersWithEvenId(List<User> users) {
        return filter(users, new Predicate<User>() {
            @Override
            public boolean test(User user) {
                return user.id % 2 == 0;
            }
        });
    }
}
```
上述代码可以使用`lambda`表达式进行简化：
```java
public class User {
    // 省略相关代码
    public static List<User> filterUsersWithEvenId(List<User> users) {
        return filter(users, user -> user.id % 2 == 0);
    }
}
```

上面的匿名内部类替换之后改为`user -> user.id % 2 == 0`这样的表达式

其实完整的语法是`(User user) -> {user.id % 2 == 0}`, 

有多个参数的时候`user`后面还可以加入新的参数，而方法体内部的逻辑如果不是单独一句的话，需要加`{}`包裹住所有的逻辑,然后进行显式的`return`（和JS中的箭头函数差不多）

例如：`(User user) -> {return user.id % 2 == 0;}`, 在上例中，由于参数个数只有一个且类型可以推断，且返回值逻辑只有一行，所以简化为上述的样子

> 一般为了保证可读性，`lambda`表达式内部的逻辑要尽可能的短

### 原理分析

下面来分析原因，为什么我们可以将一个匿名内部类转化为一个`lambda`表达式呢？

我们在`filter`方法中传入了一个`Predicate`类型的值，这个`Predicate`本质上是一个将`User`类型映射为`boolean`类型的一个函数。

而任何满足这种映射关系的东西（比如`lambda`表达式，比如**方法引用**等），都可以被转换为这个**函数接口**(FunctionalInterface)。

比如`user -> user.id % 2 == 0`，就满足这种函数映射关系，输入的是一个`User`类型的`user`,输出的是一个`user.id % 2 == 0`的`boolean`值

而下文提到的方法引用，也可以满足这种映射关系

### 方法引用(method reference)

上面的例子，我们除了使用`lambda`表达式，也可以使用方法引用来达到效果：

```java
public class User {
    // 省略不相关代码...
    
    // 过滤ID为偶数的用户
    private static boolean IsUsersWithEvenId(User user) {
        return user.getId() % 2 == 0;
    }
    public static List<User> filterUsersWithEvenId(List<User> users) {
        return filter(users, User::IsUsersWithEvenId);
    }
}
```
在上例中，`IsUsersWithEvenId`这个方法也满足了输入一个`User`类型返回一个`boolean`值的映射关系

所有也可以被转换为一个函数接口，具体的语法是`User::IsUsersWithEvenId`,意为`User`类中定义的`IsUsersWithEvenId`的方法

值得一提的是，上述的方法定义的是静态方法，但是实际上写成实例方法也是可以的：

```java
public class User {
    // 省略不相关代码...
    
    // 过滤ID为偶数的用户
    private boolean IsUsersWithEvenId() {
        return this.getId() % 2 == 0;
    }

    public static List<User> filterUsersWithEvenId(List<User> users) {
        return filter(users, User::IsUsersWithEvenId);
    }
}
```
写成实例方法的话，其实`private boolean IsUsersWithEvenId()`内部是隐式的传递了一个`IsUsersWithEvenId(User this)`的，所以也满足了映射关系

### 方法引用和`lambda`表达式的比较

相比于`lambda`表达式，方法引用的优点在于:
1. 方法是有命名的，准确清晰的方法命名比写注释要好
2. 方法引用内部的逻辑可以写很长，而`lambda`内部尽量不要有太长的逻辑（严格点的话超过一行就不要写`lambda`表达式了）

## 函数接口(FunctionalInterface)介绍

结论: **任何只包含一个抽象方法(没有方法体)的接口都可以被自动转换为一个函数接口**，就算其没有`@FunctionalInterface`注解

我们可以自己写一个接口来进行验证上面这句话：

```java
public class Main {
    /**
     * 验证函数接口满足的条件
     */
    interface SomeRandomFunctionalInterface {
        int abstractMethod (String str);
    }
    
    public static void main(String[] args) {
        test("abc", str -> 1);
    }

    private static void test (String str, SomeRandomFunctionalInterface someRandomFunctionalInterface) {
        if (someRandomFunctionalInterface.abstractMethod(str) == 1) {
            System.out.println("Test Value: 1");
        }else {
            System.out.println("Test Value: 0");
        }
    }
}
```
上述例子同样可以转换为函数接口

而JDK内置了一些常用的函数接口，可以从`java.util.function`包里找到

### 常用的函数接口

除了我们上文提到的`Predicate`之外，还有一些常用的函数接口

#### `Consumer<T>`

首先是`Consumer<T>`,内部只有一个`void accept(T t);`抽象方法

`Consumer<T>`使用举例：

```java
public class Main {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        // forEach方法接收一个Consumer类型的值
        list.forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer number) {
                System.out.println(number);
            }
        });
        // 上述例子使用lambda表达式简化为
        list.forEach(number -> System.out.println(number));
        // 还可以使用方法引用
        list.forEach(System.out::println);
    }
}
```
#### `Function<T, R>`

然后还有`java.util.function.Function<T, R>`,其有一个`R apply(T t)`方法，一般用于把一个类型`T`变换为另一个类型`R`

来看一个使用`Function`的例子：

```java
public class Main {
    public static void main(String[] args) {
        transfrom(123, number -> IntegerToString(number));
        transfrom(123, Main::IntegerToString);
    }

    private static String transfrom(Integer number, Function<Integer, String> function) {
        return function.apply(number);
    }

    private static String IntegerToString(Integer integer) {
        return integer.toString();
    }
}
```

另外值得一提的是，如果是实例方法的话，所有满足映射关系的`getter`都可以作为方法引用

```java
import java.util.function.Function;

public class Main {
    static class User {
        private String name;
        private String address;

        public User(String name, String address) {
            this.name = name;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }

    public static void main(String[] args) {
        // 这里的2个 getter 都满足 下面 Function<User, String> 定义的映射关系 User -> String 所以都可以作为方法引用
        transUserToString(new User("Yang", "5th Street"), User::getName); 
        transUserToString(new User("Yang", "5th Street"), User::getAddress);
    }
    
    public static String transUserToString(User user, Function<User, String> function) {
        return function.apply(user);
    }
}
```

从上例中就可以看出，只要满足映射关系，任意的`getter`都是可以作为方法引用的，因为其内部隐式的传递了该类型的实例(即`this`)

#### `Supplier<T>` 

内部有一个`T get()`, 和`Consumer`是相对的，来看一个使用例子：

```java
public class Main {

    public static void main(String[] args) {
        // 如下的映射都满足 void -> Object 的关系        
        createSomething(() -> "abc");
        createSomething(() -> new ArrayList<>());
        createSomething(ArrayList::new);
        createSomething(() -> 123);
    }
    
    private static Object createSomething(Supplier<Object> objectSupplier) {
        return objectSupplier.get();
    }
}
```

上例中值得一提的是`createSomething(ArrayList::new);`,这个方法引用，因为`new`方法也是从`void`到产生一个对象，所以满足映射

## Comparator

先来看看一个场景:
```java
public class Point implements Comparable<Point>{

    private final int x;
    private final int y;
    // 代表笛卡尔坐标系中的一个点
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point) o;

        if (x != point.x) {
            return false;
        }
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    // 按照先x再y，从小到大的顺序排序
    // 例如排序后的结果应该是 (-1, 1) (1, -1) (2, -1) (2, 0) (2, 1)
    public static List<Point> sort(List<Point> points) {
        Collections.sort(points);
        return points;
    }

    @Override
    public int compareTo(Point point) {
        if (point.getX() < x) {
            return 1;
        }else if(point.getX() > x){
            return -1;
        }

        if (point.getY() < y) {
            return 1;
        }else if(point.getY() > y){
            return -1;
        }

        return 0;
    }

    public static void main(String[] args) throws IOException {
        List<Point> points =
                Arrays.asList(
                        new Point(2, 0),
                        new Point(-1, 1),
                        new Point(1, -1),
                        new Point(2, 1),
                        new Point(2, -1));
        System.out.println(Point.sort(points));
    }
}
```

上述例子为了使得`Point`类可以进行排序，实现了`Comparable`接口，内部的`compareTo`方法对`x`和`y`两个属性依次做了比较。

另外也可以不实现`Comparable`接口,直接调用`Collections.sort()`然后传入一个`Comparator`接口的实现类即可，如：
```
// 省略其他代码...

// 按照先x再y，从小到大的顺序排序
// 例如排序后的结果应该是 (-1, 1) (1, -1) (2, -1) (2, 0) (2, 1)
public static List<Point> sort(List<Point> points) {
    Collections.sort(points, new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            if (o1.getX() < o2.getX()) {
                return 1;
            }else if(o1.getX() > o2.getX()){
                return -1;
            }

            if (o1.getY() < o2.getX()) {
                return 1;
            }else if(o1.getY() > o2.getX()){
                return -1;
            }

            return 0;
        }
    });
    return points;
}
```

但是这样的实现扩展性并不好，如果`Point`类中需要比较的属性很多，就会有很多的类似逻辑的比较

那么对于这种方式的比较，也可以使用函数式的方式将其简化：

```
    public static List<Point> sort(List<Point> points) {
//        Collections.sort(points, new Comparator<Point>() {
//            @Override
//            public int compare(Point o1, Point o2) {
//                if (o1.getX() < o2.getX()) {
//                    return 1;
//                }else if(o1.getX() > o2.getX()){
//                    return -1;
//                }
//
//                if (o1.getY() < o2.getX()) {
//                    return 1;
//                }else if(o1.getY() > o2.getX()){
//                    return -1;
//                }
//
//                return 0;
//            }
//        });
        // Java8 之后 Comparator 引入了一个静态方法 comparing(Function<? super T, ? extends U)
        Collections.sort(points, Comparator.comparing(Point::getX).thenComparing(Point::getY));
        return points;
    }
```

理由`Comparator`的`comparing`方法，我们可以实现用这样的方法引用去简化代码，另外如果想反序排序的话，还可以在`comparing`后面直接调用`.reversed()`

## 补充：另外一些使用函数接口优化代码的例子

优化前：
```java
public class RefactorToConsumer {
    public static void main(String[] args) {
        Map<String, String> map1 =
                Stream.of("a", "b", "c").collect(Collectors.toMap(k -> k, v -> v));
        Map<String, String> map2 =
                Stream.of("d", "e", "f").collect(Collectors.toMap(k -> k, v -> v));

        printWithComma(map1, map2);
        printWithDash(map1, map2);
        printWithColon(map1, map2);
    }

    public static void printWithComma(Map<String, String> map1, Map<String, String> map2) {
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "," + value);
        }

        for (Map.Entry<String, String> entry : map2.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "," + value);
        }
    }

    public static void printWithDash(Map<String, String> map1, Map<String, String> map2) {
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "-" + value);
        }

        for (Map.Entry<String, String> entry : map2.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "-" + value);
        }
    }

    public static void printWithColon(Map<String, String> map1, Map<String, String> map2) {
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ":" + value);
        }

        for (Map.Entry<String, String> entry : map2.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ":" + value);
        }
    }
}
```

可以看到有大量重复代码，提取公共方法进行优化：

```java
public class RefactorToConsumer {
    public static void main(String[] args) {
        Map<String, String> map1 =
                Stream.of("a", "b", "c").collect(Collectors.toMap(k -> k, v -> v));
        Map<String, String> map2 =
                Stream.of("d", "e", "f").collect(Collectors.toMap(k -> k, v -> v));

        printWithComma(map1, map2);
        printWithDash(map1, map2);
        printWithColon(map1, map2);
    }

    public static void printWithConsumer(
            Map<String, String> map1,
            Map<String, String> map2,
            BiConsumer<String, String> consumer) {
        map1.forEach(consumer);
        map2.forEach(consumer);
    }

    public static void printWithComma(Map<String, String> map1, Map<String, String> map2) {
        printWithConsumer(map1, map2, (key, value) -> System.out.println(key + "," + value));
    }

    public static void printWithDash(Map<String, String> map1, Map<String, String> map2) {
        printWithConsumer(map1, map2, (key, value) -> System.out.println(key + "-" + value));
    }

    public static void printWithColon(Map<String, String> map1, Map<String, String> map2) {
        printWithConsumer(map1, map2, (key, value) -> System.out.println(key + ":" + value));
    }
}
```

再看一例，优化前：

```java
public class RefactorToSupplier {
    private static int randomInt() {
        return new Random().nextInt();
    }

    public static void main(String[] args) {
        System.out.println(createObjects());
        System.out.println(createStrings());
        System.out.println(createRandomIntegers());
    }

    public static List<Object> createObjects() {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new Object());
        }
        return result;
    }

    public static List<Object> createStrings() {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add("" + i);
        }
        return result;
    }

    public static List<Object> createRandomIntegers() {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(randomInt());
        }
        return result;
    }
}
```

优化后：
```java
public class RefactorToSupplier {
    private static int randomInt() {
        return new Random().nextInt();
    }

    public static void main(String[] args) {
        System.out.println(createObjects());
        System.out.println(createStrings());
        System.out.println(createRandomIntegers());
    }

    // 使用函数式接口Supplier对三个方法进行重构
    public static List<Object> create(Supplier<Object> supplier) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(supplier.get());
        }
        return result;
    }

    public static List<Object> createObjects() {
        return create(Object::new);
    }

    public static List<Object> createStrings() {
        return create(() -> "");
    }

    public static List<Object> createRandomIntegers() {
        return create(RefactorToSupplier::randomInt);
    }
}
```
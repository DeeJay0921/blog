## Collections类的常见方法
为了处理Collection类的实例对象，java提供了Collections工具类来进行操作。该类为工具类，内部都为static方法。来看常见的几种使用：

### Collections.sort()
对一个有序的List做排序。可以自定义排序规则。
来看最基本的一个使用：
```
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(3);
        arr.add(1);
        arr.add(4);
        arr.add(2);

        System.out.println(arr); // [3, 1, 4, 2]
        Collections.sort(arr);
        System.out.println(arr); // [1, 2, 3, 4]
    }
}
```
如果不指定排序规则会按照默认排序进行排列。再来看自定义规则的一个例子：
```
public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(3);
        arr.add(1);
        arr.add(4);
        arr.add(2);

        System.out.println(arr); // [3, 1, 4, 2]
        Collections.sort(arr, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        System.out.println(arr); // [4, 3, 2, 1]
    }
}
```
Comparator是一个接口，实现其内部的compare方法即可按照给定规则排序。
> 上述排序例子中new Comparator<Integer>() {}是Java的内部匿名类，本身接口是不能直接new的，这个就表示实际上现在new的是这个**接口的实现类**。

### Comparator和Comparable的区别
上述例子中使用了Comparator，和前者一样，Comparable也是一个接口，二者表示的不同的地方在于：
- Comparable是排序接口。若一个类实现了Comparable接口，就意味着该类支持排序
- Comparator是比较接口，我们如果需要控制某个类的次序，而该类本身不支持排序(即没有实现Comparable接口)，那么我们就可以建立一个“该类的比较器”来进行排序，这个“比较器”只需要实现Comparator接口即可。

举个例子来说，我们这次创建一个自定义的可以排序的类。
```

```
上述例子中，SortDemo类实现了Comparable接口1，重写了compareTo方法，从而使得SortDemo类的实例对象可以相互比较排序，直接调用`Collections.sort()`即可按照实现的compareTo方法进行排序，不需要传入额外的比较逻辑。
再来看使用Comparator的例子，这次我们的SortDemo类并不支持排序，调用`Collections.sort()`传入一个实现Comparator的内部匿名类来指定排序规则。
```

```

### Collections.binarySearch

返回指定List和key的index。关于这个方法有地方需要注意:
> 要进行查找，要**先调用Collections.sort()进行自然顺序排序**，不然返回的结果会有问题
要调用Collections.sort()，那**要保证List内部的类型是实现了Comparable接口的**。

对于Integer这种Java已经实现了Comparable的类是可以不做处理的，但是我们自定义的类要进行实现Comparable。

```

```
### Collections.copy(destList, targetList)

这个方法需要注意的地方是，目的List的长度要保证不短于要被克隆的targetList

```

```

### Collections.fill() Collections.reverse() Collections.shuffle()

```

```

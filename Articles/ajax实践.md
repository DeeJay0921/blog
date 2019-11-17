---
title: ajax实践
date: 2017/08/22 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/AJAX_logo_by_gengns.svg/1200px-AJAX_logo_by_gengns.svg.png
tags: 
- 前端
- Ajax
categories: 
- 前端
---
ajax实践
<!--more-->

##1， ajax 是什么？有什么作用？
ajax（Asynchronous JavaScript and XML）以异步的方式从服务器获取数据，不必刷新页面也能获取新数据。ajax的核心对象就是XMLhttpRequest，通过JavaScript DOM将ajax获得的数据显示在页面上。

作用是带来更好的用户体验。

##2， 前后端开发联调需要注意哪些事情？后端接口完成前如何 mock 数据？
前后端开发时需要注意：
1. 约定接口类型（GET还是POST)、接口（传递到那个路由）
2. 前端传递给后端的参数（参数的类型、格式）
3. 后端返回数据的格式（是数组还是字符串还是JSON格式）

后端接口完成前可以使用server-mock搭建模拟服务器

##3，点击按钮，使用 ajax 获取数据，如何在数据到来之前防止重复点击?

设置判断变量，数据没拿到时设置为false，拿到数据才设置为true，为false时忽略后面的点击事件

本例中的isDataArrive即为判断数据是否拿到的变量
```
<script>
    //整体思路就是给按钮添加点击事件，写ajax得到数据，然后通过渲染添加到html上就可以了
    var btn = document.querySelector('#load-more');
    var ct = document.querySelector('#ct');
    var tempIndex = 3;//记录每次的变量

//    针对网速较慢并且重复点击的情况做得优化：第一种思路，如果一点时间内请求没到，忽略用户后面点击的。第二种思路，用户点击了多次，每次的输出内容都要显示
    var isDataArrive = true; // 设置判断变量，数据没拿到时设置为false


    btn.addEventListener('click',function () {

        if (!isDataArrive) {
            return; //如果数据没拿到，直接return掉，后面的点击就被忽略掉
        }

        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 &&　(xhr.status == 200 || xhr.status == 304)) {
                var content = JSON.parse(xhr.responseText);
//                console.log(content); //写到这先测试一下给的数据对不对
//                开始拼装html
//                var html = '';
//                for (var i = 0; i < content.length; i ++) {
//                    html += '<li>' + content[i] + '</li>';
//                }
//               拼装好html，应该是appendChild到ct后面，但是每次appendChild回影响性能，可以使用Fragment，所以可以改写为：
                var fragment = document.createDocumentFragment();
                for (var i = 0; i < content.length; i ++) {
                    var node = document.createElement('li');
                    node.innerText = content[i]; // 使用innerText不用innerHTML还有个好处是可以防止xss注入
                    fragment.appendChild(node);
                }
                ct.appendChild(fragment);
                tempIndex += 5;

                isDataArrive = true;//readyState === 4 表示数据已经拿到了
            }
        };
        xhr.open('get','/loadMore?index='+ tempIndex +'&length=5',true);
//       前端需要得到一个数组 ['content1','content2',...,'content5']
//       前端需要向后端发送的  /loadMore?index=3&length=5                  /loadMore为接口，后端对loadMore进行处理
//        {
//            index: 3,
//            length: 5,
//         }
/*          一般在请求数据之前都要想清楚前端需要得到什么数据，需要向后端发送的是什么接口，参数是怎么样的。
            1.GET
            2./loadMore
            3. {
                index: 3,
                length: 5,
            }
            4. ['content1','content2',...,'content5']
 */
        xhr.send();
        isDataArrive = false; // 没拿到数据设置为false
    })
</script>
```




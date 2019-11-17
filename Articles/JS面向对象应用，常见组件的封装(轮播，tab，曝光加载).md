---
title: JS面向对象应用，常见组件的封装(轮播，tab，曝光加载)
date: 2017/09/02 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Unofficial_JavaScript_logo_2.svg/1200px-Unofficial_JavaScript_logo_2.svg.png
tags: 
- 前端
- JS
- 轮播
- tab
- 懒加载
categories: 
- 前端
---
JS面向对象应用，常见组件的封装(轮播，tab，曝光加载)
<!--more-->

##tab切换
用面向对象的写法如下，创建的对象实例个个独立，不需要考虑相互影响，只需要考虑自己怎么实现即可，下面代码还可以进行优化
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>组件tab</title>
    <style>
        ul,
        li {
            margin: 0;
            padding: 0;
        }

        li {
            list-style: none;
        }

        .clearfix:after {
            content: '';
            display: block;
            clear: both;
        }

        .tab {
            width: 600px;
            margin: 20px auto;
            border: 1px solid #ccc;
            padding: 20px 10px;
            border-radius: 4px;
        }

        .tab-header {
            border-bottom: 1px solid #ccc;
        }

        .tab-header>li {
            float: left;
            color: brown;
            border-top: 1px solid #fff;
            border-left: 1px solid #fff;
            border-right: 1px solid #fff;
            padding: 10px 20px;
            cursor: pointer;
        }

        .tab-header .active {
            border: 1px solid #ccc;
            border-bottom-color: #fff;
            border-radius: 4px 4px 0 0;
            color: #333;
            margin-bottom: -1px;
        }

        .tab-container {
            padding: 20px 10px;
        }

        .tab-container>li {
            display: none;
        }

        .tab-container>.active {
            display: block;
        }

        .box {
            height: 1000px;
        }
    </style>
</head>
<body>


<div class="tab">
    <ul class="tab-header clearfix">
        <li class="active">选项1</li>
        <li>选项2</li>
        <li>选项3</li>
    </ul>
    <ul class="tab-container">
        <li class="active">内容1
            <ul>
                <li></li>
            </ul>
        </li>
        <li>内容2</li>
        <li>内容3</li>
    </ul>
</div>

<div class="tab">
    <ul class="tab-header clearfix">
        <li class="active">选项1</li>
        <li>选项2</li>
        <li>选项3</li>
        <li>选项4</li>
    </ul>
    <ul class="tab-container">
        <li class="active">内容1
            <ul>
                <li></li>
            </ul>
        </li>
        <li>内容2</li>
        <li>内容3</li>
        <li>内容4</li>
    </ul>
</div>

<div class="tab">
    <ul class="tab-header clearfix">
        <li class="active">内容1</li>
        <li>内容2</li>
        <li>内容3</li>
        <li>内容4</li>
    </ul>
    <ul class="tab-container">
        <li class="active">内容1
            <ul>
                <li></li>
            </ul>
        </li>
        <li>内容2</li>
        <li>内容3</li>
        <li>内容4</li>
    </ul>
</div>

<script src="../jquery-3.2.1.min.js"></script>
<script>


    function Tab (ct) {
        //思考Tab构造函数里的属性和方法，属性有选项和内容的li
        //这部分为初始化的东西
        this.option = ct.querySelectorAll('.tab-header>li');
        this.content = ct.querySelectorAll('.tab-container>li');
        var self = this;
        //上面部分为初始化的东西

        this.option.forEach(function (option) { //遍历选项中的每一项
            option.addEventListener('click',function (e) { //给每个选项都绑定事件
                var target = e.target;
//            var index = this.option.indexOf(target);//不能这么写。这里面的this代表当前点击的元素,所以要在外面保存this
//            var index = self.option.indexOf(target); //但是写成这样，由于self.option不是一个数组，没有indexOf方法，所以还需要使用call/apply
                var index = [].indexOf.call(self.option,target); // 所以有2点需要注意，一是this的值，2是call
                self.option.forEach(function (option) {
                    option.classList.remove('active');//把每一项的active类都去掉
                })
                target.classList.add('active'); //只给当前点击的option加上active类
                self.content.forEach(function (content) { //下面的content同理
                    content.classList.remove('active');
                })
                self.content[index].classList.add('active');
            },false)
        })

    }
//    Tab.prototype.
    new Tab(document.querySelectorAll('.tab')[0]);
    new Tab(document.querySelectorAll('.tab')[1]);
    new Tab(document.querySelectorAll('.tab')[2]);
</script>
</body>
</html>
```
把初始化的东西封装到一起，绑定事件的代码封装到一起，都作为方法绑定到原型上，优化之后的代码如下：
```
<script>


    function Tab (ct) {
        //思考Tab构造函数里的属性和方法，属性有选项和内容的li
        this.ct = ct;
        this,init();
        this.bind();
    }
    Tab.prototype.init = function () {
        //这部分为初始化的东西
        this.option = this.ct.querySelectorAll('.tab-header>li');
        this.content = this.ct.querySelectorAll('.tab-container>li');
        //上面部分为初始化的东西
    }
    Tab.prototype.bind = function () {
        var self = this;
        this.option.forEach(function (option) { //遍历选项中的每一项
            option.addEventListener('click',function (e) { //给每个选项都绑定事件
                var target = e.target;
//            var index = this.option.indexOf(target);//不能这么写。这里面的this代表当前点击的元素,所以要在外面保存this
//            var index = self.option.indexOf(target); //但是写成这样，由于self.option不是一个数组，没有indexOf方法，所以还需要使用call/apply
                var index = [].indexOf.call(self.option,target); // 所以有2点需要注意，一是this的值，2是call
                self.option.forEach(function (option) {
                    option.classList.remove('active');//把每一项的active类都去掉
                })
                target.classList.add('active'); //只给当前点击的option加上active类
                self.content.forEach(function (content) { //下面的content同理
                    content.classList.remove('active');
                })
                self.content[index].classList.add('active');
            },false)
        })
    }
    new Tab(document.querySelectorAll('.tab')[0]);
    new Tab(document.querySelectorAll('.tab')[1]);
    new Tab(document.querySelectorAll('.tab')[2]);
</script>
```

##轮播
[源代码地址](https://deejay0921.github.io/demos/advanced/task17-1.html)
进行面向对象的组件化写法：
整体思路就是把所有的属性方法都用this关联起来，这样就可以只考虑自己了，但是要注意绑定事件还有匿名函数内部的this要注意保存
```
<script>

    function Carousel ($ct) {
        this.$ct = $ct;
        this.init();
        this.bind();
    }
    Carousel.prototype.init = function () {
        var $imgct = this.$imgct= this.$ct.find('.img-ct');
        var $imgs = this.$imgs = this.$ct.find('.img-ct>li');
        //获取宽度和个数
        var imgWidth = this.$imgWidth = this.$imgs.width();
        var imgCount = this.$imgCount = this.$imgs.length;
        var $pre = this.$pre = this.$ct.find('.pre'); //不这么写的话，下面bind中是找不到$pre的，组件化的核心思想就是都去使用this去进行操作
        var $next = this.$next =this.$ct.find('.next');
        var $bullets = this.$bullets =this.$ct.find('.bullet>li');
        var pageIndex = this.pageIndex = 0;  //记录当前页码
        var animating = this.animating =  false; //针对连续重复点击，设置变量来监听是否处于动画过程中

        this.$imgct.append(this.$imgs.first().clone());
        this.$imgct.prepend(this.$imgs.last().clone());
        this.$imgct.width((this.$imgCount+2) * this.$imgWidth);
        this.$imgct.css({
            left: -this.$imgWidth,
        })
    }
    Carousel.prototype.bind = function () {
        var self = this;
        this.$pre.on('click',function () {
            self.playPre(1);//这里面的this不对，是当前点击的元素this.$pre，所以还是需要保存this
        })
        this.$next.on('click',function () {
            self.playNext(1);
        })
        this.$bullets.on('click',function () {
            var index = $(this).index();
            if (index < self.pageIndex) {
                self.playPre(self.pageIndex - index);
            }
            else {
                self.playNext(index - self.pageIndex);
            }
        })
    }
    Carousel.prototype.playPre = function (num) {
        var self = this;
        if (this.animating) {
            return; //如果还在动画中，直接return掉
        }
        this.animating = true; //进入动画，animating设为true表示正在动画中
        this.$imgct.animate({
            left: '+=' + num * this.$imgWidth,
        },function () { // 保存this，因为在匿名函数内部
            self.pageIndex -= num;
            if (self.pageIndex === -1) {
                self.pageIndex = self.$imgCount-1;
                self.$imgct.css({
                    left: -self.$imgCount*self.$imgWidth,
                })
            }
            self.setBullets();
            self.animating = false; //动画结束后重新设置为false
        })
    }
    Carousel.prototype.playNext = function (num) {
        var self = this;
        if (this.animating) {
            return;
        }
        this.animating = true;
        this.$imgct.animate({
            left: '-=' + num * this.$imgWidth,
        },function () {
            self.pageIndex += num;
            if (self.pageIndex === self.$imgCount) {
                self.pageIndex = 0;
                self.$imgct.css({
                    left: -self.$imgWidth,
                })
            }
            self.setBullets();
            self.animating = false;
        })
    }
    Carousel.prototype.setBullets = function () {
        this.$bullets.removeClass('active')
                .eq(this.pageIndex).addClass('active');
    }
    var c1 = new Carousel($('.carousel').eq(0));
    var c2 = new Carousel($('.carousel').eq(1));

</script>
```

##轮播二次封装
对于上面封装的组件，如果想达到使用`Carousel2.init($('.carousel'));`就使得所有的carousel都运转起来的话，就要使用另外一种模块化的方式

首先来看一个区别:
```
    Carousel2 = {  //方法1 写成一个对象
        init: function () {//写成这样的话 Carousel就是一个对象，只能添加一些属性方法
            console.log(123);
        }
    }
    Carousel2 = (function () { //方法2 写成一个立即执行函数
        var a = 1; //写成这样的好处在于可以赋值一些局部变量，并且这个值外部永远访问不到，并且也能在return中添加新的属性方法
        return {
            init: function () {
                console.log(a);
            }
        }
    })();
```
上例中，方法2才是真正的封装。所以我们可以把上面的Calousel构造函数直接放到里面Carousel2的立即执行函数的局部作用域里，然后再return中，遍历选取到的所有$('.carousel')，给每一个选取到的$('.carousel')new一个Carousel()的实例。
整体思路如下：
```
    Carousel2 = (function () { //方法2 写成一个立即执行函数
        // 将写好的Carousel构造函数直接放到这个局部作用域里 就不会暴露出去
        
        return {
            init: function ($ct) {
//                然后只在这里遍历选取到的所有目标元素，每一个元素都相应的创建实例
                   $ct.each(function (index,node) {
                       new Carousel($(node));
                   })
            }
        }
    })();
    Carousel2.init($('.carousel'));
```
最后可以将Carousel构造函数放到局部作用域中，这样就不会将写好的构造函数暴露出去了。
```
<script>



    Carousel2 = (function () { //方法2 写成一个立即执行函数
        // 将写好的Carousel构造函数直接放到这个局部作用域里 就不会暴露出去

        function Carousel ($ct) {
            this.$ct = $ct;
            this.init();
            this.bind();
        }
        Carousel.prototype.init = function () {
            var $imgct = this.$imgct= this.$ct.find('.img-ct');
            var $imgs = this.$imgs = this.$ct.find('.img-ct>li');
            //获取宽度和个数
            var imgWidth = this.$imgWidth = this.$imgs.width();
            var imgCount = this.$imgCount = this.$imgs.length;
            var $pre = this.$pre = this.$ct.find('.pre'); //不这么写的话，下面bind中是找不到$pre的，组件化的核心思想就是都去使用this去进行操作
            var $next = this.$next =this.$ct.find('.next');
            var $bullets = this.$bullets =this.$ct.find('.bullet>li');
            var pageIndex = this.pageIndex = 0;  //记录当前页码
            var animating = this.animating =  false; //针对连续重复点击，设置变量来监听是否处于动画过程中

            this.$imgct.append(this.$imgs.first().clone());
            this.$imgct.prepend(this.$imgs.last().clone());
            this.$imgct.width((this.$imgCount+2) * this.$imgWidth);
            this.$imgct.css({
                left: -this.$imgWidth,
            })
        }
        Carousel.prototype.bind = function () {
            var self = this;
            this.$pre.on('click',function () {
                self.playPre(1);//这里面的this不对，是当前点击的元素this.$pre，所以还是需要保存this
            })
            this.$next.on('click',function () {
                self.playNext(1);
            })
            this.$bullets.on('click',function () {
                var index = $(this).index();
                if (index < self.pageIndex) {
                    self.playPre(self.pageIndex - index);
                }
                else {
                    self.playNext(index - self.pageIndex);
                }
            })
        }
        Carousel.prototype.playPre = function (num) {
            var self = this;
            if (this.animating) {
                return; //如果还在动画中，直接return掉
            }
            this.animating = true; //进入动画，animating设为true表示正在动画中
            this.$imgct.animate({
                left: '+=' + num * this.$imgWidth,
            },function () { // 保存this，因为在匿名函数内部
                self.pageIndex -= num;
                if (self.pageIndex === -1) {
                    self.pageIndex = self.$imgCount-1;
                    self.$imgct.css({
                        left: -self.$imgCount*self.$imgWidth,
                    })
                }
                self.setBullets();
                self.animating = false; //动画结束后重新设置为false
            })
        }
        Carousel.prototype.playNext = function (num) {
            var self = this;
            if (this.animating) {
                return;
            }
            this.animating = true;
            this.$imgct.animate({
                left: '-=' + num * this.$imgWidth,
            },function () {
                self.pageIndex += num;
                if (self.pageIndex === self.$imgCount) {
                    self.pageIndex = 0;
                    self.$imgct.css({
                        left: -self.$imgWidth,
                    })
                }
                self.setBullets();
                self.animating = false;
            })
        }
        Carousel.prototype.setBullets = function () {
            this.$bullets.removeClass('active')
                    .eq(this.pageIndex).addClass('active');
        }
        return {
            init: function ($ct) {
//                然后只在这里遍历选取到的所有目标元素，每一个元素都相应的创建实例
                   $ct.each(function (index,node) {
                       new Carousel($(node));
                   })
            }
        }
    })();
    Carousel2.init($('.carousel'));
</script>
```

##懒加载（曝光加载）封装
[懒加载源代码](https://deejay0921.github.io/demos/advanced/task16.html)


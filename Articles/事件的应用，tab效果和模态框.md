---
title: 事件的应用，tab效果和模态框
date: 2017/08/18 00:00:01
tags: 
- 前端
- JS
categories: 
- 前端
---
事件的应用，tab效果和模态框
<!--more-->

### tab效果
整体思路是先在CSS中设置好相应的active类，然后根据事件，给元素增加或者删除样式类，不通过JS直接改写样式
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            list-style: none;
            box-sizing: border-box;
        }
        .clearfix:after {
            content: '';
            display: block;
            clear: both;
        }
        .tab-ct {
            width: 600px;
            margin: 50px auto;
            padding: 5px;
            border: 1px solid #ccc;
        }
        .header li {
            float: left;
            width: 33.333333%;
            border: 1px solid #ccc;
            border-right: none;
            height: 40px;
            line-height: 40px;
            text-align: center;
        }
        .header li:last-child {
            border-right: 1px solid #ccc;
        }
        /*设置样式类，用来改变样式*/
        .header .active {
            background: #ddd;
        }
        .content li {
            border: 1px solid #ccc;
            border-top: none;
            height: 200px;
            display: none;
            padding: 20px;
        }
        /*设置样式类，用来改变样式*/
        .content .active {
            display: block;
        }
    </style>
</head>
<body>

<div class="tab-ct">
    <ul class="header clearfix">
        <li class="active">tab1</li>
        <li>tab2</li>
        <li>tab3</li>
    </ul>
    <ul class="content">
        <li class="active">ct1</li>
        <li>ct2</li>
        <li>ct3</li>
    </ul>
</div>

<script>
    var headLiNodes = document.querySelectorAll('.header>li');
    var ctLiNodes = document.querySelectorAll('.content>li');

//    先遍历header中的li，可以用for循环，也可以使用forEach
    headLiNodes.forEach(function (li){
//        遍历给每个li都绑定事件
        li.addEventListener('click',function () {
//            绑定事件的时候，可以先把所有的li的active类都删除掉，然后再给当前触发事件的li加上active
//            所以可以先再遍历一次，删除所有li的active类
            headLiNodes.forEach(function(e) {
//                遍历删除所有li的active类
                e.classList.remove('active');
            })
//            删除完成后再在外部给当前this添加active类
            this.classList.add('active');

//    header部分的点击效果做好之后，要把下面的ct和上面的li联系起来，要知道点击哪个li下面会切换成哪个ct
//    上面li和下面ct的联系方式是序号，点击第一个li，下面是第一个ct，即得到序号就能联系起来，如果我们用for循环，
//    那么就可以得到序号，使用forEach可以使用call函数，结合数组的indexOf方法来得到序号
            var index = [].indexOf.call(headLiNodes,li); //得到了当前li的序号
//          遍历ctNodes，给每个ct都删除active类，然后只给和当前headerLi对应的ct加上active类
            ctLiNodes.forEach(function (ct) {
                ct.classList.remove('active');
            })
            ctLiNodes[index].classList.add('active');
        })
    })



</script>
</body>
</html>
```

###模态框 (事件冒泡阻止)

主要关键点在于设置点击框外的时候关闭模态框，会导致点击框内也会关闭模态框，所以要给框设置阻止事件冒泡。
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            list-style: none;
            box-sizing: border-box;
        }
        .clearfix:after {
            content: '';
            display: block;
            clear: both;
        }
        a {
            color: black;
            text-decoration: none;
        }
        .modal-dialog {
            display: none;
            position: fixed;
            left: 0;
            top: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.8);
        }
        .modal-dialog .modal-ct {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%,-50%);
            width: 400px;
            border-radius: 5px;
            border: 1px solid #ccc;
            background: #fff;
        }
        .modal-container {
            position: fixed;
            left: 0;
            right: 0;
            top: 0;
            bottom: 0;
        }
        .header {
            padding: 10px;
            border-bottom: 1px solid black;
        }
        .header h3 {
            float: left;
        }
        .header a {
            float: right;
        }
        .content p {
            margin: 10px;
        }
        .footer a {
            float: right;
            margin: 10px;
        }
        .btn-gruop {
            margin: 20px;
        }
        .btn-gruop #btn-modal {
            padding: 8px 16px;
        }
        /*设置展现样式*/
        .open {
            display: block;
        }
    </style>
</head>
<body>

<div class="btn-gruop">
    <button id="btn-modal">点我</button>
</div>

<div class="modal-dialog" id="modal-1">
    <div class="modal-container">
        <div class="modal-ct">
            <div class="header clearfix">
                <h3>标题标题</h3>
                <a href="#" class="close">x</a>
            </div>
            <div class="content">
                <p>内容内容1</p>
                <p>内容内容2</p>
            </div>
            <div class="footer">
                <a href="#" class="btn btn-confirm">确定</a>
                <a href="#" class="btn btn-cancel">取消</a>
            </div>
        </div>
    </div>
</div>

<script>
    var modalCt = document.querySelector('.modal-ct');
    var modalContainer = document.querySelector('.modal-container');
    var modalDialog = document.querySelector('.modal-dialog');
    var btn = document.querySelector('.btn-gruop');
    var close = document.querySelector('.close');
    var btnC = document.querySelector('.btn-cancel');

//    设置点击‘点我’弹出模态框
    btn.addEventListener('click',function () {
        modalDialog.classList.add('open');
    })
//    设置点击X关闭模态框
    close.addEventListener('click',function () {
        modalDialog.classList.remove('open');
    })
//    设置点击框外区域关闭模态框
//    框外区域即modal-ct外，modal-container里面的部分，给modalContainer绑定事件解除open类即可
    modalContainer.addEventListener('click',function () {
        modalDialog.classList.remove('open');
    })
//    到目前为止，点击X关闭，点击框外关闭，但是出现了一个问题，点击框内也会关闭，因为框内即moda-ct也是modal-container的子元素
//    点击modal-ct事件一样会触发,所以我们可以阻止事件冒泡,即在modal-ct上截断冒泡,这样点击modal-ct就不会关闭了
    modalCt.addEventListener('click',function (e) {
        e.stopPropagation();  // 阻止事件冒泡
    })
//    设置点击取消关闭
    btnC.addEventListener('click',function () {
        modalDialog.classList.remove('open');
    })

</script>
</body>
</html>
```

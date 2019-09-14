---
title: Vue中的v-model理解
date: 2017/10/13 00:00:01
tags: 
- 前端
- JS
- Vue
categories: 
- 前端
---
Vue中的v-model理解
<!--more-->

官方文档讲的比较模糊，自己通过例子进行理解。

# v-model是语法糖

首先明确一点，v-model仅仅是语法糖。

```
        <input
                type="text"
                v-model="something">
        <!--等价于-->
        <input
                type="text"
                v-bind:value="something"
                v-on:input="something = $event.target.value">
````

还要理解$emit的用法：

` vm.$emit(event,[...args])`

 触发当前实例上的事件。附加参数都会传给监听器回调。
# v-model在 普通input上

一定要明确在给 <input /> 元素添加 v-model 属性时，**默认会把 value 作为元素的属性，然后把 'input' 事件作为实时传递 value 的触发事件**（具体参考语法糖的解释）


# v-model用在组件上

这里是一个自定义组件currency-input,父组件的 price 的初始值是 100，更改子组件的值能实时更新父组件的 price

html:
```
<div id="demo">
 <currency-input v-model="price"></currentcy-input>
 <span>{{price}}</span>
</div>
```
js:
```
Vue.component('currency-input', {
 template: `
  <span>
   <input
    ref="input"
    :value="value"
    <!--为什么这里把 'input' 作为触发事件的事件名？`input` 在哪定义的？-->
    @input="$emit('input', $event.target.value)"
   >
  </span>
 `,
 props: ['value'],// 为什么这里要用 value 属性，value在哪里定义的？貌似没找到啊？
})

var demo = new Vue({
 el: '#demo',
 data: {
  price: 100,
 }
})
```
这里的js代码，props中接受了value，但是在组件中`<currency-input v-model="price"></currentcy-input>`并没有传入value，而且在input中是监听了input事件，但是并没有在父组件中定义input。

要理解这个，就要回到语法糖的问题上，
`<currency-input v-model="price"></currentcy-input>`
本质上等价于：
`<currency-input :value="price" @input="price = arguments[0]"></currency-input>`
所以可以看到value和input了

理解v-model在组件中的实现，给组件添加 v-model 属性时，**默认会把 value 作为组件的属性，然后把 'input' 值作为给组件绑定事件时的事件名**

###代码分析

所以这里在js中`  @input="$emit('input', $event.target.value)"` 子组件在监听input事件发生的时候（即@input）,向父组件传递了input($emit中的input)事件，并且传递了当前子组件的price值。

而在父组件中，监听了自定义事件input，当自定义事件input触发后，将当前父组件自身的price值改为子组件中$emit上来的值（$event.target.value 是作为$emit传递的参数，所以是arguments[0]）


##v-model存在的问题

对于复选框或者单选框的常见组件时，由于v-model默认传的是value，不是checked，触发事件也不是oninput而是onchange

- 对于单纯的input type='checkbox'
`<input type="checkbox" :checked="status" @change="status = $event.target.checked" />`
- 当用到组件上时：

```
<my-checkbox v-model="foo"></my-checkbox>

Vue.component('my-checkbox', {
 tempalte: `<input 
        type="checkbox"
        @change="$emit('input', $event.target.checked)"
        :checked="value"
       />`
 props: ['value'],
})
```

这个时候需要Vue的**model**选项
model选项可以指定当前的事件类型和传入的props

所以可以通过这么改进：
```
<my-checkbox v-model="foo"></my-checkbox>

Vue.component('my-checkbox', {
 tempalte: `<input 
        type="checkbox"
        <!--这里就不用 input 了，而是 balabala-->
        @change="$emit('balabala', $event.target.checked)"
        :checked="value"
       />`
 props: ['checked'], //这里就不用 value 了，而是 checked
 model: { // model选项来指定
  prop: 'checked',
  event: 'balabala'
 },
})
```

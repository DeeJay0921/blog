---
title: Vue后端渲染之Nuxt.js
date: 2019/11/19 14:17:01
cover: https://moriohcdn.b-cdn.net/521f533697.png
tags: 
- 前端
- Vue
- NuxtJS
- 后端渲染
categories: 
- 前端
---

最近看大佬们闲聊说起页面优化，都觉得后端渲染效果立竿见影，就来学习一波NuxtJS。
<!--more-->


# 其余SSR替代方案

由于已有`Vue`项目迁移到`NuxtJS`项目需要较大工作量的重构（二者的规则约定差别较大）

所以如果需要进行SSR的页面较少、页面内容实时性要求较低的话，可以考虑:

- [Vue.js的预渲染插件](https://github.com/chrisvfritz/prerender-spa-plugin)
- [Vue HackerNews 2.0项目](https://github.com/vuejs/vue-hackernews-2.0/)
- [Vue.js原生的SSR](https://ssr.vuejs.org/zh/)


另附上相关阅读：
- [issue: 如何在已有项目引入Nuxt.js](https://github.com/nuxt/nuxt.js/issues/2596)
- [使用Nuxt.js改善现有项目](https://zhuanlan.zhihu.com/p/30025987)
- [使用Nuxt.js改造已有项目](https://blog.csdn.net/wopelo/article/details/80486874)
- [vue-hackernews-2.0 源码解读](https://wangfuda.github.io/2017/05/14/vue-hackernews-2.0-code-explain/)

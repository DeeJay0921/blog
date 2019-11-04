---

title: TypeScript 中的模块系统

date: 2019/11/083 19:42:01

tags: 

- 前端

- TS

categories: 

- 前端

---

TypeScript 中的模块系统的简单介绍

<!--more-->

TypeScript沿用了ECMAScript中模块的概念，直接来看相关使用：

# 导出

## 导出声明

对于任何声明，都能通过添加`export`关键字来导出
> 任何声明指的是（变量， 函数， 类， 类型别名， 接口）

```typescript
export class T {
name: string
}

export interface Person {
name: string
}

export let p: string = "abc";

export function fn(s: string):number {
    return s.length;
}

export type unionType = string | number;
```

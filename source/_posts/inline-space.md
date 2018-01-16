---
title: CSS去除内联元素之间的空格
date: 2017-01-04 13:18:58
tags:
- css
---

>当内联元素不放置于同一行时，因为浏览器将回车视为一个空格，因此会产生间距问题，如下所示
<!-- more -->
![inline](/img/inline-space.png)

解决方法：
1. 写在同一行中
2. margin-right设为负值
3. 设置父元素（如body{font-size:0}），然后再设置内联元素字体大小
4. 设置浮动，float:left

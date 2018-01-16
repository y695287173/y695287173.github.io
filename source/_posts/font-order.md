---
title: font 属性简写
date: 2017-02-16 00:06:07
tags:
- css
---
css 中font的属性可以通过合并成为一个font属性，这样写起来方便，也简化了代码。
<!-- more -->

# font属性

>font-family（字体族）: “Arial”、“Times New Roman”、“宋体”、“黑体”等;
font-style（字体样式）: normal（正常）、italic（斜体）或oblique（倾斜）;
font-variant (字体变化): normal（正常）或small-caps（小体大写字母）;
font-weight (字体浓淡): 是normal（正常）或bold（加粗）。有些浏览器甚至支持采用100到900之间的数字（以百为单位）;
font-size（字体大小）: 可通过多种不同单位（比如像素或百分比等）来设置, 如：12xp，12pt，120%，1em

简写顺序如下：

>顺序：font-style | font-variant | font-weight | font-size | line-height | font-family

# 举例

```css
.font{
	font-style:italic;
	font-variant:small-caps;
	font-weight:bold;
	font-size:12px;
	line-height:1.5em;
	font-family:arial,verdana;
}
```

简写后：
```css
.font{font:italic small-caps bold 12px/1.5em arial,verdana;}
```

# 注意

1. 简写时，font-size和line-height只能通过斜杠/组成一个值，不能分开写。
 
2. 顺序不能改变.这种简写方法只有在同时指定font-size和font-family属性时才起作用。而且，如果你没有设定font-weight, font-style, 以及 font-varient ，他们会使用缺省值
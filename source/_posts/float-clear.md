---
title: 清除浮动的几种方法
date: 2017-02-27 22:42:39
tags:
- css
---
在CSS布局中，浮动是一个常用的属性，但在子元素设置float属性时height超过父元素，在父元素未设置高度或高度小于子元素时，会出现子元素溢出父元素，从而使父元素高度出现塌陷的情况。而为了防止子元素溢出到父元素包围以外的css处理，就被称为*清除浮动*
<!-- more -->

##### 此处引用W3C中清除浮动的例子：http://www.w3school.com.cn/css/css_positioning_floating.asp

	.news {
	  background-color: gray;
	  border: solid 1px black;
	  }

	.news img {
	  float: left;
	  }

	.news p {
	  float: right;
	  }

	<div class="news">
	<img src="news-pic.jpg" />
	<p>some text</p>
	</div>

![timberland](/img/css-float-1.jpg)
<br/>
# 主要几种常用清除浮动的方法：

#### 方法1：使用css的:after伪元素进行清除浮动

	.news {
	  background-color: gray;
	  border: solid 1px black;
	  }
	.news img {
	  float: left;
	  }
	.news p {
	  float: right;
	  }
	.clearfix:after{
	  content: "";
	  display: block;
	  height: 0;
	  clear: both;
	  visibility: hidden;  
	  }
	.clearfix {
	  /* 触发 hasLayout */
	  zoom: 1;
	  }
	<div class="news clearfix">
	<img src="news-pic.jpg" />
	<p>some text</p>
	</div>

此方法的原理为在要清除浮动的父元素最后添加一个带有clear：both属性，且不可见的元素，以此元素来清除浮动。与直接宰父元素最后添加一个带有clear：both的元素原理一直，但是使用：after属性不会因为添加一个无意义的元素而破坏html文档的结构。而zoom：1，是为了触发IE的hasLayout来清除浮动。

### 方法2：使用CSS的overflow属性

	overflow:auto;

或

	overflow：hidden；

另外同理需要加上zoom：1的IE HACK来兼容IE


	.news {
	  background-color: gray;
	  border: solid 1px black;
	  overflow: auto;
	  *zoom: 1;
	  }
	.news img {
	  float: left;
	  }
	.news p {
	  float: right;
	  }
	<div class="news">
	<img src="news-pic.jpg" />
	<p>some text</p>
	</div>

此处需要解释一下，这种清除浮动的方式是和上面第一种方式有着不同的原理。第一种方式是利用了父元素内最后一个元素或伪元素具有的clear属性来进行清除浮动的，而此方法是触发了BFC来清除浮动的。即Block formatting contexts （块级格式化上下文），简称 BFC。

BFC 是页面中独立的一个容器，容器内的子元素不会对外部产生影响。

BFC具有如下特性：

1. BFC会阻止内部子元素在垂直方向的外边距重叠（margin），但BFC之间和同一个BFC内的外边距重叠问题仍然存在。
2. BFC可以包裹住浮动元素
3. BFC不会重叠浮动元素

因此利用BFC可以包裹浮动元素的特性来清除浮动

### 方法3：浮动元素的外部元素也设置为浮动元素

这种方法因为会影响布局，所以一般也使用不上。主要还是推荐上述两种方法。

具体想深入了解清除浮动的方法和其中的原理，可以参考如下链接：

那些年我们一起清除过的浮动：http://www.iyunlu.com/view/css-xhtml/55.html


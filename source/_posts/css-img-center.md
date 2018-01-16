---
title: html代码固定，实现文字不居中，图片居中
date: 2017-01-02 15:24:29
tags:
- css
---
css小技巧笔记，html代码固定，实现文字不居中，图片居中
<!-- more -->
>html代码

	<div class="article">
		<p>文字不要居中，下面图片要居中</p>
		<p><img src="/img/tiancai.jpg"></p>
	</div>

>css代码

### img为内联元素，因此需要改变display方式为bolck

	.article>p>img{
		display:block;
		margin:0 auto;
	}

>效果

<div class="article" style="width:100%">
	<p>文字不要居中，下面图片要居中</p>
	<p><img src="/img/tiancai.jpg" style="display:block;margin:0 auto;"></p>
</div>
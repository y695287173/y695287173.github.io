---
title: git常用命令--git clean
date: 2017-01-03 23:06:22
tags:
- git
---

项目中的文件可以分成两种状态：tracked, untracked

>git clean 用于删除一些没有add进来的untracked文件

<!-- more -->
	git clean -n
	--显示要删除的文件和目录
	
	git clean -f
	--删除文件

	git clean -df
	--删除文件和目录
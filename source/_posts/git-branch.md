---
title: git本地创建远端分支
date: 2017-01-03 13:02:49
tags:
- git
---
在公司解决问题时，需要创建远端分支，但是每次都要在内源网站上（类似github）点击创建分支，然后再git fetch，git checkout。因为公司代码太多，网站很卡，很容易让人焦虑。然后就在网上找了找本地创建远端分支的技巧，比较简单，代码如下。
<!-- more -->
>创建本地分支

	--切到master分支
	$git checkout master
	--创建本地分支
	$git branch develop

>创建远端分支

	$git push origin develop
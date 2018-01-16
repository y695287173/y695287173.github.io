---
title: 《javaScript高级程序设计》第11章 DOM拓展总结
date: 2017-02-14 13:38:04
tags:
- javaScript
---
摘录总结自《javaScript高级程序设计》

对 DOM 的两个主要的扩展是 Selectors API（选择符 API）和 HTML5。这两个扩展都源自开发社区，而将某些常见做法及 API 标准化一直是众望所归。此外，还有一个不那么引人瞩目的 Element Traversal（元素遍历）规范，为 DOM 添加了一些属性。虽然前述两个主要规范（特别是 HTML5）已经涵盖了大量的 DOM 扩展，但专有扩展依然存在。本章也会介绍专有的 DOM 扩展。
<!-- more -->

# 11.1 选择符 API

众多 JavaScript 库中最常用的一项功能，就是根据 CSS 选择符选择与某个模式匹配的 DOM 元素。实际上，jQuery（www.jquery.com）的核心就是通过 CSS 选择符查询 DOM 文档取得元素的引用，从而抛开了 getElementById() 和 getElementsByTagName() 。

Selectors API Level 1的核心是两个方法： _querySelector()_ 和 _querySelectorAll()_ 。在兼容的浏览器中，可以通过 Document 及 Element 类型的实例调用它们。目前已完全支持 Selectors API Level 1的浏览器有 IE 8+、Firefox 3.5+、Safari 3.1+、Chrome和 Opera 10+。

### 11.1.1  querySelector() 方法

querySelector() 方法接收一个 CSS 选择符，返回与该模式匹配的第一个元素，如果没有找到匹配的元素，返回 null 。请看下面的例子。

```javascript
//取得 body 元素
var body = document.querySelector("body");

//取得 ID 为"myDiv"的元素
var myDiv = document.querySelector("#myDiv");

//取得类为"selected"的第一个元素
var selected = document.querySelector(".selected");

//取得类为"button"的第一个图像元素
var img = document.body.querySelector("img.button");
```

如果传入了不被支持的选择符， querySelector()
会抛出错误。

### 11.1.1  querySelectorAll() 方法

与querySelector()基本相同，差异为返回全部元素，如下例子：

```javascript
//取得某<div>中的所有<em>元素（类似于 getElementsByTagName("em")）
var ems = document.getElementById("myDiv").querySelectorAll("em");

//取得类为"selected"的所有元素
var selecteds = document.querySelectorAll(".selected");

//取得所有<p>元素中的所有<strong>元素
var strongs = document.querySelectorAll("p strong");

要取得返回的 NodeList 中的每一个元素，可以使用 item() 方法，也可以使用方括号语法，比如：
var i, len, strong;
for (i=0, len=strongs.length; i < len; i++){
	strong = strongs[i]; //或者 strongs.item(i)
	strong.className = "important";
}
```

### 11.1.3  matchesSelector() 方法

Selectors API Level 2 规范为 Element 类型新增了一个方法 matchesSelector() 。这个方法接收一个参数，即 CSS 选择符，如果调用元素与该选择符匹配，返回 true ；否则，返回 false 。看例子。

截至 2011 年年中，还没有浏览器支持 matchesSelector() 方法；不过，也有一些实验性的实现。IE 9+通过 msMatchesSelector() 支持该方法，Firefox 3.6+通过 mozMatchesSelector() 支持该方法，Safari 5+和 Chrome 通过 webkitMatchesSelector() 支持该方法。因此，如果你想使用这个方法，最
好是编写一个包装函数。

```javascript
function matchesSelector(element, selector){
	if (element.matchesSelector){
		return element.matchesSelector(selector);
	} else if (element.msMatchesSelector){
		return element.msMatchesSelector(selector);
	} else if (element.mozMatchesSelector){
		return element.mozMatchesSelector(selector);
	} else if (element.webkitMatchesSelector){
		return element.webkitMatchesSelector(selector);
	} else {
		throw new Error("Not supported.");
	}
}
if (matchesSelector(document.body, "body.page1")){
	//执行操作
}
```

# 11.2 元素遍历

对于元素间的空格，IE9及之前版本不会返回文本节点，而其他所有浏览器都会返回文本节点。这样，就导致了在使用 childNodes 和 firstChild 等属性时的行为不一致。为了弥补这一差异，而同时又保持 DOM规范不变，Element Traversal规范（www.w3.org/TR/ElementTraversal/）新定义了一组属性。
Element Traversal API 为 DOM 元素添加了以下 5 个属性。

>childElementCount ：返回子元素（不包括文本节点和注释）的个数。
firstElementChild ：指向第一个子元素； firstChild 的元素版。
lastElementChild ：指向最后一个子元素； lastChild 的元素版。
previousElementSibling ：指向前一个同辈元素； previousSibling 的元素版。
nextElementSibling ：指向后一个同辈元素； nextSibling 的元素版。

如下：

```javascript
var i,
	len,
child = element.firstChild;
while(child != element.lastChild){
	if (child.nodeType == 1){ //检查是不是元素
		processChild(child);
	}
	child = child.nextSibling;
}
而使用 Element Traversal 新增的元素，代码会更简洁。
var i,
	len,
child = element.firstElementChild;
while(child != element.lastElementChild){
	processChild(child); //已知其是元素
	child = child.nextElementSibling;
}
```

支持 Element Traversal 规范的浏览器有 IE 9+、Firefox 3.5+、Safari 4+、Chrome 和 Opera 10+。

# 11.3 HTML5

为了让开发人员适应并增加对 class 属性的新认识，HTML5 新增了很多 API，致力于简化 CSS 类的用法。

### 1.  getElementsByClassName() 方法

这个方法最早出现在 JavaScript 库中，是通过既有的 DOM 功能实现的，而原生的实现具有极大的性能优势。

```javascript
/取得所有类中包含"username"和"current"的元素，类名的先后顺序无所谓
var allCurrentUsernames = document.getElementsByClassName("username current");

//取得 ID 为"myDiv"的元素中带有类名"selected"的所有元素
var selected = document.getElementById("myDiv").getElementsByClassName("selected");
```

使用这个方法可以更方便地为带有某些类的元素添加事件处理程序，从而不必再局限于使用 ID或标签名。不过别忘了，因为返回的对象是 NodeList ，所以使用这个方法与使用 getElementsByTagName()以及其他返回 NodeList 的 DOM 方法都具有同样的性能问题。
支持 getElementsByClassName() 方法的浏览器有 IE 9+、Firefox 3+、Safari 3.1+、Chrome 和Opera 9.5+。

### 2.  classList 属性

在HTML5之前，我们要改变元素的class属性时，需要做如下操作：

```html
<div class="bd user disabled">...</div>
```
这个 <div> 元素一共有三个类名。要从中删除一个类名，需要把这三个类名拆开，删除不想要的那个，然后再把其他类名拼成一个新字符串。请看下面的例子。

```javascript
//删除"user"类

//首先，取得类名字符串并拆分成数组
var classNames = div.className.split(/\s+/);

//找到要删的类名
var pos = -1,
	i,
	len;
for (i=0, len=classNames.length; i < len; i++){
	if (classNames[i] == "user"){
		pos = i;
		break;
	}
}

//删除类名
classNames.splice(i,1);

//把剩下的类名拼成字符串并重新设置
div.className = classNames.join(" ");
```

HTML5 新增了一种操作类名的方式，可以让操作更简单也更安全，那就是为所有元素添加classList 属性。这个 classList 属性是新集合类型 DOMTokenList 的实例。

>add(value) ：将给定的字符串值添加到列表中。如果值已经存在，就不添加了。
contains(value) ：表示列表中是否存在给定的值，如果存在则返回 true ，否则返回 false 。
remove(value) ：从列表中删除给定的字符串。
toggle(value) ：如果列表中已经存在给定的值，删除它；如果列表中没有给定的值，添加它。

这样，前面那么多行代码用下面这一行代码就可以代替了：

```javascript
div.classList.remove("user");
```

以上代码能够确保其他类名不受此次修改的影响。其他方法也能极大地减少类似基本操作的复杂性，如下面的例子所示。

```javascript
//删除"disabled"类
div.classList.remove("disabled");

//添加"current"类
div.classList.add("current");

//切换"user"类
div.classList.toggle("user");

//确定元素中是否包含既定的类名
if (div.classList.contains("bd") && !div.classList.contains("disabled")){
	//执行操作
)

//迭代类名
for (var i=0, len=div.classList.length; i < len; i++){
	doSomething(div.classList[i]);
}
```

有了 classList 属性，除非你需要全部删除所有类名，或者完全重写元素的 class 属性，否则也就用不到 className 属性了。

支持 classList 属性的浏览器有 Firefox 3.6+和 Chrome。

### 11.3.2 焦点管理

HTML5 也添加了辅助管理 DOM 焦点的功能。首先就是 document.activeElement 属性，这个属性始终会引用 DOM 中当前获得了焦点的元素。元素获得焦点的方式有页面加载、用户输入（通常是通过按 Tab 键）和在代码中调用 focus() 方法。来看几个例子。

```javascript
var button = document.getElementById("myButton");
button.focus();
alert(document.activeElement === button); //true
```

默认情况下，文档刚刚加载完成时， document.activeElement 中保存的是 document.body 元素的引用。文档加载期间， document.activeElement 的值为 null 。
另外就是新增了 document.hasFocus() 方法，这个方法用于确定文档是否获得了焦点。

```javascript
button.focus();
alert(document.hasFocus()); //true
```

实现了这两个属性的浏览器的包括 IE 4+、Firefox 3+、Safari 4+、Chrome 和 Opera 8+。

### 11.3.3  HTMLDocument 的变化

HTML5 扩展了 HTMLDocument ，增加了新的功能。与 HTML5 中新增的其他DOM扩展类似，这些变化同样基于那些已经得到很多浏览器完美支持的专有扩展。所以，尽管这些扩展被写入标准的时间相
对不长，但很多浏览器很早就已经支持这些功能了。

#### 1.  readyState 属性

>loading ，正在加载文档；
complete ，已经加载完文档。

在这个属性得到广泛支持之前，要实现这样一个指示器，必须借助 onload 事件处理程序设置一
个标签，表明文档已经加载完毕。基本用法：

```javascript
if (document.readyState == "complete"){
	//执行操作
}
```

支持 readyState 属性的浏览器有 IE4+、Firefox 3.6+、Safari、Chrome和 Opera 9+。

#### 2. 兼容模式

就像下面例子中所展示的那样，在标准模式下， document.compatMode 的值等于 "CSS1Compat" ，而在混杂模式下， document.compatMode 的值等于 "BackCompat" 。

```javascript
if (document.compatMode == "CSS1Compat"){
	alert("Standards mode");
} else {
	alert("Quirks mode");
}
```

后来，陆续实现这个属性的浏览器有 Firefox、Safari 3.1+、Opera 和 Chrome。最终，HTML5 也把这个属性纳入标准，对其实现做出了明确规定。

#### 3.  head 属性
作为对 document.body 引用文档的 <body> 元素的补充，HTML5 新增了 document.head 属性，引用文档的 head 元素。要引用文档的 <head> 元素，可以结合使用这个属性和另一种后备方法。

```javascript
var head = document.head || document.getElementsByTagName("head")[0];
```

如果可用，就使用 document.head ，否则仍然使用 getElementsByTagName() 方法。
实现 document.head 属性的浏览器包括 Chrome 和 Safari 5。

#### 11.3.4 字符集属性

```javascript
alert(document.charset); //"UTF-16"
document.charset = "UTF-8";
```

如果文档没有使用默认的字符集，那 charset 和 defaultCharset 属性的值可能会不一样，例如：

```javascript
if (document.charset != document.defaultCharset){
	alert("Custom character set being used.");
}
```
支持 document.charset 属性的浏览器有 IE、Firefox、Safari、Opera 和 Chrome。支持document.defaultCharset 属性的浏览器有 IE、Safari 和 Chrome。

#### 11.3.5 自定义数据属性

HTML5规定可以为元素添加非标准的属性，但要添加前缀 data- ，目的是为元素提供与渲染无关的信息，或者提供语义信息。这些属性可以任意添加、随便命名，只要以 data- 开头即可。

```html
<div id="myDiv" data-appId="12345" data-myname="Nicholas"></div>
```

添加了自定义属性之后，可以通过元素的 dataset 属性来访问自定义属性的值。 dataset 属性的值是 DOMStringMap 的一个实例，也就是一个名值对儿的映射。在这个映射中，每个 data-name 形式的属性都会有一个对应的属性，只不过属性名没有 data- 前缀（比如，自定义属性是 data-myname ，那映射中对应的属性就是 myname ）。

```javascript
//本例中使用的方法仅用于演示
var div = document.getElementById("myDiv");

//取得自定义属性的值
var appId = div.dataset.appId;
var myName = div.dataset.myname;

//设置值
div.dataset.appId = 23456;
div.dataset.myname = "Michael";

//有没有"myname"值呢？
if (div.dataset.myname){
	alert("Hello, " + div.dataset.myname);
}
```

如果需要给元素添加一些不可见的数据以便进行其他处理，那就要用到自定义数据属性。在跟踪链接或混搭应用中，通过自定义数据属性能方便地知道点击来自页面中的哪个部分。
在编写本书时，支持自定义数据属性的浏览器有 Firefox 6+和 Chrome。

#### 11.3.6 插入标记


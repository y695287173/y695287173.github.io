---
title: CountDownLatch在多线程中的作用及用法
date: 2018-03-05 19:08:44
tags:
- android
- java
---

>CountDownLatch的作用用一句话来概括的话，它可以等待你想要的线程正常处理完成后再去处理其他和这些线程时序相关的代码。
<!-- more -->

>使用场景

当你有两个任务在同时进行，而你需要在两个任务都完成时提示用户已完成任务，通常我们可以是join来进行等待。如下

01
public class JoinCountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        Thread task1 = new Thread(new Runnable() {
            @Override
            public void run() {
				System.out.println("task1 finish");
            }
        });
 
        Thread task2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("task2 finish");
            }
        });
        task1.start();
        task2.start();
        task1.join();
        task2.join();
        System.out.println("all task finish");
	}
}

join()用于让当前线程等待指导join线程执行结束，实现原理就是不停地检查join线程是否存活，如果线程存活则让当前线程等待，线程结束后，会调用notify和notifyAll方法唤醒调用线程，代码如下：

while (isAlive()) {
 wait(0);
}

JDK1.5之后提供了CountDownLatch类，它除了join的功能外，还能提供一些额外的功能

public class CountDownLatchTest {

    static CountDownLatch c = new CountDownLatch(2);
 
    public static void main(String[] args) throws InterruptedException {
		Thread task1 = new Thread(new Runnable() {
            @Override
            public void run() {
				c.countDown();
				System.out.println("task1 finish");
            }
        });
 
        Thread task2 = new Thread(new Runnable() {
            @Override
            public void run() {
				c.countDown();
                System.out.println("task2 finish");
            }
        });
		
        task1.start();
		task2.start();
        c.await();
        System.out.println("3");
    }
}

CountDownLatch的构造函数接收一个int类型的参数作为计数器，如果你想等待N个点完成，这里就传入N。

当我们调用一次CountDownLatch的countDown方法时，N就会减1，CountDownLatch的await会阻塞当前线程，直到N变成零。由于countDown方法可以用在任何地方，所以这里说的N个点，可以是N个线程，也可以是1个线程里的N个执行步骤。用在多个线程时，你只需要把这个CountDownLatch的引用传递到线程里。

>其他方法
如果有某个解析sheet的线程处理的比较慢，我们不可能让主线程一直等待，所以我们可以使用另外一个带指定时间的await方法，await(long time, TimeUnit unit): 这个方法等待特定时间后，就会不再阻塞当前线程。join也有类似的方法。

>注意：
在需要通过回调函数来执行countDown时注意，如果awit方法在主线程调用，那么会阻塞回调函数的执行，从而导致死锁。
计数器必须大于等于0，只是等于0时候，计数器就是零，调用await方法时不会阻塞当前线程。
CountDownLatch不可能重新初始化或者修改CountDownLatch对象的内部计数器的值。
一个线程调用countDown方法 happen-before 另外一个线程调用await方法。


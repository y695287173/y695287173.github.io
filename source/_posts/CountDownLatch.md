---
title: CountDownLatch�ڶ��߳��е����ü��÷�
date: 2018-03-05 19:08:44
tags:
- android
- java
---

>CountDownLatch��������һ�仰�������Ļ��������Եȴ�����Ҫ���߳�����������ɺ���ȥ������������Щ�߳�ʱ����صĴ��롣
<!-- more -->

>ʹ�ó���

����������������ͬʱ���У�������Ҫ�������������ʱ��ʾ�û����������ͨ�����ǿ�����join�����еȴ�������

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

join()�����õ�ǰ�̵߳ȴ�ָ��join�߳�ִ�н�����ʵ��ԭ����ǲ�ͣ�ؼ��join�߳��Ƿ������̴߳�����õ�ǰ�̵߳ȴ����߳̽����󣬻����notify��notifyAll�������ѵ����̣߳��������£�

while (isAlive()) {
 wait(0);
}

JDK1.5֮���ṩ��CountDownLatch�࣬������join�Ĺ����⣬�����ṩһЩ����Ĺ���

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

CountDownLatch�Ĺ��캯������һ��int���͵Ĳ�����Ϊ���������������ȴ�N������ɣ�����ʹ���N��

�����ǵ���һ��CountDownLatch��countDown����ʱ��N�ͻ��1��CountDownLatch��await��������ǰ�̣߳�ֱ��N����㡣����countDown�������������κεط�����������˵��N���㣬������N���̣߳�Ҳ������1���߳����N��ִ�в��衣���ڶ���߳�ʱ����ֻ��Ҫ�����CountDownLatch�����ô��ݵ��߳��

>��������
�����ĳ������sheet���̴߳���ıȽ��������ǲ����������߳�һֱ�ȴ����������ǿ���ʹ������һ����ָ��ʱ���await������await(long time, TimeUnit unit): ��������ȴ��ض�ʱ��󣬾ͻ᲻��������ǰ�̡߳�joinҲ�����Ƶķ�����

>ע�⣺
����Ҫͨ���ص�������ִ��countDownʱע�⣬���awit���������̵߳��ã���ô�������ص�������ִ�У��Ӷ�����������
������������ڵ���0��ֻ�ǵ���0ʱ�򣬼����������㣬����await����ʱ����������ǰ�̡߳�
CountDownLatch���������³�ʼ�������޸�CountDownLatch������ڲ���������ֵ��
һ���̵߳���countDown���� happen-before ����һ���̵߳���await������


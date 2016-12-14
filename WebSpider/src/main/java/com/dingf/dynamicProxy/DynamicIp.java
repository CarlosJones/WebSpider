package com.dingf.dynamicProxy;

import java.util.Map;

/**
 * 假设有10个线程在跑，大家都正常的跑，跑着跑着达到限制了，
 * 于是大家争先恐后（几乎是同时）请求更换IP，
 * 这个时候同步的作用就显示出来了，只会有一个线程去更换IP，
 * 在他结束之前其他线程都在等，等IP更换成功成功之后，
 * 其他线程会被唤醒并返回
 *
 * 算法描述：
 * 1、假设总共有N个线程抓取网页，发现被封锁之后依次排队请求锁，注意：可以想象成是同时请求。
 * 2、线程1抢先获得锁，并且设置isDialing = true后开始更换IP，注意：线程1设置isDialing = true后其他线程才可能获得锁。
 * 3、其他线程（2-N）依次获得锁，发现isDialing = true，于是wait。注意：获得锁并判断一个布尔值，跟后面的操作比起来，时间可以忽略。
 * 4、线程1更换IP完毕isDialing = false。注意：这个时候可以断定，其他所有线程必定是处于wait状态等待唤醒。
 * 5、线程1唤醒其他线程，其他线程和线程1返回开始抓取网页。
 * 6、抓了一会儿之后，又会被封锁，于是回到步骤1。
 * 注意：在本场景中，3和4的断定是没问题的，就算是出现“不可能”的情况，
 * 即线程1已经更换完成了，可2-N还没获得锁（汗），也不会重复更换IP的情况，
 * 因为算法考虑了请求更换IP时间和上一次成功更换IP时间。
 * @return 更改IP是否成功
 */
public class DynamicIp {
	private static boolean isChanging=false;
	public static boolean toNewIp() {
        synchronized (DynamicIp.class) {
            if (isChanging) {     
                try {
                    DynamicIp.class.wait();
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
                return true;
            }
            isChanging = true;
        }
        int i=0;
        System.getProperties().setProperty("proxySet", "true");
		System.getProperties().setProperty("http.proxyHost", ProxyServer.proxyIP[i]);
		System.getProperties().setProperty("http.proxyPort", ProxyServer.proxyPort[i]); 
		if(i<5) i++;
		else i=0;
            //通知其他线程更换IP成功
            synchronized (DynamicIp.class) {
                DynamicIp.class.notifyAll();
            }            
            isChanging = false;
            return true;
	}     
}


   

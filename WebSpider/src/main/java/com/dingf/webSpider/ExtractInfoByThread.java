package com.dingf.webSpider;

import java.util.ArrayList;

public class ExtractInfoByThread extends ExtractInfo implements Runnable {
	private int threadId;
	private static ThreadLocal<FileName> TLfn = null;
	private static ThreadLocal<String> TLuri = new ThreadLocal<String>();

	public ExtractInfoByThread(int threadId) {
		this.threadId = threadId;
		TLfn = new ThreadLocal<FileName>();
	}

	public static void main(String[] args) {
		ArrayList<Thread> threadList = new ArrayList<Thread>(CrawlConfig.CRAWL_THREAD_NUM);
		try {
			
			// for (int i = 0; i < CrawlConfig.CRAWL_THREAD_NUM; i++) {
			for (int i = 0; i < 12; i++) {
				ExtractInfoByThread info = new ExtractInfoByThread(i);
				Thread t = new Thread(info);
				t.start();
				threadList.add(t);
				Thread.sleep(100L);
			}
					
			while (threadList.size() > 0) {
				Thread child = (Thread) threadList.remove(0);
				child.join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		// TODO Auto-generated method stub
		
		FileName fn = getFileName();
		try {
//			System.out.println(fn.getFileName());
			do{
			 TLuri.set(CrawlConfig.CRAWL_DOWNLOAD_PATH + fn.getFileName());

			 extractInfomation(TLuri.get());
			}while(fn!=null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private  FileName getFileName() {
		FileName fn = TLfn.get();
		if (null == fn) {
			fn = new FileName();
			TLfn.set(fn);
		}
		return fn;
	}

}

package com.dingf.webSpider;

import java.util.ArrayList;

public class MyCrawlerByThread extends MyCrawler implements Runnable{
    private int threadId;
     
    public MyCrawlerByThread(int id) {
        this.threadId = id;
    }
    /**
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            crawling(new String[]{CrawlConfig.CRAWL_PATH}, threadId);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
     
    public static void main(String[] args) {
        try {
            long startTime=System.currentTimeMillis(); 
            System.out.println("采集开始");
            ArrayList<Thread> threadList = new ArrayList<Thread>(CrawlConfig.CRAWL_THREAD_NUM);
            for(int i = 0 ; i < CrawlConfig.CRAWL_THREAD_NUM; i++) {
                MyCrawlerByThread crawler = new MyCrawlerByThread(i);
                Thread t = new Thread(crawler);
                t.start();
                threadList.add(t);
                /*
                * 太频繁的下载会激活反爬虫机制。
                * 虽然采用了多线程编程，但是为了下载更多的网页，还是设置了比较长的时间间隔，做一些尝试。
                * */
                Thread.sleep(2500L);
            }
            while(threadList.size() > 0) {
                Thread child = (Thread) threadList.remove(0);
                child.join();
            }
            System.out.println("采集结束");
            long endTime=System.currentTimeMillis(); 
            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
             
        } catch(Exception e) {
            e.printStackTrace();
        }
         
    }
}




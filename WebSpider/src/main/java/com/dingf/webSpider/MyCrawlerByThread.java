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
    @Override
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
                Thread.sleep(10L);
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




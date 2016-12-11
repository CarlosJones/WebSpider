package com.dingf.webSpider;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ConnectTimeoutException;

public class MyCrawler {
    public static BDBFrontier visitedFrontier;
    public static BDBFrontier unvisitedFrontier;
    private static int num = 0;  
     
    public MyCrawler() {
        try{
            if(visitedFrontier == null){
                visitedFrontier = new BDBFrontier(CrawlConfig.CRAWL_VISITED_FRONTIER);      //采用Nosql数据库存储访问地址方式
                visitedFrontier.clearAll();
            }
            if(unvisitedFrontier == null) {
                unvisitedFrontier = new BDBFrontier(CrawlConfig.CRAWL_UNVISITED_FRONTIER);
                unvisitedFrontier.clearAll();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
     
    private void initCrawlerWithSeeds(String[] seeds) {
        synchronized (this) {
            try {
                for(int i = 0;i<seeds.length;i++){
                    CrawlUrl url = new CrawlUrl();            //采用berkeleyDB形式
                    url.setOriUrl(seeds[i]);
                    unvisitedFrontier.putUrl(url);                   
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
     
    public  void crawling(String[] seeds, int threadId) {
        try {
            LinkFilter filter = new LinkFilter() {
                @Override
                public boolean accept(String url) {
                    if(url.startsWith(CrawlConfig.CRAWL_LIMIT_PATH1)||url.startsWith(CrawlConfig.CRAWL_LIMIT_PATH2)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            };
         
             
            initCrawlerWithSeeds(seeds);
             
            //采用berkeleyDB方式存储         
            do{
                System.out.println("线程：" + threadId);
                
                CrawlUrl visitedCrawlUrl = (CrawlUrl)unvisitedFrontier.getNext();
                
                if(visitedCrawlUrl == null) {
                    continue;
                }
                             
                String visitedUrl = visitedCrawlUrl.getOriUrl();
                if(visitedFrontier.contains(visitedUrl)) {            //同步数据
                    visitedCrawlUrl = (CrawlUrl)unvisitedFrontier.getNext();
                    continue;
                }
                 
                visitedFrontier.putUrl(visitedCrawlUrl);
                 
                if(null == visitedUrl || "".equals(visitedUrl.trim())) {   //抓取的地址为空
                    visitedFrontier.putUrl(visitedCrawlUrl);
                    visitedCrawlUrl = (CrawlUrl)unvisitedFrontier.getNext();
                    continue;
                }
                 
                try{               	
                	Pattern pattern1 = Pattern.compile("^((https|http|ftp|rtsp|mms)?://)"
                			+"(book).(douban).(com)/"
                			+"(subject)/"
                			+"([0-9]{1,20})/$"
                            ); 
                    Matcher matcher1 = pattern1.matcher(visitedUrl);
                    boolean isMatch1= matcher1.matches(); 
                    Pattern pattern2 = Pattern.compile("^((https|http|ftp|rtsp|mms)?://)"
                			+"(book).(douban).(com)/"
                			+"(tag)/(编程)"
                			+ "((.(start=)[0-9]{2,4}.(amp).(type=T))"
                			+ "|())"
                			+ "$"
                               );  
                    Matcher matcher2 = pattern2.matcher(visitedUrl);
                    boolean isMatch2 = matcher2.matches();
                    if(isMatch1||isMatch2){
                    	RetrievePage.downloadPage(visitedUrl);
                    }//下载页面
                  //https://book.douban.com/tag/编程?start=40&amp;type=T**********3
                  //CRAWL_PATH = "https://book.douban.com/tag/编程"
                    if(isMatch2){
                    Set<String> links = HtmlParserTool.extractLinks(visitedUrl, filter);
                    for(String link :links) {
                        if(!visitedFrontier.contains(link)
                            &&!unvisitedFrontier.contains(link))
                        {
                            CrawlUrl unvisitedCrawlUrl = new CrawlUrl();
                            unvisitedCrawlUrl.setOriUrl(link);
                            unvisitedFrontier.putUrl(unvisitedCrawlUrl);
                        }
                    }
                    }
                }catch(ConnectTimeoutException e) {                            //超时继续读下一个地址
                    visitedFrontier.putUrl(visitedCrawlUrl);
                    visitedCrawlUrl = (CrawlUrl)unvisitedFrontier.getNext();
                    num ++;
                    e.printStackTrace();
                    continue;
                }catch(SocketTimeoutException e) {
                    visitedFrontier.putUrl(visitedCrawlUrl);
                    visitedCrawlUrl = (CrawlUrl)unvisitedFrontier.getNext();
                    num ++;
                    e.printStackTrace();
                    continue;
                }
                visitedCrawlUrl = (CrawlUrl)unvisitedFrontier.getNext();
                num ++;
                 
            }while(BDBFrontier.threads >0 && num < 1000000);
        }
         
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
     
}
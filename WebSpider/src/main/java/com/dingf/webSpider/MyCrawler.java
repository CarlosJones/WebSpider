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
                    Pattern pattern = Pattern.compile("^((https|http|ftp|rtsp|mms)?://)"
                            + "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                            + "|"
                            + "([0-9a-z_!~*'()-]+\\.)*"
                            + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                            + "[a-z]{2,6})"
                            + "(:[0-9]{1,4})?"
                            + "((/?)|"
                            + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$"); 
                    Matcher matcher = pattern.matcher(url);
                    boolean isMatch= matcher.matches();
                    if(isMatch && url.startsWith(CrawlConfig.CRAWL_LIMIT_PATH)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            };
         
             
            initCrawlerWithSeeds(seeds);
             
            //采用berkeleyDB方式存储
                                
            CrawlUrl visitedCrawlUrl = (CrawlUrl)unvisitedFrontier.getNext();
            //visitedFrontier.putUrl(visitedCrawlUrl);
             
            do{
                System.out.println("线程：" + threadId);
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
                    RetrievePage.downloadPage(visitedUrl);                    //下载页面
                    Set<String> links = HtmlParserTool.extractLinks(visitedUrl, filter);
                    for(String link :links) {
                        if(!visitedFrontier.contains(link)
                            &&!unvisitedFrontier.contains(link)    )
                        {
                            CrawlUrl unvisitedCrawlUrl = new CrawlUrl();
                            unvisitedCrawlUrl.setOriUrl(link);
                            unvisitedFrontier.putUrl(unvisitedCrawlUrl);
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
                 
            }while(BDBFrontier.threads >0 && num < 1000);
        }
         
        catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
     
}
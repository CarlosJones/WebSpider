package com.dingf.webSpider;

public class CrawlConfig {
	public static final String CRAWL_PATH = "https://book.douban.com/tag/编程";
    public static final String CRAWL_LIMIT_PATH1 = "https://book.douban.com/subject";
    public static final String CRAWL_LIMIT_PATH2 = "https://book.douban.com/tag/编程";
    public static final String CRAWL_VISITED_FRONTIER = "/home/ding-fang/Document/gitrepo/worm/test/cache/hevisited";
    public static final String CRAWL_UNVISITED_FRONTIER = "/home/ding-fang/Document/gitrepo/worm/test/cache/heunvisited";
    public static final String CRAWL_CACHED_URL = "/home/ding-fang/Document/gitrepo/worm/test/cache/cached";
    public static final String CRAWL_DOWNLOAD_PATH = "/home/ding-fang/Document/gitrepo/worm/test/"; 
    public static final String CRAWL_SAVE_FILE = "/home/ding-fang/Document/gitrepo/worm/test/cache/result.txt"; 
    public static final int CRAWL_THREAD_NUM = 8;
}

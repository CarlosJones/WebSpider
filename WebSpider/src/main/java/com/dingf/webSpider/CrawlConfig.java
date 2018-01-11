package com.dingf.webSpider;

public class CrawlConfig {
	public static final String CRAWL_PATH = "https://book.douban.com/tag/编程?type=S";
    public static final String CRAWL_LIMIT_PATH1 = "https://book.douban.com/subject";
    public static final String CRAWL_LIMIT_PATH2 = "https://book.douban.com/tag/编程";
    public static final String CRAWL_VISITED_FRONTIER = "/home/dingf/WebSpider/cache/hevisited";
    public static final String CRAWL_UNVISITED_FRONTIER = "/home/dingf/WebSpider/cache/heunvisited";
    public static final String CRAWL_CACHED_URL = "/home/dingf/WebSpider/cache/cached";
    public static final String CRAWL_DOWNLOAD_PATH = "/home/dingf/WebSpider/results/pages/";
    public static final String CRAWL_SAVE_FILE = "/home/dingf/WebSpider/cache/result.txt";
    public static final int CRAWL_THREAD_NUM = 8;
}

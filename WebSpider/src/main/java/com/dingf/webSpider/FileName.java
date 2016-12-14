package com.dingf.webSpider;

import java.io.FileNotFoundException;

import com.sleepycat.je.DatabaseException;

public class FileName {
	public static BDBFrontier cached;
	public FileName(){
		try {
			cached = new BDBFrontier(CrawlConfig.CRAWL_CACHED_URL);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getFileName() throws Exception{				
		CrawlUrl value= (CrawlUrl) cached.getNext();
		String url=value.getOriUrl();
		url = url.substring(7);
		url = url.replaceAll("[\\?/:*|<>\"]","_") + ".html";
		return url;
	}
}

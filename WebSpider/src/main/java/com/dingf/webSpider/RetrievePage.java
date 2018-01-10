package com.dingf.webSpider;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RetrievePage {
    
	   
	  private static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727";
	    private static String DEFAULT_CHARSET = "GB2312,utf-8;q=0.7,*;q=0.7";
	    private static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	     
	    /**
	     * 下载文件
	     * @param path
	     * @return
	     * @throws Exception
	     * @throws IOException
	     */
	    public static boolean downloadPage(String path) throws Exception,IOException
	    {
	        CloseableHttpClient httpclient = HttpClients.createDefault();
	        HttpGet httpget = new HttpGet(path);
	         
	        httpget.addHeader("Accept-Charset", DEFAULT_CHARSET);
	    //    httpget.addHeader("Host", host);
	        httpget.addHeader("Accept", ACCEPT);
	        httpget.addHeader("User-Agent", USER_AGENT);
	         
	        RequestConfig requestConfig = RequestConfig.custom()             //设置超时
	                .setSocketTimeout(3000)
	                .setConnectTimeout(3000)
	                .build();
	        httpget.setConfig(requestConfig);
	        CloseableHttpResponse response = httpclient.execute(httpget);
	        try {
	            HttpEntity entity = response.getEntity();
	            StatusLine statusLine = response.getStatusLine();
	             
	            if(statusLine.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY ||                      //如果是转移
	                    statusLine.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY ||
	                    statusLine.getStatusCode() == HttpStatus.SC_SEE_OTHER ||
	                    statusLine.getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT)
	            {
	                Header header = httpget.getFirstHeader("location");
	                if(header != null){
	                    String newUrl = header.getValue();
	                    if(newUrl == null || newUrl.equals(""))
	                    {
	                        newUrl = "/";
	                        HttpGet redirect = new HttpGet(newUrl);
	                    }
	                }
	            }
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK) {                   //成功访问
	                if (entity == null) {
	                    throw new ClientProtocolException("Response contains no content");
	                }
	                else {
	                    InputStream instream = entity.getContent();
	                    String filename = getFilenameByUrl(path,entity.getContentType().getValue());
	   
	                  OutputStream outstream = new FileOutputStream(CrawlConfig.CRAWL_DOWNLOAD_PATH + filename);                 //存储到磁盘
	                    try {
	                        //System.out.println(convertStreamToString(instream));
	                        int tempByte = -1;
	                        while((tempByte = instream.read())>0)
	                        {
	                            outstream.write(tempByte);
	                        }
	                        return true;
	                    }
	                    catch(Exception e){
	                        e.printStackTrace();
	                        return false;
	                    } finally {
	                        if(instream != null)
	                        {
	                            instream.close();
	                        }
	                        if(outstream != null)
	                        {
	                            outstream.close();
	                        }
	                    }
	                }
	            }
	            return false;
	        }finally {
	            response.close();
	        }
	    }
	     
	     
	    public static String getFilenameByUrl(String url, String contentType) {
	        url = url.substring(7);
	        if(contentType.indexOf("html") != -1) {
	            url = url.replaceAll("[\\?/:*|<>\"]","_") + ".html";
	            return url;
	        }
	        else {
	            url = url.replaceAll("[\\?/:*|<>\"]","_") + contentType.substring(contentType.lastIndexOf('/') + 1);
	            return url;
	        }
	    }
	     
	     
	    /**
	     * 转换流数据为字符串
	     * @param is
	     * @return
	     */
	    public static String convertStreamToString(InputStream is) {   
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
	        StringBuilder sb = new StringBuilder();   
	        String line = null;   
	        try {   
	            while ((line = reader.readLine()) != null) {   
	                sb.append(line + "/n");   
	            }   
	        } catch (IOException e) {   
	            e.printStackTrace();   
	        } finally {   
	        }   
	        return sb.toString();   
	    }    
	     
	    public static void main(String[] args)
	    {
	        try{
	            System.out.println("下载开始");
	            RetrievePage.downloadPage(CrawlConfig.CRAWL_PATH);
	            System.out.println("下载结束");
	        }
	        catch(HttpException e){
	            e.printStackTrace();
	        }
	        catch(IOException e)
	        {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	         
	    }
	     
	}

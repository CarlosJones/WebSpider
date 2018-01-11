package com.dingf.webSpider;

import java.io.FileNotFoundException;
import java.util.Map.Entry;
import java.util.Set;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;



public class BDBFrontier extends AbstractFrontier implements Frontier{
    @SuppressWarnings("rawtypes")
	private StoredMap pendingUrisDB = null;
    public static int threads = CrawlConfig.CRAWL_THREAD_NUM;
     
    /**
     * Creates a new instance of BDBFrontier.
     *
     * @param homeDirectory
     * @throws DatabaseException
     * @throws FileNotFoundException
     */
     
    @SuppressWarnings("unchecked")
	public BDBFrontier(String homeDirectory) throws DatabaseException,
            FileNotFoundException {
        super(homeDirectory);
        @SuppressWarnings({ "rawtypes", "unchecked" })
		EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
        @SuppressWarnings({ "unchecked", "rawtypes" })
		EntryBinding valueBinding = new SerialBinding(javaCatalog, CrawlUrl.class);
        pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
    }
     
    /**
     * 
     * clearAll:
     * 清除数据库
     *
     * @param参数
     * @return void    返回值
     * @throws 
     *
     */
    public void clearAll() {
        if(!pendingUrisDB.isEmpty())
            pendingUrisDB.clear();
    }
    /**
     * 获得下一条记录
     * @seecom.fc.frontier.Frontier#getNext()
     */
    public synchronized CrawlUrl getNext() throws Exception {
        CrawlUrl result = null;
        while(true) {
            if(!pendingUrisDB.isEmpty()) {
                Set entrys = pendingUrisDB.entrySet();
              
              Entry<String, CrawlUrl> entry = (Entry<String,CrawlUrl>) pendingUrisDB.entrySet().iterator().next();
                result = entry.getValue();         //下一条记录
                delete(entry.getKey());            //删除当前记录
 //               System.out.println("del:"+entry.getKey());
                System.out.println("get:" + entrys);
                return result;
            } else {
                threads --;
                if(threads > 0) {
                    wait();
                    threads ++;
                } else {
                    notifyAll();
                    return null;
                }
            }
        }
    }
    /**
     * 存入url
     * @seecom.fc.frontier.Frontier#putUrl(com.fc.CrawlUrl)
     */
    public synchronized boolean putUrl(CrawlUrl url) throws Exception {
        if(url.getOriUrl() != null && !url.getOriUrl().equals("") 
                && !pendingUrisDB.containsKey(url.getOriUrl())) 
        {
            Set entrys = pendingUrisDB.entrySet();
             
            put(url.getOriUrl(), url);
            notifyAll();
            System.out.println("put:" + url.getOriUrl() + url);
            return true;
        }
        return false;
    }

    public boolean contains(Object key) {
        if(pendingUrisDB.containsKey(key))
            return true;
        return false;
    }
    /**
     * 存入数据库
     * @seecom.fc.frontier.AbstractFrontier#put(java.lang.Object, java.lang.Object)
     */
    @Override
    protected synchronized void put(Object key, Object value) {
        pendingUrisDB.put(key, value);
         
    }
     
     
    /**
     * 从数据库取出
     * @seecom.fc.frontier.AbstractFrontier#get(java.lang.Object)
     */
    @Override
    protected synchronized Object get(Object key) {
        return pendingUrisDB.get(key);
    }
    /**
     * 删除
     * @seecom.fc.frontier.AbstractFrontier#delete(java.lang.Object)
     */
    @Override
    protected synchronized Object delete(Object key) {
        return pendingUrisDB.remove(key);
    }
     
    /**
     * 
     * calculateUrl:
     * 对Url进行计算，可以用压缩算法
     *
     * @param参数
     * @return String    返回值
     * @throws 
     *
     */
    private String calculateUrl(String url) {
        return url;
    }
     
    public static void main(String[] strs) {
        try {
            BDBFrontier bdbFrontier = new BDBFrontier("/home/dingf/WebSpider/cache/hevisited");
//            System.out.println(((CrawlUrl)bdbFrontier.getNext()));
            Set entrys = bdbFrontier.pendingUrisDB.entrySet();
            bdbFrontier.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}

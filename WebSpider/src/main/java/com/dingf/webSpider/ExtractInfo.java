package com.dingf.webSpider;

import java.io.IOException;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class ExtractInfo {
	ThreadLocal<Book> TLBook=new ThreadLocal<Book>();
	Book book = getBook();
	
	private Book getBook(){
		Book book = TLBook.get();
		if(null == book){
			book = new Book();
			TLBook.set(book);
		}
		return book;
	}
	public synchronized void extractInfomation(String url) throws ParserException, IOException{
        extractName(url);
        extractAveComment(url);
        extractCommentNum(url);
        extractBookInf(url);
        FileOperation fo = new FileOperation();
        fo.saveToText(CrawlConfig.CRAWL_SAVE_FILE,book);
	}
	
	public synchronized void extractName(String url) throws ParserException{
  // 书名 
		 Parser parser = new Parser(url);
	     parser.setEncoding("utf-8");
        NodeFilter nameFilter = new HasAttributeFilter("property", "v:itemreviewed");
        NodeList name = parser.extractAllNodesThatMatch(nameFilter); 
        for(int i = 0; i <name.size();i++)
        {
            Node tag = name.elementAt(i);          
            if(tag instanceof Span) {                         //链接文字               
                String bookName = tag.getFirstChild().getText();
                System.out.println(bookName); 
                book.name=bookName;
            }
	     }
	} 
        
         //获取平均评分
	public synchronized void extractAveComment(String url) throws ParserException{
		Parser parser = new Parser(url);
	     parser.setEncoding("utf-8");
        NodeFilter averageFilter = new HasAttributeFilter("property", "v:average");
        NodeList average = parser.extractAllNodesThatMatch(averageFilter); 
        for(int i = 0; i <average.size();i++)
        {
            Node tag = average.elementAt(i);
            if(tag instanceof TagNode) {
                TagNode t =(TagNode)tag;
                String text1 = t.getNextSibling().getText();
                System.out.println(text1);
                book.aveComment=text1;
            }
	     }
	} 
        
         //评论人数
	public synchronized void extractCommentNum(String url) throws ParserException{
		Parser parser = new Parser(url);
	     parser.setEncoding("utf-8");
        NodeFilter numFilter = new HasAttributeFilter("property", "v:votes");
        NodeList num = parser.extractAllNodesThatMatch(numFilter); 
        for(int i = 0; i <num.size();i++)
        {
            Node tag = num.elementAt(i);          
            if(tag instanceof Span) {                         //链接文字               
                String numComment = tag.getFirstChild().getText();
                System.out.println(numComment);
                book.commentNum=numComment;
            }
	     }
	}
        //作者,出版社，出版日期，价格
	public synchronized void extractBookInf(String url) throws ParserException{
		Parser parser = new Parser(url);
	     parser.setEncoding("utf-8");
        NodeFilter authorFilter = new HasAttributeFilter("class", "pl");
        NodeList author = parser.extractAllNodesThatMatch(authorFilter); 
        for(int i = 0; i <author.size();i++)
        {
            Node tag = author.elementAt(i);
            String txt=null;
            if(tag instanceof Span){
            	Span s =(Span)tag;
            if(0!=s.getChildCount()){
            	txt=s.getFirstChild().getText();
//            	System.out.println("---------------"+txt);
//            	System.out.println(s.getChildCount());
        
            if((txt.equals(" 作者"))) {             	
                String authorName = tag.getNextSibling().getNextSibling().getFirstChild().getText();               
                System.out.println(authorName);              
                book.author = authorName;
            }
            if((txt.equals("出版社:"))) {                                        
                String chuBanShe = tag.getNextSibling().getText();
                System.out.println(chuBanShe);
                book.chuBanShe=chuBanShe;
            }
            if((txt.equals("出版年:"))) {                                        
                String authorName = tag.getNextSibling().getText();
                System.out.println(authorName);
                book.time=authorName;
            }
            if((txt.equals("定价:"))) {                                        
                String price = tag.getNextSibling().getText();
                System.out.println(price);
                book.price=price;
            }
            }else{
            	break;
            }
	     }
        }
	}
	public static void main(String[] args) throws ParserException, IOException {
//		String fileName=CrawlConfig.CRAWL_DOWNLOAD_PATH+"_book.douban.com_subject_1101158_.html";
		String fileName=CrawlConfig.CRAWL_DOWNLOAD_PATH+"_book.douban.com_subject_1219329_.html";
		ExtractInfo e = new ExtractInfo();
		e.extractInfomation(fileName);
	}
}

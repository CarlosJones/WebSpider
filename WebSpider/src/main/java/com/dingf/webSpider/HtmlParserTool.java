package com.dingf.webSpider;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class HtmlParserTool {
    public static Set<String> extractLinks(String url, LinkFilter filter){
        Set<String> links = new HashSet<String>();
        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");
           
            @SuppressWarnings("serial")
			NodeFilter frameFilter = new NodeFilter() {      //过滤节点
                public boolean accept(Node node) {
                    if(node.getText().startsWith("frame src=")) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            };
             
            OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);           //获取所有合适的节点
            for(int i = 0; i <list.size();i++)
            {
                Node tag = list.elementAt(i);
                if(tag instanceof LinkTag) {                         //<a>标签，链接文字
                    LinkTag linkTag = (LinkTag) tag;
                    String text = linkTag.getLinkText();//链接文字
                    String linkUrl = linkTag.getLink();//url
                    System.out.println(linkUrl + "**********" + text);                  	                                     
                    if(filter.accept(linkUrl))
                        links.add(linkUrl);
                }
                else if (tag instanceof ImageTag)   //<img> 标签              //链接图片
                {
                    ImageTag image = (ImageTag) list.elementAt(i);
                    System.out.print(image.getImageURL() + "********");//图片地址
                    System.out.println(image.getText());//图片文字
                    if(filter.accept(image.getImageURL()))
                        links.add(image.getImageURL());
                }
                else//<frame> 标签
                {
                    //提取 frame 里 src 属性的链接如 <frame src="test.html"/>
                    String frame = tag.getText();
                    int start = frame.indexOf("src=");
                    frame = frame.substring(start);
                    int end = frame.indexOf(" ");
                    if (end == -1)
                        end = frame.indexOf(">");
                    frame = frame.substring(5, end - 1);
                    System.out.println(frame);
                    if(filter.accept(frame))
                        links.add(frame);
                }
            }
            return links;           
        } catch (ParserException e) {
            e.printStackTrace();
        	return null;
        }
    }
}

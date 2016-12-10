package com.dingf.crawler;

public class Test {
	public static void main(String[] args) {			
        String url = "http://www.baidu.com/";
        String regex = "hidefocus.+?src=\"//(.+?)\"";
        System.out.println(regex);
        String result = HttpGetUtils.get(url);
        System.out.println(result);
        String src = RegexStringUtils.regexString(result, regex);
        System.out.println(src);
    }
}

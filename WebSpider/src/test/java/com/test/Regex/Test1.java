package com.test.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
	public static void main(String[] args) {
//		String visitedUrl ="https://book.douban.com/subject/336424/";
//		String visitedUrl ="https://www.douban.com/";
		String visitedUrl ="https://book.douban.com/tag/编程";
//		String visitedUrl ="https://book.douban.com/tag/编程?start=20&amp;type=T";
		Pattern pattern = Pattern.compile("^((https|http|ftp|rtsp|mms)?://)"
			+"(book).(douban).(com)/"
			+"(tag)/(编程)"
			+ "((.(start=)[0-9]{2,4}.(amp).(type=T))"
			+ "|())"
			+ "$"
               ); 
//                Pattern pattern = Pattern.compile("^((https|http|ftp|rtsp|mms)?://)"
//            			+"(book).(douban).(com)/"
//            			+"(subject)/"
//            			+"([0-9]{1,20})/$"
//                        ); 
        Matcher matcher = pattern.matcher(visitedUrl);
        boolean isMatch= matcher.matches();
        System.out.println(isMatch);
	}
}

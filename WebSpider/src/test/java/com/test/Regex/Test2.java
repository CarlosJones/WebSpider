package com.test.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dingf on 18-1-14.
 */
public class Test2 {
    public static void main(String[] args) {
        String visitedUrl = "https://book.douban.com/subject/19952400/?channel = ";
        Pattern pattern2 = Pattern.compile("^((https|http|ftp|rtsp|mms)?://)"
                +"(book).(douban).(com)/"
                +"(subject)/"
                +"([0-9]{1,20})/"
//                +"|(\\?channel=subject_list&amp;platform=web)"
//                + "|()"
                +"$"
        );
        Matcher matcher2 = pattern2.matcher(visitedUrl);
        boolean isMatch2 = matcher2.matches();
        System.out.println(isMatch2);
    }
}

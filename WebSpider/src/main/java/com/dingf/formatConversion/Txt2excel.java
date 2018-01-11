package com.dingf.formatConversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
/**
 * Created by dingf on 18-1-11.
 */
public class Txt2excel {
    public static void main(String[] args) {

        File file = new File("/home/dingf/WebSpider/cache/result.txt");// 将读取的txt文件
        File file2 = new File("/home/dingf/WebSpider/cache/result.xls");// 将生成的excel表格

        if (file.exists() && file.isFile()) {

            InputStreamReader read = null;
            String line = "";
            BufferedReader input = null;
            WritableWorkbook wbook = null;
            WritableSheet sheet;

            try {
                read = new InputStreamReader(new FileInputStream(file), "utf-8");
                input = new BufferedReader(read);

                wbook = Workbook.createWorkbook(file2);// 根据路径生成excel文件
                sheet = wbook.createSheet("first", 0);// 新标签页

                try {
                    Label company = new Label(0, 0, "书名");// 如下皆为列名
                    sheet.addCell(company);
                    Label score = new Label(1, 0, "评分");
                    sheet.addCell(score);
                    Label evaluate = new Label(2, 0, "评价人数");
                    sheet.addCell(evaluate);
                    Label author = new Label(3, 0, "作者");
                    sheet.addCell(author);
                    Label publishingHouse = new Label(4, 0, "出版社");
                    sheet.addCell(publishingHouse);
                    Label date = new Label(5, 0, "出版日期");
                    sheet.addCell(date);
                    Label price = new Label(6, 0, "价格");
                    sheet.addCell(price);
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

                int m = 1;// excel行数
                int n = 0;// excel列数
                Label t;
                while ((line = input.readLine()) != null) {

                    String[] words = line.split(",");// 把读出来的这行根据空格或tab分割开

                    for (int i = 0; i < words.length; i++) {
                        if (!words[i].matches("\\s*")) { // 当不是空行时
                            t = new Label(n, m, words[i].trim());
                            sheet.addCell(t);
                            n++;
                        }else{
                            t = new Label(n,m,"");
                            sheet.addCell(t);
                            n++;
                        }
                    }
                    n = 0;// 回到列头部
                    m++;// 向下移动一行
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            } finally {
                try {
                    wbook.write();
                    wbook.close();
                    input.close();
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("over!");
            System.exit(0);
        } else {
            System.out.println("file is not exists or not a file");
            System.exit(0);
        }
    }
}

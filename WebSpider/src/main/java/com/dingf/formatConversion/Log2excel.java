package com.dingf.formatConversion;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.*;

/**
 * Created by dingf on 18-1-12.
 */
public class Log2excel {
    public static void main(String[] args) {

//        File file = new File("/home/dingf/WebSpider/log/download.log);// 将读取的txt文件
//        File file2 = new File("/home/dingf/WebSpider/log/download.xls");// 将生成的excel表格

//        File file = new File("/home/dingf/WebSpider/log/allUrl.log");// 将读取的txt文件
//        File file2 = new File("/home/dingf/WebSpider/log/allUrl.xls");// 将生成的excel表格

        File file = new File("/home/dingf/WebSpider/log/filterUrl.log");// 将读取的txt文件
        File file2 = new File("/home/dingf/WebSpider/log/filterUrl.xls");// 将生成的excel表格

//        File file = new File("/home/dingf/WebSpider/log/filterLink.log");// 将读取的txt文件
//        File file2 = new File("/home/dingf/WebSpider/log/filterLink.xls");// 将生成的excel表格

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

                int m = 0;// excel行数
                int n = 0;// excel列数
                Label t;
                while ((line = input.readLine()) != null) {

                    String[] words = line.split("[ \t]");// 把读出来的这行根据空格或tab分割开

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

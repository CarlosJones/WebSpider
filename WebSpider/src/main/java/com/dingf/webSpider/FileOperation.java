package com.dingf.webSpider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperation {
	public synchronized void  saveToText(String location,Book book) throws IOException{
		File f = new File(location);
		FileWriter ps = null;
		if(!f.exists()){
			System.out.println("File not exist!");
			f.createNewFile();
		} 
		try {
			ps = new FileWriter(f,true);
			ps.write(book.name+",");
			ps.write(book.aveComment+",");
			ps.write(book.commentNum+",");
			ps.write(book.author+",");
			ps.write(book.chuBanShe+",");
			ps.write(book.time+",");
			ps.write(book.price+"\n");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ps.close();
		}
		
	}
}

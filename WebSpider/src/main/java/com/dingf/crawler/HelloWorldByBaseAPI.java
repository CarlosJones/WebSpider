package com.dingf.crawler;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class HelloWorldByBaseAPI {
	
	private File file = new File("/home/ding-fang/Document/gitrepo/worm/test/cache/helloworld");
	private Environment env;
	private Database database;
	
	//建立环境
	private void setUp(){
		
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true); 
		env = new Environment(file, envConfig);
		
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		database = env.openDatabase(null, "baseAPIDemo", dbConfig);
	}
	
	//保存数据
	private void save(){
		String key = "BaseAPI";
		String value = "Hello World!";
		
		try {
			DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));//键
			DatabaseEntry valueEntry = new DatabaseEntry(value.getBytes("utf-8"));//值
			database.put(null, keyEntry, valueEntry);//保存键值对
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//检索数据
	private void get(){
		String key = "BaseAPI";
		String value;
		
		try {
			DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));//键
			DatabaseEntry valueEntry = new DatabaseEntry();//空的值
			if(database.get(null, keyEntry, valueEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS){//根据键来查找
				value = new String(valueEntry.getData(), "utf-8");//找到了对于的值
				System.out.println("key:BaseAPI,value:"+value);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	//关闭环境
	private void shutDown(){
		database.close();
		env.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HelloWorldByBaseAPI myCase = new HelloWorldByBaseAPI();
		myCase.setUp();
//		myCase.save();
		myCase.get();
		myCase.shutDown();
	}

}

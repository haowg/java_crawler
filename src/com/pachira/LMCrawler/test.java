package com.pachira.LMCrawler;

import bdbUtil.BdbPersistentQueue;

public class test {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		test t = new test();
		
		t.checkdbdb("mydbs");
//		t.testBDB3();
//		t.testBDB();
//		t.testotherBDB();
		
		
		long time2 = System.currentTimeMillis();
		System.out.println(time2 - time1);
	}
	void checkdbdb(String dbname){
		Berkeley_DB bdb = new Berkeley_DB();
		bdb.openDatabase(dbname,"D:\\bdb");
		for (String string : bdb.getEveryItem()) {
			System.out.println(string);
		}
		bdb.closeDatabase();
	}
	void testBDB3(){
		BdbPersistentQueue<String> persistentQueue = new BdbPersistentQueue<String>("D:\\bdb","pq",String.class);
		for(int i=0;i<10000;i++){
            persistentQueue.add(i+"");
            System.out.println(i);
        }
		persistentQueue.close();
		
		
		
	}
	void testotherBDB(){
		Berkeley_DB bdb = new Berkeley_DB();
		bdb.openDatabase("mydbs","D:\\bdb");
		CrawlUrl url = new CrawlUrl();
		for (int i = 0; i < 10; i++) {
			 url.setOriUrl("http://www.163.comsss"+i);
			System.out.println(i);
//			bBDBFrontier.put("sdfs", "asdf");
			bdb.writeToDatabase(url.getOriUrl(), url.getOriUrl(), false);
		}

		for (String string : bdb.getEveryItem()) {
			System.out.println(string);
		}
		bdb.closeDatabase();

	}
	
	void testBDB(){
		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("D:\\bsdb");
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			bBDBFrontier.putUrl(url);
			for (int i = 0; i < 1; i++) {
				 url.setOriUrl("http://www.163.comsdfsd"+i);
				System.out.println(i);
//				bBDBFrontier.put("sdfs", "asdf");
				bBDBFrontier.putUrl(url);
			}
			
			url.setOriUrl("http://www.163.comsdfsd");
			bBDBFrontier.putUrl(url);
			System.out.println(((CrawlUrl) bBDBFrontier.getNext()).getOriUrl());

			bBDBFrontier.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}

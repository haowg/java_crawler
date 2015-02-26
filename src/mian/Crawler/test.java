package mian.Crawler;

import filter.SimpleBloomFilter;
import Froniter.BDBFrontier;
import mian.Crawler.CrawlUrl;
import mian.bdbUtil.BdbPersistentQueue;
import mian.bdbUtil.Berkeley_DB;
import mian.tools.CheckMethods;

public class test {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		test t = new test();
		
		
//		t.checkdbdb("testURLs");
//		t.testBDB3();
		t.testBDB();
//		t.testotherBDB();
		
		
		long time2 = System.currentTimeMillis();
		CheckMethods.PrintInfoMessage("time\t"+String.valueOf(time2 - time1));
	}
	void checkdbdb(String dbname){
		Berkeley_DB bdb = new Berkeley_DB();
		bdb.openDatabase(dbname,"D:\\bsdb");
		for (String string : bdb.getEveryItem()) {
//			CheckMethods.PrintInfoMessage(string);
		}
		bdb.closeDatabase();
	}
	void testBDB3(){
		BdbPersistentQueue<String> persistentQueue = new BdbPersistentQueue<String>("D:\\bdb","pq",String.class);
		for(int i=0;i<10000;i++){
            persistentQueue.add(i+"");
            CheckMethods.PrintInfoMessage(String.valueOf(i));
        }
		persistentQueue.close();
		
		
		
	}
	void testotherBDB(){
		Berkeley_DB bdb = new Berkeley_DB();
		bdb.openDatabase("mydbs","D:\\bdb");
		CrawlUrl url = new CrawlUrl();
		for (int i = 0; i < 100; i++) {
			 url.setOriUrl("http://www.163.comsss"+i);
			CheckMethods.PrintInfoMessage(String.valueOf(i));
//			bBDBFrontier.put("sdfs", "asdf");
			bdb.writeToDatabase(url.getOriUrl(), url.getOriUrl(), false);
		}

		for (String string : bdb.getEveryItem()) {
			CheckMethods.PrintInfoMessage(string);
		}
		bdb.closeDatabase();

	}
	
	void testBDB(){
		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("D:\\bsdb","testURLs", true,new SimpleBloomFilter());
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			bBDBFrontier.putUrl(url);
			for (int i = 0; i < 100000; i++) {
				 url.setOriUrl("http://www.163.comsdfsd"+i);
CheckMethods.PrintInfoMessage(String.valueOf(i));
//				bBDBFrontier.put("sdfs", "asdf");
CheckMethods.PrintInfoMessage(url.getOriUrl()+" \t"+bBDBFrontier.size());
				bBDBFrontier.putUrl(url);
			}
			
			url.setOriUrl("http://www.163.comsdfsd");
			bBDBFrontier.putUrl(url);
CheckMethods.PrintInfoMessage(((CrawlUrl) bBDBFrontier.pool()).getOriUrl());

			bBDBFrontier.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
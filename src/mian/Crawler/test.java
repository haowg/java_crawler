package mian.Crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import filter.SimpleBloomFilter;
import Froniter.BDBFrontier;
import mian.Crawler.CrawlUrl;
import mian.bdbUtil.BdbPersistentQueue;
import mian.bdbUtil.Berkeley_DB;
import mian.tools.CheckMethods;
import mian.tools.myCookies;

public class test {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		test t = new test();
		
		t.testRegex("https://www.baidu.com/jsp?wd=Dangling%20meta%20character%20%27%3F%27%20near%20index%200&rsv_spt=1&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_pq=f4736661000061d0&rsv_t=83a6zu4dY%2BZruDu9dF0LsA9fj%2BW7yzmUngBIO%2FZ%2FBBFqRUlg%2FgStwf3A7ATOi6QrvWsy&inputT=1236&oq=win7%20vs&rsv_n=2&rsv_sug3=20&rsv_sug2=0&rsv_sug4=1236&rsv_sug=1");
//		t.testCrawler();
//		t.checkdbdb("jingdongvisitedSet");
//		t.testBDB3();
//		t.testBDB();
//		t.testotherBDB();
		
		
		long time2 = System.currentTimeMillis();
		CheckMethods.PrintInfoMessage("time\t"+String.valueOf(time2 - time1));
	}
	void testJson(){
		myCookies cookies = new myCookies("x.txt");
		System.out.println(cookies.getCookies());
	}
	void testCrawler(){
		
//		CrawlUrl[] jingdongSeeds = new CrawlUrl[] {new CrawlUrl("http://www.jd.com/allSort.aspx")};
		HashSet<CrawlUrl> jingdongSeeds = new HashSet<CrawlUrl>();
		jingdongSeeds.add(new CrawlUrl("http://www.jd.com/allSort.aspx", 0));
		HashSet<LinkFilter> linkFilters = new HashSet<>();
		linkFilters.add(new LinkFilter(".*jd\\.com.*"));
		GeneralCrawler crawler = new GeneralCrawler(linkFilters,"D:\\bsdb","jingdong",jingdongSeeds,new ArrayList<String>(), Integer.MAX_VALUE);
		
		new Thread(crawler).start();
//		new Thread(new MyCrawler(".*jd\\.com.*","D:\\bsdb","jd",jingdongSeeds)).start();
	}
	
	void checkdbdb(String dbname){
		Berkeley_DB bdb = new Berkeley_DB();
		bdb.openDatabase(dbname,"D:\\bsdb");
//		for (String string : bdb.getEveryItem()) {
//			CheckMethods.PrintInfoMessage(string);
//		}
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
	
	void testRegex(String url){
		String regex = ".*(/|com|cn|gov|edu|jsp|php|asp)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url.split("#")[0].split("\\?")[0]);
		if(m.matches()){
			System.out.println("match !");
		}
	}
	
	void testBDB(){
		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("D:\\bsdb","testURLs", true,new SimpleBloomFilter(),new SimpleBloomFilter());
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

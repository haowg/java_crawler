package mian.Crawler;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mian.tools.CheckMethods;
import mian.tools.HtmlParserTool;

import org.jsoup.nodes.Document;

import filter.SimpleBloomFilter;
import Froniter.BDBFrontier;

public class MyCrawler {


	/**
	 * 抓取过程
	 * 
	 * @return
	 * @param seeds
	 */

	/**
	 * 使用种子初始化 URL 队列
	 * 
	 * @return
	 * @param seeds
	 *            种子 URL
	 */
	

	
	public void crawlingbyDBFrt(CrawlUrl[] seeds) {
		// 定义过滤器
		// 开头的链接
		LinkFilter filter = new LinkFilter() {
			public boolean accept(String url) {
				String regex = ".*jd\\.com.*";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(url);
				if (m.matches()) {
					return true;
				} else {
					return false;
				}
			}
		};
		// 初始化 URL 队列
		SimpleBloomFilter sbf = new SimpleBloomFilter();
		BDBFrontier unVisitedSet=new BDBFrontier("D:\\bsdb","unVisitedSet", true,sbf);
		BDBFrontier visitedSet=new BDBFrontier("D:\\bsdb","visitedSet", false,sbf);
CheckMethods.PrintInfoMessage(unVisitedSet.size()+"");
CheckMethods.PrintInfoMessage(visitedSet.size()+"");
		initDBFrtrawlerWithSeeds(seeds,unVisitedSet);
CheckMethods.PrintInfoMessage(unVisitedSet.size()+"");
		
		// 循环条件：待抓取的链接不空且抓取的网页不多于 1000
		while (!unVisitedSet.isEmpty()
//				&& unVisitedSet.size() <= 10000) {
				&& visitedSet.size() <= 4) {
			// 队头 URL 出队列
			CrawlUrl visitUrl = null;
			try {
CheckMethods.PrintInfoMessage("unVisitedSet size = "+String.valueOf(unVisitedSet.size()));
				visitUrl = unVisitedSet.pool();
CheckMethods.PrintInfoMessage("unVisitedSet size = "+String.valueOf(unVisitedSet.size()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (visitUrl == null)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl.getOriUrl());
			// System.out.println(doc);
CheckMethods.PrintInfoMessage("URL:\t" + visitUrl.getOriUrl());
			// 该 URL 放入已访问的 URL 中
			visitedSet.putUrl(visitUrl);
CheckMethods.PrintInfoMessage("visitedSize = "+visitedSet.size());

			// 提取出下载网页中的 URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// 新的未访问的 URL 入队
//CheckMethods.PrintInfoMessage("unVisitedSet size = "+String.valueOf(unVisitedSet.size())+"------");
			for (String link : links) {
				CrawlUrl curl = new CrawlUrl();
				curl.setOriUrl(link);
				unVisitedSet.putUrl(curl);
//				curl.setOriUrl("http://www.jd.com/allSort.aspx");
//				unVisitedSet.putUrl(curl);
			}
//CheckMethods.PrintInfoMessage("unVisitedSet size = "+String.valueOf(unVisitedSet.size())+"------");
		}
		visitedSet.close();
		unVisitedSet.close();
	}

	private void initDBFrtrawlerWithSeeds(CrawlUrl[] seeds,BDBFrontier unVisitedSet) {
		for (int i = 0; i < seeds.length; i++)
			unVisitedSet.putUrl(seeds[i]);
		
	}
	
	// main 方法入口
	public static void main(String[] args) {

//		try {
//			BDBFrontier bBDBFrontier = new BDBFrontier("D:\\bdb");
//			CrawlUrl url = new CrawlUrl();
//			url.setOriUrl("http://www.163.com");
//			bBDBFrontier.putUrl(url);
//			for (int i = 0; i < 10000; i++) {
////				url.setOriUrl("http://www.163.comsdfsd");
//				System.out.println("i");
//				bBDBFrontier.put("sdfs","asdf");
//			}
//			url.setOriUrl("http://www.163.comsdfsd");
//			bBDBFrontier.putUrl(url);
//			Berkeley_DB bd = new Berkeley_DB();
//			bd.closeDatabase();
//			bd.openDatabase("D:\\bdb");
//			System.out.println(bd.getEveryItem());
//System.out.println(((CrawlUrl) bBDBFrontier.getNext()).getOriUrl());
//System.out.println(((CrawlUrl) bBDBFrontier.getNext()).getOriUrl());
//
//			bBDBFrontier.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//		}
		MyCrawler crawler = new MyCrawler();
//		crawler.crawling(new String[] { "http://www.lietu.com" });
//		crawler.crawling(new String[] { "http://www.jd.com/allSort.aspx" },new memoryLinkQueue());
		CrawlUrl curl = new CrawlUrl();
		curl.setOriUrl("http://www.jd.com/allSort.aspx" );
		crawler.crawlingbyDBFrt(new CrawlUrl[] {curl});
	}
}

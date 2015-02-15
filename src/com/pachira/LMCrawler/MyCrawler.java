package com.pachira.LMCrawler;

import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import bdbUtil.BdbPersistentQueue;

public class MyCrawler {
	/**
	 * 使用种子初始化 URL 队列
	 * 
	 * @return
	 * @param seeds
	 *            种子 URL
	 */
	private void initCrawlerWithSeeds(String[] seeds,memoryLinkQueue memoryLinkQueue) {
		for (int i = 0; i < seeds.length; i++)
			memoryLinkQueue.addUnvisitedUrl(seeds[i]);
	}

	/**
	 * 抓取过程
	 * 
	 * @return
	 * @param seeds
	 */
	public void crawling(String[] seeds,memoryLinkQueue memoryLinkQueue) { // 定义过滤器
											// 开头的链接
		LinkFilter filter = new LinkFilter() {
			public boolean accept(String url) {
				String regex = ".*jd\\.com.*";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(url);
				if (m.matches()) {
					return true;
				}
				else {
					return false;
				}
//				if (url.contains("jd.com")){
//					return true;}
//				else
//					return false;
//				if(url.startsWith("http://www.lietu.com"))
//					return true;
//				else
//					return false;
			}
		};
		// 初始化 URL 队列
		initCrawlerWithSeeds(seeds,memoryLinkQueue);
		// 循环条件：待抓取的链接不空且抓取的网页不多于 1000
		while (!memoryLinkQueue.unVisitedUrlsEmpty()
				&& memoryLinkQueue.getVisitedUrlNum() <= 1000) {
			// 队头 URL 出队列
			String visitUrl = (String) memoryLinkQueue.unVisitedUrlDeQueue();
			if (visitUrl == null)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl);
//			System.out.println(doc);
			CheckMethods.PrintInfoMessage("URL:\t"+visitUrl);
			// 该 URL 放入已访问的 URL 中
			memoryLinkQueue.addVisitedUrl(visitUrl);
			
			// 提取出下载网页中的 URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// 新的未访问的 URL 入队
			for (String link : links) {
				memoryLinkQueue.addUnvisitedUrl(link);
			}
		}
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
		//crawler.crawling(new String[] { "http://www.lietu.com" });
//		crawler.crawling(new String[] { "http://www.jd.com/allSort.aspx" },new memoryLinkQueue());
		crawler.crawlingbyDB(new String[] { "http://www.jd.com/allSort.aspx" });
	}

	private void crawlingbyDB(String[] seeds) {
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
		
		java.util.Queue<String> unVisitedSet=new BdbPersistentQueue<String>("D:\\bdb","unVisited",String.class);
		java.util.Queue<String> visitedSet=new BdbPersistentQueue<String>("D:\\bdb","visited",String.class);
		initDBCrawlerWithSeeds(seeds,unVisitedSet);
		
		// 循环条件：待抓取的链接不空且抓取的网页不多于 1000
		while (!unVisitedSet.isEmpty()
//				&& unVisitedSet.size() <= 10000) {
				&& visitedSet.size() <= 10) {
			// 队头 URL 出队列
			String visitUrl = (String) unVisitedSet
					.poll();
			if (visitUrl == null)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl);
			// System.out.println(doc);
			CheckMethods.PrintInfoMessage("URL:\t" + visitUrl);
			// 该 URL 放入已访问的 URL 中
			visitedSet.add(visitUrl);

			// 提取出下载网页中的 URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// 新的未访问的 URL 入队
			for (String link : links) {
				unVisitedSet.add(link);
			}
		}
//		visitedSet.

	}

	private void initDBCrawlerWithSeeds(String[] seeds,Queue<String> queue) {
		for (int i = 0; i < seeds.length; i++)
			queue.add(seeds[i]);
		
	}
}

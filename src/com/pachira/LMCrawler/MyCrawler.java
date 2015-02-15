package com.pachira.LMCrawler;

import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import bdbUtil.BdbPersistentQueue;

public class MyCrawler {
	/**
	 * ʹ�����ӳ�ʼ�� URL ����
	 * 
	 * @return
	 * @param seeds
	 *            ���� URL
	 */
	private void initCrawlerWithSeeds(String[] seeds,memoryLinkQueue memoryLinkQueue) {
		for (int i = 0; i < seeds.length; i++)
			memoryLinkQueue.addUnvisitedUrl(seeds[i]);
	}

	/**
	 * ץȡ����
	 * 
	 * @return
	 * @param seeds
	 */
	public void crawling(String[] seeds,memoryLinkQueue memoryLinkQueue) { // ���������
											// ��ͷ������
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
		// ��ʼ�� URL ����
		initCrawlerWithSeeds(seeds,memoryLinkQueue);
		// ѭ����������ץȡ�����Ӳ�����ץȡ����ҳ������ 1000
		while (!memoryLinkQueue.unVisitedUrlsEmpty()
				&& memoryLinkQueue.getVisitedUrlNum() <= 1000) {
			// ��ͷ URL ������
			String visitUrl = (String) memoryLinkQueue.unVisitedUrlDeQueue();
			if (visitUrl == null)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl);
//			System.out.println(doc);
			CheckMethods.PrintInfoMessage("URL:\t"+visitUrl);
			// �� URL �����ѷ��ʵ� URL ��
			memoryLinkQueue.addVisitedUrl(visitUrl);
			
			// ��ȡ��������ҳ�е� URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// �µ�δ���ʵ� URL ���
			for (String link : links) {
				memoryLinkQueue.addUnvisitedUrl(link);
			}
		}
	}

	// main �������
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
		// ���������
		// ��ͷ������
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
		// ��ʼ�� URL ����
		
		java.util.Queue<String> unVisitedSet=new BdbPersistentQueue<String>("D:\\bdb","unVisited",String.class);
		java.util.Queue<String> visitedSet=new BdbPersistentQueue<String>("D:\\bdb","visited",String.class);
		initDBCrawlerWithSeeds(seeds,unVisitedSet);
		
		// ѭ����������ץȡ�����Ӳ�����ץȡ����ҳ������ 1000
		while (!unVisitedSet.isEmpty()
//				&& unVisitedSet.size() <= 10000) {
				&& visitedSet.size() <= 10) {
			// ��ͷ URL ������
			String visitUrl = (String) unVisitedSet
					.poll();
			if (visitUrl == null)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl);
			// System.out.println(doc);
			CheckMethods.PrintInfoMessage("URL:\t" + visitUrl);
			// �� URL �����ѷ��ʵ� URL ��
			visitedSet.add(visitUrl);

			// ��ȡ��������ҳ�е� URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// �µ�δ���ʵ� URL ���
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

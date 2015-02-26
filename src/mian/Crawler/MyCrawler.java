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
	 * ץȡ����
	 * 
	 * @return
	 * @param seeds
	 */

	/**
	 * ʹ�����ӳ�ʼ�� URL ����
	 * 
	 * @return
	 * @param seeds
	 *            ���� URL
	 */
	

	
	public void crawlingbyDBFrt(CrawlUrl[] seeds) {
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
		SimpleBloomFilter sbf = new SimpleBloomFilter();
		BDBFrontier unVisitedSet=new BDBFrontier("D:\\bsdb","unVisitedSet", true,sbf);
		BDBFrontier visitedSet=new BDBFrontier("D:\\bsdb","visitedSet", false,sbf);
CheckMethods.PrintInfoMessage(unVisitedSet.size()+"");
CheckMethods.PrintInfoMessage(visitedSet.size()+"");
		initDBFrtrawlerWithSeeds(seeds,unVisitedSet);
CheckMethods.PrintInfoMessage(unVisitedSet.size()+"");
		
		// ѭ����������ץȡ�����Ӳ�����ץȡ����ҳ������ 1000
		while (!unVisitedSet.isEmpty()
//				&& unVisitedSet.size() <= 10000) {
				&& visitedSet.size() <= 4) {
			// ��ͷ URL ������
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
			// �� URL �����ѷ��ʵ� URL ��
			visitedSet.putUrl(visitUrl);
CheckMethods.PrintInfoMessage("visitedSize = "+visitedSet.size());

			// ��ȡ��������ҳ�е� URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// �µ�δ���ʵ� URL ���
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
//		crawler.crawling(new String[] { "http://www.lietu.com" });
//		crawler.crawling(new String[] { "http://www.jd.com/allSort.aspx" },new memoryLinkQueue());
		CrawlUrl curl = new CrawlUrl();
		curl.setOriUrl("http://www.jd.com/allSort.aspx" );
		crawler.crawlingbyDBFrt(new CrawlUrl[] {curl});
	}
}

package mian.Crawler;

import htmlPaser.htmlFilter;
import htmlPaser.parseByRule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mian.tools.CheckMethods;
import mian.tools.HtmlParserTool;

import org.jsoup.nodes.Document;

import filter.SimpleBloomFilter;
import Froniter.BDBFrontier;

public class GeneralCrawler implements Runnable{

	LinkFilter filter = null;
	private String homeDirectory = null;
	private String crawlerName = null;
	HashSet<CrawlUrl> seeds = null;
	HashMap<String,String> rules = null;

	public GeneralCrawler(final String filterRegex, String homeDirectory,
			String crawlerName,HashSet<CrawlUrl> jingdongSeeds, HashMap<String,String> rules) {
		// ���������
		LinkFilter filter = new LinkFilter() {
			public boolean accept(String url) {
				Pattern p = Pattern.compile(filterRegex);
				Matcher m = p.matcher(url);
				if (m.matches()) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		this.filter = filter;
		this.homeDirectory = homeDirectory;
		this.crawlerName = crawlerName;
		this.seeds = jingdongSeeds;
		this.rules = rules;
//		System.err.println(filterRegex + "\t "+homeDirectory+"\t"+crawlerName);
//		for (CrawlUrl crawlUrl : jingdongSeeds) {
//			System.err.println(crawlUrl.getOriUrl());
//		}
		for (String key : rules.keySet()) {
			CheckMethods.PrintInfoMessage(key+":\t"+rules.get(key));
		}
	}
	
	public void run() {
		crawlingbyDBFrt(seeds,rules);
	}

	/**
	 * ץȡ����
	 * 
	 * @return
	 * @param seeds2
	 * @param rules2 
	 */

	public void crawlingbyDBFrt(HashSet<CrawlUrl> seeds2, HashMap<String, String> rules2) {

		// ��ʼ�� URL ����
		
		
		SimpleBloomFilter sbf = new SimpleBloomFilter();
		BDBFrontier unVisitedSet = new BDBFrontier(homeDirectory, crawlerName
				+ "unVisitedSet", true, sbf);
		BDBFrontier visitedSet = new BDBFrontier("D:\\bsdb", crawlerName
				+ "visitedSet", false, sbf);
		initDBFrtrawlerWithSeeds(seeds2, unVisitedSet);

		CheckMethods.PrintDebugMessage("unvisitedSet size:\t"
				+ unVisitedSet.size());
		CheckMethods
				.PrintDebugMessage("visitedSet size:\t" + visitedSet.size());

		// ѭ����������ץȡ�����Ӳ���
		while (!unVisitedSet.isEmpty()
		// && visitedSet.size() <= 8
		) {
			// ��ͷ URL ������
			CrawlUrl visitUrl = null;
			try {
				visitUrl = unVisitedSet.pool();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (visitUrl == null)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl.getOriUrl());
			getLMData(doc,visitUrl.getOriUrl(),rules2);
			CheckMethods.PrintInfoMessage("URL:\t" + visitUrl.getOriUrl());
			// �� URL �����ѷ��ʵ� URL ��
			visitedSet.putUrl(visitUrl);
			CheckMethods.PrintInfoMessage("visitedSize = " + visitedSet.size());
			// ��ȡ��������ҳ�е� URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// �µ�δ���ʵ� URL ���
			for (String link : links) {
				CrawlUrl curl = new CrawlUrl();
				curl.setOriUrl(link);
				unVisitedSet.putUrl(curl);
			}
		}
		visitedSet.close();
		unVisitedSet.close();
	}


	private void getLMData(Document doc, String oriUrl, HashMap<String, String> myrules) {
		for (String rexgex : myrules.keySet()) {
			if (htmlFilter.accept(rexgex, oriUrl)) {
				CheckMethods.PrintInfoMessage("accapt");
				parseByRule.parse(myrules.get(rexgex), doc);
			}
		}
		
	}

	/**
	 * ʹ�����ӳ�ʼ�� URL ����
	 * 
	 * @return
	 * @param seeds2
	 *            ���� URL
	 */
	private void initDBFrtrawlerWithSeeds(HashSet<CrawlUrl> seeds2,
			BDBFrontier unVisitedSet) {
		for (CrawlUrl crawlUrl : seeds2) {
			unVisitedSet.putUrl(crawlUrl);
		}
	}

	// ���Ժ���
	public static void main(String[] args) {
		HashSet<CrawlUrl> jingdongSeeds = new HashSet<CrawlUrl>();
		jingdongSeeds.add(new CrawlUrl("http://www.jd.com/allSort.aspx"));
		
		GeneralCrawler crawler = new GeneralCrawler(".*jd\\.com.*","D:\\bsdb","jingdong",jingdongSeeds,new HashMap<String, String>());
		
		new Thread(crawler).start();
	}

}

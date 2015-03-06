package mian.Crawler;

import htmlPaser.htmlFilter;
import htmlPaser.parseByRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mian.tools.CheckMethods;
import mian.tools.HtmlParserTool;

import org.jsoup.nodes.Document;

import Froniter.BDBFrontier;
import filter.SimpleBloomFilter;

public class GeneralCrawler implements Runnable{

	HashSet<LinkFilter> LinkFilters = null;
	private String homeDirectory = null;
	private String crawlerName = null;
	HashSet<CrawlUrl> seeds = null;
	List<String> rules = null;
	private int layer = 0;

	public GeneralCrawler(final HashSet<LinkFilter> linkFilters, String homeDirectory,
			String crawlerName,HashSet<CrawlUrl> jingdongSeeds, List<String> rules2) {
		this(linkFilters, crawlerName, crawlerName, jingdongSeeds, rules2, Integer.MAX_VALUE);
	}
	public GeneralCrawler(final HashSet<LinkFilter> linkFilters, String homeDirectory,
			String crawlerName,HashSet<CrawlUrl> jingdongSeeds, List<String> rules2,int layer) {
		
		this.LinkFilters = linkFilters;
		this.homeDirectory = homeDirectory;
		this.crawlerName = crawlerName;
		this.seeds = jingdongSeeds;
		this.rules = rules2;
		this.layer = layer;
//		System.err.println(filterRegex + "\t "+homeDirectory+"\t"+crawlerName);
//		for (CrawlUrl crawlUrl : jingdongSeeds) {
//			System.err.println(crawlUrl.getOriUrl());
//		}
		for (String rule : rules2) {
			CheckMethods.PrintInfoMessage("GC54\t"+rule.split("}")[0].substring(1)+":\t"+rule.split("}")[1]);
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

	public void crawlingbyDBFrt(HashSet<CrawlUrl> seeds2, List<String> rules2) {

		// ��ʼ�� URL ����
		
		
		SimpleBloomFilter sbf = new SimpleBloomFilter();
		SimpleBloomFilter sbf_dturl = new SimpleBloomFilter();
		BDBFrontier unVisitedSet = new BDBFrontier(homeDirectory, crawlerName
				+ "unVisitedSet", true, sbf,sbf_dturl);
		BDBFrontier visitedSet = new BDBFrontier(homeDirectory, crawlerName
				+ "visitedSet", false, sbf,sbf_dturl);
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
				e.printStackTrace();
			}
			if (visitUrl == null||visitUrl.getLayer()>layer)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl.getOriUrl());
			getLMData(doc,visitUrl.getOriUrl(),rules2);
CheckMethods.PrintInfoMessage("URL:\t" + visitUrl.getOriUrl()+"\t"+visitUrl.getLayer());
			// �� URL �����ѷ��ʵ� URL ��
			visitedSet.putUrl(visitUrl);
CheckMethods.PrintInfoMessage("visitedSize = " + visitedSet.size());
			// ��ȡ��������ҳ�е� URL
			Set<String> links = HtmlParserTool.extracLinks(doc, LinkFilters);
			// �µ�δ���ʵ� URL ���
			for (String link : links) {
				CrawlUrl curl = new CrawlUrl();
				curl.setOriUrl(link);
				curl.setLayer(visitUrl.getLayer()+1);
				unVisitedSet.putUrl(curl);
			}
		}
		visitedSet.close();
		unVisitedSet.close();
	}


	private void getLMData(Document doc, String oriUrl, List<String> rules2) {
		for (String rule : rules2) {
			String regex = rule.split("}")[0].substring(1);
			String selectRule = rule.split("}")[1];
//CheckMethods.PrintInfoMessage(regex+"\t:\t"+oriUrl);
			if (htmlFilter.accept(regex, oriUrl)) {
				CheckMethods.PrintInfoMessage("GC120"+"accapt");
				parseByRule.parse(selectRule, doc,crawlerName,oriUrl);
				System.out.println("getLMDATA"+regex+"\t"+selectRule);
			}else{
CheckMethods.PrintInfoMessage("not accept!!");
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
		jingdongSeeds.add(new CrawlUrl("http://www.jd.com/allSort.aspx",0));
		HashSet<LinkFilter> linkFilters = new HashSet<>();
		linkFilters.add(new LinkFilter(".*jd\\.com.*"));
		
		GeneralCrawler crawler = new GeneralCrawler(linkFilters,"D:\\bsdb","jingdong",jingdongSeeds, new ArrayList<String>());
		
		new Thread(crawler).start();
	}

}

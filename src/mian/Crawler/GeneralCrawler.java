package mian.Crawler;

import htmlPaser.HtmlParserTool;
import htmlPaser.htmlFilter;
import htmlPaser.parseByRule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mian.tools.CheckMethods;
import mian.tools.myCookies;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import Froniter.BDBFrontier;
import filter.SimpleBloomFilter;

public class GeneralCrawler implements Runnable{

	HashSet<LinkFilter> LinkFilters = null;
	private String homeDirectory = null;
	private String crawlerName = null;
	HashSet<CrawlUrl> seeds = null;
	List<String> rules = null;
	private int maxLayer = 0;
	myCookies cookies = null;

	public GeneralCrawler(final HashSet<LinkFilter> linkFilters, String homeDirectory,
			String crawlerName,HashSet<CrawlUrl> jingdongSeeds, List<String> rules2,int layer) {
		
		this.LinkFilters = linkFilters;
		this.homeDirectory = homeDirectory;
		this.crawlerName = crawlerName;
		this.seeds = jingdongSeeds;
		this.rules = rules2;
		this.maxLayer = layer;
//		System.err.println(filterRegex + "\t "+homeDirectory+"\t"+crawlerName);
//		for (CrawlUrl crawlUrl : jingdongSeeds) {
//			System.err.println(crawlUrl.getOriUrl());
//		}
		for (String rule : rules2) {
			CheckMethods.PrintInfoMessage("GC54\t"+rule.split("}")[0].substring(1)+":\t"+rule.split("}")[1]);
		}
	}
	
	public GeneralCrawler(HashSet<LinkFilter> linkFilters2,
			String homeDirectory2, String crawlerName2,
			HashSet<CrawlUrl> seeds2, List<String> rules2, int maxlayer,
			myCookies cookies) {
		this(linkFilters2, homeDirectory2, crawlerName2, seeds2, rules2, maxlayer);
		this.cookies = cookies;
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
			if (visitUrl == null||visitUrl.getLayer()>maxLayer)
				continue;
			Document doc = null;
			if(cookies==null){
				doc = HtmlParserTool.getDocument(visitUrl.getOriUrl());
CheckMethods.PrintInfoMessage("no cookies");
CheckMethods.PrintInfoMessage("------"+visitUrl.getOriUrl()+"-----"+visitUrl.getLayer()+"-----");
			}else{
				doc = HtmlParserTool.getDocument(visitUrl.getOriUrl(), cookies);
CheckMethods.PrintInfoMessage("have cookies");
			}
			getLMData(doc,visitUrl.getOriUrl(),rules2);
CheckMethods.PrintInfoMessage("URL:\t" + visitUrl.getOriUrl()+"\tlayer: "+visitUrl.getLayer());
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
CheckMethods.PrintInfoMessage("GC136"+"accapt");
				parseByRule.parse(selectRule, doc,crawlerName,oriUrl);
				System.out.println("getLMDATA"+regex+"\t"+selectRule);
			}else{
CheckMethods.PrintInfoMessage("getLMData not accept!!");
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
		
		GeneralCrawler crawler = new GeneralCrawler(linkFilters,"D:\\bsdb","jingdong",jingdongSeeds, new ArrayList<String>(), Integer.MAX_VALUE);
		
		new Thread(crawler).start();
	}

}

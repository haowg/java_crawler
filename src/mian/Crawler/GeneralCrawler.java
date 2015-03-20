package mian.Crawler;

import htmlPaser.HtmlParserTool;
import htmlPaser.htmlFilter;
import htmlPaser.parseByRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mian.tools.CheckMethods;
import mian.tools.myCookies;

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
	 * 抓取过程
	 * 
	 * @return
	 * @param seeds2
	 * @param rules2 
	 */

	public void crawlingbyDBFrt(HashSet<CrawlUrl> seeds2, List<String> rules2) {

		// 初始化 URL 队列
		
		
		SimpleBloomFilter sbf = new SimpleBloomFilter();
		SimpleBloomFilter sbf_dturl = new SimpleBloomFilter();
		SimpleBloomFilter sbf_unvisited = new SimpleBloomFilter();
		BDBFrontier visitedSet = new BDBFrontier(homeDirectory, crawlerName
				+ "visitedSet", false, sbf,sbf_dturl);
		BDBFrontier unVisitedSet = new BDBFrontier(homeDirectory, crawlerName
				+ "unVisitedSet", true, sbf,sbf_dturl,sbf_unvisited);
		
		initDBFrtrawlerWithSeeds(seeds2, unVisitedSet);

		CheckMethods.PrintDebugMessage("unvisitedSet size:\t"
				+ unVisitedSet.size());
		CheckMethods
				.PrintDebugMessage("visitedSet size:\t" + visitedSet.size());

		// 循环条件：待抓取的链接不空
		while (!unVisitedSet.isEmpty()
		// && visitedSet.size() <= 8
		) {
			// 队头 URL 出队列
			CrawlUrl visitUrl = null;
//System.out.println(unVisitedSet.size());
			try {
				visitUrl = unVisitedSet.pool();
			} catch (Exception e) {
				e.printStackTrace();
			}
//System.out.println(unVisitedSet.size());
			if (visitUrl == null||visitUrl.getLayer()>maxLayer)
				continue;
//System.out.println(visitUrl.getOriUrl());
			if(sbf.contains(visitUrl.getOriUrl())||sbf_dturl.contains(visitUrl.getOriUrl())){
CheckMethods.PrintInfoMessage("get a visited url !!! "+visitUrl.getOriUrl());
				continue;
			}
//System.out.println(visitUrl.getOriUrl());
			Document doc = null;
			if(cookies==null){
				doc = HtmlParserTool.getDocument(visitUrl.getOriUrl());
//CheckMethods.PrintInfoMessage("no cookies");
//CheckMethods.PrintInfoMessage("------"+visitUrl.getOriUrl()+"-----"+visitUrl.getLayer()+"-----");
			}else{
				doc = HtmlParserTool.getDocument(visitUrl.getOriUrl(), cookies);
//CheckMethods.PrintInfoMessage("have cookies");
			}
			getLMData(doc,visitUrl.getOriUrl(),rules2);
//CheckMethods.PrintInfoMessage("URL:\t" + visitUrl.getOriUrl()+"\tlayer: "+visitUrl.getLayer());
			
			
//CheckMethods.PrintInfoMessage("visitedSize = " + visitedSet.size());
			// 提取出下载网页中的 URL
			Set<String> links = HtmlParserTool.extracLinks(doc, LinkFilters);
			// 新的未访问的 URL 入队
			for (String link : links) {
				link = link.split("#")[0];
				if(!(sbf.contains(link.trim())||sbf_dturl.contains(link.trim())||sbf_unvisited.contains(link.trim()))){
//				if(!(sbf.contains(link.trim())||sbf_dturl.contains(link.trim()))){
					CrawlUrl curl = new CrawlUrl();
					curl.setOriUrl(link);
					curl.setLayer(visitUrl.getLayer()+1);
					unVisitedSet.putUrl(curl);
					sbf_unvisited.add(curl);
//				}else{
//CheckMethods.PrintInfoMessage("find a visited url "+link);
				}
			}
			//把新url同步到硬盘上
			unVisitedSet.sync();
			// 该 URL 放入已访问的 URL 中
			if(doc != null){
				visitedSet.putUrl(visitUrl);
CheckMethods.PrintDebugMessage(visitUrl.getOriUrl());
			}
//System.out.println((sbf.contains(new CrawlUrl("http://www.dongfeng-nissan.com.cn/op", 0)))||sbf_dturl.contains(new CrawlUrl("http://www.dongfeng-nissan.com.cn/op", 0)));
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
				parseByRule.parse(selectRule, doc,crawlerName,oriUrl);
//System.out.println("getLMDATA"+regex+"\t"+selectRule);
			}
		}
		
	}

	/**
	 * 使用种子初始化 URL 队列
	 * 
	 * @return
	 * @param seeds2
	 *            种子 URL
	 */
	private void initDBFrtrawlerWithSeeds(HashSet<CrawlUrl> seeds2,
			BDBFrontier unVisitedSet) {
		for (CrawlUrl crawlUrl : seeds2) {
			unVisitedSet.putUrl(crawlUrl);
		}
	}

	// 测试函数
	public static void main(String[] args) {
		HashSet<CrawlUrl> jingdongSeeds = new HashSet<CrawlUrl>();
		jingdongSeeds.add(new CrawlUrl("http://www.jd.com/allSort.aspx",0));
		HashSet<LinkFilter> linkFilters = new HashSet<LinkFilter>();
		linkFilters.add(new LinkFilter(".*jd\\.com.*"));
		
		GeneralCrawler crawler = new GeneralCrawler(linkFilters,"D:\\bsdb","jingdong",jingdongSeeds, new ArrayList<String>(), Integer.MAX_VALUE);
		
		new Thread(crawler).start();
	}

}

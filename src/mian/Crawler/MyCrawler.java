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

	LinkFilter filter = null;
	private String homeDirectory = null;
	private String crawlerName = null;

	public MyCrawler(final String filterRegex, String homeDirectory,
			String crawlerName) {
		// 定义过滤器
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
	}

	/**
	 * 抓取过程
	 * 
	 * @return
	 * @param seeds
	 */

	public void crawlingbyDBFrt(CrawlUrl[] seeds) {

		// 初始化 URL 队列
		SimpleBloomFilter sbf = new SimpleBloomFilter();
		BDBFrontier unVisitedSet = new BDBFrontier(homeDirectory, crawlerName
				+ "unVisitedSet", true, sbf);
		BDBFrontier visitedSet = new BDBFrontier("D:\\bsdb", crawlerName
				+ "visitedSet", false, sbf);
		initDBFrtrawlerWithSeeds(seeds, unVisitedSet);

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
			try {
				visitUrl = unVisitedSet.pool();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (visitUrl == null)
				continue;
			Document doc = HtmlParserTool.getDocument(visitUrl.getOriUrl());
			CheckMethods.PrintInfoMessage("URL:\t" + visitUrl.getOriUrl());
			// 该 URL 放入已访问的 URL 中
			visitedSet.putUrl(visitUrl);
			CheckMethods.PrintInfoMessage("visitedSize = " + visitedSet.size());
			// 提取出下载网页中的 URL
			Set<String> links = HtmlParserTool.extracLinks(doc, filter);
			// 新的未访问的 URL 入队
			for (String link : links) {
				CrawlUrl curl = new CrawlUrl();
				curl.setOriUrl(link);
				unVisitedSet.putUrl(curl);
			}
		}
		visitedSet.close();
		unVisitedSet.close();
	}

	/**
	 * 使用种子初始化 URL 队列
	 * 
	 * @return
	 * @param seeds
	 *            种子 URL
	 */

	private void initDBFrtrawlerWithSeeds(CrawlUrl[] seeds,
			BDBFrontier unVisitedSet) {
		for (int i = 0; i < seeds.length; i++)
			unVisitedSet.putUrl(seeds[i]);
	}

	// 测试函数
	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler(".*jd\\.com.*", "D:\\bsdb",
				"jingdong");
		CrawlUrl curl = new CrawlUrl();
		curl.setOriUrl("http://www.jd.com/allSort.aspx");
		crawler.crawlingbyDBFrt(new CrawlUrl[] { curl });
	}
}

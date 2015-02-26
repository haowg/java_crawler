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
	}

	/**
	 * ץȡ����
	 * 
	 * @return
	 * @param seeds
	 */

	public void crawlingbyDBFrt(CrawlUrl[] seeds) {

		// ��ʼ�� URL ����
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

	/**
	 * ʹ�����ӳ�ʼ�� URL ����
	 * 
	 * @return
	 * @param seeds
	 *            ���� URL
	 */

	private void initDBFrtrawlerWithSeeds(CrawlUrl[] seeds,
			BDBFrontier unVisitedSet) {
		for (int i = 0; i < seeds.length; i++)
			unVisitedSet.putUrl(seeds[i]);
	}

	// ���Ժ���
	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler(".*jd\\.com.*", "D:\\bsdb",
				"jingdong");
		CrawlUrl curl = new CrawlUrl();
		curl.setOriUrl("http://www.jd.com/allSort.aspx");
		crawler.crawlingbyDBFrt(new CrawlUrl[] { curl });
	}
}

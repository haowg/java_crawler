package mian.Crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



public class Entrance {
	
	/*
	 * 主函数
	 */
	
	public static void main(String[] args) {
		Entrance ent = new Entrance();
		ent.parseConfig("config.xml");
		ent.parseConfig("config2.xml");
	}
	/*
	 * 读取配置文件生成seeds 文件
	 */
	private HashSet<CrawlUrl> getseeds(String configxml){
		HashSet<CrawlUrl> seeds = new HashSet<>();
		for (String line : configxml.split("\n")) {
			if (line.indexOf("SEED=") > -1) {
				String seed = line.trim().substring(line.indexOf("SEED=") + 4);
				seeds.add(new CrawlUrl(seed));
			}
		}
		return seeds;
	}
	/*
	 * 解析配置文件生成爬虫线程并启动
	 */
	private void parseConfig(String configPath){
		String filterRegex = "";
		String homeDirectory = "";
		String crawlerName = "";
		String configxml = "";
		Entrance ent = new Entrance();
		try {
			configxml = FileUtils.readFileToString(new File(configPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(configxml);
		Element e = doc.getElementsByTag("config").first();

		filterRegex = e.attr("filterRegex");
		homeDirectory = e.attr("homeDirectory");
		crawlerName = e.attr("crawlerName");
		System.out.println(filterRegex + "\t" + homeDirectory + "\t"
				+ crawlerName);
		HashSet<CrawlUrl> seeds =  ent.getseeds(configxml);
		GeneralCrawler crawler = new GeneralCrawler(filterRegex,homeDirectory,crawlerName,seeds);
		new Thread(crawler).start();
	}
	
}

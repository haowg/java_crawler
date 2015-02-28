package mian.Crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
			line = line.trim();
			if (line.startsWith("SEED")) {
				line = line.substring(4);
				line = line.trim();
				line = line.substring(1).trim();
				if (line.startsWith("\"")) {
					line = line.substring(1);
				}if (line.endsWith("\"")) {
					line = line.substring(0,line.length()-1);
				}
				String seed = line.trim();
//				String seed = line.trim().substring(line.indexOf("SEED=") + 4);
				seeds.add(new CrawlUrl(seed));
			}
		}
		return seeds;
	}
	
	private HashMap<String, String> getrules(String configxml){
		HashMap<String,String> rules = new HashMap<>();
		for (String line : configxml.split("\n")) {
			line = line.trim();
			if (line.startsWith("SELECTRULE")) {
				line = line.substring(10);
				line = line.trim();
				line = line.substring(1).trim();
				if (line.startsWith("\"")) {
					line = line.substring(1);
				}if (line.endsWith("\"")) {
					line = line.substring(0,line.length()-1);
				}
				line = line.trim();
				String rule = line.split("}")[1].trim();
				String regex = line.split("}")[0].trim().substring(1).trim();
//				String seed = line.trim().substring(line.indexOf("SEED=") + 4);
				rules.put(regex, rule);
			}
		}
		return rules;
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
//		System.out.println(filterRegex + "\t" + homeDirectory + "\t"+ crawlerName);
		HashSet<CrawlUrl> seeds =  ent.getseeds(configxml);
		HashMap<String,String> rules = ent.getrules(configxml);
		GeneralCrawler crawler = new GeneralCrawler(filterRegex,homeDirectory,crawlerName,seeds,rules);
		crawler.toString();
//		new Thread(crawler).start();
	}
	
}

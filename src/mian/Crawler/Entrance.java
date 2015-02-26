package mian.Crawler;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;



public class Entrance {
	public static void main(String[] args) {
		
		String filterRegex = "";
		String homeDirectory = "";
		String crawlerName = "";
		String seeds = "";
		
		String configxml = "";
		try {
			configxml = FileUtils.readFileToString(new File("config.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(configxml);
		Element e = doc.getElementsByTag("config").first();
		System.out.println(e);
		filterRegex = e.attr("filterRegex");
		homeDirectory = e.attr("homeDirectory");
		crawlerName = e.attr("crawlerName");
		for (Node ele : e.childNodes()) {
			System.out.println(ele);
		}
//		CrawlUrl[] jingdongSeeds = new CrawlUrl[] {new CrawlUrl("http://www.jd.com/allSort.aspx")};
//		MyCrawler crawler = new MyCrawler(".*jd\\.com.*","D:\\bsdb","jingdong",jingdongSeeds);
//		new Thread(crawler).start();
//		
//		new Thread(new MyCrawler(".*jd\\.com.*","D:\\bsdb","jd",jingdongSeeds)).start();
		
	}
}

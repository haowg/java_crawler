package mian.Crawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import mian.tools.CheckMethods;
import mian.tools.myCookies;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



public class Entrance {
	
	/*
	 * ������
	 */
	
	public static void main(String[] args) {
		Entrance ent = new Entrance();
//		ent.parseConfig("config.xml");
//		ent.parseConfig("config2.xml");
//		ent.parse("News_config.xml");
//		ent.parse("testCookies.xml");
		ent.parse("testMerge.xml");
//		ent.parse("qqNews_config2.xml");
	}
	/*
	 * ��ȡ���xml�����ļ������ֿ�ÿһ�����������
	 */
	private void parse(String configPath) {
		String configxml = "";
		try {
			configxml = FileUtils.readFileToString(new File(configPath));
		} catch (IOException e) {
//			e.printStackTrace();
CheckMethods.PrintDebugMessage(e.getMessage());
		}
		if (!checkNames(configxml)) {
CheckMethods.PrintDebugMessage("Name is same ! please check it");
			System.exit(0);
		}
		String[] configs = configxml.split("<config");
		for(int i = 1;i<configs.length;i++){
			String config = "<config"+configs[i];
//			System.out.println(config);
			parseConfigToCrawler(config);
		}
	}
	
	/*
	 * ���������ļ����������̲߳�����
	 */
	private void parseConfigToCrawler(String configxml){
		HashSet<LinkFilter> linkFilters  =  null;
		String homeDirectory = "";
		String crawlerName = "";
		String cookieLocation = "";
		myCookies cookies = null;
		int maxlayer = Integer.MAX_VALUE;
		Entrance ent = new Entrance();
		Document doc = Jsoup.parse(configxml);
		Element e = doc.getElementsByTag("config").first();

		linkFilters = getFilters(configxml);
		homeDirectory = e.attr("homeDirectory");
		crawlerName = e.attr("crawlerName");
		
		if(!(e.attr("cookiesloc").equals("null")||e.attr("cookiesloc").equals(""))){
			cookieLocation = e.attr("cookiesloc");
			cookies = new myCookies(cookieLocation);
		}
		if(!(e.attr("maxlayer").equals("null")||e.attr("maxlayer").equals(""))){
			try{
			maxlayer = Integer.parseInt(e.attr("maxlayer"));
			}catch(Exception ex){
CheckMethods.PrintDebugMessage("MAXLAYER format is wrong please check it");
			}
		}
//		System.out.println(filterRegex + "\t" + homeDirectory + "\t"+ crawlerName);
		HashSet<CrawlUrl> seeds =  ent.getseeds(configxml);
		List<String> rules = ent.getrules(configxml);
		
		
		GeneralCrawler crawler = null;
		if(cookies==null){
			crawler = new GeneralCrawler(linkFilters,homeDirectory,crawlerName,seeds,rules,maxlayer);
//CheckMethods.PrintInfoMessage("Dont not have cookies");
		}else{
			crawler = new GeneralCrawler(linkFilters,homeDirectory,crawlerName,seeds,rules,maxlayer,cookies);
//CheckMethods.PrintInfoMessage("Yes there are cookies");
		}
		new Thread(crawler).start();
	}
	
	/*
	 * �������߳����Ƿ�һ��
	 */
	private boolean checkNames(String configxml) {
		String[] configs = configxml.split("<config");
		HashSet<String> nameSet = new HashSet<>();
		for (int i = 1; i< configs.length;i++) {
			String config = "<config"+configs[i];
			String name = Jsoup.parse(config).getElementsByTag("config").first().attr("CRAWLERNAME");
//			System.out.println(name);
			nameSet.add(name);
		}
		if(nameSet.size()!=configs.length-1){
			return false;
		}
		return true;
	}
	/*
	 * ��ȡ�����ļ�����seeds 
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
				seeds.add(new CrawlUrl(seed,0));
			}
		}
		return seeds;
	}
	/*
	 * ��ȡ����ļ�����rules
	 */
	private List<String> getrules(String configxml){
		List<String> rules = new ArrayList<String>();
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
//				String rule = line.split("}")[1].trim();
//				String regex = line.split("}")[0].trim().substring(1).trim();
//				String seed = line.trim().substring(line.indexOf("SEED=") + 4);
				rules.add(line);
			}
		}
		return rules;
	}
	
	
	/*
	 * ��ȡ����ļ�����rules
	 */
	private HashSet<LinkFilter> getFilters(String configxml){
		HashSet<LinkFilter> filters = new HashSet<>();
		for (String line : configxml.split("\n")) {
			line = line.trim();
			if (line.startsWith("FILTERREGEX")) {
				line = line.substring(11);
				line = line.trim();
				line = line.substring(1).trim();
				if (line.startsWith("\"")) {
					line = line.substring(1);
				}if (line.endsWith("\"")) {
					line = line.substring(0,line.length()-1);
				}
				String filterRegex = line.trim();
//				String seed = line.trim().substring(line.indexOf("SEED=") + 4);
				filters.add(new LinkFilter(filterRegex));
				
			}
		}
		return filters;
	}
	

	
}

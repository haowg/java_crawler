package mian.tools;

import java.util.HashSet;
import java.util.Set;

import mian.Crawler.LinkFilter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserTool {
	
	public static  Document getDocument(String url) {
		Document doc = null;
		try {
			doc = Jsoup
					.connect(url)
					.timeout(8000)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WO"
									+ "W64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
					.get();
		} catch (Exception e) {
			String msg = e.getMessage();
			if(msg.toLowerCase().contains("time")){
				System.err.println("[error]: connect to "+ url + " time out.");
//			}else if(rule){
				doc = getDocument(url);
			}else{
				System.err.println("[error]: " + e.getMessage()+url);
			}
			e.printStackTrace();
		}
		if (doc == null) {
			System.err.println("¡¨Ω” ß∞‹");
		}
		return doc;
	}
	public static  String totitle(String titleString) {
		String regEX = "[^\u4e00-\u9fa5\\w\\.]+";
		titleString = titleString.replaceAll(regEX, "");
		return titleString;
	}
	public static String convertunicode(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		
		while((i=utfString.indexOf("\\u", pos)) != -1){
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}
		
		return sb.toString();
	}
	public Elements slect(Element e,String rule) {
		Elements eles = new Elements();
		try {
			 eles = e.select(rule);
		} catch (Exception exception) {
			System.err.println("select err ");
			exception.printStackTrace();
		}
		return eles;
	}
	public static Set<String> extracLinks(Document doc, LinkFilter filter) {
		Set<String> set = new HashSet<String>();

		if (doc == null) {
			return set;
		}
//		Elements eles = doc.select("a[href~=.*[^(ppt)(jpg)(doc)(jsp)]$]");
		Elements eles = doc.select("a");
		for (Element element : eles) {
			String url = element.absUrl("href");
			url = url.split("#")[0];
			if(filter.accept(url)){
				set.add(url);
			}
		}
		return set;
	}

}


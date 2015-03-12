package htmlPaser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import mian.Crawler.LinkFilter;
import mian.tools.CheckMethods;
import mian.tools.myCookies;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserTool {
	/*
	 * 访问url并获得网页内容
	 */
	public static  Document getDocument(String url){
		Document doc = getDocument(url,0);
		return doc;
	}
	private static  Document getDocument(String url,int level) {
		if(level >= 6) return null;
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
				doc = getDocument(url,++level);
			}else{
				System.err.println("[error]: " + e.getMessage()+url);
			}
			e.printStackTrace();
		}
		if (doc == null) {
			System.err.println("连接失败");
		}
		return doc;
	}
	public static Document getDocument(String url, myCookies cookies) {
		Document doc = getDocument(url, cookies, 0);
		return doc;
	}
	private static Document getDocument(String url, myCookies cookies,int level) {
		if(level >=6)
			return null;
		Response res = null;
		Document doc = new Document("");
		try {
			res=Jsoup
					.connect(url)
					.cookies(cookies.getCookies())
					.timeout(8000)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WO"
									+ "W64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
					.method(Method.GET)
					.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CheckMethods.PrintDebugMessage(e.getMessage());
		}
		try {
			if(res == null){
				return doc;
			}
			doc = res.parse();
			cookies.setCookies(res.cookies());
			return doc;
		} catch (IOException e) {
			String msg = e.getMessage();
			if(msg.toLowerCase().contains("time")){
				System.err.println("[error]: connect to "+ url + " time out.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException threadex) {
					threadex.printStackTrace();
				}
				doc = getDocument(url,cookies,level++);
			}else{
				CheckMethods.PrintDebugMessage("[error]: " + e.getMessage());
			}
//			e.printStackTrace();
		}
		return doc;
	}
	/*
	 * 去除汉字和英文字符数字之外的字符
	 */
	public static  String totitle(String titleString) {
		String regEX = "[^\u4e00-\u9fa5\\w\\.]+";
		titleString = titleString.replaceAll(regEX, "");
		return titleString;
	}
	/*
	 * 把Unicode转换成一般的字符串
	 */
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
	/*
	 * Jsoup 的 select 的封装
	 */
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
	/*
	 * 获取网页中的链接
	 */
	public static Set<String> extracLinks(Document doc, HashSet<LinkFilter> linkFilters) {
		Set<String> set = new HashSet<String>();
CheckMethods.PrintInfoMessage("git other links");
		if (doc == null) {
			return set;
		}
//		Elements eles = doc.select("a[href~=.*[^(ppt)(jpg)(doc)(jsp)]$]");
		Elements eles = doc.select("a");
		for (Element element : eles) {
			boolean accept = false;
			String url = element.absUrl("href");
			url = url.split("#")[0];
			if (url.endsWith("/")) {
//				System.out.println(url+"------------------------------");
				url = url.substring(0, url.length()-1);
			}
			for (LinkFilter linkFilter : linkFilters) {
				if(linkFilter.accept(url)){
//CheckMethods.PrintInfoMessage("accept\t"+linkFilter.filterRegex+url);
					accept = true;
					break;
				}
			}
			if (accept) {
				set.add(url);
			}
			if (!accept) {
//CheckMethods.PrintInfoMessage("not accept ！！！！！！！");
//for (LinkFilter lf : linkFilters) {
//	CheckMethods.PrintInfoMessage(lf.filterRegex+"\t"+url);
//}
			}
			
		}
		return set;
	}

}


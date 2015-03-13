package htmlPaser;

import java.io.IOException;

import mian.tools.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class parseByRule {
	/*
	 * 利用rule查找并目标提取文字并写入文本文档
	 */
	public static void parse(String rule,Document doc,String name,String url){
		try {
			name = name+".txt";
			Elements eles = null;
			eles = doc.select(rule);
			if(!eles.toString().trim().equals("")){
//				System.out.println("eles----"+eles+"-------");
				Utils.write(url+"\n", name);
			}
//			System.out.println("eles--------------"+eles+"\t"+rule);
			for (Element element : eles) {
				String text = Jsoup.clean(element.toString(), Whitelist.none()).replaceAll("&[^;]*;", "");
//				String text = element.toString().replaceAll("<[^>]*[bBPp][^>]*>", "\n").replaceAll("<[^>]*>\n*","" ).replaceAll("((\r\n)|\n| |　|\t)+","\n");
//				String text = element.toString().replaceAll("<[^>]*>\n*","" );
//				System.out.println(text);
				Utils.write(text+"\n", name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/*
	 * test method
	 */
	public static void main(String[] args) {
		try {
			parse("div#endText", Jsoup.connect("http://news.163.com/10/0101/21/5RVMPI4V000120GR.html").get(), "test","http://item.jd.com/657799.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

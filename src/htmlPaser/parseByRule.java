package htmlPaser;

import java.io.IOException;

import mian.tools.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class parseByRule {
	public static void parse(String rule,Document doc){
		try {
			Elements eles = doc.select(rule);
//			System.out.println("eles--------------"+eles+"\t"+rule);
			for (Element element : eles) {
//				System.out.println(element);
				Utils.write(element.toString(), "ss");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		try {
			parse("div[id~=^consult-[0-9]+]", Jsoup.connect("http://item.jd.com/657799.html").get());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

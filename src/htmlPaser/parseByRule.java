package htmlPaser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class parseByRule {
	public static void parse(String rule,Document doc){
		try {
			Elements eles = doc.select(rule);
			for (Element element : eles) {
				System.out.println(element);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}

package htmlPaser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class htmlFilter {

	public static boolean accept(String regex,String url) {
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
//CheckMethods.PrintInfoMessage("HF"+regex);
		if (m.matches()) {
//CheckMethods.PrintInfoMessage("true");
			return true;
		} else {
			return false;
		}
	}
//	public static void main(String[] args) {
//		Pattern p = Pattern.compile(".*jd.com.*");
//		Matcher m = p.matcher("http://www.jd.com/?utm_source=media&utm_medium=cpc&utm_campaign=&utm_term=media_8_58871498_s1277054f3c843c85db9.16060899");
//		if (m.matches()) {
//			System.out.println("MATCH!!!");
//		}else {
//			System.out.println("NO");
//		}
//	}
}

package htmlPaser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * ����������ʽ����url
 */
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
}

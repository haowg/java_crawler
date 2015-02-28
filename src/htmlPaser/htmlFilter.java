package htmlPaser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class htmlFilter {

	public static boolean accept(String regex,String url) {
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}
}

package mian.Crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFilter {
	public String filterRegex;
	public LinkFilter(String filterRegex) {
		this.filterRegex = filterRegex;
	}
	public boolean accept(String url) {
		Pattern p = Pattern.compile(filterRegex);
		Matcher m = p.matcher(url);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		LinkFilter linkFilter = new LinkFilter(".*jd\\.com.*");
		System.out.println(linkFilter.accept("http://club.jd.com"));
	}
}

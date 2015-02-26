package Froniter;

import mian.Crawler.CrawlUrl;


public interface Frontier {
	public CrawlUrl pool()throws Exception;
	public boolean putUrl(CrawlUrl url) throws Exception;
	public boolean visited(CrawlUrl url);
}

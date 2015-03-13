package Froniter;

import mian.Crawler.CrawlUrl;

/*
 * 队列接口
 */
public interface Frontier {
	/*
	 * 返回并删除队列中的一个元素
	 */
	public CrawlUrl pool()throws Exception;
	/*
	 * 添加元素到队列中
	 */
	public boolean putUrl(CrawlUrl url) throws Exception;
	/*
	 * 返回是否访问过该元素
	 */
	public boolean visited(CrawlUrl url);
}

package Froniter;

import mian.Crawler.CrawlUrl;

/*
 * ���нӿ�
 */
public interface Frontier {
	/*
	 * ���ز�ɾ�������е�һ��Ԫ��
	 */
	public CrawlUrl pool()throws Exception;
	/*
	 * ���Ԫ�ص�������
	 */
	public boolean putUrl(CrawlUrl url) throws Exception;
	/*
	 * �����Ƿ���ʹ���Ԫ��
	 */
	public boolean visited(CrawlUrl url);
}

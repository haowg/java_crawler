package mian.Crawler;

import java.io.Serializable;

public class CrawlUrl implements Serializable{

//	public CrawlUrl(String oriUrl){
//			this.oriUrl = oriUrl;
//			this.layer = 0;
//		}
	public CrawlUrl(String oriUrl,int layer){
		this.oriUrl = oriUrl;
		this.layer = layer;
	}
	
	public CrawlUrl() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234567890L;

	
	private String oriUrl;// 原始 URL 的值，主机部分是域名
	private int layer; //爬取的层次， 从种子开始， 依次为第0层， 第1层...
	
//	private String url; // URL 的值，主机部分是 IP，为了防止重复主机的出现
//	private int urlNo; // URL NUM
//	private String type; // 文章类型
//	private int statusCode; // 获取 URL 返回的结果码
//	private int hitNum; // 此 URL 被其他文章引用的次数
//	private String charSet; // 此 URL 对应文章的汉字编码
//	private String abstractText; // 文章摘要
//	private String author; // 作者
//	private int weight; // 文章的权重(包含导向词的信息)
//	private String description; // 文章的描述
//	private int fileSize; // 文章大小
//	private Timestamp lastUpdateTime; // 最后修改时间
//	private Date timeToLive; // 过期时间
//	private String title; // 文章名称
//	private String[] urlRefrences; // 引用的链接
	
	public String getOriUrl() {
		return oriUrl;
	}
	public void setOriUrl(String oriUrl) {
		this.oriUrl = oriUrl;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}


}

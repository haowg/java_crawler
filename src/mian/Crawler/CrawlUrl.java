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

	
	private String oriUrl;// ԭʼ URL ��ֵ����������������
	private int layer; //��ȡ�Ĳ�Σ� �����ӿ�ʼ�� ����Ϊ��0�㣬 ��1��...
	
//	private String url; // URL ��ֵ������������ IP��Ϊ�˷�ֹ�ظ������ĳ���
//	private int urlNo; // URL NUM
//	private String type; // ��������
//	private int statusCode; // ��ȡ URL ���صĽ����
//	private int hitNum; // �� URL �������������õĴ���
//	private String charSet; // �� URL ��Ӧ���µĺ��ֱ���
//	private String abstractText; // ����ժҪ
//	private String author; // ����
//	private int weight; // ���µ�Ȩ��(��������ʵ���Ϣ)
//	private String description; // ���µ�����
//	private int fileSize; // ���´�С
//	private Timestamp lastUpdateTime; // ����޸�ʱ��
//	private Date timeToLive; // ����ʱ��
//	private String title; // ��������
//	private String[] urlRefrences; // ���õ�����
	
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

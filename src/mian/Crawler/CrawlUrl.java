package mian.Crawler;

import java.io.Serializable;

public class CrawlUrl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234567890L;

	
	private String oriUrl;// ԭʼ URL ��ֵ����������������
	
	
	private String url; // URL ��ֵ������������ IP��Ϊ�˷�ֹ�ظ������ĳ���
	private int urlNo; // URL NUM
	private String type; // ��������
	private int layer; //��ȡ�Ĳ�Σ� �����ӿ�ʼ�� ����Ϊ��0�㣬 ��1��...
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getUrlNo() {
		return urlNo;
	}
	public void setUrlNo(int urlNo) {
		this.urlNo = urlNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}


}

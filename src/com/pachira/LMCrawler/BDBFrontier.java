package com.pachira.LMCrawler;

import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Set;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;

public class BDBFrontier extends AbstractFrontier implements Frontier {
	
	private StoredMap pendingUrisDB = null;

	// ʹ��Ĭ�ϵ�·���ͻ����С���캯��
	public BDBFrontier(String homeDirectory) throws DatabaseException{

		super(homeDirectory);
		EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
		EntryBinding valueBinding = new SerialBinding(javaCatalog,CrawlUrl.class);
		pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
	}

	// ��ò�ɾ����һ����¼
	public CrawlUrl getNext() throws Exception {
		CrawlUrl result = null;
		if (!pendingUrisDB.isEmpty()) {
			Set entrys = pendingUrisDB.entrySet();
			// System.out.println(entrys);
			Entry<String, CrawlUrl> entry = (Entry<String, CrawlUrl>) pendingUrisDB.entrySet().iterator().next();
			result = entry.getValue();
			delete(entry.getKey());
		}
		return result;
	}

	// ���� URL

	public boolean putUrl(CrawlUrl url) {
		put(url.getOriUrl(), url);
		return true;
	}

	// �������ݿ�ķ���
	protected void put(Object key, Object value) {
		pendingUrisDB.put(key, value);
	}

	// ȡ��
	protected Object get(Object key) {
		return pendingUrisDB.get(key);
	}

	// ɾ��
	protected Object delete(Object key) {
		return pendingUrisDB.remove(key);
	}

	// ���� URL �����ֵ������ʹ�ø���ѹ���㷨������ MD5 ��ѹ���㷨
	private String caculateUrl(String url) {
		try {
			return MD5.getMD5string(url.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return url;
	}

	// ���Ժ���
	public static void main(String[] args) {
		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("d:\\bdb");
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			bBDBFrontier.put("test", "value");
			bBDBFrontier.putUrl(url);
			bBDBFrontier.putUrl(url);
			System.out.println(((CrawlUrl) bBDBFrontier.getNext()).getOriUrl());
			System.out.println(bBDBFrontier.get("test"));

			bBDBFrontier.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
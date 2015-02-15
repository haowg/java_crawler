package com.pachira.LMCrawler;

import java.util.HashSet;
import java.util.PriorityQueue;

public class memoryLinkQueue {
	// �ѷ��ʵ� url ����
	private HashSet<String> visitedUrl = new HashSet<String>();
	// �����ʵ� url ����
	private PriorityQueue<String> unVisitedUrl = new PriorityQueue<String>();

	// ��� URL ����
	public PriorityQueue<String> getUnVisitedUrl() {
		return unVisitedUrl;
	}

	// ��ӵ����ʹ��� URL ������
	public void addVisitedUrl(String visitUrl) {
		visitedUrl.add(MD5.getMD5string(visitUrl.getBytes()));
	}

	// �Ƴ����ʹ��� URL
	public void removeVisitedUrl(String url) {
		visitedUrl.remove(url);
	}

	// δ���ʵ� URL ������
	public Object unVisitedUrlDeQueue() {
		return unVisitedUrl.poll();
	}

	// ��֤ÿ�� URL ֻ������һ��
	public void addUnvisitedUrl(String url) {
//		CheckMethods.PrintInfoMessage(visitedUrl.toString());
//		CheckMethods.PrintInfoMessage(MD5.getMD5byte(url.getBytes()).toString());
//		CheckMethods.PrintInfoMessage(String.valueOf(visitedUrl.contains(MD5.getMD5byte(url.getBytes()))));
		if (url != null && !url.trim().equals("")
				&& !visitedUrl.contains(MD5.getMD5string(url.getBytes()))
				&& !unVisitedUrl.contains(url))
			unVisitedUrl.add(url);
	}

	// ����Ѿ����ʵ� URL ��Ŀ
	public int getVisitedUrlNum() {
		return visitedUrl.size();
	}

	// �ж�δ���ʵ� URL �������Ƿ�Ϊ��
	public boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
}

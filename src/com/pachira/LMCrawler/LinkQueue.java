package com.pachira.LMCrawler;

import java.util.HashSet;

public class LinkQueue {
	//�ѷ��ʵ� url ����
	private static HashSet<String> visitedUrl = new HashSet<String>();
	//�����ʵ� url ����
	private static Queue unVisitedUrl = new Queue();
	//��� URL ����
	public static Queue getUnVisitedUrl() {
	return unVisitedUrl;
	}
	//��ӵ����ʹ��� URL ������
	public static void addVisitedUrl(String url) {
	visitedUrl.add(url);
	}
	//�Ƴ����ʹ��� URL
	public static void removeVisitedUrl(String url) {
	visitedUrl.remove(url);
	}
	//δ���ʵ� URL ������
	public static Object unVisitedUrlDeQueue() {
	return unVisitedUrl.deQueue();
	}
	// ��֤ÿ�� URL ֻ������һ��
	public static void addUnvisitedUrl(String url) {
	if (url != null && !url.trim().equals("")
	&& !visitedUrl.contains(url)
	&& !unVisitedUrl.contians(url))
	unVisitedUrl.enQueue(url);
	}
	//����Ѿ����ʵ� URL ��Ŀ
	public static int getVisitedUrlNum() {
	return visitedUrl.size();
	}
	//�ж�δ���ʵ� URL �������Ƿ�Ϊ��
	public static boolean unVisitedUrlsEmpty() {
	return unVisitedUrl.empty();
	}
}

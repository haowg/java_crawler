package com.pachira.LMCrawler;

import java.util.HashSet;
import java.util.PriorityQueue;

public class LinkQueue {
	// �ѷ��ʵ� url ����
	private static HashSet<String> visitedUrl = new HashSet<String>();
	// �����ʵ� url ����
	private static PriorityQueue<String> unVisitedUrl = new PriorityQueue<String>();

	// ��� URL ����
	public static PriorityQueue<String> getUnVisitedUrl() {
		return unVisitedUrl;
	}

	// ��ӵ����ʹ��� URL ������
	public static void addVisitedUrl(String visitUrl) {
		visitedUrl.add(MD5.getMD5string(visitUrl.getBytes()));
	}

	// �Ƴ����ʹ��� URL
	public static void removeVisitedUrl(String url) {
		visitedUrl.remove(url);
	}

	// δ���ʵ� URL ������
	public static Object unVisitedUrlDeQueue() {
		return unVisitedUrl.poll();
	}

	// ��֤ÿ�� URL ֻ������һ��
	public static void addUnvisitedUrl(String url) {
//		CheckMethods.PrintInfoMessage(visitedUrl.toString());
//		CheckMethods.PrintInfoMessage(MD5.getMD5byte(url.getBytes()).toString());
//		CheckMethods.PrintInfoMessage(String.valueOf(visitedUrl.contains(MD5.getMD5byte(url.getBytes()))));
		if (url != null && !url.trim().equals("")
				&& !visitedUrl.contains(MD5.getMD5string(url.getBytes()))
				&& !unVisitedUrl.contains(url))
			unVisitedUrl.add(url);
	}

	// ����Ѿ����ʵ� URL ��Ŀ
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}

	// �ж�δ���ʵ� URL �������Ƿ�Ϊ��
	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
}

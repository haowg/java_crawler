package com.pachira.LMCrawler;

import java.util.HashSet;
import java.util.PriorityQueue;

public class memoryLinkQueue {
	// 已访问的 url 集合
	private HashSet<String> visitedUrl = new HashSet<String>();
	// 待访问的 url 集合
	private PriorityQueue<String> unVisitedUrl = new PriorityQueue<String>();

	// 获得 URL 队列
	public PriorityQueue<String> getUnVisitedUrl() {
		return unVisitedUrl;
	}

	// 添加到访问过的 URL 队列中
	public void addVisitedUrl(String visitUrl) {
		visitedUrl.add(MD5.getMD5string(visitUrl.getBytes()));
	}

	// 移除访问过的 URL
	public void removeVisitedUrl(String url) {
		visitedUrl.remove(url);
	}

	// 未访问的 URL 出队列
	public Object unVisitedUrlDeQueue() {
		return unVisitedUrl.poll();
	}

	// 保证每个 URL 只被访问一次
	public void addUnvisitedUrl(String url) {
//		CheckMethods.PrintInfoMessage(visitedUrl.toString());
//		CheckMethods.PrintInfoMessage(MD5.getMD5byte(url.getBytes()).toString());
//		CheckMethods.PrintInfoMessage(String.valueOf(visitedUrl.contains(MD5.getMD5byte(url.getBytes()))));
		if (url != null && !url.trim().equals("")
				&& !visitedUrl.contains(MD5.getMD5string(url.getBytes()))
				&& !unVisitedUrl.contains(url))
			unVisitedUrl.add(url);
	}

	// 获得已经访问的 URL 数目
	public int getVisitedUrlNum() {
		return visitedUrl.size();
	}

	// 判断未访问的 URL 队列中是否为空
	public boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
}

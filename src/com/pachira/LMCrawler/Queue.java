package com.pachira.LMCrawler;

import java.util.LinkedList;

/**
 * 队列，保存将要访问的 URL
 */
public class Queue {
	// 使用链表实现队列
	private LinkedList<String> queue = new LinkedList<String>();

	// 入队列
	public void enQueue(String t) {
		queue.addLast(t);
	}

	// 出队列
	public Object deQueue() {
		return queue.removeFirst();
	}

	// 判断队列是否为空
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	// 判断队列是否包含 t
	public boolean contians(Object t) {
		return queue.contains(t);
	}

	public boolean empty() {
		return queue.isEmpty();
	}
}
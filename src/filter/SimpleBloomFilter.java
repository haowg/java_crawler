package filter;

import java.util.BitSet;

import mian.Crawler.CrawlUrl;
import mian.tools.CheckMethods;


public class SimpleBloomFilter {
	private static final int DEFAULT_SIZE = 2 << 24;
	private static final int[] seeds = new int[] { 7, 11, 13, 31, 37, 61, };
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	private SimpleHash[] func = new SimpleHash[seeds.length];
	private int filterNum = 0;



	public SimpleBloomFilter() {
		for (int i = 0; i < seeds.length; i++) {
			func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}
	}

	// 覆盖方法，把 URL 添加进来
	public void add(CrawlUrl value) {
		if (value != null) {
			String url = value.getOriUrl();
			url = url.trim();
			add(url);
		}
	}

	// 覆盖方法，把 URL 添加进来
	public void add(String value) {
		if (!contains(value)) {
CheckMethods.PrintDebugMessage(filterNum+++"\t fileterNum"+"\t"+value);
			for (SimpleHash f : func) {
				bits.set(f.hash(value), true);
			}
		}
	}

	// 覆盖方法，是否包含 URL
	public boolean contains(CrawlUrl value) {
		if (contains(value.getOriUrl())) {
			return true;
		}
		return false;
//		return contains(value.getOriUrl());
	}

	// 覆盖方法，是否包含 URL
	public boolean contains(String value) {
		value = value.trim();
		if (value == null) {
			return false;
		}
		boolean ret = true;
		for (SimpleHash f : func) {
			ret = ret && bits.get(f.hash(value));
		}
//CheckMethods.PrintInfoMessage(ret+"\t"+value);
		return ret;
	}

	public static class SimpleHash {
		private int cap;
		private int seed;

		public SimpleHash(int cap, int seed) {
			this.cap = cap;
			this.seed = seed;

		}

		public int hash(String value) {
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++) {
				result = seed * result + value.charAt(i);
			}
			return (cap - 1) & result;
		}
	}
	
	/*
	 * 测试函数
	 */
		public static void main(String[] args) {
			String value = "stone2083@yahoo.cn";
			SimpleBloomFilter filter = new SimpleBloomFilter();
			System.out.println(filter.contains(value));
			filter.add(value);
			System.out.println(filter.contains(value));
			filter.add("sssss");	
		}
}
package Froniter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import mian.Crawler.CrawlUrl;
import mian.Crawler.MD5;
import mian.tools.CheckMethods;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseNotFoundException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

import filter.SimpleBloomFilter;
/*
 * 实现利用BerkeleyDB的队列
 */
public class BDBFrontier extends AbstractFrontier implements Frontier {

	@SuppressWarnings("rawtypes")
	private StoredMap pendingUrisDB = null;
	SimpleBloomFilter sbf_link = null;
	SimpleBloomFilter sbf_dturl = null;
	private boolean isunvisited = false;
	private String dbName = "db";
	private String homeDirectory = null;
	
	// 使用默认的缓存大小构造函数
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BDBFrontier(String homeDirectory, String dbName,
			boolean isunvisited, SimpleBloomFilter sbf_link,SimpleBloomFilter sbf_dturl)
			throws DatabaseException {

		super(homeDirectory, dbName);
		this.dbName = dbName;
		this.sbf_link = sbf_link;
		this.sbf_dturl = sbf_dturl;
		this.homeDirectory = homeDirectory;

		CheckMethods.PrintInfoMessage("dbName:\t" + dbName);
		this.isunvisited = isunvisited;
		EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
		EntryBinding valueBinding = new SerialBinding(javaCatalog,
				CrawlUrl.class);
		pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
		
		if (!isunvisited) {
//			if (!getEveryItem()) {
			if (!prestrain()) {
				CheckMethods.PrintDebugMessage("read exist data fault!");
				System.exit(-1);
			}
		}
	}

	// 返回并删除下一条记录
	@SuppressWarnings({ "unchecked" })
	public CrawlUrl pool() throws Exception {
		CrawlUrl result = null;
		if (!pendingUrisDB.isEmpty()) {
			// Set entrys = pendingUrisDB.entrySet();
			Entry<String, CrawlUrl> entry = (Entry<String, CrawlUrl>) pendingUrisDB
					.entrySet().iterator().next();
			result = entry.getValue();
			delete(entry.getKey());
		}
		return result;
	}

	/*
	 * 判断是不是动态网页
	 */
	public boolean isDynamicUrl(String url){
		String regex = "(.*(/|com|cn|gov|edu|jsp|php|asp)|.*index.*)";
//		String regex_index = ".*(index|list).*";
//		Pattern p_index = Pattern.compile(regex_index);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url.split("#")[0].split("\\?")[0]);
//		Matcher m_index = p_index.matcher(url.split("#")[0].split("\\?")[0]);
		return m.matches();//||m_index.matches();
	}
	public boolean isDynamicUrl(CrawlUrl url){
		return isDynamicUrl(url.getOriUrl());
	}
	
	// 存入 CrawlURL
	public boolean putUrl(CrawlUrl url) {
		
		if( (!isunvisited)&&isDynamicUrl(url) ){
			sbf_dturl.add(url);
		}else{
			put(url.getLayer()+"____"+url.getOriUrl(), url);
		}
		return true;
	}

	// 存入数据库的方法(Object)
	@SuppressWarnings("unchecked")
	protected void put(Object key, Object value) {
		if (isunvisited) {
			if (visited((CrawlUrl) value)) {
				return;
			} else {
				pendingUrisDB.put(key, value);
			}
		} else {
			sbf_link.add((CrawlUrl) value);
			pendingUrisDB.put(key, value);
			sync();
		}

	}

	// 取出
	protected CrawlUrl get(Object key) {
		return (CrawlUrl) pendingUrisDB.get(key);
	}

	// 清除
	protected Object delete(Object key) {
		return pendingUrisDB.remove(key);
	}

	/*
	 * 删除 删除整个环境中的所有数据库，慎用！
	 */
	public void clear() {
		try {
			close();
			if (env != null && database != null) {
				env.removeDatabase(null,
						dbName == null ? database.getDatabaseName() : dbName);
				env.close();
			}
		} catch (DatabaseNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (this.homeDirectory != null) {
					FileUtils.deleteDirectory(new File(this.homeDirectory));
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 根据 URL 计算键值，可以使用各种压缩算法，包括 MD5 等压缩算法
	@SuppressWarnings("unused")
	private String caculateUrl(String url) {
		try {
			return MD5.getMD5string(url.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return url;
	}

	/*
	 * 预加载数据库中的元素到filter中
	 */
	public boolean getEveryItem() {
		CheckMethods.PrintInfoMessage("2dbName:\t" + dbName);
		CheckMethods.PrintDebugMessage("===========遍历数据库" + dbName
				+ "中的所有数据==========");
		Cursor myCursor = null;
		// ArrayList<String> resultList = new ArrayList<String>();
		Transaction txn = null;
		try {
			txn = env.beginTransaction(null, null);
			CursorConfig cc = new CursorConfig();
			cc.setReadCommitted(true);
			if (myCursor == null)
				myCursor = database.openCursor(txn, cc);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			// 使用cursor.getPrev方法来遍历游标获取数据
			if (myCursor.getFirst(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String theKey = new String(foundKey.getData(), "UTF-8");
				theKey = theKey.substring(3);
//				 String theData = new String(foundData.getData(), "UTF-8");
				// resultList.add(theKey);
				sbf_link.add(theKey);

				while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
					theKey = new String(foundKey.getData(), "UTF-8");
					theKey = theKey.substring(3);
//					 theData = new String(foundData.getData(), "GBK");
					if(!isDynamicUrl(theKey)){
						sbf_link.add(theKey);
					}
				}
			}
			myCursor.close();
			txn.commit();
			CheckMethods.PrintDebugMessage("===========数据库" + dbName
					+ "共有有数据==========:\t" + size());

			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			CheckMethods.PrintInfoMessage("getEveryItem处理出现异常");
			CheckMethods.PrintInfoMessage(e.getMessage().toString());
			CheckMethods.PrintInfoMessage(e.getCause().toString());

			txn.abort();
			if (myCursor != null) {
				myCursor.close();
			}
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	/*
	 * 预加载数据库中的url到布隆过滤器中
	 */
	public boolean prestrain() {
		CrawlUrl result = null;
		Iterator<?> iter = pendingUrisDB.entrySet().iterator();
		try{
		while (iter.hasNext()) {
			// Set entrys = pendingUrisDB.entrySet();
			Entry<String, CrawlUrl> entry = (Entry<String, CrawlUrl>) iter.next();
			result = entry.getValue();
			if(!isDynamicUrl(entry.getKey())){
				sbf_link.add(result);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*
	 * @see Froniter.Frontier#visited(mian.Crawler.CrawlUrl) 
	 * 是否访问过，利用bloomfilter
	 */
	@Override
	public boolean visited(CrawlUrl url) {
		if (sbf_link.contains(url)||sbf_dturl.contains(url)) {
			return true;
		}
		return false;
	}

	/*
	 * 是否为空
	 */
	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		}
		return false;
	}

	/*
	 * 返回队列大小
	 */
	public long size() {
		return database.count();
	}

	/*
	 * 同步到磁盘上去
	 */
	public void sync() {
		try {
			env.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 测试函数
	public static void main(String[] args) {
		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("D:\\bdb", "url", false,
					new SimpleBloomFilter(),new SimpleBloomFilter());
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			// bBDBFrontier.put("test", "value");
			bBDBFrontier.putUrl(url);
			/*
			 * getNext 包括删除方法
			 */
			CheckMethods.PrintInfoMessage(((CrawlUrl) bBDBFrontier.pool())
					.getOriUrl());
			bBDBFrontier.putUrl(url);

			CheckMethods.PrintInfoMessage(bBDBFrontier
					.get("http://www.163.com").getOriUrl());

			bBDBFrontier.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

}
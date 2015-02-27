package Froniter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
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

public class BDBFrontier extends AbstractFrontier implements Frontier {

	@SuppressWarnings("rawtypes")
	private StoredMap pendingUrisDB = null;
	SimpleBloomFilter sbf = null;
	private boolean isunvisited = false;
	private String dbName = "db";
	private String homeDirectory = null;

	// ʹ��Ĭ�ϵĻ����С���캯��
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BDBFrontier(String homeDirectory, String dbName,
			boolean isunvisited, SimpleBloomFilter sbf)
			throws DatabaseException {

		super(homeDirectory, dbName);
		this.dbName = dbName;
		this.sbf = sbf;
		this.homeDirectory = homeDirectory;
		if (!isunvisited) {
			if (!getEveryItem()) {
				CheckMethods.PrintDebugMessage("read exist data fault!");
				System.exit(-1);
			}
		}

		CheckMethods.PrintInfoMessage("dbName:\t" + dbName);
		this.isunvisited = isunvisited;
		EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
		EntryBinding valueBinding = new SerialBinding(javaCatalog,
				CrawlUrl.class);
		pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
	}

	// ��ò�ɾ����һ����¼
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

	// ���� CrawlURL

	public boolean putUrl(CrawlUrl url) {
		put(url.getOriUrl(), url);
		return true;
	}

	// �������ݿ�ķ���(Object)
	@SuppressWarnings("unchecked")
	protected void put(Object key, Object value) {
		if (isunvisited) {
			if (visited((CrawlUrl) value)) {
				return;
			} else {
				pendingUrisDB.put(key, value);
			}
		} else {
			sbf.add((CrawlUrl) value);
			pendingUrisDB.put(key, value);
			sync();
		}

	}

	// ȡ��
	protected CrawlUrl get(Object key) {
		return (CrawlUrl) pendingUrisDB.get(key);
	}

	// ���
	protected Object delete(Object key) {
		return pendingUrisDB.remove(key);
	}

	/*
	 * ɾ�� ɾ�����������е��������ݿ⣬���ã�
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

	// ���� URL �����ֵ������ʹ�ø���ѹ���㷨������ MD5 ��ѹ���㷨
	@SuppressWarnings("unused")
	private String caculateUrl(String url) {
		try {
			return MD5.getMD5string(url.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return url;
	}

	/*
	 * Ԥ�������ݿ��е�Ԫ�ص�filter��
	 */
	public boolean getEveryItem() {
		CheckMethods.PrintInfoMessage("2dbName:\t" + dbName);
		CheckMethods.PrintDebugMessage("===========�������ݿ�" + dbName
				+ "�е���������==========");
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
			// ʹ��cursor.getPrev�����������α��ȡ����
			if (myCursor.getFirst(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String theKey = new String(foundKey.getData(), "UTF-8");
				theKey = theKey.substring(3);
				// String theData = new String(foundData.getData(), "UTF-8");
				// resultList.add(theKey);
				sbf.add(theKey);

				while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
					theKey = new String(foundKey.getData(), "UTF-8");
					theKey = theKey.substring(3);
					// theData = new String(foundData.getData(), "UTF-8");
					sbf.add(theKey);
				}
			}
			myCursor.close();
			txn.commit();
			CheckMethods.PrintDebugMessage("===========���ݿ�" + dbName
					+ "����������==========:\t" + size());

			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			CheckMethods.PrintInfoMessage("getEveryItem��������쳣");
			CheckMethods.PrintInfoMessage(e.getMessage().toString());
			CheckMethods.PrintInfoMessage(e.getCause().toString());

			txn.abort();
			if (myCursor != null) {
				myCursor.close();
			}
			return false;
		}

	}

	/*
	 * @see Froniter.Frontier#visited(mian.Crawler.CrawlUrl) 
	 * �Ƿ���ʹ�������bloomfilter
	 */
	@Override
	public boolean visited(CrawlUrl url) {
		if (sbf.contains(url)) {
			return true;
		}
		return false;
	}

	/*
	 * �Ƿ�Ϊ��
	 */
	public boolean isEmpty() {
		if (size() == 0) {
			return true;
		}
		return false;
	}

	/*
	 * ���ض��д�С
	 */
	public long size() {
		return database.count();
	}

	/*
	 * ͬ����������ȥ
	 */
	public void sync() {
		try {
			env.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// ���Ժ���
	public static void main(String[] args) {
		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("D:\\bdb", "url", false,
					new SimpleBloomFilter());
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			// bBDBFrontier.put("test", "value");
			bBDBFrontier.putUrl(url);
			/*
			 * getNext ����ɾ������
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
package com.pachira.LMCrawler;

import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
/*
 * 使用一个抽象类来封装对 Berkeley DB 的操作
 */
public abstract class AbstractFrontier {
	private Environment env;
	private static final String CLASS_CATALOG = "java_class_catalog";
	protected StoredClassCatalog javaCatalog;
	protected Database catalogdatabase;
	protected Database database;

	public AbstractFrontier(String homeDirectory) throws DatabaseException{
		// 打开 env
		System.out.println("Opening environment in: " + homeDirectory);
		File file = new File(homeDirectory);
        if(!file.exists()){
            try {
            	file.mkdirs();
			} catch (Exception e) {
				CheckMethods.PrintDebugMessage("数据库环境路径非法，请检查！");
			}
        }
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setTxnNoSyncVoid(true);
//		envConfig.setTxnWriteNoSyncVoid(false);
		envConfig.setAllowCreate(true);
		env = new Environment(new File(homeDirectory), envConfig);
		// 设置 DatabaseConfig
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);
		// 打开
		catalogdatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
		javaCatalog = new StoredClassCatalog(catalogdatabase);
		// 设置 DatabaseConfig
		DatabaseConfig dbConfig0 = new DatabaseConfig();
		dbConfig0.setTransactional(true);
		dbConfig0.setAllowCreate(true);
		// 打开
		database = env.openDatabase(null, "URL", dbConfig);
	}

	// 关闭数据库，关闭环境
	public void close() throws DatabaseException {
		database.close();
		javaCatalog.close();
		env.close();
	}

	// put 方法
	protected abstract void put(Object key, Object value);

	// get 方法
	protected abstract Object get(Object key);

	// delete 方法
	protected abstract Object delete(Object key);
}
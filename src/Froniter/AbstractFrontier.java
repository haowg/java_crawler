package Froniter;

import java.io.File;
import java.util.concurrent.TimeUnit;

import mian.tools.CheckMethods;

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
	protected Environment env;
	private static final String CLASS_CATALOG = "java_class_catalog";
	protected StoredClassCatalog javaCatalog;
	protected Database catalogdatabase;
	protected Database database;
	
	
	
	
	public AbstractFrontier(String homeDirectory,String dbName) throws DatabaseException{
		// 打开 env
		File file = new File(homeDirectory);
CheckMethods.PrintInfoMessage("Opening environment in: " + homeDirectory);
        if(!file.exists()){
CheckMethods.PrintDebugMessage("file not exists");
            try {
            	file.mkdirs();
			} catch (Exception e) {
CheckMethods.PrintDebugMessage("数据库环境路径非法，请检查！");
			}
            if (!file.exists()) {
CheckMethods.PrintDebugMessage("数据库环境路径非法，请检查！");
System.exit(-1);
			}
        }
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setTxnNoSyncVoid(true);
		envConfig.setTransactional(true);
		envConfig.setReadOnly(false);
		envConfig.setTxnTimeout(10000, TimeUnit.MILLISECONDS);
		envConfig.setLockTimeout(10000, TimeUnit.MILLISECONDS);
		envConfig.setTxnNoSyncVoid(true);//设定事务提交时是否写更改的数据到磁盘，true不写磁盘。
//		envConfig.setTxnWriteNoSyncVoid(true);//设定事务在提交时，是否写缓冲的log到磁盘。如果写磁盘会影响性能，不写会影响事务的安全。随机应变。
//		envConfig.setTxnWriteNoSyncVoid(false);
		envConfig.setAllowCreate(true);
		env = new Environment(new File(homeDirectory), envConfig);
		// 设置 DatabaseConfig
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setSortedDuplicates(false);
		dbConfig.setAllowCreate(true);
//		dbConfig.set
		// 打开
		catalogdatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
		javaCatalog = new StoredClassCatalog(catalogdatabase);
		// 设置 DatabaseConfig
		DatabaseConfig dbConfig0 = new DatabaseConfig();
		dbConfig0.setTransactional(true);
		dbConfig0.setAllowCreate(true);
		// 打开
		database = env.openDatabase(null,dbName, dbConfig);
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
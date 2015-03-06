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
 * ʹ��һ������������װ�� Berkeley DB �Ĳ���
 */
public abstract class AbstractFrontier {
	protected Environment env;
	private static final String CLASS_CATALOG = "java_class_catalog";
	protected StoredClassCatalog javaCatalog;
	protected Database catalogdatabase;
	protected Database database;
	
	
	
	
	public AbstractFrontier(String homeDirectory,String dbName) throws DatabaseException{
		// �� env
		File file = new File(homeDirectory);
CheckMethods.PrintInfoMessage("Opening environment in: " + homeDirectory);
        if(!file.exists()){
CheckMethods.PrintDebugMessage("file not exists");
            try {
            	file.mkdirs();
			} catch (Exception e) {
CheckMethods.PrintDebugMessage("���ݿ⻷��·���Ƿ������飡");
			}
            if (!file.exists()) {
CheckMethods.PrintDebugMessage("���ݿ⻷��·���Ƿ������飡");
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
		envConfig.setTxnNoSyncVoid(true);//�趨�����ύʱ�Ƿ�д���ĵ����ݵ����̣�true��д���̡�
//		envConfig.setTxnWriteNoSyncVoid(true);//�趨�������ύʱ���Ƿ�д�����log�����̡����д���̻�Ӱ�����ܣ���д��Ӱ������İ�ȫ�����Ӧ�䡣
//		envConfig.setTxnWriteNoSyncVoid(false);
		envConfig.setAllowCreate(true);
		env = new Environment(new File(homeDirectory), envConfig);
		// ���� DatabaseConfig
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setSortedDuplicates(false);
		dbConfig.setAllowCreate(true);
//		dbConfig.set
		// ��
		catalogdatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
		javaCatalog = new StoredClassCatalog(catalogdatabase);
		// ���� DatabaseConfig
		DatabaseConfig dbConfig0 = new DatabaseConfig();
		dbConfig0.setTransactional(true);
		dbConfig0.setAllowCreate(true);
		// ��
		database = env.openDatabase(null,dbName, dbConfig);
	}

	// �ر����ݿ⣬�رջ���
	public void close() throws DatabaseException {
		database.close();
		javaCatalog.close();
		env.close();
	}

	// put ����
	protected abstract void put(Object key, Object value);

	// get ����
	protected abstract Object get(Object key);

	// delete ����
	protected abstract Object delete(Object key);
}
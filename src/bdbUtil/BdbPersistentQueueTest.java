package bdbUtil;


import java.io.File;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.TestCase;

public class BdbPersistentQueueTest extends TestCase{
    Queue<String> memoryQueue;
    Queue<String> persistentQueue;
    
    protected void setUp() throws Exception {
        super.setUp();
        memoryQueue=new LinkedBlockingQueue<String>();
        String dbDir="E:/java/test/bdbDir";
        File file=new File(dbDir);
        if(!file.exists()||!file.isDirectory()){
            file.mkdirs();
        }
        persistentQueue=new BdbPersistentQueue<String>(dbDir,"pq",String.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        memoryQueue.clear();
        memoryQueue=null;
        persistentQueue.clear();
        persistentQueue=null;
    }
    
    /**
     * �ŷ�ֵ
     * @param queue
     * @return      �ŷŵ����ݸ���
     */
    public int drain(Queue<String> queue){
        int count=0;
        while(true){
            try {
                queue.remove();
                count++;
            } catch (Exception e) {
                return count;
            }
        }
       
    }
    /**
     * 
     * @param queue
     * @param size
     */
    public void fill(Queue<String> queue,int size){
        for(int i=0;i<size;i++){
            queue.add(i+"");
        }
    }
    
    public void checkTime(int size){
        System.out.println("1.�ڴ�Queue������ſ���������ʱ��");
        long time=0;
        long start=System.nanoTime();
        fill(memoryQueue,size);
        time=System.nanoTime()-start;
        System.out.println("\t��� "+size+" �����ݺ�ʱ: "+(double)time/1000000+" ����,������ʱ: "+((double)time/size)+" ����");
        start=System.nanoTime();
        drain(memoryQueue);
        time=System.nanoTime()-start;
        System.out.println("\t�ſ� "+size+" �����ݺ�ʱ: "+(double)time/1000000+" ����,������ʱ: "+((double)time/size)+" ����");
        
        System.out.println("2.�־û�Queue������ſ���������ʱ��");
        start=System.nanoTime();
        fill(persistentQueue,size);
        time=System.nanoTime()-start;
        System.out.println("\t��� "+size+" �����ݺ�ʱ: "+(double)time/1000000+" ����,������ʱ: "+((double)time/size/1000000)+" ����");
        start=System.nanoTime();
        drain(persistentQueue);
        time=System.nanoTime()-start;
        System.out.println("\t�ſ� "+size+" �����ݺ�ʱ: "+(double)time/1000000+" ����,������ʱ: "+((double)time/size/1000)+" ����");
        
    }
    
    /**
     * ʮ������������
     */
    public void testTime_tenThousand(){
        System.out.println("========����1000000(ʮ��)������=================");
        checkTime(100000);
    }
    
    
    /**
     * ��������������
     */
    public void testTime_mil(){
        System.out.println("========����1000000(����)������=================");
        checkTime(1000000);
    }
    

    /**
     * ǧ������������,ע��Ҫ��ֹ�ڴ����
     */
    public void testTime_tenMil(){
        System.out.println("========����10000000(ǧ��)������=================");
        checkTime(10000000);
    }
    
    /**
     * ���Զ�������׼ȷ��
     * @param queue
     * @param queueName
     * @param size
     */
    public void checkDataExact(Queue<String> queue,String queueName,int size){
    	if(queue.size()!=size){
    		System.err.println("Error size of "+queueName);
    	}
    	String value=null;
    	for(int i=0;i<size;i++){
    		value=queue.remove();
    		if(!((i+"").equals(value))){
    			System.err.println("Error "+queueName+":"+i+"->"+value);
    		}
    	}
    }
    
    /**
     * ���Զ��������ݵ�׼ȷ��,��������
     */
    public void testExact(){
    	int size=100;
    	fill(memoryQueue,size);
    	fill(persistentQueue,size);
    	
    	checkDataExact(memoryQueue,"MemoryQueue",100);
    	checkDataExact(persistentQueue,"PersistentQueue",100);
    	 
    }
    
}

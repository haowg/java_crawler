package com.pachira.LMCrawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileTools {
	/*
	 * ɾ���ļ�
	 */
	
	public static  void deleteFile(File file) {
		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������˼;
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					deleteFile(files[i]); // ��ÿ���ļ� ������������е���
				}
			}
			file.delete();
		} else {
			System.out.println("��ɾ�����ļ������ڣ�" + '\n');
		}
	} 

	/*
	 * �����ļ���
	 */
	
	public static  void creatdir(String lostr) {
		try{
		File file = new File(lostr);
//		System.out.println(lostr);
		if (!file.exists()) {
			file.mkdirs();
		}
		file.mkdirs();
		}catch(Exception e){
			System.err.println("�����ļ���ʧ�ܣ�");
		}
	
	}
	/*
	 * �����ļ�
	 */
	public static boolean Copy_byfileChannel(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//�õ���Ӧ���ļ�ͨ��
            out = fo.getChannel();//�õ���Ӧ���ļ�ͨ��
            in.transferTo(0, in.size(), out);//��������ͨ�������Ҵ�inͨ����ȡ��Ȼ��д��outͨ��
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
/*
 * ɾ�����ļ���
 */
	public static void deleteEmptyDir(String filePath) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			if (subFiles != null && subFiles.length > 0) {
				for (int i = 0; i < subFiles.length; i++) {
					deleteEmptyDir(subFiles[i].getPath());
				}
			} else {
				file.delete();
				return;
			}
			File[] newsubFiles = file.listFiles();
			if (newsubFiles == null || newsubFiles.length == 0) {
				file.delete();
			}
		}
	}
	/*
	 * д���ļ�
	 */
	public static synchronized void write(String inputString,String list) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(list,true);
			writer.write(inputString);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * �ݹ����Ŀ¼�е��ļ�
	 */
	public static ArrayList<String> getFiles(String filePath) {
		ArrayList<String> filelist = new ArrayList<String>();
		File root = new File(filePath);

		File[] files = root.listFiles();

		for (File file : files) {
			/*
			 * �ݹ����
			 */
			try {
				if (file.isDirectory()) {
					filelist.addAll(getFiles(file.getCanonicalPath()));
				} else {
					filelist.add(file.getCanonicalPath());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return filelist;
	}
	/*
	 * ��ø�·��
	 */
	public static String getParentDir (String filePath){
		
		String dirString;
		File f = new File(filePath);
		dirString = f.getParent();
		return dirString;
		
	}
	/*
	 * �õ���ǰ·��
	 */
	public static String getDir (String filePath){
		
		String dirString;
		File f = new File(filePath);
		dirString = f.getAbsolutePath();
		return dirString;
		
	}
}

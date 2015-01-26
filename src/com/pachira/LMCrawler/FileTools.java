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
	 * 删除文件
	 */
	
	public static  void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	} 

	/*
	 * 创建文件夹
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
			System.err.println("创建文件夹失败！");
		}
	
	}
	/*
	 * 复制文件
	 */
	public static boolean Copy_byfileChannel(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
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
 * 删除空文件夹
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
	 * 写入文件
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
	 * 递归查找目录中的文件
	 */
	public static ArrayList<String> getFiles(String filePath) {
		ArrayList<String> filelist = new ArrayList<String>();
		File root = new File(filePath);

		File[] files = root.listFiles();

		for (File file : files) {
			/*
			 * 递归调用
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
	 * 获得父路径
	 */
	public static String getParentDir (String filePath){
		
		String dirString;
		File f = new File(filePath);
		dirString = f.getParent();
		return dirString;
		
	}
	/*
	 * 得到当前路径
	 */
	public static String getDir (String filePath){
		
		String dirString;
		File f = new File(filePath);
		dirString = f.getAbsolutePath();
		return dirString;
		
	}
}

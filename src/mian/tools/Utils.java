package mian.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Utils {

	/*
	 * 链接URL获取相应的信息
	 */
	public static  Document connect(String url) {
		
		Document doc = null;
		try {
//System.out.println(url);
			doc = Jsoup
					.connect(url)
					.timeout(8000)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WO"
									+ "W64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
					.get();
			if (doc==null) {
				doc = Jsoup
						.connect(url)
						.timeout(8000)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WO"
										+ "W64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
						.post();
			}
		} catch (IOException e) {
			String msg = e.getMessage();
			if(msg.toLowerCase().contains("time")){
				System.err.println("[error]: connect to "+ url + " time out.");
//			}else if(rule){
				doc = connect(url);
			}else{
				System.err.println("[error]: " + e.getMessage());
			}
//			System.err.println("错误\nurl为:     "+url+"\nrule为:     "+rule);
//			System.err.println(e.getMessage());
			e.printStackTrace();
//			System.exit(0);
		}
		if (doc == null) {
			System.err.println("连接失败");
		}
		return doc;
	}
	
	/*
	 * 通过URL和相应的rule得到相应信息（elements）
	 */
	public static  Elements GetData(String url, String rule) {
		Elements eles = null;
			Document doc = null;
			System.out.println("[info]: connect to " + url);
			doc = connect(url);
			
			eles = doc.select(rule);
		
		if (eles.isEmpty()) {
			System.err.println("warning:所抓取的数据为空\n"+url);
		}
		return eles;
	}

	/*
	 * 写文件
	 */
	public static void write(Set<String> data, String string) {
		write(data,  string,"");
	}
	
	public static void write(Set<String> strsSet, String lostr, String prestring) {
		String str = "";
		for (String s : strsSet){
			str = str + prestring + s + "\n";
		}
		write(str, lostr);
//		 System.out.println(str);
		
	}

	public static  void write(String str,String lostr) {
//		System.out.println(str);
		BufferedWriter bufw = null;
		PrintWriter pw = null;
		File f = new File(lostr);
		lostr = f.getAbsolutePath();
		File file = new File(lostr);
//		System.out.println(lostr);
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(lostr, true);
			bufw = new BufferedWriter(fw);
			pw = new PrintWriter(bufw);
			pw.write(str);
		} catch (NullPointerException e) {
			System.out.println("err:\t地址错误\r\n地址：" + lostr + "\n文本：" + str.substring(0,100).trim()+"........");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.err.println("[error]:write data to file error.");
			// e.printStackTrace();
		} finally {
			try {
				if (bufw != null)
					bufw.close();
				bufw = null;
				if (pw != null)
					pw.close();
				pw = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 过滤为title可用的字符串
	 */
	public static  String totitle(String titleString) {
		String regEX = "[^\u4e00-\u9fa5\\w\\.]+";
		titleString = titleString.replaceAll(regEX, "");
		return titleString;
	}
	
	
	/*
	 * 解析单个命令所需的配置，生成用于格式化输出的blocke并返回
	 */
	public static  Element parsesingleroote(Element roote) {
		Element blocke = Jsoup.parse("<block>").getElementsByTag("block").first();
		if (roote.hasAttr("ID")&&!roote.attr("ID") .equals("")) {
			blocke.attr("ID",roote.attr("ID"));
		}
		if (roote.hasAttr("LANGUAGE")&&!roote.attr("LANGUAGE") .equals("")) {
			blocke.attr("LANGUAGE",roote.attr("LANGUAGE"));
		}
		if (roote.hasAttr("TRADE")&&!roote.attr("TRADE") .equals("")) {
			blocke.attr("TRADE",roote.attr("TRADE"));
		}
		if (roote.hasAttr("FORMAT")&&!roote.attr("FORMAT") .equals("")) {
			blocke.attr("FORMAT",roote.attr("FORMAT"));
		}
		
		return blocke;
		
	}
	
	/*
	 * 读取xml配置文件
	 */
	public static  String readXml(String filePath) {
		String s = "";
		try {
			String encoding = "GB18030";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					s += lineTxt;
					s +="\n";
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return s;
	}

	
	/*
	 * java运行命令行的语句的工具类
	 */
	public static  int java_os(String cmdStr,String os) {
		int exitVal = 0;
		cmdStr = cmdStr.trim();
		String[] comands = null;
		if (os.equals("linux")) {
			comands = new String[] { "/bin/sh", "-c", cmdStr };
		}else if (os.equals("win")) {
			comands = new String[] {"cmd.exe","/c",cmdStr};
		}
		
		System.out.println("--"+cmdStr+"--");
		InputStream stderr = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
        try {
        	Runtime rt = Runtime.getRuntime();
        	Process p ;
        	if (comands!=null) {
    			p = rt.exec(comands);
			}else {
    			p = rt.exec(cmdStr);
			}
//			stderr = p.getErrorStream();  
//            isr = new InputStreamReader(stderr);  
//            br = new BufferedReader(isr); 
//            String line = null;  
//            while ((line = br.readLine()) != null){ 
////            	System.out.println("============");
//                System.out.println(line);
//            }
            System.out.println("-------------");
			exitVal += doWaitFor(p);
			System.out.println("Process exitValue: " + exitVal);
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			
			try {
				if (stderr!=null) {
					stderr.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (br !=null) {
					br.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			};
		}
		return exitVal;
	}

	public static  int java_os(String cmdStr){
		int i = java_os(cmdStr,"");
		return i;
	} 
	
	private static  int doWaitFor(Process p) {
		int exitValue = -1; // returned to caller when p is finished
		try {

			InputStream in = p.getInputStream();
			InputStream err = p.getErrorStream();
			boolean finished = false; // Set to true when p is finished

			while (!finished)
			{
				try {
					while (in.available() > 0)
					{
						// Print the output of our system call
						Character c = new Character((char) in.read());
						System.out.print(c);
					}
					while (err.available() > 0)
					{
						// Print the output of our system call
						Character c = new Character((char) err.read());
						System.out.print(c);
					}

					// Ask the process for its exitValue. If the process
					// is not finished, an IllegalThreadStateException
					// is thrown. If it is finished, we fall through and
					// the variable finished is set to true.
					exitValue = p.exitValue();
					finished = true;
				} catch (IllegalThreadStateException e) {
					Thread.currentThread();
					// Process is not finished yet;
					// Sleep a little to save on CPU cycles
					Thread.sleep(500);
				}
			}
		} catch (Exception e) {
			// unexpected exception! print it out for debugging...
			System.err.println("doWaitFor(): unexpected exception - "
					+ e.getMessage());
		}

		// return completion status to caller
		return exitValue;
	}

	/*
	 * 获取当前时间
	 */
	public static  String gettime() {
		
		Date d = new Date();
		long longtime = d.getTime();
//		System.out.println(longtime);
		// 自用SimpleDateFormat函数变自想要格式,注意需要import java.text.SimpleDateFormat;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String timeString = sdf.format(longtime);
		return timeString;

	}
	
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
	 * 获取央视新闻真实视频连接
	 */
	public static  HashSet<String> getcntvrealurl(String vediourl) {
		HashSet<String> set = new HashSet<String>();
		Document doc = connect(vediourl);
		try {
			String id = "";
			for (String s : doc.toString().split("\n")) {
				if (s.contains("videoCenterId")) {
					id = s.replaceAll("^.*videoCenterId\",\"","");
					id = id.replaceAll("\".*$","").trim();
					break;
				}
			}
			String passurl = "http://vdn.apps.cntv.cn/api/getHttpVideoInfo.do?pid=" + id;
			Document passdoc = connect(passurl);
			/*
			 * 从中间网页获得标题
			 */
			int titleloc = passdoc.text().indexOf("title");
			System.out.println(titleloc);
			String title = passdoc.text().substring( titleloc+8,passdoc.text().substring(titleloc).indexOf(",")+titleloc-1);
			title = totitle(title);
			set.add("<title>"+title);
			/*
			 *获得低清的视频的下载真实链接 
			 */
			int urlloc = passdoc.text().indexOf("lowChapters");
			String urlsString = passdoc.text().substring(urlloc).substring(0,passdoc.text().substring(urlloc).indexOf("}]"));
			String regex = "url\":\"[^\"]*";
			Pattern pat = Pattern.compile(regex);
			Matcher mat = pat.matcher(urlsString);
			while (mat.find()) {
				String group = mat.group();
				set.add("<audio_url>"+group.substring(6));
			}
			
		} catch (Exception e) {
			System.err.println("获取央视新闻真实视频地址失败！！！");
		}
		
		return set;
	}
	
	public static  boolean fileurl(List<String> urls,String url) {
		int max=urls.size()-1;
		int min=0;
		int mid = 0;
		while (max>=min) {
			mid = (max+min)/2;
			if (url.trim().equals(urls.get(mid).trim())) {
				return false;
			}else if (url.compareTo(urls.get(mid))>0) {
				min = mid+1;
			}else {
				max=mid-1;
			}
		}
		return true;
	}
	
	public static String convertunicode(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		
		while((i=utfString.indexOf("\\u", pos)) != -1){
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}
		
		return sb.toString();
	}
}

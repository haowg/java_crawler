package com.pachira.LMCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Java_os {
	StringBuffer message = new StringBuffer();
	/*
	 * Ö´ÐÐÏµÍ³ÃüÁî
	 */
	public int java_os(String cmdStr) {
		int exitVal = 0;
		cmdStr = cmdStr.trim();
		String[] comands = null;
		if (System.getProperty("os.name").contains("inux")) {
			comands = new String[] { "/bin/sh", "-c", cmdStr };
		}else if (System.getProperty("os.name").contains("indow")) {
			comands = new String[] {"cmd.exe","/c",cmdStr};
		}
		
//System.out.println("--"+cmdStr+"--");
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

//            System.out.println("-------------");
			exitVal += doWaitFor(p);
//			System.out.println("Process exitValue: " + exitVal);
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
	
	private int doWaitFor(Process p) {
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
						this.message.append(c);
						//System.out.print(c);
					}
					while (err.available() > 0)
					{
						// Print the output of our system call
						Character c = new Character((char) err.read());
						this.message.append(c);
						//System.out.print(c);
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
	 
}


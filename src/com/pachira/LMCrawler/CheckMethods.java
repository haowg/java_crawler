package com.pachira.LMCrawler;

public class CheckMethods {

	public static void PrintDebugMessage(String string) {
		// TODO Auto-generated method stub
		FileTools.write(string, "log");
	}

	public static void PrintInfoMessage(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

}

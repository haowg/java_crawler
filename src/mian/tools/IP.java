package mian.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {
	public static void main(String[] args) throws IOException{
		String hostName;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println();
			System.out.println("input HostName:\t");
			hostName = input.readLine();
			System.out.println("the IP is :\t"+getIP(hostName));			
		}
	}

	private static String getIP(String hostName) {
		InetAddress ipaddress = null;
		try {
			ipaddress = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
//			e.printStackTrace();
			return "bushidao";
		}
		return ipaddress.getHostAddress();
	}
}

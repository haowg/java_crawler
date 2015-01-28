package com.pachira.LMCrawler;

/* ���������һ���ֽ�����
 * �����������ֽ������ MD5 ����ַ���
 */
public class MD5 {
	public static String getMD5string(byte[] source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' }; // �������ֽ�ת����ʮ�����Ʊ�ʾ���ַ�
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
			// ���ֽڱ�ʾ���� 16 ���ֽ�
			char str[] = new char[16 * 2]; // ÿ���ֽ���ʮ�����Ʊ�ʾ�Ļ���ʹ�������ַ���
			// ���Ա�ʾ��ʮ��������Ҫ 32 ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
			// ת����ʮ�������ַ�
				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת����
				// >>> Ϊ�߼����ƣ�������λһ������
				str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ������Ľ��ת��Ϊ�ַ���
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static byte[] getMD5byte(byte[] source) {
		byte[] tmp = null;
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			tmp = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	public static String MD5byte2string(byte[] source){
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' }; // �������ֽ�ת����ʮ�����Ʊ�ʾ���ַ�
		try {
			
			char str[] = new char[16 * 2]; // ÿ���ֽ���ʮ�����Ʊ�ʾ�Ļ���ʹ�������ַ���
											// ���Ա�ʾ��ʮ��������Ҫ 32 ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
			// ת����ʮ�������ַ�
				byte byte0 = source[i]; // ȡ�� i ���ֽ�
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת����
				// >>> Ϊ�߼����ƣ�������λһ������
				str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ������Ľ��ת��Ϊ�ַ���
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	private static int parse(char c) {  
	    if (c >= 'a')  
	        return (c - 'a' + 10) & 0x0f;  
	    if (c >= 'A')  
	        return (c - 'A' + 10) & 0x0f;  
	    return (c - '0') & 0x0f;  
	} 
	public static byte[] string2MD5byte(String s){
		byte source[] = new byte[16];
		try {
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < source.length; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
			// ת����ʮ�������ַ�
				char c0 = s.charAt(k++);
				char c1 = s.charAt(k++);
				source[i] = (byte)((parse(c0)<<4)|parse(c1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}
	
}
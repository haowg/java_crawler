package mian.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class myCookies {
	
	private Map<String,String> cookies = null;
	
	public myCookies(String location){

		String s = "";
		try {
			s = FileUtils.readFileToString(new File(location));
		} catch (IOException e) {
			CheckMethods.PrintDebugMessage("Can not find cookies file !!!");
			System.exit(0);
		}
		JSONArray json = null;
		Map<String, String> cookies = new HashMap<String, String>();

		try {
			json = new JSONArray(s);

			for (int i = 0; i < json.length(); i++) {
				JSONObject js = (JSONObject) json.get(i);
				cookies.put(js.getString("name"), js.getString("value"));
			}

		} catch (Exception e) {
			CheckMethods.PrintDebugMessage("parse cookie file err !!! ");
			System.exit(-1);
		}
		this.cookies = cookies;
	}
	public Map<String, String> getCookies() {
		return cookies;
	}
	public void setCookies(Map<String, String> cookies) {
		if(cookies != null){
//System.err.println(this.cookies);
//System.err.println(cookies);
//			this.cookies = cookies;
			for (Entry<String, String> entry :cookies.entrySet()) {
				this.cookies.put(entry.getKey(), entry.getValue());
			}
//System.err.println(this.cookies);
		}
	}
}

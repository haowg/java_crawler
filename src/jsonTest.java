import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mian.tools.CheckMethods;
import mian.tools.Utils;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
/*
 * 测试用json方式导出cookie后，利用其访问需要登录的网页
 * 测试用代码
 */
public class jsonTest {
	public static void main(String[] args) throws IOException {
		String s = "";
		try {
			s = FileUtils.readFileToString(new File("x.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray json = null;
		/*
		 * jsoup 中导入cookie需要使用map格式
		 */
		Map<String, String> cookies = new HashMap<>();

		try {
			json = new JSONArray(s);

			// System.out.println(s);
			// System.out.println(json);
			for (int i = 0; i < json.length(); i++) {
				JSONObject js = (JSONObject) json.get(i);
				// System.out.print(js.get("name")+"\t");
				// System.out.println(js.get("value"));
				cookies.put(js.getString("name"), js.getString("value"));

			}
		} catch (Exception e) {

		}
		CheckMethods.PrintInfoMessage(cookies.toString());
		/*
		 * jsoup 利用cookie访问网页
		 */
		Response res = Jsoup
				.connect("http://weibo.cn/renminwang?page=2")
				.cookies(cookies)
				.timeout(8000)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; WO"
								+ "W64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
				.method(Method.GET).execute();
//		System.out.println(res.body());
		Utils.write(res.body(), "x.html");

		// WebClient wc = new WebClient();
		// wc.getOptions()
		// HtmlPage hp = new HtmlPage("", res, webWindow)

		cookies = res.cookies();
	}
}

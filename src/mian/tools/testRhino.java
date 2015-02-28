package mian.tools;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.Scriptable;

public class testRhino {
	public static void main(String[] args) {
		Context ctx = Context.enter();
		Scriptable scope = ctx.initStandardObjects();

		String jsStr = "100*20/10";
		Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
		System.out.println("result=" + result);
	}
}

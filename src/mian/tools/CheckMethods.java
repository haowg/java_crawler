package mian.tools;


public class CheckMethods {

	public static void PrintDebugMessage(String string) {
		// TODO Auto-generated method stub
		System.err.println(string);
		FileTools.write("########################"+Utils.gettime()+"###########################\n","log");
		FileTools.write(string+"\n", "log");
	}

	public static void PrintInfoMessage(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

}

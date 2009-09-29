import java.io.BufferedReader;
import java.io.InputStreamReader;
import watij.runtime.ie.IE;

public class test {
	
	public static void main(String[] args) throws Exception {
		IE ie = new IE();
		String url = "http://www.google.com/";
		ie.start(url);
		Thread.sleep(2000);
		Runtime r = Runtime.getRuntime();
		Process p = r.exec("tasklist /FI \"IMAGENAME eq iexplore.exe\" /NH /FO CSV");
		BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		if(err.readLine() == null) {
			System.err.println("kill");
			r.exec("taskkill /f /im iexplore.exe /t");
		}
		ie.close();
	}
}

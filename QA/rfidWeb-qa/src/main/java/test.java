import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import watij.elements.Button;
import watij.elements.Link;
import watij.elements.Links;
import watij.elements.SelectList;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import static watij.finders.FinderFactory.*;

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

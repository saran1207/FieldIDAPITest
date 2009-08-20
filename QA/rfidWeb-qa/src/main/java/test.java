import com.n4systems.fieldid.FieldIDMisc;

import watij.elements.TextField;
import watij.runtime.ie.IE;
import static watij.finders.FinderFactory.*;

public class test {
	
	IE ie;

	/**
	 * This should do the following:
	 * 
	 * 		Open the browser to Google
	 * 		Start the refresh monitor
	 * 		Set the search text to "Darrell"
	 * 		Wait for 90 seconds (refresh should occur in 60 seconds)
	 * 		Approximately 30 seconds after the refresh, set the search text to "Mindy"
	 * 		Stop the monitor
	 * 		Wait for 90 seconds ("Mindy" should never get cleared)
	 * 		The text "Mindy" should change to "Elliot"
	 * 		Start the monitor
	 * 		Wait for 90 seconds (refresh should occur in 60 seconds)
	 * 		Approximately 30 seconds later, set to "Cindy"
	 * 		Stop the monitor
	 * 		Wait for 90 seconds ("Cindy" should never get cleared)
	 *  
	 * @param url
	 * @throws Exception
	 */
	public void a(String url) throws Exception {
		ie.start(url);
		FieldIDMisc.startMonitor();
		TextField q = ie.textField(xpath("//INPUT[@name='q']"));
		q.set("Darrell");
		Thread.sleep(70000);
		q = ie.textField(xpath("//INPUT[@name='q']"));
		q.set("Mindy");
		FieldIDMisc.stopMonitor();
		Thread.sleep(70000);
		q.set("Elliot");
		FieldIDMisc.startMonitor();
		Thread.sleep(70000);
		q = ie.textField(xpath("//INPUT[@name='q']"));
		q.set("Cindy");
		FieldIDMisc.stopMonitor();
		Thread.sleep(70000);
		FieldIDMisc.quitMonitor();
		Thread.sleep(10000);
		ie.close();
	}

	public static void main(String[] args) throws Exception {
		test a = new test();
		String url = "http://www.google.ca/";
		a.a(url);
	}
	
	public test() {
		ie = new IE();
	}
}

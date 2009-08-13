import com.n4systems.fieldid.Refresh;

import watij.elements.TextField;
import watij.runtime.ie.IE;
import static watij.finders.FinderFactory.*;

public class test {
	
	static Refresh monitor;
	
	public static void main(String[] args) throws Exception {
		String url = "http://www.google.ca/";
		IE ie = new IE();
		ie.start(url);
		startMonitor(ie);
		TextField q = ie.textField(xpath("//INPUT[@name='q']"));
		q.set("Darrell");
		Thread.sleep(70000);	// monitor should clear the "Darrell"
		q.set("Mindy");
		stopMonitor();			// stop the monitor, "Mindy" should not clear
		Thread.sleep(70000);
		q.set("Darrell");		// change Mindy to Darrell
		startMonitor(ie);
		Thread.sleep(70000);	// monitor should clear the "Darrell" before changing to Mindy
		q.set("Mindy");
		ie.close();
	}
	
	public static void startMonitor(IE ie) {
		monitor = new Refresh("monitor", ie);
		monitor.start();
	}
	
	public static void stopMonitor() {
		monitor.quit();
	}
}

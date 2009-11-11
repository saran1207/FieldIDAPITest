package com.n4systems.fieldid.selenium.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.n4systems.fieldid.selenium.testcase.FieldIDTestCase;
import com.thoughtworks.selenium.Selenium;

/**
 * For any functionality which spans multiple features. For example,
 * chooseOwner is on multiple pages but it is really the same code.
 * 
 * @author Darrell Grainger
 *
 */
public class CommonMethods extends FieldIDTestCase {


	private Random r = new Random();

	/**
	 * Initialize the library to use the same instance of Selenium as the
	 * test cases.
	 * 
	 * @param selenium Initialized instance of selenium used to access the application under test
	 */
	public CommonMethods(Selenium selenium) {
		assertTrue("Instance of Selenium is null", selenium != null);
		this.selenium = selenium;
	}

	/**
	 * Create a directory with a timestamp of the current time. The format of
	 * the directory will be yyyyMMdd-HHmmss, where y is year, M is month, d is
	 * day of month, H = 24 hour, m = minutes and s = seconds. If the method
	 * is successful it will return the directory it created. If it fails it
	 * will return null.
	 * 
	 * @return name of the directory which was created or null if it failed.
	 */
	public String createTimestampDirectory() {
		SimpleDateFormat now = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String timestamp = now.format(new Date());
		File d = new File(timestamp);
		if (d.mkdirs())
			return timestamp;
		return null;
	}

	/**
	 * Will forcefully kill a process on the Windows operating system.
	 * Just give the method the name of the process to kill. For example,
	 * to kill Internet Explorer I would use:
	 * 
	 * 		forcefullyKillWindowsProcess("iexplore.exe");
	 * 
	 * @param imageName
	 * @throws Exception
	 */
	public void forcefullyKillWindowsProcess(String imageName) throws Exception {
		String os = System.getProperty("os.name");
		if(os.contains("Windows")) {
			Runtime r = Runtime.getRuntime();
			// check to see if the process is running
			Process p = r.exec("tasklist /FI \"IMAGENAME eq " + imageName + "\" /NH /FO CSV");
			// if running, error stream will be empty, i.e. null
			BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			if(err.readLine() == null) {
				// if it is running, kill it
				r.exec("taskkill /f /im " + imageName + " /t");
				Thread.sleep(5000);	// give it 5 seconds to die
			}
		}
	}

	/**
	 * Gets today's date in the format MM/dd/yy
	 * 
	 * @return "MM/dd/yy" where MM is the month, dd is the day and yy is the year
	 */
	public String getDateString() {
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		String today = now.format(new Date());
		return today;
	}

	/**
	 * Get the date string in the format MM/dd/yy but change the day.
	 * If the input is negative this will go backward in time. Positive will
	 * go forward in time. For example, if today is 12/25/09 we would get the
	 * following from this method:
	 * 
	 * 		getDateStringChangeYear(-1);	// 12/24/09
	 * 		getDateStringChangeYear(0);		// 12/25/09
	 * 		getDateStringChangeYear(1);		// 12/26/09
	 * 
	 * @param i how much to change the day by
	 * @return a date string in the format MM/dd/yy
	 */
	public String getDateStringChangeDay(int i) {
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, i);
		String dateString = now.format(c.getTime());
		return dateString;
	}

	/**
	 * Get the date string in the format MM/dd/yy but change the month.
	 * If the input is negative this will go backward in time. Positive will
	 * go forward in time. For example, if today is 12/25/09 we would get the
	 * following from this method:
	 * 
	 * 		getDateStringChangeYear(-1);	// 11/25/09
	 * 		getDateStringChangeYear(0);		// 12/25/09
	 * 		getDateStringChangeYear(1);		// 01/25/10
	 * 
	 * @param i how much to change the month by
	 * @return a date string in the format MM/dd/yy
	 */
	public String getDateStringChangeMonth(int i) {
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, i);
		String dateString = now.format(c.getTime());
		return dateString;
	}

	/**
	 * Get the date string in the format MM/dd/yy but change the year.
	 * If the input is negative this will go backward in time. Positive will
	 * go forward in time. For example, if today is 12/25/09 we would get the
	 * following from this method:
	 * 
	 * 		getDateStringChangeYear(-1);	// 12/25/08
	 * 		getDateStringChangeYear(0);		// 12/25/09
	 * 		getDateStringChangeYear(1);		// 12/25/10
	 * 
	 * @param i how much to change the year by
	 * @return a date string in the format MM/dd/yy
	 */
	public String getDateStringChangeYear(int i) {
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, i);
		String dateString = now.format(c.getTime());
		return dateString;
	}

	public String getDateStringWithTime() {
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy h:mm aa");
		String today = now.format(new Date());
		return today;
	}
	
	/**
	 * Returns a positive, random integer.
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRandomInteger() throws Exception {
		return Math.abs(r.nextInt());
	}
	
	/**
	 * Returns a random integer between 0 (inclusive) and limit (exclusive)
	 * 
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public int getRandomInteger(int limit) throws Exception {
		assertTrue("Got " + limit + " but expected a positive integer.", limit > 0);
		return r.nextInt(limit);
	}
	
	/**
	 * Returns an integer in the range low (inclusive) to high (inclusive).
	 * If I wanted to simulate a die roll I can use:
	 * 
	 * 		getRandomInteger(1,6);
	 * 
	 * @param low
	 * @param high
	 * @return
	 * @throws Exception
	 */
	public int getRandomInteger(int low, int high) throws Exception {
		assertTrue("low value must be less than high value.", low <= high);
		int range = (high - low) + 1;
		return r.nextInt(range) + low;
	}
	
	/**
	 * Returns a hexidecimal string which can be used as an RFID number.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRandomRFID() throws Exception {
		String s = new String(Long.toHexString(r.nextLong()));
		return s.toUpperCase();
	}

	/**
	 * Generate a random string of the given length. First letter will be
	 * capitalize and subsequent letters will be lowercase. The string will
	 * be complete gibberish.
	 * 
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public String getRandomString(int length) throws Exception {
		assertTrue("length must be a positive number", length > 0);
		String s = "";
		int range = 'z' - 'a';
		for(int i = 0; i < length; i++) {
			int c = getRandomInteger(range) + 'a';
			if(i == 0) {
				c = Character.toUpperCase(c);
			}
			s += (char)c;
		}
		return s;
	}
}

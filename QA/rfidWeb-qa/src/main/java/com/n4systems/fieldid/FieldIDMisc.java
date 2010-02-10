package com.n4systems.fieldid;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import com.jniwrapper.win32.ui.Wnd;
import com.n4systems.fieldid.datatypes.Owner;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import watij.runtime.ie.IEPromptDialog;
import junit.framework.TestCase;
import static watij.finders.FinderFactory.*;

public class FieldIDMisc extends TestCase {

	IE ie = null;
	public static Refresh monitor = null;
	private static boolean enabled = true;
	static long hack = 5000;	// wait 5 seconds for Javascript actions to complete
	static long lightBoxHack = 10000;
	static long lightBoxHackLoops = 60; // will loop 60 times, 1 second each loop or less
	public static long dayInMilliseconds = 86400000;	// used for scheduling inspections
	Random r = new Random();
	private Finder lightBoxOKButtonFinder;
	private Finder lightBoxMessageFinder;
	private Finder lightBoxMessageHeaderFinder;
	private Finder errorMessageFinder;
	private Finder backToAdministationLinkFinder;
	private Finder successMessageFinder;
	private Finder contentTitleFinder;
	private Finder assetsViewAllFinder;
	private Finder loadingIndicatorDivFinder;
	private Finder labelForOfXPagesFinder;
	private Finder nextPageLinkFinder;
	private Finder firstPageLinkFinder;
	private Finder tellUsWhatHappenedLinkFinder;
	private Finder chooseOwnerOrganizationSelectFinder;
	private Finder chooseOwnerCustomerSelectFinder;
	private Finder chooseOwnerDivisionSelectFinder;
	private Finder chooseOwnerSelectButtonFinder;
	private Finder chooseOwnerCancelButtonFinder;
	private Finder addProductChooseOwnerLinkFinder;
	private Finder chooseOwnerDialogFinder;
	
	public FieldIDMisc(IE ie) {
		this.ie = ie;
		if(monitor == null) {
			monitor = new Refresh("refresh", ie);
		}
		tellUsWhatHappenedLinkFinder = xpath("//A[contains(text(),'Tell us what happened')]");
		lightBoxOKButtonFinder = tag("button");
		lightBoxMessageFinder = xpath("//P[@id='modalBox_message']");
		lightBoxMessageHeaderFinder = xpath("//DIV[@class='ajaxView']/H2[text()='Messages']");
		errorMessageFinder = xpath("//DIV[@class='errorMessage']");
		backToAdministationLinkFinder = xpath("//DIV[@id='contentTitle']/DIV[@class='backToLink']/A[contains(text(),'back to administration')]");
		successMessageFinder = xpath("//DIV[@id='message']/UL/LI/SPAN[@class='actionMessage']");
		contentTitleFinder = xpath("//DIV[@id='contentTitle']/H1");
		assetsViewAllFinder = xpath("//DIV[@id='resultsTable']/TABLE/TBODY/TR/TD/BUTTON[contains(text(),'Select')]");
		loadingIndicatorDivFinder = xpath("//DIV[@class='loading']/..");
		labelForOfXPagesFinder = xpath("//DIV[@id='pageContent']/DIV[@class='paginationWrapper']/UL/LI/SPAN[@class='gotoPage']/LABEL[@for='lastPage']");
		nextPageLinkFinder = xpath("//DIV[@id='pageContent']/DIV[@class='paginationWrapper']/UL/LI/A[contains(text(),'Next')]");
		firstPageLinkFinder = xpath("//DIV[@id='pageContent']/DIV[@class='paginationWrapper']/UL/LI/A[contains(text(),'First')]");
		chooseOwnerDialogFinder = xpath("//DIV[@id='orgSelector']");
		addProductChooseOwnerLinkFinder = xpath("//A[@class='searchOwner' and contains(text(), 'Choose')]");
		chooseOwnerCancelButtonFinder = xpath("//INPUT[@id='cancelOrgSelect']");
		chooseOwnerSelectButtonFinder = xpath("//INPUT[@id='selectOrg']");
		chooseOwnerOrganizationSelectFinder = xpath("//SELECT[@id='orgList']");
		chooseOwnerCustomerSelectFinder = xpath("//SELECT[@id='customerList']");
		chooseOwnerDivisionSelectFinder = xpath("//SELECT[@id='divisionList']");
	}
	
	/**
	 * Creates a PNG format screen capture of the current Window. This is useful
	 * for saving the window image of IE during testing. It is recommended to
	 * the getCaptureName method. This will generate a filename based on date
	 * and tenant. Therefore on multiple runs or multiple captures for one
	 * tenant you will not overwrite existing images.
	 * 
	 * @param filename
	 *            - the filename the PNG image will be saved to
	 * @param ie
	 *            - a reference to the Internet Explorer
	 * @throws Exception
	 */
	public void myWindowCapture(String filename) throws Exception {
		assertNotNull(filename);
		myWindowCapture(filename, "png");
	}

	/**
	 * Creates a window capture and saves it to a file. The format parameter
	 * must be one of the supported formats for the ImageIO class. To get a list
	 * of the supported formats, look at the String[] returned by
	 * ImageIO.getReaderFormatNames().
	 * 
	 * Currently supported formats are:
	 * 
	 * BMP bmp jpeg wbmp gif png JPG jpg JPEG WBMP
	 * 
	 * @param filename - the filename the image will be saved to
	 * @param format - one of bmp, gif, jpeg, jpg, png, wbmp (case-sensitive)
	 * @throws Exception
	 */
	public void myWindowCapture(String filename, String format) throws Exception {
		assertNotNull(filename);
		assertFalse(filename.equals(""));
		assertNotNull(format);
		assertFalse(format.equals(""));
//		ie.waitUntilReady();
		int x = ie.top();
		int y = ie.left();
		int h = ie.height();
		int w = ie.width();
		Rectangle r = new Rectangle(x, y, w, h);
		BufferedImage image = (new Robot()).createScreenCapture(r);
		// If there is a directory path, make sure it exists
		int endIndex = filename.lastIndexOf('/');
		if (endIndex != -1) {
			String dirname = filename.substring(0, endIndex);
			File dir = new File(dirname);
			if (!dir.exists())
				dir.mkdirs();
		}
		ImageIO.write(image, format, new File(filename));
	}

	/**
	 * Fills in the Owner dialog. Assumes you have clicked a link to open
	 * the dialog already. After setting the dialog you want to call either
	 * selectOwner() or cancelOwner().
	 * 
	 * @param o
	 * @throws Exception
	 */
	public void setOwner(Owner o) throws Exception {
		stopMonitor();
		assertNotNull(o);
		assertNotNull(o.getOrganization());
		
		SelectList org = getOrganizationSelectListFromChooseOwner();
		String s = "/" + o.getOrganization() + "/";
		Option o2 = org.option(text(s));
		assertTrue("Could not find an Option matching '" + s + "'", o2.exists());
		o2.select();
		waitForJavascript();
		
		SelectList customer = getCustomerSelectListFromChooseOwner();
		if(o.getCustomer() != null) {
			String c = "/^" + o.getCustomer() + " /";
			customer.option(text(c)).select();
			waitForJavascript();
		}
		
		SelectList division = getDivisionSelectListFromChooseOwner();
		if(o.getDivision() != null) {
			String d = "/^" + o.getDivision() + "/";
			division.option(text(d)).select();
			waitForJavascript();
		}
		startMonitor();
	}
	
	public SelectList getOrganizationSelectListFromChooseOwner() throws Exception {
		SelectList org = ie.selectList(chooseOwnerOrganizationSelectFinder);
		assertTrue("Could not find the select list for Organization", org.exists());
		return org;
	}
	
	public SelectList getCustomerSelectListFromChooseOwner() throws Exception {
		SelectList customer = ie.selectList(chooseOwnerCustomerSelectFinder);
		assertTrue("Could not find the select list for Customer", customer.exists());
		return customer;
	}
	
	public SelectList getDivisionSelectListFromChooseOwner() throws Exception {
		SelectList division = ie.selectList(chooseOwnerDivisionSelectFinder);
		assertTrue("Could not find the select list for Division", division.exists());
		return division;
	}
	
	public void selectOwner() throws Exception {
		Button select = ie.button(chooseOwnerSelectButtonFinder);
		assertTrue("Could not find the Select button", select.exists());
		select.click();
		checkForErrorMessagesOnCurrentPage();
	}

	public void cancelOwner() throws Exception {
		Button cancel = ie.button(chooseOwnerCancelButtonFinder);
		assertTrue("Could not find the Cancel button", cancel.exists());
		cancel.click();
		checkForErrorMessagesOnCurrentPage();
	}

	public void gotoChooseOwner() throws Exception {
		Link choose = ie.link(addProductChooseOwnerLinkFinder);
		assertTrue("Could not find the link to choose owner during asset creation", choose.exists());
		choose.click();
		waitForJavascript();
		this.checkOwnerSelectorDialog();
	}

	private void checkOwnerSelectorDialog() throws Exception {
		Div orgSelector = ie.div(chooseOwnerDialogFinder);
		assertTrue("Could not find the choose owner dialog", orgSelector.exists());
		assertFalse("The choose owner dialog is not visible", !orgSelector.html().toUpperCase().contains("DISPLAY: NONE"));
	}

	/**
	 * Create a directory with a timestamp of the current time. The format of
	 * the dierctory will be yyyyMMdd-HHmmss, where y = year, M = month, d = day
	 * of month, H = 24 hour, m = minutes and s = seconds. If the method is
	 * successful it will return the directory it created. If it fails it will
	 * return null.
	 * 
	 * @return name of the directory which was created or null if it failed.
	 */
	public String createTimestampDirectory() {
		String timestamp = null;
		SimpleDateFormat now = new SimpleDateFormat("yyyyMMdd-HHmmss");
		timestamp = now.format(new Date());
		File d = new File(timestamp);
		if (d.mkdirs())
			return timestamp;
		return null;
	}

	/*
	 * There are startMonitorStatus and stopMonitorStatus methods. The
	 * startMonitorStatus will check the status bar of IE once a second.
	 * If the status bar does not change after a period of time (max)
	 * we assume IE is hung and send a refresh signal. If the period of
	 * time gets reset, i.e. the status bar changes, we record the reset.
	 * If the page appears to be hung, we refresh the page and reset the
	 * counter.
	 */
	static long refreshes = 1;
	static long resets = 1;
	static final int TIMEOUT = 180;	// number of seconds before Watij times out 
	private static final int dialogCheck = 5;	// number of times to loop checking for a dialog
	public static boolean running = false;
	
	/**
	 * For various reasons, e.g. transparent PNGs cause pages to occasionally
	 * fail to load in Internet Explorer, you need to run this method once the
	 * IE window is open. It will monitor the status bar of the window. Every
	 * second the thread will wake up and see what the status bar is displaying.
	 * If it has not changed a counter is incremented. Once the counter reaches
	 * a maximum value, we assume IE is stuck and needs to be refreshed. So we
	 * refresh the page and start waiting again. Every time the page status
	 * changes, we reset the counter.
	 * 
	 * Any time you do an ie.start(), ie.attach(), ie.navigate(), ie.goTo()
	 * you should call this method and start a monitoring thread for that
	 * instance of IE. When you are going to quit IE, call the quitMonitor
	 * method to kill the thread. 
	 * 
	 */

	/**
	 * Whenever you start IE, use this method instead of calling the ie.start()
	 * method directly. It will start a thread to monitor the status of IE.
	 * If the status does not change within a period of time it will refresh
	 * the page in hopes of restoring it.
	 * 
	 * @param ie
	 * @param loginURL
	 * @throws Exception
	 */
	public void start(String loginURL) throws Exception {
		assertNotNull(loginURL);
		assertFalse(loginURL.equals(""));
		ie.start(loginURL);
	}

	/**
	 * See the start() with two parameters.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void start() throws Exception {
		ie.start();
	}
	
	/**
	 * The ie.waitUntilReady() method waits for the page to finished loading
	 * but does not wait for scripts to finish executing. This method will
	 * handle waiting for the scripts to finish executing. 
	 * 
	 * Currently, it just waits for a few seconds. In the future we can look
	 * into something better.
	 * 
	 * @throws Exception
	 */
	public void waitForJavascript() throws Exception {
		Thread.sleep(hack);
	}

	/**
	 * For some reason the lightbox is taking a long time to come up.
	 * 
	 * @throws Exception
	 */
	public void waitForLightBox() throws Exception {
		int count = 0;
		while(count++ < lightBoxHackLoops) {
			Thread.sleep(1000);	// one second
			HtmlElement b = ie.htmlElement(lightBoxOKButtonFinder);
			if(b.exists()) {
				break;
			}
		}
	}

	/**
	 * Returns a random positive long value as a string.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRandomString() throws Exception {
		String s = null;
		s = new String(Long.toString(Math.abs(r.nextLong())));
		return s;
	}
	
	/**
	 * Returns a hexidecimal string which can be used as an RFID number.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRandomRFID() throws Exception {
		String s = null;
		s = new String(Long.toHexString(r.nextLong()));
		return s.toUpperCase();
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
	 * This method will use the getErrorMessagesOnCurrentPage method
	 * to get all the error messages on the current page. If the
	 * String is not an empty string, i.e. "", then this will throw
	 * an Exception.
	 * 
	 * @throws Exception
	 */
	public void checkForErrorMessagesOnCurrentPage() throws Exception {
		String errors = getErrorMessagesOnCurrentPage();
		assertTrue("Error on the current page:\n" + errors, errors.equals(""));
	}
	
	/**
	 * Currently, the convention is to print error messages into
	 * a DIV at the top of each page. If the DIV exists, we parse
	 * it for SPANs. Each SPAN is a line in the error message.
	 * This will return all the lines concatenated into one long
	 * String.
	 * 
	 * @throws Exception
	 */
	public String getErrorMessagesOnCurrentPage() throws Exception {
		Finder errorMessageFinder = xpath("//DIV[@id='formErrors']/DIV/UL/LI/SPAN[@class='errorMessage']");
		Spans errorMessages = ie.spans(errorMessageFinder);
		String errors = "";
		Iterator<Span> i = errorMessages.iterator();
		while(i.hasNext()) {
			Span s = i.next();
			errors += s.text();
			errors += "\n";
		}
		return errors;
	}
	
	/**
	 * Looks for the blue notification messages at the top of the page.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getNotificationsOnCurrentPage() throws Exception {
		String result = "";
		Finder notificationMessagesFinder = xpath("//DIV[@id='notifications']/DIV[@='messages']/UL/LI/SPAN");
		Spans notificationMessages = ie.spans(notificationMessagesFinder);
		Iterator<Span> i = notificationMessages.iterator();
		while(i.hasNext()) {
			Span s = i.next();
			result += s.text();
			result += "\n";
		}
		return result;
	}
	
	/**
	 * Any time a click pops open an Are You Sure?  dialog you
	 * want to start this running before you do the click. By
	 * calling this, a thread will be running in the background
	 * waiting for the dialog to appear. So when you click the
	 * button/link/etc. this will close the dialog.
	 * 
	 * @throws Exception
	 */
	public void createThreadToCloseAreYouSureDialog() throws Exception {
		// set up a thread to answer the "Are you sure?" dialog
		new Thread(new Runnable() {
			public void run() {
				try {
					Wnd w = getWindowHandle();
					IEPromptDialog confirm = new IEPromptDialog(w, ie);
					confirm.ok();
				} catch (Exception e) {
				}
			}
		}).start();
	}
	
	public void forcefullyKillInternetExplorer() throws Exception {
		String imageName = "iexplore.exe";
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
	
	public void createThreadToConstantlyMonitorForModalDialogs() throws Exception {
		new Thread(new Runnable() {
			public void run() {
				try {
					FieldIDMisc.enabled = true;
					while(FieldIDMisc.enabled) {
						Thread.sleep(1000);
						Wnd w = Wnd.findWindow("#32770");
						IEPromptDialog confirm = new IEPromptDialog(w, ie);
						if(confirm.exists()) {
							confirm.ok();
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}
	
	public void killThreadToConstantlyMonitorForModalDialogs() throws Exception {
		FieldIDMisc.enabled = false;
	}
	
	/**
	 * Checks to see if a modal dialog has been opened.
	 * Will return true if the dialog exists. Otherwise
	 * returns false. Does not close the dialog.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean isDialog() throws Exception {
		boolean result = false;
		try {
			Wnd w = getWindowHandle();
			IEPromptDialog confirm = new IEPromptDialog(w, ie);
			result = confirm.exists();
			confirm.text();
		} catch (Exception e) {
		}
		return result;
	}
	
	public Wnd getWindowHandle() throws Exception {
		int n = 0;
		Wnd w = null;
		while(dialogCheck > n++ && w == null) {
			Thread.sleep(1000);
			w = Wnd.findWindow("#32770");
		}
		return w;
	}
	
	/**
	 * Gets the text in the body of a modal dialog.
	 * Does not close the dialog.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDialogText() throws Exception {
		String result = null;
		try {
			Wnd w = getWindowHandle();
			IEPromptDialog confirm = new IEPromptDialog(w, ie);
			result = confirm.text();
		} catch (Exception e) {
		}
		return result;
	}
	
	/**
	 * Clicks the OK button on a modal dialog.
	 * 
	 * @throws Exception
	 */
	public void okDialog() throws Exception {
		try {
			Wnd w = getWindowHandle();
			IEPromptDialog confirm = new IEPromptDialog(w, ie);
			confirm.ok();
		} catch (Exception e) {
		}
	}
	
	/**
	 * Clicks the Cancel button on a modal dialog.
	 * 
	 * @throws Exception
	 */
	public void cancelDialog() throws Exception {
		try {
			Wnd w = getWindowHandle();
			IEPromptDialog confirm = new IEPromptDialog(w, ie);
			confirm.cancel();
		} catch (Exception e) {
		}
	}
	
	/**
	 * The convention in HTML is that a LABEL tag's FOR attribute 
	 * will hold the ID attribute for the associated checkbox.
	 * This method will find the LABEL and use the FOR attribute
	 * to find the checkbox and set it according to the boolean b.
	 * 
	 * @param labelText
	 * @param b
	 * @throws Exception
	 */
	public void setCheckboxViaLabel(String labelText, boolean b) throws Exception {
		Label l = ie.label(text(labelText));
		assertTrue("Could not find a label with the text '" + labelText + "'", l.exists());
		String id = l.htmlFor();
		Checkbox c = ie.checkbox(id(id));
		assertTrue("Could not find a checkbox associated with the label '" + labelText + "'", c.exists());
		c.set(b);
	}
	
	/**
	 * Using the same convention described in setCheckboxViaLabel
	 * this method will find the state of a checkbox based on the
	 * label associated with the checkbox.
	 *  
	 * @param labelText
	 * @return
	 * @throws Exception
	 */
	public boolean getCheckboxViaLabel(String labelText) throws Exception {
		boolean result = false;
		Label l = ie.label(text(labelText));
		assertTrue("Could not find a label with the text '" + labelText + "'", l.exists());
		String id = l.htmlFor();
		Checkbox c = ie.checkbox(id(id));
		assertTrue("Could not find a checkbox associated with the label '" + labelText + "'", c.exists());
		result = c.getState();
		return result;
	}

	public void clickLightboxOKbutton(String message) throws Exception {
		waitForLightBox();
		HtmlElement b = ie.htmlElement(lightBoxOKButtonFinder);
		assertTrue("Could not find the OK button of the lightbox", b.exists());
		HtmlElement header = ie.htmlElement(lightBoxMessageHeaderFinder);
		assertTrue("Could not find the 'Messages' header at the top of the lightbox", header.exists());
		HtmlElement p = ie.htmlElement(lightBoxMessageFinder);
		assertTrue("Could not find a message area in the lightbox", p.exists());
		assertTrue("The message in the lightbox was not '" + message + "'", p.text().contains(message));
		b.click();
		waitForJavascript();
	}

	public boolean checkForHidden(String html) throws Exception {
		boolean result = html.toLowerCase().contains("display: none");
		return result;
	}

	public void logout() throws Exception {
		Link logout = ie.link(text("/Sign Out/"));
		assertTrue("Could not find the logout link", logout.exists());
		logout.click();
	}

	public String getDateString() {
		String today = null;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		today = now.format(new Date());
		return today;
	}

	public String getRandomString(int length) throws Exception {
		assertTrue("Length must be a positive number", length > 0);
		String s = this.getRandomString();
		if(length > s.length())
			length = s.length() - 1;
		return s.substring(0, length);
	}
	
	private Divs errorMessageClass() throws Exception {
		Divs errors = ie.divs(errorMessageFinder);
		assertNotNull(errors);
		return errors;
	}
	
	public boolean checkForErrorMessagesOnAForm() throws Exception {
		boolean result = false;
		Divs errors = errorMessageClass();
		assertNotNull(errors);
		if(errors.length() != 0) {
			result = true;
		}
		
		return result;
	}
	
	public String getErrorMessagesOnAForm() throws Exception {
		String messages = "";
		Divs errors = errorMessageClass();
		Iterator<Div> i = errors.iterator();
		while(i.hasNext()) {
			Div error = i.next();
			assertTrue("Could not find the Div with an error message", error.exists());
			messages += error.text();
			messages += "\n";
		}
		
		return messages;
	}

	public void gotoBackToAdministration() throws Exception {
		Link back = ie.link(backToAdministationLinkFinder);
		assertTrue("Could not find a link to go back to administration", back.exists());
		back.click();
		Admin admin = new Admin(ie);
		admin.checkAdminPageContentHeader();
	}

	public String getSuccessMessageOnCurrentPage() throws Exception {
		String s = "";
		Span actionMessage = ie.span(successMessageFinder);
		if(actionMessage.exists()) {
			s = actionMessage.text().trim();
		}
		return s;
	}

	public String getDateStringLastYear() {
		String lastYear = null;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		lastYear = now.format(c.getTime());
		return lastYear;
	}

	public String getDateStringBackNMonths(int n) {
		String lastYear = null;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1 * n);
		lastYear = now.format(c.getTime());
		return lastYear;
	}

	public String getDateStringLastMonth() {
		String lastYear = null;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		lastYear = now.format(c.getTime());
		return lastYear;
	}

	public String getDateStringNextYear() {
		String lastYear = null;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 1);
		lastYear = now.format(c.getTime());
		return lastYear;
	}

	public String getDateStringNextMonth() {
		String lastYear = null;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		lastYear = now.format(c.getTime());
		return lastYear;
	}

	public String getContentTitle() throws Exception {
		HtmlElement he = ie.htmlElement(contentTitleFinder);
		assertTrue("Could not find the content title element", he.exists());
		String result = he.text().trim();
		return result;
	}

	public int getSmartSearchResultCount() throws Exception {
		String contentTitle = getContentTitle();
		if(contentTitle.contains("Asset - "))
			return 1;
		Buttons b = ie.buttons(assetsViewAllFinder);
		return b.length();
	}

	public void waitForLoading() throws Exception {
		boolean waiting = true;
		waitForJavascript();
		while(waiting) {
			Div loading = ie.div(loadingIndicatorDivFinder);
			assertTrue("Could not find the parent DIV of the loading image", loading.exists());
			String html = loading.html();
			int i =  html.indexOf(">");
			String tag = html.substring(0, i).toLowerCase();
			waiting = !tag.contains("display: none");
			Thread.sleep(250);	// wait a 1/4 second between checks.
		}
	}

	public String getDateStringWithTime() {
		String today = null;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy h:mm aa");
		today = now.format(new Date());
		return today;
	}

	public int getNumberOfPages() throws Exception {
		Label lastPage = ie.label(labelForOfXPagesFinder);
		int n = 1;
		if(lastPage.exists()) {
			String s = lastPage.text().trim();
			s = s.replace("of ", "");
			s = s.replace(" pages", "");
			n = Integer.parseInt(s);
		}
		return n;
	}

	public void gotoNextPage() throws Exception {
		Link next = ie.link(nextPageLinkFinder);
		assertTrue("Could not find the 'Next>' link", next.exists());
		next.click();
		ie.waitUntilReady();
		checkForErrorMessagesOnCurrentPage();
	}

	public boolean isLastPage() throws Exception {
		Link next = ie.link(nextPageLinkFinder);
		return !next.exists();
	}

	public void gotoFirstPage() throws Exception {
		Link first = ie.link(firstPageLinkFinder);
		assertTrue("Could not find the 'First' link", first.exists());
		first.click();
		ie.waitUntilReady();
		checkForErrorMessagesOnCurrentPage();
	}

	public boolean isFirstPage() throws Exception {
		Link first = ie.link(firstPageLinkFinder);
		return !first.exists();
	}

	public void checkForOopsPage(IE ie) throws Exception {
		String title = ie.title();
		String badTitle = "Oops!";
		assertFalse("Title of the page is '" + badTitle + "', something bad must have happened.", title.contains(badTitle));
		String tellUsWhatHappened = "Tell us what happened";
		Link helpServe = ie.link(tellUsWhatHappenedLinkFinder);
		assertFalse("Link to '" + tellUsWhatHappened + "' exists.", helpServe.exists());
	}

	public static void stopMonitor() {
		if(monitor != null) {
			monitor.disable();
		}
	}

	public static void startMonitor() {
		if(monitor != null) {
			monitor.enable();
		}
	}

	public static void initMonitor() {
		if(monitor != null && !monitor.isRunning()) {
			monitor.start();
		}
	}
	
	public static void quitMonitor() {
		if(monitor != null) {
			monitor.quit();
			monitor = null;
		}
	}

	public List<String> getOrganizations() throws Exception {
		List<String> results = new ArrayList<String>();
		SelectList orgs = getOrganizationSelectListFromChooseOwner();
		Iterator<Option> i = orgs.options().iterator();
		while(i.hasNext()) {
			Option o = i.next();
			results.add(o.text());
		}
		return results;
	}

	public Owner getOwner() throws Exception {
		Owner o = new Owner(getOrganizations().get(0));
		List<String> customers = getCustomerSelectListFromChooseOwner().getSelectedItems();
		String customer = null;
		if(customers.size() > 0) {
			customer = customers.get(0);
		}
		o.setCustomer(customer);
		List<String> divisions = getDivisionSelectListFromChooseOwner().getSelectedItems();
		String division = null;
		if(divisions.size() > 0) {
			division = divisions.get(0);
		}
		o.setDivision(division);

		return o;
	}

	public void clickLightboxCloseThisMessageButton() throws Exception {
		waitForLightBox();
		Button b = ie.button(text("Close this message"));
		assertTrue("Could not find the button to close the lightbox", b.exists());
		b.click();
	}
}

package com.n4systems.fieldid.testcase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_690 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	static Random r = new Random();
	static int n = Math.abs(r.nextInt());
	List<String> prodTypes = null;
	int index = -1;
	String poNumber = "123456";
	String productType = null;
	String[] productParameters = { "" };
	String serialNumber = null;
	List<String> inspectTypes = null;
	String inspectionType = null;
	boolean master = false;
	String duration = "31";
	int dur = Integer.parseInt(duration);
	String startDate = "01/01/08 10:00 AM";
	String estimateDate = "12/31/20 10:00 PM";
	static int many = 30;
	List<String> manyDates = new ArrayList<String>();

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
			String user = "n4systems";
			String password = "Xk43g8!@";
			helper.loginJobsTenant(ie, user, password);
			prodTypes = helper.getProductTypes(ie);
			index = r.nextInt(prodTypes.size());
			productType = prodTypes.get(index);
			String[] productParameters = { "" };
			serialNumber = helper.addProduct(ie, null, true, null, poNumber, null, null, null, null, null, null, null, null, null, productType, productParameters, null, null, "Save");
			
			inspectTypes = helper.getInspectionTypes(ie);
			index = r.nextInt(inspectTypes.size());
			inspectionType = inspectTypes.get(index);
			master = helper.isInspectionTypeMaster(ie, inspectionType);
			helper.addInspectionTypeToProductType(ie, inspectionType, productType);

			startDate = generateDate(0);		// MM/dd/yy hh:mm aa
			estimateDate = generateDate(dur);	// MM/dd/yy hh:mm aa
			helper.addInspectionSchedule(ie, serialNumber, inspectionType, startDate.split(" ")[0], null);
			helper.addInspectionSchedule(ie, serialNumber, inspectionType, estimateDate.split(" ")[0], null);

			// Generate 'many' dates after the two dates above
			for(int i = 0, count = 1; i < many; i++, count++) {
				String d = generateDate(count*14+dur);
				manyDates.add(d);
			}

			ie.close();
		}
	}
	
	private String generateDate(int daysFromNow) throws Exception {
		long dayInMilliseconds = 24 * 60 * 60 * 1000;
		long milliseconds = daysFromNow * dayInMilliseconds;
		long date = System.currentTimeMillis() + milliseconds;
		SimpleDateFormat now = new SimpleDateFormat("MM/dd/yy hh:mm aa");
		String s = now.format(new Date(date));
		return s;
	}
	
	public void testAssigningScheduleToEventJob() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "Xk43g8!@";
			helper.loginJobsTenant(ie, user, password);
			String typeOfJob = "Event";
			String jobID = "jid-" + n;
			String title = "title-" + n;
			String workPerformed = "Nothing.";
			helper.addJob(ie, typeOfJob, jobID, title, null, null, null, true, null, startDate, estimateDate, null, duration, poNumber, workPerformed);
			helper.addEventToEventJob(ie, title, null, serialNumber, null, null, null, productType, null, startDate, estimateDate, -1);

			// Validate
			String nextScheduledDate = startDate.split(" ")[0];	// drop the hh:mm PM from string
			if(!helper.isScheduleAssignedToJob(ie, title, serialNumber, inspectionType, nextScheduledDate)) {
				throw new TestCaseFailedException();
			}
			helper.myWindowCapture(timestamp + "/" + method + ".png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCannotAssignEventToTwoJobs() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "Xk43g8!@";
			helper.loginJobsTenant(ie, user, password);
			String typeOfJob = "Event";
			String jobID1 = "job1-" + n;
			String title1 = "title1-" + n;
			String workPerformed1 = "Something.";
			helper.addJob(ie, typeOfJob, jobID1, title1, null, null, null, true, null, startDate, estimateDate, null, duration, poNumber, workPerformed1);
			helper.addEventToEventJob(ie, title1, null, serialNumber, null, null, null, productType, null, estimateDate, estimateDate, -1);

			String jobID2 = "job2-" + n;
			String title2 = "title2-" + n;
			String workPerformed2 = "Everything.";
			helper.addJob(ie, typeOfJob, jobID2, title2, null, null, null, true, null, startDate, estimateDate, null, duration, poNumber, workPerformed2);
			helper.addEventToEventJob(ie, title2, null, serialNumber, null, null, null, productType, null, estimateDate, estimateDate, -1);
			
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testAddManyEventsToOneJob() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "Xk43g8!@";
			helper.loginJobsTenant(ie, user, password);
			String typeOfJob = "Event";
			String jobID = "many-" + n;
			String title = "many-" + n;
			String workPerformed = "Many events added to this job.";
			Iterator<String> i = manyDates.iterator();
			while(i.hasNext()) {
				String d = i.next().split(" ")[0];	// drop the hh:mm PM from string
				helper.addInspectionSchedule(ie, serialNumber, inspectionType, d.split(" ")[0], null);
			}
			helper.addJob(ie, typeOfJob, jobID, title, null, null, null, true, null, startDate, estimateDate, null, duration, poNumber, workPerformed);
			helper.addEventToEventJob(ie, title, null, serialNumber, null, null, null, productType, null, manyDates.get(0), manyDates.get(many-1), -1);
			i = manyDates.iterator();
			while(i.hasNext()) {
				String nextScheduledDate = i.next().split(" ")[0];	// drop the hh:mm PM from string
				if(!helper.isScheduleAssignedToJob(ie, title, serialNumber, inspectionType, nextScheduledDate)) {
					throw new TestCaseFailedException();
				}
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testCanEditAnEventAndChangeTheJob() throws Exception {
		String method = helper.getMethodName();

		try {
			String user = "n4systems";
			String password = "Xk43g8!@";
			helper.loginJobsTenant(ie, user, password);
			helper.gotoJobs(ie);
			String oldJobID = "oji" + n;
			String oldJobTitle = "ojt" + n;
			String newJobID = "nji" + n;
			String newJobTitle = "njt" + n;
			List<String> jobs = helper.getEventJobs(ie);
			if(jobs.size() < 1) {
				helper.addJob(ie, "Event", oldJobID, oldJobTitle, null, null, null, true, null, startDate, estimateDate, duration, null, null, null);
			}
			else {
				oldJobTitle = jobs.get(0);
			}
			if(jobs.size() < 2) {
				helper.addJob(ie, "Event", newJobID, newJobTitle, null, null, null, true, null, startDate, estimateDate, duration, null, null, null);
			}
			else {
				newJobTitle = jobs.get(1);
			}
			helper.addInspectionSchedule(ie, serialNumber, inspectionType, startDate, null);
			helper.editJobForInspectionSchedule(ie, serialNumber, inspectionType, startDate.split(" ")[0], newJobTitle);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}

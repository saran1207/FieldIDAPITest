package com.n4systems.taskscheduling.task;

import java.io.File;


import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.CertificatePrinter;
import com.n4systems.reporting.EventSummaryGenerator;
import com.n4systems.reporting.ReportDefiner;

public class PrintEventSummaryReportTask extends DownloadTask {
	private final EventSummaryGenerator reportGen;
	
	private ReportDefiner reportDefiner;

	public PrintEventSummaryReportTask(DownloadLink downloadLink, String downloadUrl, EventSummaryGenerator reportGen) {
		super(downloadLink, downloadUrl, "eventSummaryCert");
		this.reportGen = reportGen;
	}
	
	public PrintEventSummaryReportTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, new EventSummaryGenerator(new DateTimeDefiner(downloadLink.getUser())));
	}

	@Override
	protected void generateFile(File downloadFile, User user, String downloadName) throws Exception {
		new CertificatePrinter().printToPDF(reportGen.generate(reportDefiner, user), downloadFile);			
	}

	public void setReportDefiner(ReportDefiner reportDefiner) {
		this.reportDefiner = reportDefiner;
	}
}

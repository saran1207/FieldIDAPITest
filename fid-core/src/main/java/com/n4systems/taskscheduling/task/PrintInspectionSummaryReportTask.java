package com.n4systems.taskscheduling.task;

import java.io.File;


import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.CertificatePrinter;
import com.n4systems.reporting.InspectionSummaryGenerator;
import com.n4systems.reporting.ReportDefiner;

public class PrintInspectionSummaryReportTask extends DownloadTask {
	private final InspectionSummaryGenerator reportGen;
	
	private ReportDefiner reportDefiner;

	public PrintInspectionSummaryReportTask(DownloadLink downloadLink, String downloadUrl, InspectionSummaryGenerator reportGen) {
		super(downloadLink, downloadUrl, "eventSummaryCert");
		this.reportGen = reportGen;
	}
	
	public PrintInspectionSummaryReportTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, new InspectionSummaryGenerator(new DateTimeDefiner(downloadLink.getUser())));
	}

	@Override
	protected void generateFile(File downloadFile, User user, String downloadName) throws Exception {
		new CertificatePrinter().printToPDF(reportGen.generate(reportDefiner, user), downloadFile);			
	}

	public void setReportDefiner(ReportDefiner reportDefiner) {
		this.reportDefiner = reportDefiner;
	}
}

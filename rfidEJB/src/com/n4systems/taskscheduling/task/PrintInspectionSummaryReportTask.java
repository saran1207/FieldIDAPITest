package com.n4systems.taskscheduling.task;

import java.io.File;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.reporting.CertificatePrinter;
import com.n4systems.reporting.InspectionSummaryGenerator;
import com.n4systems.reporting.ReportDefiner;

public class PrintInspectionSummaryReportTask extends DownloadTask {
	private final InspectionSummaryGenerator reportGen;
	
	private ReportDefiner reportDefiner;

	public PrintInspectionSummaryReportTask(DownloadLink downloadLink, String downloadUrl, InspectionSummaryGenerator reportGen) {
		super(downloadLink, downloadUrl, "inspectionSummaryCert");
		this.reportGen = reportGen;
	}
	
	public PrintInspectionSummaryReportTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, new InspectionSummaryGenerator());
	}

	@Override
	protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
		CertificatePrinter.printToPDF(reportGen.generate(reportDefiner, user), downloadFile);			
	}

	public void setReportDefiner(ReportDefiner reportDefiner) {
		this.reportDefiner = reportDefiner;
	}
}

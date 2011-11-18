package com.n4systems.taskscheduling.task;

import java.io.File;
import java.util.List;


import com.n4systems.fieldid.service.certificate.CertificatePrinter;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.EventSummaryGenerator;
import com.n4systems.reporting.ReportDefiner;

public class PrintEventSummaryReportTask extends DownloadTask {
	private final EventSummaryGenerator reportGen;
	
	private ReportDefiner reportDefiner;
    private List<Long> eventIds;

	public PrintEventSummaryReportTask(DownloadLink downloadLink, String downloadUrl, EventSummaryGenerator reportGen) {
		super(downloadLink, downloadUrl, "eventSummaryCert");
		this.reportGen = reportGen;
	}
	
	public PrintEventSummaryReportTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, new EventSummaryGenerator(new DateTimeDefiner(downloadLink.getUser())));
	}

	@Override
	protected void generateFile(File downloadFile, User user, String downloadName) throws Exception {
		new CertificatePrinter().printToPDF(reportGen.generate(reportDefiner, eventIds, user), downloadFile);
	}

	public void setReportDefiner(ReportDefiner reportDefiner) {
		this.reportDefiner = reportDefiner;
	}

	public void setEventIds(List<Long> eventIds) {
		this.eventIds = eventIds;
	}
}

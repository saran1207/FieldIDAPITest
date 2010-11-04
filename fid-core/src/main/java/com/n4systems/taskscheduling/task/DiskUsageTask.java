package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.usage.TenantDiskUsageCalculator;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.persistence.QueryBuilder;

public class DiskUsageTask extends ScheduledTask {

	public DiskUsageTask() {
		super(60 * 60, TimeUnit.SECONDS);
	}

	@Override
	protected void runTask() throws Exception {
		List<TenantDiskUsageCalculator> summaries = new ArrayList<TenantDiskUsageCalculator>();
		
		for (Tenant tenant: getTenants()) {
			summaries.add(new TenantDiskUsageCalculator(tenant));
		}
		
		emailSummary(summaries);
	}

	private List<Tenant> getTenants() {
		PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
		List<Tenant> tenants = persistenceManager.findAll(new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter()).addOrder("name"));
		return tenants;
	}

	private File createSummaryFileOutput(List<TenantDiskUsageCalculator> summaries) throws IOException {
		String csv = createCsvContent(summaries);
		return saveFile(csv); 
	}

	private String createCsvContent(List<TenantDiskUsageCalculator> summaries) {
		String csv = "Tenant, tenant file size, tenant file count, Event Attachment file size,	Event Attachment file count," +
						"Event Chart Image file size, Event Chart Image file count, Event Prooftest File file size, Event Prooftest File file count," + 
						"Asset Attachment file size, Asset Attachment file count, Asset Type Image file size,	Asset Type Image file count," +
						"Asset Type Attachment file size, Asset Type Attachment file count, Job Note file size, Job Note file count, User file size," +
						"User file count\n";
		for (TenantDiskUsageCalculator summary : summaries) {
			csv += summary.getTenant().getName() + ",";
			csv += summary.totalSpaceUsed() + ",";
			csv += summary.totalFiles() + ",";
			csv += summary.getEventAttachmentUsage().getTotalSize() + ",";
			csv += summary.getEventAttachmentUsage().getNumberOfFiles() + ",";
			csv += summary.getEventChartImageUsage().getTotalSize() + ",";
			csv += summary.getEventChartImageUsage().getNumberOfFiles() + ",";
			csv += summary.getEventProoftestFileUsage().getTotalSize() + ",";
			csv += summary.getEventProoftestFileUsage().getNumberOfFiles() + ",";
			csv += summary.getProductAttachmentUsage().getTotalSize() + ",";
			csv += summary.getProductAttachmentUsage().getNumberOfFiles() + ",";
			csv += summary.getProductTypeImageUsage().getTotalSize() + ",";
			csv += summary.getProductTypeImageUsage().getNumberOfFiles() + ",";
			csv += summary.getProductTypeAttachmentUsage().getTotalSize() + ",";
			csv += summary.getProductTypeAttachmentUsage().getNumberOfFiles() + ",";
			csv += summary.getJobNoteUsage().getTotalSize() + ",";
			csv += summary.getJobNoteUsage().getNumberOfFiles() + ",";
			csv += summary.getUserUsage().getTotalSize() + ",";
			csv += summary.getUserUsage().getNumberOfFiles() + "\n";
			
		}
		return csv;
	}

	private File saveFile(String csv) throws IOException {
		File tmp = PathHandler.getTempFile("disk_usage.csv");
		FileUtils.writeStringToFile(tmp, csv);
		return tmp;
	}

	private void emailSummary(List<TenantDiskUsageCalculator> summaries) {
		MailMessage message = createMessage(summaries);
		
		try {
			ServiceLocator.getMailManager().sendMessage(message);
		} catch (MessagingException e) {
			System.out.print(e.getMessage());
		}
	}

	private MailMessage createMessage(List<TenantDiskUsageCalculator> summaries) {
		String subject = "Disk Usage for " + DateHelper.date2String("yyyy-MM-dd", new Date());
		String body = "<h1>Disk Usage Attached</h1>";
		
		MailMessage message = null;
		try {
			File summaryFileOutput = createSummaryFileOutput(summaries);
			message = new MailMessage(subject, body, ConfigContext.getCurrentContext().getString(ConfigEntry.FIELDID_ADMINISTRATOR_EMAIL));
			message.getAttachments().put(summaryFileOutput.getName(), getFileData(summaryFileOutput));
		} catch (IOException e) {}

		
		return message;
	}
	
	private byte[] getFileData(File summaryFileOutput) throws IOException {
		byte[] data = null;
		InputStream in = null;
		try {
			data = IOUtils.toByteArray(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return data;
	}

}

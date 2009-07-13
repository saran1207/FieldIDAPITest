package com.n4systems.usage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.n4systems.model.TenantOrganization;
import com.n4systems.reporting.PathHandler;

public class DiskUsageCalculator {

	
	private List<TenantDiskUsageSummary> usageSummaries = new ArrayList<TenantDiskUsageSummary>();
	private TenantDiskUsageSummary currentDiskUsageSummary;
	
	public DiskUsageCalculator(List<TenantOrganization> tenants) {
		super();
		for (TenantOrganization tenantOrganization : tenants) {
			usageSummaries.add(new TenantDiskUsageSummary(tenantOrganization));
		}
	}
	
	
	public List<TenantDiskUsageSummary> calculateUsages() {
		for (TenantDiskUsageSummary diskUsageSummary : usageSummaries) {
			calculateUsage(diskUsageSummary);			
		}
		return usageSummaries;
	}


	private void calculateUsage(TenantDiskUsageSummary diskUsageSummary) {
		currentDiskUsageSummary = diskUsageSummary;
		calculateInspectionAttachmentUsage();
		calculateInspectionChartImageUsage();
		calculateInspectionProoftestFileUsage(); 
		calculateProductAttachmentUsage();
		calculateProductTypeImageUsage();
		calculateProductTypeAttachmentUsage();
		calculateJobNoteUsage();
		calculateUserUsage();
	}


	private void calculateUserUsage() {
		File userFileDirectory = PathHandler.getAbsoluteUserBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setUserUsage(calcuateSingleDiskUsage(userFileDirectory));
	}

	private void calculateJobNoteUsage() {
		File jobNoteDirectory = PathHandler.getJobAttachmentFileBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setJobNoteUsage(calcuateSingleDiskUsage(jobNoteDirectory));
	}


	

	private void calculateProductTypeAttachmentUsage() {
		File productTypeAttachmentDirectory = PathHandler.getProductTypeAttachmentBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setProductTypeAttachmentUsage(calcuateSingleDiskUsage(productTypeAttachmentDirectory));
	}


	private void calculateProductTypeImageUsage() {
		File productTypeImageDirectory = PathHandler.getProductTypeImageBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setProductTypeImageUsage(calcuateSingleDiskUsage(productTypeImageDirectory));
	}


	private void calculateProductAttachmentUsage() {
		File productAttachmentDirectory = PathHandler.getProductAttachmentBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setProductAttachmentUsage(calcuateSingleDiskUsage(productAttachmentDirectory));
	}


	private void calculateInspectionProoftestFileUsage() {
		File inspectionProoftestFileDirectory = PathHandler.getInspectionProoftestBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setInspectionProoftestFileUsage(calcuateSingleDiskUsage(inspectionProoftestFileDirectory));
	}


	private void calculateInspectionChartImageUsage() {
		File inspectionChartImageDirectory = PathHandler.getInspectionChartImageBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setInspectionChartImageUsage(calcuateSingleDiskUsage(inspectionChartImageDirectory));
	}


	private void calculateInspectionAttachmentUsage() {
		File inspectionAttachmentDirectory = PathHandler.getInspectionAttachmentBaseFile(currentDiskUsageSummary.getTenant());
		currentDiskUsageSummary.setInspectionAttachmentUsage(calcuateSingleDiskUsage(inspectionAttachmentDirectory));
	}
	
	
	private DiskUsage calcuateSingleDiskUsage(File directory) {
		int numberOfFiles = 0;
		long sizeOfDirectory = 0;
		if (directory.exists()) {
			numberOfFiles = FileUtils.listFiles(directory, null, true).size();
			sizeOfDirectory = FileUtils.sizeOfDirectory(directory);
		} 

		return new DiskUsage(sizeOfDirectory, numberOfFiles);
	}
}

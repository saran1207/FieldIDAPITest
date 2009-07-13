package com.n4systems.usage;

import org.apache.commons.io.FileUtils;

import com.n4systems.model.TenantOrganization;

public class TenantDiskUsageSummary {

	private final TenantOrganization tenant;
	
	private DiskUsage inspectionAttachmentUsage = new DiskUsage(); 
	private DiskUsage inspectionChartImageUsage = new DiskUsage();
	private DiskUsage inspectionProoftestFileUsage = new DiskUsage(); 
	private DiskUsage productAttachmentUsage = new DiskUsage(); 
	private DiskUsage productTypeImageUsage = new DiskUsage();
	private DiskUsage productTypeAttachmentUsage = new DiskUsage();
	private DiskUsage jobNoteUsage = new DiskUsage(); 
	private DiskUsage userUsage = new DiskUsage();
	
	
	public TenantDiskUsageSummary(TenantOrganization tenant) {
		this.tenant = tenant;
		
	}


	public DiskUsage getInspectionAttachmentUsage() {
		return inspectionAttachmentUsage;
	}


	public void setInspectionAttachmentUsage(DiskUsage inspectionAttachmentUsage) {
		this.inspectionAttachmentUsage = inspectionAttachmentUsage;
	}


	public DiskUsage getInspectionChartImageUsage() {
		return inspectionChartImageUsage;
	}


	public void setInspectionChartImageUsage(DiskUsage inspectionChartImageUsage) {
		this.inspectionChartImageUsage = inspectionChartImageUsage;
	}


	public DiskUsage getInspectionProoftestFileUsage() {
		return inspectionProoftestFileUsage;
	}


	public void setInspectionProoftestFileUsage(DiskUsage inspectionProoftestFileUsage) {
		this.inspectionProoftestFileUsage = inspectionProoftestFileUsage;
	}


	public DiskUsage getProductAttachmentUsage() {
		return productAttachmentUsage;
	}


	public void setProductAttachmentUsage(DiskUsage productAttachmentUsage) {
		this.productAttachmentUsage = productAttachmentUsage;
	}


	public DiskUsage getProductTypeImageUsage() {
		return productTypeImageUsage;
	}


	public void setProductTypeImageUsage(DiskUsage productTypeImageUsage) {
		this.productTypeImageUsage = productTypeImageUsage;
	}


	public DiskUsage getProductTypeAttachmentUsage() {
		return productTypeAttachmentUsage;
	}


	public void setProductTypeAttachmentUsage(DiskUsage productTypeAttachmentUsage) {
		this.productTypeAttachmentUsage = productTypeAttachmentUsage;
	}


	public DiskUsage getJobNoteUsage() {
		return jobNoteUsage;
	}


	public void setJobNoteUsage(DiskUsage jobNoteUsage) {
		this.jobNoteUsage = jobNoteUsage;
	}


	public DiskUsage getUserUsage() {
		return userUsage;
	}


	public void setUserUsage(DiskUsage userUsage) {
		this.userUsage = userUsage;
	}


	public long totalFiles() {
		
		long totalUsage = 0L;
		totalUsage += inspectionAttachmentUsage.getNumberOfFiles(); 
		totalUsage += inspectionChartImageUsage.getNumberOfFiles();
		totalUsage += inspectionProoftestFileUsage.getNumberOfFiles(); 
		totalUsage += productAttachmentUsage.getNumberOfFiles();
		totalUsage += productTypeImageUsage.getNumberOfFiles();
		totalUsage += productTypeAttachmentUsage.getNumberOfFiles();
		totalUsage += jobNoteUsage.getNumberOfFiles();
		totalUsage += userUsage.getNumberOfFiles();
		return totalUsage;
	}
	
	public long totalSpaceUsed() {
		
		long totalUsage = 0L;
		totalUsage += inspectionAttachmentUsage.getTotalSize(); 
		totalUsage += inspectionChartImageUsage.getTotalSize();
		totalUsage += inspectionProoftestFileUsage.getTotalSize(); 
		totalUsage += productAttachmentUsage.getTotalSize();
		totalUsage += productTypeImageUsage.getTotalSize();
		totalUsage += productTypeAttachmentUsage.getTotalSize();
		totalUsage += jobNoteUsage.getTotalSize();
		totalUsage += userUsage.getTotalSize();
		return totalUsage;
	}
	
	
	public TenantOrganization getTenant() {
		return tenant;
	}
	
	public String toString() {
		return tenant.getName() + " file usage " + FileUtils.byteCountToDisplaySize(totalSpaceUsed()) + " over " + totalFiles() + " files";
	}
	
}

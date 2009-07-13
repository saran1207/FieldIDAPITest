package com.n4systems.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.graphing.Chart;

public class FileDataContainer {
	private byte[] fileData;
	
	private byte[] chart;
	private Chart chartData;
	
	private String fileName;
	
	private List<String> serialNumbers = new ArrayList<String>();
	private String peakLoad;
	private String inspectionDate;
	private String testDuration;
	private String peakLoadDuration;
	private String comments;
	
	// XXX - The extraInfo has only been implemented for the RobertsFileProcessor.  It has not been implemented for Chant, NA and Wirop at this time (17/10/08)
	private Map<String, String> extraInfo = new HashMap<String, String>();
	
	private boolean createProduct = false;
	private boolean createCustomer = false;
	private boolean resolveCustomer = false;
	private String customerName;
	
	private ProofTestType fileType;
	
	public FileDataContainer() {
		chartData = Chart.newInstance();
	}

	public byte[] getChart() {
		return chart;
	}

	public void setChart(byte[] chart) {
		this.chart = chart;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ProofTestType getFileType() {
		return fileType;
	}

	public void setFileType(ProofTestType fileType) {
		this.fileType = fileType;
	}

	public Chart getChartData() {
		return chartData;
	}

	public void setChartData(Chart chartData) {
		this.chartData = chartData;
	}

	public List<String> getSerialNumbers() {
		return serialNumbers;
	}

	public void setSerialNumbers(List<String> serialNumbers) {
		this.serialNumbers = serialNumbers;
	}

	public void setSerialNumbers(String serialNumbers) {
		this.serialNumbers = new ArrayList<String>();
		
		if(serialNumbers != null) {
			for(String serialNumber: serialNumbers.split(",")) {
				this.serialNumbers.add(serialNumber);
			}
		}
	}
	
	public boolean hasSerialNumber(String serialNo) {
		boolean found = false;
		for(String serialNumber: serialNumbers) {
			if(serialNumber.equalsIgnoreCase(serialNo)) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public String getPeakLoad() {
		return peakLoad;
	}

	public void setPeakLoad(String peakLoad) {
		this.peakLoad = peakLoad;
	}

	public String getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getTestDuration() {
		return testDuration;
	}

	public void setTestDuration(String testDuration) {
		this.testDuration = testDuration;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public boolean isCreateProduct() {
		return createProduct;
	}

	public void setCreateProduct(boolean createProduct) {
		this.createProduct = createProduct;
	}

	public boolean isCreateCustomer() {
		return createCustomer;
	}

	public void setCreateCustomer(boolean createCustomer) {
		this.createCustomer = createCustomer;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public boolean isResolveCustomer() {
		return resolveCustomer;
	}

	public void setResolveCustomer(boolean resolveCustomer) {
		this.resolveCustomer = resolveCustomer;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Map<String, String> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Map<String, String> productOptions) {
		this.extraInfo = productOptions;
	}

	public String getPeakLoadDuration() {
		return peakLoadDuration;
	}

	public void setPeakLoadDuration(String peakLoadDuration) {
		this.peakLoadDuration = peakLoadDuration;
	}
	
}

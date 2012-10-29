package com.n4systems.tools;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.graphing.Chart;

import java.util.*;

public class FileDataContainer {

    public static final FileDataContainer UNCHANGED = new FileDataContainer();

	private byte[] fileData;
	
	private byte[] chart;
	private Chart chartData;
	
	private String fileName;
	
	private List<String> identifiers = new ArrayList<String>();
	private String peakLoad;
	private Date datePerformed;
	private String testDuration;
	private String peakLoadDuration;
	private String comments;
	
	// XXX - The extraInfo has only been implemented for the RobertsFileProcessor.  It has not been implemented for Chant, NA and Wirop at this time (17/10/08)
	private Map<String, String> extraInfo = new HashMap<String, String>();
	
	private boolean createAsset = false;
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

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}

	public void setIdentifiers(String identifiersString) {
		this.identifiers = new ArrayList<String>();
		
		if(identifiersString != null) {
            this.identifiers.addAll(Arrays.asList(identifiersString.split(",")));
		}
	}
	
	public String getPeakLoad() {
		return peakLoad;
	}

	public void setPeakLoad(String peakLoad) {
		this.peakLoad = peakLoad;
	}

	public Date getDatePerformed() {
		return datePerformed;
	}

	public void setDatePerformed(Date datePerformed) {
		this.datePerformed = datePerformed;
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

	public boolean isCreateAsset() {
		return createAsset;
	}

	public void setCreateAsset(boolean createAsset) {
		this.createAsset = createAsset;
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

	public void setExtraInfo(Map<String, String> assetOptions) {
		this.extraInfo = assetOptions;
	}

	public String getPeakLoadDuration() {
		return peakLoadDuration;
	}

	public void setPeakLoadDuration(String peakLoadDuration) {
		this.peakLoadDuration = peakLoadDuration;
	}
	
}

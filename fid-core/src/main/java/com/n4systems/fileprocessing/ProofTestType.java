package com.n4systems.fileprocessing;

import com.n4systems.model.api.Listable;

public enum ProofTestType implements Listable<String> {
	ROBERTS(RobertsFileProcessor.class, "label.robertlog", true), NATIONALAUTOMATION(NationalAutomationFileProcessor.class, "label.naexcel", true), CHANT(ChantFileProcessor.class, "label.chantlog", true), WIROP(WiropFileProcessor.class, "label.wiroplog", true),
	OTHER( ManualFileProcessor.class, "label.other", false );
	
	private Class<? extends FileProcessor> fileProcessorClass;
	private boolean uploadable;
	private String label;
	
	private <T extends FileProcessor> ProofTestType(Class<T> fileProcessorClass, String label, boolean uploadable) {
		this.fileProcessorClass = fileProcessorClass;
		this.label = label;
		this.uploadable = uploadable;
	}
	
	public FileProcessor getFileProcessorInstance() throws IllegalAccessException, InstantiationException {
		FileProcessor fileProcessor = null;
		if(fileProcessorClass != null) {
			fileProcessor = fileProcessorClass.newInstance();
		}
		return fileProcessor;
	}
	
	public Class<? extends FileProcessor> getFileProcessorClass() {
		return fileProcessorClass;
	}
	
	public String getLegacyName() {
		return name().toLowerCase();
	}
	
	public boolean isUploadable() {
		return uploadable;
	}

	public String getLabel() {
		return label;
	}
	
	public static ProofTestType getUploadFileType(String legacyName) {
		return ProofTestType.valueOf(legacyName.toUpperCase());
	}

	public String getDisplayName() {
		return getLabel();
	}

	public String getId() {
		return name();
	}
}

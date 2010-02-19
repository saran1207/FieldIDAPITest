package com.n4systems.exporting;


import java.util.UUID;

import org.apache.log4j.Logger;


public class ImportTask implements Runnable {
	private Logger logger = Logger.getLogger(ImportTask.class);
	
	public enum Status { 
		PENDING("label.pending"), RUNNING("label.running"), SUCCESSFUL("label.successful"), FAILED("error.import_failed");
	
		String label;
		
		Status(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return this.label;
		}
	}
	
	private final String id;
	private Status status = Status.PENDING;
	private Importer importer;

	public ImportTask(Importer importer) {
		this.id = UUID.randomUUID().toString();
		this.importer = importer;
	}
	
	@Override
	public void run() {
		setStatus(Status.RUNNING);
		try {
			
			importer.runImport();
			
			setStatus(Status.SUCCESSFUL);
			
		} catch (Exception e) {
			setStatus(Status.FAILED);
			logger.error("Failed import", e);
		}
	}

	public String getId() {
		return id;
	}

	private synchronized void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public boolean isCompleted() {
		return (status == Status.SUCCESSFUL || status == Status.FAILED);
	}
	
	public int getCurrentRow() {
		return importer.getCurrentRow();
	}
	
	public int getTotalRows() {
		return importer.getTotalRows();
	}
}

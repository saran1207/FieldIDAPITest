package com.n4systems.taskscheduling.task;


import org.apache.log4j.Logger;

import com.n4systems.exporting.Importer;

public class ImportTask implements Runnable {
	private Logger logger = Logger.getLogger(ImportTask.class);
	
	public enum Status { PENDING, RUNNING, COMPLETED }
	
	private Status status = Status.PENDING;
	private Importer importer;

	public ImportTask(Importer importer) {
		this.importer = importer;
	}
	
	@Override
	public void run() {
		setStatus(Status.RUNNING);
		try {
			
			importer.runImport();
			
		} catch (Exception e) {
			logger.error("Failed import", e);
		} finally {
			setStatus(Status.COMPLETED);
			// make sure the importer gets cleaned up now since
			// it could be holding a fair amount of mem
			importer = null;
		}	
	}

	private synchronized void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public boolean isCompleted() {
		return (status == Status.COMPLETED);
	}
	
	public int getCurrentRow() {
		return importer.getCurrentRow();
	}
	
	public int getTotalRows() {
		return importer.getTotalRows();
	}
}

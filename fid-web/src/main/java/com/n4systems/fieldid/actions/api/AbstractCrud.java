package com.n4systems.fieldid.actions.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.n4systems.ejb.PersistenceManager;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
abstract public class AbstractCrud extends AbstractAction implements Preparable {
	protected Long uniqueID;
	private Integer currentPage;
	
	public AbstractCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public void prepare() throws Exception {
		if(getUniqueID() != null) {
			loadMemberFields(getUniqueID());
		} else {
			initMemberFields();
		}
		postInit();
	}
	
	/**
	 * This should load the main class that will be edited with this CRUD
	 * @param uniqueId
	 */
	abstract protected void loadMemberFields(Long uniqueId);

	/**
	 * This should initialize a new instance of the main class that will be edited with this CRUD
	 */
	abstract protected void initMemberFields();
	
	/**
	 * this should do anything to the crud objects assuming the members have been loaded.
	 */
	protected void postInit() {}
	
	protected byte[] convertToByteArray(File file) {
		byte[] byteArray = null;

		try {
			FileInputStream inputStream = new FileInputStream(file);
			byteArray = new byte[inputStream.available()];
			inputStream.read(byteArray);
		} catch (IOException e) {
			return null;
		}

		return byteArray;
	}
	
	public Integer getCurrentPage() {
		if (currentPage == null) {
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}

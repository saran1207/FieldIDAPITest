package com.n4systems.fieldid.actions.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.activation.FileTypeMap;

import com.n4systems.ejb.PersistenceManager;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
abstract public class AbstractCrud extends AbstractAction implements Preparable {
	protected Long uniqueID;
	private Integer currentPage;
	public AbstractCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
		getSession();
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
	
	
	/**
	 * used for the on radio button list
	 * 
	 * @return  a map with just a true key
	 */
	@SuppressWarnings("unchecked")
	public Map getOn(){
		return getOn("");
	}
	
	@SuppressWarnings("unchecked")
	public Map getOn(String label){
		Map<Boolean, String> onOff = new HashMap<Boolean, String>();
		onOff.put(true, label);
		return onOff;
	}
	
	/**
	 * used for the off radio button list
	 * 
	 * @return  a map with just a false key
	 */
	@SuppressWarnings("unchecked")
	public Map getOff(){
		return getOff("");
	}
	
	@SuppressWarnings("unchecked")
	public Map getOff(String label){
		Map<Boolean, String> onOff = new HashMap<Boolean, String>();
		onOff.put(false, label);
		return onOff;
	}
	
	
	/**
	 * used for the off radio button list
	 * 
	 * @return  a map with just a false key
	 */
	@SuppressWarnings("unchecked")
	public Map getSingleMapElement(Long id){
		Map<Long, String> singleElementMap = new HashMap<Long, String>();
		singleElementMap.put(id, "");
		return singleElementMap;
	}
	
	/**
	 * Tests if the filename has a content type starting with <code>'image/'</code>. The
	 * content type is queried from {@link FileTypeMap#getContentType(String)}.
	 * 
	 * @param fileName	String file name, including extension.
	 * @return			<code>true</code> if content type starts with <code>'image/'</code>.
	 */
	public boolean isImage(String fileName) {
		return FileTypeMap.getDefaultFileTypeMap().getContentType(fileName).startsWith("image/");
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

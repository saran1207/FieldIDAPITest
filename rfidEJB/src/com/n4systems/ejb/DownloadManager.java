package com.n4systems.ejb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.ejb.Local;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.DownloadManagerImpl.Visibility;

@Local
public interface DownloadManager {
	
	public File getBaseDownloadPath() throws IOException;
	
	
	public File getTenantPath(Long tenantId) throws IOException;
	public File getCustomerPath(Long tenantId, Long customerId) throws IOException;
	public File getDivisionPath(Long tenantId, Long customerId, Long divisionId) throws IOException;
	public File getUserPath(Long tenantId, Long customerId, Long divisionId, Long userId) throws IOException;

	public String putFile(String fileName, InputStream fileInput, UserBean user) throws FileNotFoundException, IOException;
	public String putFile(String fileName, InputStream fileInput, UserBean user, Visibility visibility) throws FileNotFoundException, IOException;
	public String putFile(File inputFile, UserBean user) throws FileNotFoundException, IOException;
	public String putFile(File inputFile, UserBean user, boolean moveFile) throws FileNotFoundException, IOException;
	public String putFile(File inputFile, UserBean user, Visibility visibility, boolean moveFile) throws FileNotFoundException, IOException;
	
	public File getFile(String relativePath, UserBean user) throws FileNotFoundException;
	public InputStream getFileInputStream(String relativePath, UserBean user) throws FileNotFoundException;
	
	public File getBaseAccessDir(UserBean user) throws IOException;
	
	public boolean hasAccess(String path, UserBean user);
	public String resolveRelativePath(File systemPath);
	public File resolveSystemPath(String relativePath);
	
}

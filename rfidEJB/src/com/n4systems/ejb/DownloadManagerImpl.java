package com.n4systems.ejb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Stack;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.util.ConfigContext;

@Interceptors({TimingInterceptor.class})
@Stateless
public class DownloadManagerImpl implements DownloadManager {
	private static final String BASE_DOWNLOAD = "downloads";
	private static final Long defaultId = 0L;
	private static final int bufferLen = 1024 * 16;
	
	public enum Visibility {
		GLOBAL, TENANT, CUSTOMER, DIVISION, SELF;
	}
	
	private File formatBaseDownloadPath() {
		return new File(ConfigContext.getCurrentContext().getAppRoot(), BASE_DOWNLOAD);
	}
	
	private File formatDownloadPath(Stack<Long> pathStack) {
		File downloadPath = formatBaseDownloadPath();
		
		while(!pathStack.isEmpty()) {
			downloadPath = new File(downloadPath, pathStack.pop().toString());			
		}
				
		return downloadPath;
	}
	
	private File formatDownloadPath(Long tenantId, Long customerId, Long divisionId, Long userId) {
		Stack<Long> pathStack = new Stack<Long>();
		
		for(Long part: Arrays.asList(userId, divisionId, customerId, tenantId)) {
			if(part != null) {
				pathStack.push(part);
			}
		}
		
		return formatDownloadPath(pathStack);
	}
	
	public File getBaseDownloadPath() throws IOException {
		File baseDir = formatBaseDownloadPath();

		if(!baseDir.exists()) {
			FileUtils.forceMkdir(baseDir);
		}
		
		return baseDir;
	}
	
	private File getDownloadPath(Stack<Long> pathParts) throws IOException {
		File downloadPath = formatDownloadPath(pathParts);

		if(!downloadPath.exists()) {
			FileUtils.forceMkdir(downloadPath);
		}		
		
		return downloadPath;
	}
	
	private File getDownloadPath(Long tenantId, Long customerId, Long divisionId, Long userId) throws IOException {
		File downloadPath = formatDownloadPath(tenantId, customerId, divisionId, userId);

		if(!downloadPath.exists()) {
			FileUtils.forceMkdir(downloadPath);
		}		
		
		return downloadPath;
	}
	
	private Visibility getBaseVisibility(UserBean user) {
		Visibility visibility;
		
		if(user.getR_Division() != null) {
			visibility = Visibility.DIVISION;
		} else if(user.getR_EndUser() != null) {
			visibility = Visibility.CUSTOMER;
		} else if(user.getTenant().getId() != null) {
			visibility = Visibility.TENANT;
		} else {
			visibility = Visibility.GLOBAL;
		}
		
		return visibility;
	}
	
	private Stack<Long> getPathStack(UserBean user, Visibility visibility) {
		Stack<Long> pathStack = new Stack<Long>();
		
		switch(visibility) {
			case SELF:
				pathStack.add(user.getUniqueID());
			case DIVISION:
				if(user.getR_Division() != null) {
					pathStack.add(user.getR_Division());
				} else {
					pathStack.add(defaultId);
				}	
			case CUSTOMER:
				if(user.getR_EndUser() != null) {
					pathStack.add(user.getR_EndUser());
				} else {
					pathStack.add(defaultId);
				}					
			case TENANT:
				if(user.getTenant().getId() != null) {
					pathStack.add(user.getTenant().getId());
				} else {
					pathStack.add(defaultId);
				}					
			case GLOBAL:
				break;
		}
		
		return pathStack;
	}
	
	private Stack<Long> getBasePathStack(UserBean user) {
		return getPathStack(user, getBaseVisibility(user));
	}
	
	public File getTenantPath(Long tenantId) throws IOException {
		return getDownloadPath(tenantId, null, null, null);
	}
	
	public File getCustomerPath(Long tenantId, Long customerId) throws IOException {
		return getDownloadPath(tenantId, customerId, null, null);
	}
	
	public File getDivisionPath(Long tenantId, Long customerId, Long divisionId) throws IOException {
		return getDownloadPath(tenantId, customerId, divisionId, null);
	}
	
	public File getUserPath(Long tenantId, Long customerId, Long divisionId, Long userId) throws IOException {
		return getDownloadPath(tenantId, customerId, divisionId, userId);
	}

	private File putFile(String fileName, InputStream fileInput, Stack<Long> pathStack) throws FileNotFoundException, IOException {
		int readBytes;
		byte[] buffer = new byte[bufferLen];

		File downloadFile = new File(getDownloadPath(pathStack), fileName);

		OutputStream fileOut = null;
		BufferedOutputStream buffOut = null;
		BufferedInputStream buffIn = null;
		
		try {
			fileOut = new FileOutputStream(downloadFile);
			buffOut = new BufferedOutputStream(fileOut);
			buffIn = new BufferedInputStream(fileInput);
			
			while((readBytes = buffIn.read(buffer)) != -1) {
				buffOut.write(buffer, 0, readBytes); 
			}
			
		} finally {
			IOUtils.closeQuietly(buffIn);
			IOUtils.closeQuietly(buffOut);
			IOUtils.closeQuietly(fileOut);
		}
		
		
		return downloadFile;
	}
	
	private File putFile(File inputFile, Stack<Long> pathStack, boolean moveFile) throws FileNotFoundException, IOException {
		File downloadFile = null;
		InputStream fileInput = null;
		
		try {
			fileInput = new FileInputStream(inputFile);
			downloadFile = putFile(inputFile.getName(), fileInput, pathStack);
		} finally {
			IOUtils.closeQuietly(fileInput);
		}
		
		if(moveFile) {
			inputFile.delete();
		}
		
		return downloadFile;
	}

	public String putFile(String fileName, InputStream fileInput, UserBean user) throws FileNotFoundException, IOException {
		return resolveRelativePath(putFile(fileName, fileInput, getBasePathStack(user)));
	}
	
	public String putFile(String fileName, InputStream fileInput, UserBean user, Visibility visibility) throws FileNotFoundException, IOException {
		return resolveRelativePath(putFile(fileName, fileInput, getPathStack(user, visibility)));
	}
	
	public String putFile(File inputFile, UserBean user) throws FileNotFoundException, IOException {
		return resolveRelativePath(putFile(inputFile, getBasePathStack(user), true));
	}	
	
	public String putFile(File inputFile, UserBean user, boolean moveFile) throws FileNotFoundException, IOException {
		return resolveRelativePath(putFile(inputFile, getBasePathStack(user), moveFile));
	}
	
	public String putFile(File inputFile, UserBean user, Visibility visibility, boolean moveFile) throws FileNotFoundException, IOException {
		return resolveRelativePath(putFile(inputFile, getPathStack(user, visibility), moveFile));
	}

	private File formatBaseAccessDir(UserBean user) {
		return formatDownloadPath(getBasePathStack(user));
	}

	public File getBaseAccessDir(UserBean user) throws IOException {
		File baseAccessDir = formatDownloadPath(getBasePathStack(user));
		if(!baseAccessDir.exists()) {
			FileUtils.forceMkdir(baseAccessDir);
		}
		return baseAccessDir;
	}
	
	private boolean hasAccess(File fullPath, UserBean user) {
		String baseAccessPath = formatBaseAccessDir(user).getAbsolutePath();
		String filePath = fullPath.getParentFile().getAbsolutePath();
		
		//if the file path starts with the base access path then it is a sub directory
		// users may see below their baseAccessPath, not above
		return filePath.startsWith(baseAccessPath) ? true : false;
	}
	
	public String resolveRelativePath(File systemPath) {
		//remove the base path from the full path
		return systemPath.getAbsolutePath().substring(formatBaseDownloadPath().getAbsolutePath().length());
	}
	
	public File resolveSystemPath(String relativePath) {
		//remove the base path from the full path
		return new File(formatBaseDownloadPath(), relativePath);
	}

	public boolean hasAccess(String relativePath, UserBean user) {
		return hasAccess(resolveSystemPath(relativePath), user);
	}
	
	public File getFile(String relativePath, UserBean user) throws FileNotFoundException {
		File downloadFile = resolveSystemPath(relativePath);
		
		if(!hasAccess(downloadFile, user)) {
			// XXX this should probably be it's own exception 
			throw new FileNotFoundException("User [" + user.getUserID() + "] does not have access to download file");
		}
			
		if(!downloadFile.exists()) {
			throw new FileNotFoundException("No file found at [" + downloadFile.getPath() + "]");
		}
		
		return downloadFile;
	}
	
	public InputStream getFileInputStream(String relativePath, UserBean user) throws FileNotFoundException {
		return new FileInputStream(getFile(relativePath, user));
	}
	
}

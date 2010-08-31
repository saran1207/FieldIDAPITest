package com.n4systems.model;

import javax.activation.FileTypeMap;

public interface Attachment {

	public String getFileName();

	public void setFileName(String fileName);

	public String getComments();

	public void setComments(String comments);

	/**
	 * Tests if the filename has a content type starting with <code>'image/'</code>. The
	 * content type is queried from {@link FileTypeMap#getContentType(String)}.
	 * 
	 * @param fileName	String file name, including extension.
	 * @return			<code>true</code> if content type starts with <code>'image/'</code>.
	 */
	public boolean isImage();

	public boolean hasAttachedFile();

}
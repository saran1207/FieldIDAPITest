package com.n4systems.testutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

public class TestDoubleMessage extends Message {

	private String toStringText;
	
	public TestDoubleMessage setToStringText(String toStringText) {
		this.toStringText = toStringText;
		return this;
	}

	@Override
	public String toString() {
		return toStringText;
	}

	@Override
	public void addFrom(Address[] arg0) throws MessagingException {
	}

	@Override
	public void addRecipients(RecipientType arg0, Address[] arg1) throws MessagingException {
	}

	@Override
	public Flags getFlags() throws MessagingException {
		return null;
	}

	@Override
	public Address[] getFrom() throws MessagingException {
		return null;
	}

	@Override
	public Date getReceivedDate() throws MessagingException {
		return null;
	}

	@Override
	public Address[] getRecipients(RecipientType arg0) throws MessagingException {
		return null;
	}

	@Override
	public Date getSentDate() throws MessagingException {
		return null;
	}

	@Override
	public String getSubject() throws MessagingException {
		return null;
	}

	@Override
	public Message reply(boolean arg0) throws MessagingException {
		return null;
	}

	@Override
	public void saveChanges() throws MessagingException {
	}

	@Override
	public void setFlags(Flags arg0, boolean arg1) throws MessagingException {
	}

	@Override
	public void setFrom() throws MessagingException {
	}

	@Override
	public void setFrom(Address arg0) throws MessagingException {
	}

	@Override
	public void setRecipients(RecipientType arg0, Address[] arg1) throws MessagingException {
	}

	@Override
	public void setSentDate(Date arg0) throws MessagingException {
	}

	@Override
	public void setSubject(String arg0) throws MessagingException {
	}

	@Override
	public void addHeader(String arg0, String arg1) throws MessagingException {
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration getAllHeaders() throws MessagingException {
		return null;
	}

	@Override
	public Object getContent() throws IOException, MessagingException {
		return null;
	}

	@Override
	public String getContentType() throws MessagingException {
		return null;
	}

	@Override
	public DataHandler getDataHandler() throws MessagingException {
		return null;
	}

	@Override
	public String getDescription() throws MessagingException {
		return null;
	}

	@Override
	public String getDisposition() throws MessagingException {
		return null;
	}

	@Override
	public String getFileName() throws MessagingException {
		return null;
	}

	@Override
	public String[] getHeader(String arg0) throws MessagingException {
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException, MessagingException {
		return null;
	}

	@Override
	public int getLineCount() throws MessagingException {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration getMatchingHeaders(String[] arg0) throws MessagingException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration getNonMatchingHeaders(String[] arg0) throws MessagingException {
		return null;
	}

	@Override
	public int getSize() throws MessagingException {
		return 0;
	}

	@Override
	public boolean isMimeType(String arg0) throws MessagingException {
		return false;
	}

	@Override
	public void removeHeader(String arg0) throws MessagingException {
	}

	@Override
	public void setContent(Multipart arg0) throws MessagingException {
	}

	@Override
	public void setContent(Object arg0, String arg1) throws MessagingException {
	}

	@Override
	public void setDataHandler(DataHandler arg0) throws MessagingException {
	}

	@Override
	public void setDescription(String arg0) throws MessagingException {
	}

	@Override
	public void setDisposition(String arg0) throws MessagingException {
	}

	@Override
	public void setFileName(String arg0) throws MessagingException {
	}

	@Override
	public void setHeader(String arg0, String arg1) throws MessagingException {
	}

	@Override
	public void setText(String arg0) throws MessagingException {
	}

	@Override
	public void writeTo(OutputStream arg0) throws IOException, MessagingException {
	}

}

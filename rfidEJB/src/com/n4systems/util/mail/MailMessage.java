package com.n4systems.util.mail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileTypeMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;

import com.n4systems.model.downloadlink.DownloadLink;

public class MailMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String subjectPrefix;
	private String subject;
	private String bodyHeader;
	private String body;
	private String bodyFooter;
	private String fromAddress;
	private String replyTo;
	private ContentType contentType = ContentType.HTML;
	private Set<String> toAddresses = new HashSet<String>();
	private Set<String> ccAddresses = new HashSet<String>();
	private Set<String> bccAddresses = new HashSet<String>();
	private Map<String, byte[]> attachments = new HashMap<String, byte[]>();
	
	// this is only used in compiling the message
	transient private Message message;
	
	public enum ContentType {
		PLAIN("text/plain", "txt"), HTML("text/html", "html");
		
		private String contentTypeString;
		private String fileExtension;
		
		ContentType(String contentTypeString, String fileExtension) {
			this.contentTypeString = contentTypeString;
			this.fileExtension = fileExtension;
		}
		
		public String getContentTypeString() {
			return contentTypeString;
		}
		
		public boolean isHtml() {
			return (this == HTML) ? true : false;
		}

		public String fileExtension() {
			return fileExtension;
		}
	}
	
	public MailMessage() {}
	
	public MailMessage(ContentType contentType, String subject, String body) {
		setContentType(contentType);
		setSubject(subject);
		setBody(body);
	}
	
	public MailMessage(String subject, String body) {
		this(ContentType.HTML, subject, body);
	}
	
	public MailMessage(String subject, String body, String toAddress) {
		this(subject, body);
		getToAddresses().add(toAddress);
	}
	
	public MailMessage(DownloadLink link, String body) {
		this(link.getName(), body, link.getUser().getEmailAddress());
	}

	public String getSubjectPrefix() {
		return subjectPrefix;
	}

	public void setSubjectPrefix(String subjectPrefix) {
		this.subjectPrefix = subjectPrefix;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBodyHeader() {
		return bodyHeader;
	}

	public void setBodyHeader(String bodyHeader) {
		this.bodyHeader = bodyHeader;
	}

	public String getBody() throws MessagingException {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBodyFooter() {
		return bodyFooter;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	public void setBodyFooter(String bodyFooter) {
		this.bodyFooter = bodyFooter;
	}

	public ContentType getContentType() {
		return contentType;
	}

	private void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Set<String> getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(Set<String> toAddresses) {
		this.toAddresses = toAddresses;
	}

	public Set<String> getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(Set<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public Set<String> getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(Set<String> bccAddresses) {
		this.bccAddresses = bccAddresses;
	}


	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	
	public void removeAllAttachments() {
		attachments.clear();
	}
	
	public void removeAttachment(String name) {
		if(name != null) {
			attachments.remove(name);
		}
	}
	
	public void addAttachment(String fileName, byte[] data) {
		attachments.put(fileName, data);
	}
	
	public void addAttachment(String filePath) throws FileNotFoundException, IOException {
		addAttachment(new File(filePath));
	}
	
	public void addAttachment(File file) throws FileNotFoundException, IOException {
		InputStream is = null;
		BufferedInputStream bIs = null;
		
		// Integer max is 2GB .. very few mta/mda's or mail clients for that matter will support a size even close to this
		// ... also anything larger would clog the interweb tubes :)
		if(file.length() > Integer.MAX_VALUE) {
			throw new IOException("File Attachment too large: length: [" + file.length() + "] bytes, max: [" + Integer.MAX_VALUE + "] bytes");
		}
		byte[] data = new byte[(int)file.length()];
		
		try {
			is = new FileInputStream(file);
			bIs = new BufferedInputStream(is);
			
			bIs.read(data);
			
		} finally {
			IOUtils.closeQuietly(bIs);
			IOUtils.closeQuietly(is);
		}
	
		addAttachment(file.getName(), data);
	}
	
	public Message compileMessage(Session session) throws MessagingException {
		message = new MimeMessage(session);
		
		message.setFrom(stringToAddress(getFromAddress()));
		message.setSubject(getSubjectPrefix() + getSubject());
		
		if(replyTo != null && replyTo.length() > 0) {
			message.addHeader("Reply-To", getReplyTo());
		}
		
		attachRecipients(Message.RecipientType.TO, getToAddresses());
		attachRecipients(Message.RecipientType.CC, getCcAddresses());
		attachRecipients(Message.RecipientType.BCC, getBccAddresses());
		
		Multipart multipart = new MimeMultipart();
		
		multipart.addBodyPart(createMainMessageBodyPart());
		
		for(Map.Entry<String, byte[]> attachmentEntry: attachments.entrySet()) {
			multipart.addBodyPart(createAttachmentPart(attachmentEntry.getValue(), attachmentEntry.getKey()));
		}
		
		message.setContent(multipart);
		
		return message;
	}

	private void attachRecipients(Message.RecipientType type, Set<String> addrs) throws MessagingException {
		for (String addr: addrs) {
			message.addRecipient(type, stringToAddress(addr));
		}
	}
	
	private Address stringToAddress(String strAddress) throws AddressException {
		return new InternetAddress(strAddress);
	}
	
	private BodyPart createMainMessageBodyPart() throws MessagingException {
		MimeBodyPart part = new MimeBodyPart();
		part.setContent(getBodyHeader() + getBody() + getBodyFooter(), getContentType().getContentTypeString());
		return part;
	}
	
	private BodyPart createAttachmentPart(byte[] data, String name) throws MessagingException {
		MimeBodyPart part = new MimeBodyPart();
		String fileType = FileTypeMap.getDefaultFileTypeMap().getContentType(name);
	    ByteArrayDataSource source = new ByteArrayDataSource(data, fileType);
	    source.setName(name);
	    
	    part.addHeader("Content-ID", name);
	    part.setDataHandler(new DataHandler(source));
	    part.setFileName(source.getName());
	    return part;
	}
	
	
	

	@Override
	public String toString() {
		try {
			return "MailMessage: " 
				+ "subjectPrefix [" + subjectPrefix
				+ "] subject [" + subject
				+ "] bodyHeader [" + bodyHeader
				+ "] body [" + getBody() 
				+ "] bodyFooter [" + bodyFooter
				+ "] fromAddress [" + fromAddress
				+ "] replyTo [" + replyTo
				+ "] contentType [" + getContentType().toString()
				+ "] toAddresses [" + toAddresses
				+ "] ccAddresses [" + ccAddresses
				+ "] bccAddresses [" + bccAddresses
				+ "] attachments [" + attachments.keySet() + "]";
		} catch (MessagingException e) {
			throw new RuntimeException("could not produce message string", e);
		}
	}
}

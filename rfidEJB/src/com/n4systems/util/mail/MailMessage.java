package com.n4systems.util.mail;

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

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.StringUtils;

public class MailMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ConfigContext config;
	private final MessageType contentType;
	
	private String subject;
	private String body;
	private Set<String> toAddresses = new HashSet<String>();
	private Set<String> ccAddresses = new HashSet<String>();
	private Set<String> bccAddresses = new HashSet<String>();
	private Map<String, byte[]> attachments = new HashMap<String, byte[]>();
	
	// this is only used in compiling the message
	transient private Message message;
	
	public enum MessageType {
		PLAIN("text/plain", "txt"), HTML("text/html", "html");
		
		private String contentTypeString;
		private String fileExtension;
		
		MessageType(String contentTypeString, String fileExtension) {
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
	
	public MailMessage(MessageType type, ConfigContext config) {
		this.contentType = type;
		this.config = config;
	}
	
	public MailMessage(String subject, String body, String...toAddresses) {
		this(MessageType.HTML, ConfigContext.getCurrentContext());
		setSubject(subject);
		setBody(body);

		for (String toAddr: toAddresses) {
			getToAddresses().add(toAddr);
		}
	}

	public MessageType getContentType() {
		return contentType;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getFromAddress() {
		return config.getString(ConfigEntry.MAIL_FROM_ADDR);
	}
	
	public String getReplyTo() {
		return config.getString(ConfigEntry.MAIL_REPLY_TO);
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
	
	public String getFullSubject() {
		String fullSubject = config.getString(ConfigEntry.MAIL_SUBJECT_PREFIX) + getSubject();
		return fullSubject;
	}

	public String getFullBody() {
		boolean isHtml = getContentType().isHtml();
		
		ConfigEntry headerConfig = isHtml ? ConfigEntry.MAIL_BODY_HTML_HEADER : ConfigEntry.MAIL_BODY_PLAIN_HEADER;
		ConfigEntry footerConfig = isHtml ? ConfigEntry.MAIL_BODY_HTML_FOOTER : ConfigEntry.MAIL_BODY_PLAIN_FOOTER;
		
		String fullBody = config.getString(headerConfig) + getBody() + config.getString(footerConfig);
		return fullBody;
	}
	
	public Map<String, byte[]> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(Map<String, byte[]> attachments) {
		this.attachments = attachments;
	}
	
	public Message compileMessage(Session session) throws MessagingException {
		message = new MimeMessage(session);
		
		message.setFrom(stringToAddress(getFromAddress()));
		message.setSubject(getFullSubject());
		
		if (StringUtils.isNotEmpty(getReplyTo())) {
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
		part.setContent(getFullBody(), getContentType().getContentTypeString());
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
		return "MailMessage: " 
			+ "fullSubject [" + getFullSubject()
			+ "] fullBody [" + getFullBody()
			+ "] fromAddress [" + getFromAddress()
			+ "] replyTo [" + getReplyTo()
			+ "] contentType [" + getContentType().toString()
			+ "] toAddresses [" + getToAddresses()
			+ "] ccAddresses [" + getCcAddresses()
			+ "] bccAddresses [" + getBccAddresses()
			+ "] attachments [" + attachments.keySet() + "]";
	}
}

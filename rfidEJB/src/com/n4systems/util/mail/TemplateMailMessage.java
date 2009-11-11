package com.n4systems.util.mail;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import com.n4systems.freemarker.FreemarkerTemplateFactory;

import freemarker.template.Template;

public class TemplateMailMessage extends MailMessage {
	private static final long serialVersionUID = 1L;
	
	private String templateName;
	private Map<String, Object> templateMap = new HashMap<String, Object>();

	private ContentType templateType;
	
	public TemplateMailMessage() {
		this(null, null);
	}
	
	public TemplateMailMessage(String subject, String templatePath) {
		super(subject, null);
		this.templateName = templatePath;
	}
	
	public String getTemplatePath() {
		return templateName;
	}

	public void setTemplatePath(String templatePath) {
		this.templateName = templatePath;
	}

	public Map<String, Object> getTemplateMap() {
		return templateMap;
	}

	public void setTemplateMap(Map<String, Object> templateMap) {
		this.templateMap = templateMap;
	}

	@Override
	public String getBody() throws MessagingException {
		StringWriter body = new StringWriter();
		
		try {
			Template template = FreemarkerTemplateFactory.getTemplate(templateName, getContentType());
			template.process(templateMap, body);
		} catch(Exception e) {
			throw new MessagingException("Could not render template body", e);
		}
		
		return body.toString();
	}

	@Override
	public ContentType getContentType() {
		if (templateType == null) {
			templateType = FreemarkerTemplateFactory.getTemplateType(templateName);
		}
		return templateType;
	}

}

package com.n4systems.util.mail;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import com.n4systems.freemarker.FreemarkerTemplateFactory;

import freemarker.template.Template;

public class TemplateMailMessage extends MailMessage {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	private Map<String, Object> templateMap = new HashMap<String, Object>();
	
	public TemplateMailMessage() {
		this(null, null);
	}
	
	public TemplateMailMessage(String subject, String templatePath) {
		super(ContentType.HTML, subject, null);
		applyDefaultFooter();
		this.templatePath = templatePath;
	}
	
	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
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
			Template template = FreemarkerTemplateFactory.getTemplate(templatePath);
			
			template.process(templateMap, body);
			
		} catch(Exception e) {
			throw new MessagingException("Could not render template body", e);
		}
		
		return body.toString();
	}
	
	
	
}

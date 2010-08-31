package com.n4systems.util.mail;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.freemarker.FreemarkerTemplateFactory;
import com.n4systems.util.ConfigContext;

import freemarker.template.Template;

public class TemplateMailMessage extends MailMessage {
	private static final long serialVersionUID = 1L;
	
	private String templatePath;
	private Map<String, Object> templateMap = new HashMap<String, Object>();

	private MessageType templateType;
	
	public TemplateMailMessage() {
		super(MessageType.HTML, ConfigContext.getCurrentContext());
	}
	
	public TemplateMailMessage(String subject, String templatePath) {
		this();
		setSubject(subject);
		setTemplatePath(templatePath);
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
	
	public void setEmailConent(String bodyContent) {
		templateMap.put("content", bodyContent);
		templatePath = "injectBody";
	}

	@Override
	public String getBody() {
		StringWriter body = new StringWriter();
		
		try {
			Template template = FreemarkerTemplateFactory.getTemplate(templatePath, getContentType());
			template.process(templateMap, body);
		} catch(Exception e) {
			throw new MailException("Could not render template body", e);
		}
		
		return body.toString();
	}

	@Override
	public MessageType getContentType() {
		if (templateType == null) {
			templateType = FreemarkerTemplateFactory.getTemplateType(templatePath);
		}
		return templateType;
	}

}

package com.n4systems.freemarker;

import java.io.IOException;
import java.util.Locale;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.mail.MailMessage.ContentType;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerTemplateFactory {

	private static Configuration config;
	
	private static final ContentType[] fileTypes = { ContentType.HTML, ContentType.PLAIN };
	private static final String templateExtention = "ftl";
	
	public static synchronized Configuration getConfig() throws IOException {
		if (config == null) {
			config = new Configuration();
			config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			config.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
			config.setDefaultEncoding("ISO-8859-1");
			config.setOutputEncoding("UTF-8");
	        config.setLocale(Locale.US);
		
	        FileTemplateLoader loader = new FileTemplateLoader(PathHandler.getCommonTemplatePath(), true); 
	        config.setTemplateLoader(loader);
		}
		return config;
	}

	public static Template getTemplate(String templatePath, ContentType contentType) throws IOException {
		return getConfig().getTemplate(fullTemplateName(templatePath, contentType));
	}

	private static String fullTemplateName(String templatePath, ContentType contentType) {
		return templatePath + "." + contentType.fileExtension() + "." + templateExtention;
	}
	
	public static ContentType getTemplateType(String templateName) {
		for (ContentType type : fileTypes) {
			try {
				if (getTemplate(templateName, type) != null) { 
					return type;
				}
			} catch (IOException e) {}
		}
		throw new InvalidArgumentException("template could not be found");
	}

	
}

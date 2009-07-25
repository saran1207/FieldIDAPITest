package com.n4systems.freemarker;

import java.io.IOException;
import java.util.Locale;

import com.n4systems.reporting.PathHandler;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerTemplateFactory {

	private static Configuration config;
	
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

	public static Template getTemplate(String templatePath) throws IOException {
		return getConfig().getTemplate(templatePath);
	}
}

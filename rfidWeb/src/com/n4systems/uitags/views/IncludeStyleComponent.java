package com.n4systems.uitags.views;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.util.UrlHelper;

import com.opensymphony.xwork2.util.ValueStack;

public class IncludeStyleComponent extends UIBean {
	public static final String TEMPLATE = "includeStyle";
	
	private enum StyleType {
		DEFAULT(""), PAGE("pageStyles/"), FEATURE("featureStyles/");
		
		private String directory;
		private StyleType(String directory) {
			this.directory = directory;
		}
	}
	
	private static final String DEFAULT_STYLE_SHEET_MEDIA = "all";
	private static final String STYLE_DIRECTORY = "style";
	private static final String STYLE_EXTENSION = "css";
	
	
	private String href;
	private String media;
	private StyleType type = StyleType.DEFAULT;
	
	
	protected IncludeStyleComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	
	public void evaluateParams() {
        super.evaluateParams();
        buildCssSource();
        setupMediaType();
        
    }

	private void buildCssSource() {
		if (href != null) {
        	formateSrc();
			String result = UrlHelper.buildUrl(href, request, response, null);
            addParameter("href", result);
        }
	}
	
	private void formateSrc() {
		if (href.startsWith("/") || href.toLowerCase().startsWith("http")) {
			return;
		}
		
		String directoryOfFile = formatStyleDirectory();
		
		href = directoryOfFile + href;
		
		
		if (!href.toLowerCase().endsWith("." + STYLE_EXTENSION)) {
			href += "." + STYLE_EXTENSION;
		}
	}


	private String formatStyleDirectory() {
		return "/" + STYLE_DIRECTORY + "/" + type.directory;
	}
	
	private void setupMediaType() {
		if (media == null) {
			media = DEFAULT_STYLE_SHEET_MEDIA;
		}
		addParameter("media", media);
	}

	
	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}



	public String getHref() {
		return href;
	}



	public void setHref(String href) {
		this.href = href;
	}


	public String getMedia() {
		return media;
	}


	public void setMedia(String media) {
		this.media = media;
	}


	public String getType() {
		return type.name();
	}


	public void setType(String type) {
		this.type = StyleType.valueOf(type.toUpperCase());
	}

	

}

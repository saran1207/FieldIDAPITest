package com.n4systems.model.staticdownloads;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("StaticDownload")
public class StaticDownload implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("name")
   	@XStreamAsAttribute
	private String name;
	
	@XStreamAlias("url")
   	@XStreamAsAttribute
	private String url;

	@XStreamAlias("desc")
   	@XStreamAsAttribute
	private String description;
	
	public StaticDownload() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

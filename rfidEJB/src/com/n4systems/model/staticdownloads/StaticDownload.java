package com.n4systems.model.staticdownloads;

import java.io.Serializable;

import com.n4systems.util.HashCode;
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
	
	public StaticDownload(String name, String url, String desc) {
		this.name = name;
		this.url = url;
		this.description = desc;
	}

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StaticDownload) {
			StaticDownload other = (StaticDownload)obj;
			return (name.equals(other.name) && url.equals(other.url) && description.equals(other.description));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return HashCode.newHash().add(name).add(url).add(description).toHash();
	}
	
}

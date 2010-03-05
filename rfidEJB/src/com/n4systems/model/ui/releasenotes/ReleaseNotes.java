package com.n4systems.model.ui.releasenotes;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ReleaseNotes")
public class ReleaseNotes {

	@XStreamAlias("title")
   	@XStreamAsAttribute
	private String title = "No Release Notes";
	
	@XStreamAlias("url")
   	@XStreamAsAttribute
	private String url = "#";
	
	@XStreamAlias("bullets")
	private List<BulletPoint> bullets = new ArrayList<BulletPoint>();
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<BulletPoint> getBullets() {
		return bullets;
	}
	
	
}

package com.n4systems.model.ui.releasenotes;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("BulletPoint")
public class BulletPoint {
	public static BulletPoint emptyBulletPoint() {
		return new BulletPoint();
	}
	
	@XStreamAlias("point")
   	@XStreamAsAttribute
	private String point;

	public BulletPoint() {
		this("");
	}
	
	public BulletPoint(String point) {
		super();
		setPoint(point);
	}

	@Override
	public String toString() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point.trim();
	}

	public String getPoint() {
		return point;
	}

	@Override
	public int hashCode() {
		return point.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BulletPoint)) {
			return false;
		}
		BulletPoint other = (BulletPoint) obj;
		
		return point.equals(other.point);
	}
	
	
	
}

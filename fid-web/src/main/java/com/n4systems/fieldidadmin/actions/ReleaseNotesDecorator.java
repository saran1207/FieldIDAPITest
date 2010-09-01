package com.n4systems.fieldidadmin.actions;

import java.util.List;

import com.n4systems.model.ui.releasenotes.BulletPoint;
import com.n4systems.model.ui.releasenotes.ReleaseNotes;

public class ReleaseNotesDecorator extends ReleaseNotes {

	private final ReleaseNotes delegate;
	private final StrutsAutoGrowingList<BulletPoint> listDelegate;

	public ReleaseNotesDecorator(ReleaseNotes delegate) {
		super();
		this.delegate = delegate;
		this.listDelegate = new StrutsAutoGrowingList<BulletPoint>(delegate.getBullets(), new BulletPointObjectFactory());
	}

	public List<BulletPoint> getBullets() {
		return listDelegate;
	}

	public String getTitle() {
		return getDelegate().getTitle();
	}

	public String getUrl() {
		return getDelegate().getUrl();
	}

	public void setTitle(String title) {
		getDelegate().setTitle(title);
	}

	public void setUrl(String url) {
		getDelegate().setUrl(url);
	}

	public boolean equals(Object obj) {
		return getDelegate().equals(obj);
	}

	public int hashCode() {
		return getDelegate().hashCode();
	}

	public String toString() {
		return getDelegate().toString();
	}

	public ReleaseNotes getDelegate() {
		return delegate;
	}
	
	
	public void cleanInputs() {
		while(getBullets().contains(BulletPoint.emptyBulletPoint())) {
			getBullets().remove(BulletPoint.emptyBulletPoint());
		}
	}
	
}

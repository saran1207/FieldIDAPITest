package com.n4systems.fieldidadmin.actions;

import com.n4systems.model.ui.releasenotes.BulletPoint;

public class BulletPointObjectFactory implements ObjectFactor<BulletPoint> {

	public BulletPoint create() {
		return new BulletPoint("");
	}
	
}

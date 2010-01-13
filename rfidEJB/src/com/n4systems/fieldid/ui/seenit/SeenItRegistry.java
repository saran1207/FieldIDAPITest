package com.n4systems.fieldid.ui.seenit;

import com.n4systems.model.ui.seenit.SeenItItem;

public interface SeenItRegistry {

	public abstract boolean haveISeen(SeenItItem item);

	public abstract void iHaveSeen(SeenItItem item);

	public abstract void iHaveNotSeen(SeenItItem item);

}
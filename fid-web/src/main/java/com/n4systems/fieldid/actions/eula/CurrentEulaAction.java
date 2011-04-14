package com.n4systems.fieldid.actions.eula;


import javax.activation.FileTypeMap;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.eula.EULA;
import com.n4systems.util.ContentTypeUtil;

public class CurrentEulaAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private EULA currentEULA;
	
	public CurrentEulaAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doShow() {
		currentEULA = getLoaderFactory().createCurrentEulaLoader().load();
		getServletResponse().setContentType( ContentTypeUtil.getContentType("something.txt") );
		return SUCCESS;
	}

	public EULA getCurrentEULA() {
		return currentEULA;
	}

}

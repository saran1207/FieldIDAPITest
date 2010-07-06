package com.n4systems.fieldid.actions.location;

import java.util.ArrayList;
import java.util.List;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.viewhelpers.LocationHelper;
import com.n4systems.fieldid.viewhelpers.TrimmedString;
import com.n4systems.uitags.views.Node;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

public class PredefinedLocationCrud extends AbstractCrud {

	private LocationHelper locationHelper = new LocationHelper();
	private List<Node> nodes;
	private List<TrimmedString> infoFieldNames;
	
	public PredefinedLocationCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		nodes = new ArrayList<Node>();
	}

	@Override
	//Need to query DB for data. For now use helper to generate tree.
	protected void loadMemberFields(Long uniqueId) {
		nodes = new ArrayList<Node>(locationHelper.createNodes());
	}

	public String doList() {
		return SUCCESS;
	}

	public List<Node> getNodes() {
		nodes = new ArrayList<Node>(locationHelper.createNodes());
			return nodes;
	}

}

package com.n4systems.fieldid.actions.location;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.viewhelpers.LocationHelper;
import com.n4systems.fieldid.viewhelpers.TrimmedString;
import com.n4systems.uitags.views.HierarchicalNode;

public class PredefinedLocationCrud extends AbstractCrud {

	private LocationHelper locationHelper = new LocationHelper();
	private List<HierarchicalNode> nodes;
	private List<TrimmedString> infoFieldNames;
	
	public PredefinedLocationCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		nodes = new ArrayList<HierarchicalNode>();
	}

	@Override
	//Need to query DB for data. For now use helper to generate tree.
	protected void loadMemberFields(Long uniqueId) {
		nodes = new ArrayList<HierarchicalNode>(locationHelper.createNodes());
	}

	public String doList() {
		return SUCCESS;
	}

	public List<HierarchicalNode> getNodes() {
		nodes = new ArrayList<HierarchicalNode>(locationHelper.createNodes());
			return nodes;
	}

}

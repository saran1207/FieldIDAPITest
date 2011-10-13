package com.n4systems.model.asset;

import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

@SuppressWarnings("serial")
public class SmartSearchWhereClause extends WhereParameterGroup {
	public static final String CLAUSE_NAME = "smart_search_group"; 
	
	private final boolean useIdentifier;
	private final boolean useRfidNumber;
	private final boolean useRefNumber;
	private final String searchText;
	
	public SmartSearchWhereClause(String searchText, boolean useIdentifier, boolean useRfidNumber, boolean useRefNumber) {
		super(CLAUSE_NAME);
		this.searchText = searchText;
		this.useIdentifier = useIdentifier;
		this.useRfidNumber = useRfidNumber;
		this.useRefNumber = useRefNumber;
		
		init();
	}
	
	private void init() {
		if (useIdentifier) {
			addClause(WhereClauseFactory.create("identifier", searchText, WhereParameter.IGNORE_CASE|WhereParameter.TRIM, ChainOp.OR));
		}
		
		if (useRfidNumber) {
			addClause(WhereClauseFactory.create("rfidNumber", searchText, WhereParameter.IGNORE_CASE|WhereParameter.TRIM, ChainOp.OR));
		}
		
		if (useRefNumber) {
			addClause(WhereClauseFactory.create("customerRefNumber", searchText, WhereParameter.IGNORE_CASE|WhereParameter.TRIM, ChainOp.OR));
		}
	}
	
}

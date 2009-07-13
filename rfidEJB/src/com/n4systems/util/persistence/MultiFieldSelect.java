package com.n4systems.util.persistence;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.exceptions.InvalidQueryException;

public class MultiFieldSelect extends SelectClause {
	private static final long serialVersionUID = 1L;
	
	private List<String> fields = new ArrayList<String>();
	
	public MultiFieldSelect() {}
	
	public MultiFieldSelect(List<String> fields) {
		this.fields = fields;
	}
	
	public MultiFieldSelect(String...fields) {
		for(String field: fields) {
			this.fields.add(field);
		}
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	
	@Override
	protected String getClauseArgument(FromTable table) throws InvalidQueryException {
		String clause = "";
		
		if(fields == null || fields.isEmpty()) {
			throw new InvalidQueryException("Multi select queries must specify at least one select field");
		}
		
		boolean firstField = true;
		for(String arg: fields) {
			if(firstField) {
				firstField = false;
			} else {
				clause += ", ";
			}
			
			clause += table.prepareField(arg);
		}

		return clause;
	}

}

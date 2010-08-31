package com.n4systems.util.persistence.search.terms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

public class DateRangeTerm implements SearchTermDefiner {
	private static final long serialVersionUID = 1L;
	private static final String START_FIELD_POSTFIX = "_start";
	private static final String END_FIELD_POSTFIX = "_end";
	
	private String field;
	private Date start;
	private Date end;
	
	public DateRangeTerm() {}
	
	public DateRangeTerm(String field, Date start, Date end) {
		this.field = field;
		this.start = start;
		this.end = end;
	}
	
	public List<WhereClause<?>> getWhereParameters() {
		List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();
		
		if (start != null) {
			params.add(getDateParameter(field, start, true));
		}
		
		if (end != null) {
			params.add(getDateParameter(field, end, false));
		}
		
		return params;
	}
	
	private WhereClause<Date> getDateParameter(String field, Date date, boolean isStart) {
		WhereParameter.Comparator comp = (isStart) ? WhereParameter.Comparator.GE : WhereParameter.Comparator.LE;
		
		String fieldName = StringUtils.pathToName(field);
		fieldName += (isStart) ? START_FIELD_POSTFIX : END_FIELD_POSTFIX;
		
		return new WhereParameter<Date>(comp, fieldName, field, date, null, false);
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}

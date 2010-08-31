package com.n4systems.util.persistence.search.terms;

import java.io.Serializable;
import java.util.List;

import com.n4systems.util.persistence.WhereClause;

public interface SearchTermDefiner extends Serializable {
	public List<WhereClause<?>> getWhereParameters();
}

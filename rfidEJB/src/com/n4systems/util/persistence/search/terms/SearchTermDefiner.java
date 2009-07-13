package com.n4systems.util.persistence.search.terms;

import com.n4systems.util.persistence.WhereParameter;

import java.io.Serializable;
import java.util.List;

public interface SearchTermDefiner extends Serializable {
	public List<WhereParameter<?>> getWhereParameters();
}

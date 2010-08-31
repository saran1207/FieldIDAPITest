package com.n4systems.fieldid.viewhelpers;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public abstract class SearchContainerTestCase {

	protected WhereParameter<?> getSingleWhereClause(BaseSearchDefiner definer) {
		SearchTermDefiner term = getSingleSearchTerm(definer);

		List<WhereClause<?>> wheres = term.getWhereParameters();
		assertEquals("Should be a single where clause", 1, wheres.size());

		return (WhereParameter<?>) wheres.get(0);
	}

	protected SearchTermDefiner getSingleSearchTerm(BaseSearchDefiner definer) {
		List<SearchTermDefiner> terms = definer.getSearchTerms();
		assertEquals("Should be a single search term", 1, terms.size());

		return terms.get(0);
	}
	
	protected WhereParameter<?> getWhereClauseNamed(BaseSearchDefiner definer, String name) {
		List<SearchTermDefiner> terms = definer.getSearchTerms();
		for (SearchTermDefiner term : terms) {
			List<WhereClause<?>> whereParameters = term.getWhereParameters();
			for (WhereClause<?> param : whereParameters) {
				if (name.equals(param.getName())) {
					return (WhereParameter<?>) param;
				}
			}
		}
		return null;
	}

}

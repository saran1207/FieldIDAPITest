package com.n4systems.fieldid.ws.v1.resources.smartsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.OrderClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Component
@Path("smartSearch")
public class ApiSmartSearchResource extends FieldIdPersistenceService {
	private static final int SearchOptions = WhereParameter.IGNORE_CASE|WhereParameter.TRIM|WhereParameter.WILDCARD_RIGHT;
	private static final int MaxResults = 8;
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiSmartSearchSuggestion> getSmartSearchSuggestions(@QueryParam("searchText") String searchText) {
		/*
		 * To get full-text search like functionality, we run individual searches on the identifier, and customerReferenceNumber
		 * sorting by the field length.  Then we merge the two lists.
		 */
		List<ApiSmartSearchSuggestion> identifierSuggestions = loadSuggestions("identifier", searchText);
		List<ApiSmartSearchSuggestion> refNumberSuggestions = loadSuggestions("customerRefNumber", searchText);

		List<ApiSmartSearchSuggestion> suggestions = mergeSuggestions(identifierSuggestions, refNumberSuggestions);
		
		ListResponse<ApiSmartSearchSuggestion> response = new ListResponse<ApiSmartSearchSuggestion>(suggestions, 0, MaxResults, suggestions.size());
		return response;
	}
	
	private List<ApiSmartSearchSuggestion> loadSuggestions(String field, String searchText) {
		QueryBuilder<ApiSmartSearchSuggestion> builder = new QueryBuilder<ApiSmartSearchSuggestion>(Asset.class, securityContext.getUserSecurityFilter());
		builder.setSelectArgument(new NewObjectSelect(ApiSmartSearchSuggestion.class, "mobileGUID", "type.name", "identifier", "customerRefNumber", "length(" + field + ")"));
		
		builder.addWhere(WhereClauseFactory.create(Comparator.LIKE, field, searchText, SearchOptions, ChainOp.AND));
		
		// order by the legnth of the field first (a shorter length will be closer to searchText), then alpha by the field itself
		OrderClause lengthOrder = new OrderClause("length(" + field + ")", true);
		lengthOrder.setAlwaysDropAlias(true);
		builder.getOrderArguments().add(lengthOrder);
		builder.getOrderArguments().add(new OrderClause(field, true));
		
		List<ApiSmartSearchSuggestion> suggestions = persistenceService.findAll(builder, 0, MaxResults);
		return suggestions;
	}

	private List<ApiSmartSearchSuggestion> mergeSuggestions(List<ApiSmartSearchSuggestion> identifierSuggestions, List<ApiSmartSearchSuggestion> refNumberSuggestions) {
		// This removes duplicates and sorts by the field length
        TreeSet<ApiSmartSearchSuggestion> suggestions = new TreeSet<ApiSmartSearchSuggestion>();
		suggestions.addAll(identifierSuggestions);
		suggestions.addAll(refNumberSuggestions);

        ArrayList<ApiSmartSearchSuggestion> suggestionList = new ArrayList<ApiSmartSearchSuggestion>(suggestions);
		return (suggestionList.size() <= MaxResults) ? suggestionList : suggestionList.subList(0, MaxResults);
	}

}

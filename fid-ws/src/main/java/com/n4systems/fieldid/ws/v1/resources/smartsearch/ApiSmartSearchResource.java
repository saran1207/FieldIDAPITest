package com.n4systems.fieldid.ws.v1.resources.smartsearch;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		List<ApiSmartSearchSuggestion> combinedSuggestions = new ArrayList<ApiSmartSearchSuggestion>(identifierSuggestions);
		combinedSuggestions.addAll(refNumberSuggestions);
		
		Collections.sort(combinedSuggestions, new java.util.Comparator<ApiSmartSearchSuggestion>() {
			@Override
			public int compare(ApiSmartSearchSuggestion ss1, ApiSmartSearchSuggestion ss2) {
				return ss1.getFieldLength().compareTo(ss2.getFieldLength());
			}
		});
		
		for(int i = combinedSuggestions.size() - 1; i > 0; i--) {
			ApiSmartSearchSuggestion current = combinedSuggestions.get(i);
			for(int j = 0; j < i; j++) {
				if(combinedSuggestions.get(j).getSid().equals(current.getSid())) {
					combinedSuggestions.remove(i);
					break;
				}
			}
		}
		
		return (combinedSuggestions.size() <= MaxResults) ? combinedSuggestions : combinedSuggestions.subList(0, MaxResults);
	}

}

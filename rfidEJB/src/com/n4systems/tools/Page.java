package com.n4systems.tools;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

/**
 * Contains a single 'page' of results from a ejb3 query.  
 * Provides utility methods for seeing if there are next and previous pages.
 * @author Jesse Miller
 * 
 */


public class Page<k> implements Pager<k> {

	protected static final Integer startingPageNumber = 0;
	
	private List<k> results;
	protected Integer pageSize;
	protected Integer page; // page starts at 0
	private long totalResults;
	private long totalPages;
	private Map<String,Object> resultContextMappings;
	private String resultContextMappingGetStringWeb;
	
	
	@SuppressWarnings("unchecked")
	public Page(Query query, Query countQuery, Integer page, Integer pageSize) {
		
		Long totalResults = (Long)countQuery.getSingleResult();
		this.totalResults = totalResults.longValue();
		this.totalPages = totalResults / pageSize;
		if (totalResults % pageSize > startingPageNumber ) {
			this.totalPages++;
		}
		
		
		this.page = (page != null ) ? page - 1 : startingPageNumber;
		
			
		this.pageSize = pageSize;
		
		// We set the page size to +1 so we know if there is a next page
		results = query.setFirstResult( this.page * this.pageSize ).setMaxResults( this.pageSize + 1 ).getResultList();
		
	}
	
	public boolean isHasNextPage() {
		return results.size() > pageSize;
	}
	
	public boolean isHasPreviousPage() {
		return page > startingPageNumber;
	}
	
	public List<k> getList() {
		return isHasNextPage() ?
				results.subList(0, pageSize) :
				results;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public Integer getPreviousPage() {
		return ( isHasPreviousPage() ? page - 1 : page ) + 1;
	}
	
	public Integer getNextPage() {
		return ( isHasNextPage() ? page + 1 : page ) + 1;
	}
	
	public Integer getCurrentPage() {
		return page;
	}
	
	public int getReadableCurrentPage() {
		return page+1;
	}
	
	public long getTotalResults() {
		return totalResults;
	}
	
	public long getTotalPages() {
		return totalPages;
	}
	
	public boolean hasResults() {
		return ( totalResults > 0 );
	}
	
	public boolean validPage() {
		return ( page <= getLastPage() && page >= startingPageNumber );
	}
		
	public long getLastPage() {
		return totalPages-1;
	}
	
	public Map<String, Object> getResultContextMappings() {
		return resultContextMappings;
	}

	public void setResultContextMappings(Map<String, Object> resultContextMappings) {
		this.resultContextMappings = resultContextMappings;
	}

	public String getResultContextMappingGetStringWeb() {
		if( resultContextMappingGetStringWeb == null ) {
			resultContextMappingGetStringWeb = buildGetString( resultContextMappings );
		} 
		return resultContextMappingGetStringWeb;
	}
	

	
	private static String buildGetString( Map<String, Object> getStringPairs ) {
		StringBuffer getString = new StringBuffer();
		if( getStringPairs != null ) {
			boolean first = true;
			for (Map.Entry<String, Object> entry : getStringPairs.entrySet()) {
				if( !first ) {
					getString.append( '&' );
				} else {
					first = false;
				}
				getString.append( entry.getKey() + "=" + entry.getValue().toString() );
			}
		} else {
			getString.append( "" );
		}
		return getString.toString();
	}
	
}

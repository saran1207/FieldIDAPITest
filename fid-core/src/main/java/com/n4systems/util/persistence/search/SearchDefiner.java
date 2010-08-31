package com.n4systems.util.persistence.search;

//TODO:  see why only the search containers doesn't implement the result transformer method  (ie implements the BaseSearchDefiner only)
public interface SearchDefiner<K> extends Paginated, BaseSearchDefiner {
	public ResultTransformer<K> getTransformer();
}

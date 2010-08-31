package com.n4systems.util.persistence.search;


public class ImmutableSearchDefiner<K> extends ImmutableBaseSearchDefiner implements SearchDefiner<K> {

	private static final long serialVersionUID = 1L;
		
	private final ResultTransformer<K> transformer;
	private final int page;
	private final int pageSize;
	
	public ImmutableSearchDefiner() {
		this(null);
	}
	
	public ImmutableSearchDefiner(SearchDefiner<K> definer) {
		super(definer);
		
		transformer = definer.getTransformer();
		page = definer.getPage();
		pageSize = definer.getPageSize();
		
	}

	public ResultTransformer<K> getTransformer() {
		return transformer;
	}

	public int getPage() {
		return page;
	}

	public int getPageSize() {
		return pageSize;
	}


}

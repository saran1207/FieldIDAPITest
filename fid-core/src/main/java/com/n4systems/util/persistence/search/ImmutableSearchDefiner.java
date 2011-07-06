package com.n4systems.util.persistence.search;

public class ImmutableSearchDefiner<K> extends ImmutableBaseSearchDefiner implements SearchDefiner<K> {

	private static final long serialVersionUID = 1L;
		
	private final ResultTransformer<K> transformer;
	private final int page;
	private final int pageSize;
	
	public ImmutableSearchDefiner(SearchDefiner<K> definer) {
		super(definer);
		
		transformer = definer.getTransformer();
		page = definer.getPage();
		pageSize = definer.getPageSize();
	}

	public ImmutableSearchDefiner(BaseSearchDefiner definer, ResultTransformer<K> transformer, int page, int pageSize) {
		super(definer);

		this.transformer = transformer;
		this.page = page;
		this.pageSize = pageSize;
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

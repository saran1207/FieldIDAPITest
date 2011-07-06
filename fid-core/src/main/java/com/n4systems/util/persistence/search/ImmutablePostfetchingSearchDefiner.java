package com.n4systems.util.persistence.search;

import java.util.List;

public class ImmutablePostfetchingSearchDefiner<T> extends ImmutableSearchDefiner<T> implements PostfetchingDefiner {

    private List<String> postFetchFields;

    public ImmutablePostfetchingSearchDefiner(BaseSearchDefiner definer, ResultTransformer<T> tResultTransformer, int page, int pageSize, List<String> postFetchFields) {
        super(definer, tResultTransformer, page, pageSize);
        this.postFetchFields = postFetchFields;
    }

    @Override
    public List<String> getPostFetchFields() {
        return postFetchFields;
    }

}

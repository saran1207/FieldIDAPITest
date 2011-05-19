package com.n4systems.fieldid.wicket.data;

import com.n4systems.tools.Pager;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import rfid.web.helper.Constants;

import java.io.Serializable;
import java.util.Iterator;

public abstract class PagerAdapterDataProvider<T extends Serializable> extends FieldIDDataProvider<T> {

    protected Pager<T> pager;
    private int pageSize;

    public PagerAdapterDataProvider() {
        this(Constants.PAGE_SIZE);
    }

    public PagerAdapterDataProvider(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Iterator<? extends T> iterator(int first, int count) {
        if (!pagerIsValidFor(first, count)) {
            pager = getPager((first / pageSize) + 1, pageSize);
        }
        return pager.getList().iterator();
    }

    @Override
    public int size() {
        if (pager == null) {
            pager = getPager(1, pageSize);
        }
        return (int) pager.getTotalResults();
    }

    @Override
    public IModel<T> model(T object) {
        return new Model<T>(object);
    }

    protected abstract Pager<T> getPager(int pageNumber, int pageSize);

    protected boolean pagerIsValidFor(int first, int count) {
        return pager != null && pager.getCurrentPage() == (first / pageSize);
    }

    @Override
    public void detach() {
        super.detach();
        pager = null;
    }

}

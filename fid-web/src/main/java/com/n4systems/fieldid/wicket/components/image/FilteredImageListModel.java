package com.n4systems.fieldid.wicket.components.image;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.model.common.EditableImage;
import org.apache.wicket.model.IModel;

import java.util.List;

public class FilteredImageListModel<T extends EditableImage> implements IModel<List<T>> {

    private final IModel<List<T>> model;

    public FilteredImageListModel(IModel<List<T>> model) {
        super();
        this.model = model;
    }

    @Override
    public List<T> getObject() {
        List<T> result = model.getObject();
        Iterable<T> filtered = Iterables.filter(result, new Predicate<T>() {
            @Override
            public boolean apply(T p) {
                return p.getAnnotations().size()>0;
            }
        });
        return Lists.newArrayList(filtered.iterator());
    }

    @Override
    public void setObject(List<T> object) {

    }

    @Override
    public void detach() {

    }
}

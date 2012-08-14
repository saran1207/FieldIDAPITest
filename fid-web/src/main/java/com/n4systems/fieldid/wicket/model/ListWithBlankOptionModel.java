package com.n4systems.fieldid.wicket.model;

import com.n4systems.model.BaseEntity;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class ListWithBlankOptionModel<T extends BaseEntity> extends LoadableDetachableModel<List<T>> {

    private Class<T> clazz;
    private IModel<List<T>> wrappedModel;

    public ListWithBlankOptionModel(Class<T> clazz, IModel<List<T>> wrappedModel) {
        this.clazz = clazz;
        this.wrappedModel = wrappedModel;
    }

    @Override
    protected List<T> load() {
        try {
            T blankOption = clazz.newInstance();
            blankOption.setId(0L);

            List<T> list = new ArrayList<T>(wrappedModel.getObject());
            list.add(0, blankOption);

            return list;
        } catch (Exception e) {
            throw new RuntimeException("could not instantiate list item class", e);
        }
    }

}

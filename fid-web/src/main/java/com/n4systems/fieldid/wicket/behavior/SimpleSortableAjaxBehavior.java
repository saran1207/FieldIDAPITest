package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

public abstract class SimpleSortableAjaxBehavior extends SortableAjaxBehavior {

    @Override
    public void onReceive(Component sortedComponent, int index, Component parentSortedComponent, AjaxRequestTarget ajaxRequestTarget) {
    }

    @Override
    public void onRemove(Component sortedComponent, AjaxRequestTarget ajaxRequestTarget) {
    }

    @Override
    public void onUpdate(Component sortedComponent, int index, AjaxRequestTarget ajaxRequestTarget) {
    }

}

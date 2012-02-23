package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

@SuppressWarnings("serial")
public class SimpleSortableAjaxBehavior<T extends Component> extends SortableAjaxBehavior<T> {

    @Override
    public void onReceive(T sortedComponent, int index, Component parentSortedComponent, AjaxRequestTarget ajaxRequestTarget) {
    }

    @Override
    public void onRemove(T sortedComponent, AjaxRequestTarget ajaxRequestTarget) {
    }

    @Override
    public void onUpdate(T sortedComponent, int index, AjaxRequestTarget ajaxRequestTarget) {
    }
}

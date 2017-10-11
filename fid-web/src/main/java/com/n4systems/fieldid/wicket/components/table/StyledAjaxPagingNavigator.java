package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.model.Model;

public class StyledAjaxPagingNavigator extends AjaxPagingNavigator implements StyledAjaxPagingLinkListener {

    private Component repaintTarget;
    private TextField<Integer> currentPageField;

    public StyledAjaxPagingNavigator(final String id, final IPageable pageable, Component repaintTarget)
    {
        super(id, pageable);
        this.repaintTarget = repaintTarget;
    }

    public StyledAjaxPagingNavigator(final String id, final IPageable pageable,
                                     final IPagingLabelProvider labelProvider, Component repaintTarget) {
        super(id, pageable, labelProvider);
        this.repaintTarget = repaintTarget;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/featureStyles/pagination.css");
    }

    @Override
    protected void onBeforeRender() {
        if (get("first") == null) {
            currentPageField = new TextField<Integer>("currentPageNumber", Model.of(1));
            add(currentPageField);
            currentPageField.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    Object val = getComponent().getDefaultModelObject();
                    boolean resetPageToCurrent = true;
                    if (val != null && !val.toString().trim().isEmpty()) {
                        try {
                            int newPage = Integer.parseInt(val.toString());
                            if (newPage > 0 && newPage <= getPageable().getPageCount()) {
                                resetPageToCurrent = false;
                                currentPageField.getModel().setObject(new Integer(newPage));
                                target.add(currentPageField);
                                getPageable().setCurrentPage(newPage-1);
                                target.add(repaintTarget);
                            }
                        }
                        catch(NumberFormatException ex) {
                            // user typed non numeric characters - ignore input and reset number
                        }
                    }
                    if (resetPageToCurrent) {
                        currentPageField.getModel().setObject(new Integer(getPageable().getCurrentPage() + 1));
                        target.add(currentPageField);
                    }
                }
            });
        }
        super.onBeforeRender();
    }

    @Override
    protected Link<?> newPagingNavigationIncrementLink(String id, IPageable pageable, int increment) {
        return new StyledAjaxPagingNavigationIncrementLink(id, pageable, increment, this);
    }

    @Override
    protected Link<?> newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
        return new StyledAjaxPagingNavigationLink(id, pageable, pageNumber, true,this);
    }

    @Override
    protected PagingNavigation newNavigation(final String id, final IPageable pageable,
                                             final IPagingLabelProvider labelProvider)
    {
        return new StyledAjaxPagingNavigation(id, pageable, labelProvider, this);
    }

    @Override
    public boolean isVisible() {
        return (getPageable().getPageCount() > 1) && super.isVisible();
    }

    public int getNumPages() {
        return getPageable().getPageCount();
    }

    @Override
    public void onCurrentPageChange(AjaxRequestTarget target) {
        currentPageField.getModel().setObject(new Integer(getPageable().getCurrentPage()+1));
        target.add(currentPageField);
    }
}

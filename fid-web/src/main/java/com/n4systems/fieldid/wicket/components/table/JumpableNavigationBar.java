package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.ClickOnComponentWhenEnterKeyPressedBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.ModalLoadingPanel;

public class JumpableNavigationBar extends Panel implements IAjaxIndicatorAware {

    WebMarkupContainer paginationContainer;
    protected final DataTable<?> table;
    protected final SimpleDataTable<?> simpleDataTable;
    protected final ModalLoadingPanel loadingPanel;
    
	public JumpableNavigationBar(String id, SimpleDataTable<?> simpleDataTable) {
		super(id);
        this.simpleDataTable = simpleDataTable;
        this.table = simpleDataTable.getTable();
        setOutputMarkupId(true);
        add(CSSPackageResource.getHeaderContribution("style/featureStyles/pagination.css"));

        paginationContainer = new WebMarkupContainer("paginationContainer") {
            @Override
            public boolean isVisible() {
                return getTable().getPageCount() > 1;
            }
        };

		paginationContainer.add(new AttributeModifier("colspan", true, new Model<String>(
			String.valueOf(table.getColumns().length))));

        addPreviousLinks(paginationContainer);
        addNextLinks(paginationContainer);
        addPageNavLinks(paginationContainer);

        paginationContainer.add(new JumpForm("jumpForm"));
        add(paginationContainer);
        
        loadingPanel = new ModalLoadingPanel("loadingAnim", table);
        add(loadingPanel);
	}

    private void addNextLinks(WebMarkupContainer pagerContainer) {
        pagerContainer.add(createGotoRelativePageLink("nextLink", 1, ifNotOnLastPage()));
        pagerContainer.add(createGotoLastPageLink("lastLink", ifNotOnLastPage()));
    }

    private void addPreviousLinks(WebMarkupContainer pagerContainer) {
        pagerContainer.add(createGotoRelativePageLink("prevLink", -1, ifNotOnFirstPage()));
        pagerContainer.add(createGotoPageLink("firstLink", 1, ifNotOnFirstPage()));
    }

    private void addPageNavLinks(WebMarkupContainer pagerContainer) {
        pagerContainer.add(new ListView<Integer>("pageList", new PagesToLinkToModel(getTable())) {
            @Override
            protected void populateItem(final ListItem<Integer> item) {
                Integer pageLinkNumber = item.getModelObject() - 1;
                final boolean isCurrentPage = pageLinkNumber.equals(getTable().getCurrentPage());
                if (isCurrentPage) {
                    item.add(new SimpleAttributeModifier("class", "currentPage"));
                }
                AjaxLink pageLink;
                item.add(pageLink = createGotoPageLink("pageLink", item.getModelObject(), new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return !isCurrentPage;
                    }
                }));
                pageLink.add(new FlatLabel("pageLabel", item.getModelObject()+""));
                item.add(new Label("pageLabel", item.getModelObject()+"").setVisible(isCurrentPage));
            }
        });
    }

    class JumpForm extends Form {
        public JumpForm(String id) {
            super(id);

            AjaxButton hiddenSubmitButton;
            TextField<Integer> jumpPageField;

            add(new FlatLabel("numPages", new PropertyModel<String>(getTable(), "pageCount")));
            add(jumpPageField = new TextField<Integer>("jumpPage", new PropertyModel<Integer>(this, "jumpPage")));
            add(hiddenSubmitButton = new AjaxButton("hiddenJumpButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(simpleDataTable);
                    onPageChanged(target);
                }
            });

            jumpPageField.add(new ClickOnComponentWhenEnterKeyPressedBehavior(hiddenSubmitButton));
        }

        public Integer getJumpPage() {
            return getTable().getCurrentPage() + 1;
        }

        public void setJumpPage(Integer page) {
            getTable().setCurrentPage(Math.max(0, page - 1));
        }
    }

    protected AjaxLink createGotoPageLink(String linkId, final int pageNumber, final Predicate visiblePredicate) {
        return new AjaxLink(linkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getTable().setCurrentPage(pageNumber - 1);
                target.addComponent(simpleDataTable);
                onPageChanged(target);
            }

            @Override
            public boolean isVisible() {
                return visiblePredicate.evaluate();
            }
        };
    }

    private AjaxLink createGotoRelativePageLink(String linkId, final int offset, final Predicate visiblePredicate) {
        return new AjaxLink(linkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getTable().setCurrentPage(getTable().getCurrentPage() + offset);
                target.addComponent(simpleDataTable);
                onPageChanged(target);
            }

            @Override
            public boolean isVisible() {
                return visiblePredicate.evaluate();
            }
        };
    }

    private AjaxLink createGotoLastPageLink(String linkId, final Predicate visiblePredicate) {
        return new AjaxLink(linkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getTable().setCurrentPage(getTable().getPageCount() - 1);
                target.addComponent(simpleDataTable);
                onPageChanged(target);
            }

            @Override
            public boolean isVisible() {
                return visiblePredicate.evaluate();
            }
        };
    }

    protected Predicate ifNotOnFirstPage() {
        return new Predicate() {
            @Override
            public boolean evaluate() {
                return getTable().getCurrentPage() > 0;
            }
        };
    }

    protected Predicate ifNotOnLastPage() {
        return new Predicate() {
            @Override
            public boolean evaluate() {
                return getTable().getCurrentPage() + 1 < getTable().getPageCount();
            }
        };
    }

    protected DataTable<?> getTable() {
        return table;
    }
    
    @Override
	public String getAjaxIndicatorMarkupId() {
		return loadingPanel.getMarkupId();
	}
    
    protected void onPageChanged(AjaxRequestTarget target) { }

}

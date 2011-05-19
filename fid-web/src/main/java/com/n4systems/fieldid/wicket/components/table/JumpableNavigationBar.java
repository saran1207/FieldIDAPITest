package com.n4systems.fieldid.wicket.components.table;

import com.n4systems.fieldid.utils.Predicate;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class JumpableNavigationBar extends Panel {

    WebMarkupContainer paginationContainer;
    protected final DataTable<?> table;

	public JumpableNavigationBar(String id, DataTable<?> table) {
		super(id);
        this.table = table;
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
	}

    private void addNextLinks(WebMarkupContainer pagerContainer) {
        pagerContainer.add(createGotoRelativePageLink("nextLink", 1, ifNotOnLastPage()));
        pagerContainer.add(createGotoPageLink("lastLink", getTable().getPageCount(), ifNotOnLastPage()));
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
                Link pageLink;
                item.add(pageLink = createGotoPageLink("pageLink", item.getModelObject(), new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return !isCurrentPage;
                    }
                }));
                pageLink.add(new Label("pageLabel", item.getModelObject()+"").setRenderBodyOnly(true));
                item.add(new Label("pageLabel", item.getModelObject()+"").setVisible(isCurrentPage));
            }
        });
    }

    class JumpForm extends Form {
        public JumpForm(String id) {
            super(id);

            add(new Label("numPages", getTable().getPageCount()+"").setRenderBodyOnly(true));
            add(new TextField<Integer>("jumpPage", new PropertyModel<Integer>(this, "jumpPage")));
        }

        public Integer getJumpPage() {
            return getTable().getCurrentPage() + 1;
        }

        public void setJumpPage(Integer page) {
            getTable().setCurrentPage(page - 1);
        }
    }

    protected Link createGotoPageLink(String linkId, final int pageNumber, final Predicate visiblePredicate) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                getTable().setCurrentPage(pageNumber - 1);
            }

            @Override
            public boolean isVisible() {
                return visiblePredicate.evaluate();
            }
        };
    }

    private Component createGotoRelativePageLink(String linkId, final int offset, final Predicate visiblePredicate) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                getTable().setCurrentPage(getTable().getCurrentPage() + offset);
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

}

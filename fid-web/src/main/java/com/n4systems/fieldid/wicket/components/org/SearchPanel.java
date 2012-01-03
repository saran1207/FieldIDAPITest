package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.ClickOnComponentWhenEnterKeyPressedBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.BaseOrgListLoader;
import com.n4systems.tools.Pager;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class SearchPanel extends Panel {

    private IModel<BaseOrg> orgModel;
    private List<BaseOrg> orgs;
    private boolean alreadySearched = false;
    private Integer displayingResults = 0;
    private Long totalResults = 0L;

    public SearchPanel(String id, final IModel<BaseOrg> model) {
        super(id);
        this.orgModel = model;
        setOutputMarkupPlaceholderTag(true);

        add(new SearchForm("searchForm"));

        add(new ListView<BaseOrg>("results", new PropertyModel<List<BaseOrg>>(this, "orgs")) {
            @Override
            protected void populateItem(final ListItem<BaseOrg> item) {
                AjaxLink pickLink = new AjaxLink("pickLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        orgModel.setObject(item.getModelObject());
                        onOrgSelected(target);
                    }
                };
                pickLink.add(new FlatLabel("orgNameLabel", new PropertyModel<String>(item.getModel(), "name")));

                item.add(pickLink);
                item.add(new HierarchyDisplayPanel("hierarchy", item.getModel()));
            }
        });

        add(new WebMarkupContainer("noResultsContainer") {
            @Override
            public boolean isVisible() {
                return alreadySearched && !hasResults();
            }
        });

        WebMarkupContainer resultCountContainer = new WebMarkupContainer("resultCountContainer") {
            @Override
            public boolean isVisible() {
                return alreadySearched && hasResults();
            }
        };
        resultCountContainer.add(new WebMarkupContainer("refineContainer") {
            @Override
            public boolean isVisible() {
                return hasResults() && totalResults > displayingResults;
            }
        });
        resultCountContainer.add(new FlatLabel("displayingResults", new PropertyModel<Integer>(this, "displayingResults")));
        resultCountContainer.add(new FlatLabel("totalResults", new PropertyModel<Long>(this, "totalResults")));
        add(resultCountContainer);
    }

    class SearchForm extends Form {

        private String searchText;

        public SearchForm(String id) {
            super(id);

            TextField<String> searchTextField;
            AjaxButton searchButton;
            add(searchTextField = new TextField<String>("searchText", new PropertyModel<String>(this, "searchText")));
            add(searchButton = new AjaxButton("searchButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    alreadySearched = true;

                    Pager<BaseOrg> pager = new BaseOrgListLoader(FieldIDSession.get().getSessionUser().getSecurityFilter())
                            .setSearchName(searchText)
                            .setOrgType("all")
                            .setPage(10)
                            .setFirstPage()
                            .load();

                    orgs = pager.getList();
                    displayingResults = orgs.size();
                    totalResults = pager.getTotalResults();

                    target.add(SearchPanel.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });

            searchTextField.add(new ClickOnComponentWhenEnterKeyPressedBehavior(searchButton));
        }

    }

    private boolean hasResults() {
        return orgs != null && !orgs.isEmpty();
    }

    protected void onOrgSelected(AjaxRequestTarget target) { }

}

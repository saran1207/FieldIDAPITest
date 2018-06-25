package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.List;


abstract public class CustomerFilterPanel extends Panel {

    private LoaderFactory loaderFactory;
    private List<ListingPair> internalOrgList;
    private IModel<String> nameFilterValue;
    private IModel<String> idFilterValue;
    private IModel<ListingPair> orgFilterValue;

    public CustomerFilterPanel(String id, LoaderFactory loaderFactory) {
        super(id);
        this.loaderFactory = loaderFactory;
        nameFilterValue = Model.of("");
        idFilterValue = Model.of("");
        orgFilterValue = Model.of((ListingPair) null);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/customerFilterPanel.css");
    }

    private void addComponents() {
        Form filterForm = new Form("listFilterForm");
        final TextField nameFilter = new TextField("nameFilter", nameFilterValue);
        filterForm.add(nameFilter);
        final TextField idFilter = new TextField("idFilter", idFilterValue);
        filterForm.add(idFilter);

        final DropDownChoice<ListingPair> orgFilter = new DropDownChoice<ListingPair>("orgFilter",
                orgFilterValue,
                new LoadableDetachableModel<List<ListingPair>>() {
                    @Override
                    protected List<ListingPair> load() {
                        List<ListingPair> orgs = getParentOrgs();
                        return orgs;
                    }
                },
                new IChoiceRenderer<ListingPair>() {
                    @Override
                    public Object getDisplayValue(ListingPair object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(ListingPair object, int index) {
                        return object.getId().toString();
                    }
                }
        ) {
            protected String getNullValidKey()
            {
                return "DropDownListAllOption";
            }
        };
        orgFilter.setNullValid(true);
        filterForm.add(orgFilter);
        filterForm.add(new AjaxButton("filterSubmit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                applyFilter(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        });
        filterForm.add(new AjaxSubmitLink("clearSubmit") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                nameFilterValue.setObject("");
                idFilterValue.setObject("");
                orgFilterValue.setObject(null);
                target.add(nameFilter);
                target.add(idFilter);
                target.add(orgFilter);
                applyClear(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        });
        add(filterForm);
    }

    public String getNameFilter() {
        String val = nameFilterValue.getObject();
        return (val == null || val.isEmpty()) ? null : val.trim();
    }

    public String getIdFilter() {
        String val = idFilterValue.getObject();
        return (val == null || val.isEmpty()) ? null : val.trim();
    }

    public Long getOrgFilter() {
        ListingPair lp = orgFilterValue.getObject();
        return (lp != null) ? lp.getId() : null;
    }

    abstract protected void applyFilter(AjaxRequestTarget target);

    abstract protected void applyClear(AjaxRequestTarget target);

    private LoaderFactory getLoaderFactory() {
        return loaderFactory;
    }

    private List<ListingPair> getParentOrgs() {
        if( internalOrgList == null ) {
            List<Listable<Long>> orgListables = getLoaderFactory().createInternalOrgListableLoader().load();
            internalOrgList = ListHelper.longListableToListingPair(orgListables);
        }
        return internalOrgList;
    }
}

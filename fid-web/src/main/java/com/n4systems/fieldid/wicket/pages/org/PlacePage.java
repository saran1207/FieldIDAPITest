package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.PlaceActionGroup;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.org.OrgViewPage;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;


import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class PlacePage extends FieldIDTemplatePage {

    protected IModel<BaseOrg> orgModel;

    public PlacePage(PageParameters params) {
        orgModel = new EntityModel<BaseOrg>(BaseOrg.class, params.get("id").toLong());
    }

    public PlacePage(IModel<BaseOrg> model) {
        orgModel = model;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new PropertyModel<String>(orgModel, "name"));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        BaseOrg org = orgModel.getObject();
        List<NavigationItem> navItems = createBreadCrumbs(org);
        add(new BreadCrumbBar(breadCrumbBarId, navItems.toArray(new NavigationItem[0])));
    }

    protected List<NavigationItem> createBreadCrumbs(BaseOrg org) {
        List<NavigationItem> navItems = Lists.newArrayList();

        navItems.add(aNavItem().label("label.places").page(OrgViewPage.class).build());

        if(org.getPrimaryOrg() != null) {
            navItems.add(aNavItem().label(new PropertyModel<String>(org.getPrimaryOrg(), "name")).page(this.getClass()).params(PageParametersBuilder.id(org.getPrimaryOrg().getId())).build());
        }
        if(org.getCustomerOrg() != null) {
            navItems.add(aNavItem().label(new PropertyModel<String>(org.getCustomerOrg(), "name")).page(this.getClass()).params(PageParametersBuilder.id(org.getCustomerOrg().getId())).build());
        }
        if(org.getDivisionOrg() != null) {
            navItems.add(aNavItem().label(new PropertyModel<String>(org.getDivisionOrg(), "name")).page(this.getClass()).params(PageParametersBuilder.id(org.getDivisionOrg().getId())).build());
        }
        return navItems;
    }

    @Override
    protected void addNavBar(String navBarId) {
        Long orgId = orgModel.getObject().getId();
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.summary")).page(PlaceSummaryPage.class).params(PageParametersBuilder.id(orgId)).build(),
                aNavItem().label(new FIDLabelModel("label.events")).page(PlaceEventsPage.class).params(PageParametersBuilder.id(orgId)).build()
        //        aNavItem().label(new FIDLabelModel("label.people")).page(PlacePeoplePage.class).params(PageParametersBuilder.id(orgId)).build()
        ));
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new PlaceActionGroup(actionGroupId,orgModel);
    }

    protected BaseOrg getOrg() {
        return orgModel.getObject();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pages/places.css");
    }
}

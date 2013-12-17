package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class PlaceEventTypesPage extends PlacePage {

    private @SpringBean PlaceService placeService;
    private @SpringBean PersistenceService persistenceService;

    private List<PlaceEventType> eventTypes;

    public PlaceEventTypesPage(IModel<BaseOrg> model) {
        super(model);
        init();
    }

    public PlaceEventTypesPage(PageParameters params) {
        super(params);
        init();
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.associate_event_types"));
    }

    @Override
    protected List<NavigationItem> createBreadCrumbs(BaseOrg org) {
        List<NavigationItem> navItems = super.createBreadCrumbs(org);
        navItems.add(aNavItem().label(new FIDLabelModel("label.event_types")).page(getClass()).params(PageParametersBuilder.id(org.getId())).build());
        return navItems;
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new WebMarkupContainer(actionGroupId).setVisible(false);
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId).setVisible(false));
    }

    private void init() {
        eventTypes = Lists.newArrayList(orgModel.getObject().getEventTypes());

        add(new Form("form")
                .add(new MultiSelectDropDownChoice<PlaceEventType>("eventTypes", new PropertyModel(this, "eventTypes"), getAllEventTypes(), new EventTypeChoiceRenderer<PlaceEventType>()))
                        .add(new AjaxSubmitLink("submit") {
                            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                                getOrg().setEventTypes(Sets.newHashSet(eventTypes));
                                persistenceService.update(getOrg());
                                backToSummaryPage();
                            }

                            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {

                            }
                        })
                        .add(new AjaxLink("cancel") {
                            @Override public void onClick(AjaxRequestTarget target) {
                                backToSummaryPage();
                            }
                        }));
    }

    private void backToSummaryPage() {
        setResponsePage(PlaceSummaryPage.class, PageParametersBuilder.id(getOrg().getId()));
    }


    private List<PlaceEventType> getAllEventTypes() {
        return placeService.getEventTypes();
    }

}

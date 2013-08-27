package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class EventTypeAssociationsPage extends FieldIDFrontEndPage {

    private IModel<AssetType> assetType;

    @SpringBean
    private AssetTypeService assetTypeService;

    @SpringBean
    private EventTypeService eventTypeService;

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;

    private List<EventType> selectedTypes;
    private Form form;

    public EventTypeAssociationsPage(PageParameters params) {
        super(params);
        assetType = Model.of(assetTypeService.getAssetType(params.get("uniqueID").toLong()));

        selectedTypes = getSelectedTypes(assetType.getObject().getAssociatedEventTypes());

        add(new FIDFeedbackPanel("feedbackPanel"));

        form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                try{
                    associatedEventTypesService.addAndRemove(assetType.getObject(), selectedTypes);
                    info(new FIDLabelModel("message.eventtypesselected").getObject());
                }catch (Exception e) {
                    error(new FIDLabelModel("error.failedtosaveeventtypeselection").getObject());
                }
            }
        };

        form.add(new ListView<EventType>("eventType", eventTypeService.getAllEventTypesExcludingActions()) {
            @Override
            protected void populateItem(final ListItem<EventType> item) {
                final IModel<Boolean> checked = Model.of(getCheckedValue(item.getModelObject()));
                CheckBox checkBox;
                item.add(checkBox = new CheckBox("checked", checked));
                checkBox.add(new OnChangeAjaxBehavior() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        EventType eventType = item.getModelObject();
                        if(checked.getObject()) {
                            selectedTypes.add(eventType);
                        } else if(selectedTypes.contains(eventType)) {
                            selectedTypes.remove(eventType);
                        }
                    }
                });
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
            }
        });

        form.add(new AjaxLink<Void>("all") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                selectedTypes.clear();
                selectedTypes.addAll(eventTypeService.getAllEventTypesExcludingActions());
                target.add(form);
            }
        });

        form.add(new AjaxLink<Void>("none") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                selectedTypes.clear();
                target.add(form);
            }
        });
        form.add(new SubmitLink("submit"));

        form.setOutputMarkupId(true);

        add(form);
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.asset_type_select_event_types"));
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.event_type_associations"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        Long assetTypeId = assetType.getObject().getId();
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build(),
                aNavItem().label("nav.view").page("assetType.action").params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.edit").page(EditAssetTypePage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.event_type_associations").page(EventTypeAssociationsPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.schedules").page(AssetTypeSchedulesPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("label.subassets").page("assetTypeConfiguration.action").params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build()
        ));
    }

    private List<EventType> getSelectedTypes(Set<AssociatedEventType> associatedEventTypes) {
        List<EventType> selectedTypes = Lists.newArrayList();
        for (AssociatedEventType type: associatedEventTypes) {
            selectedTypes.add(type.getEventType());
        }
        return selectedTypes;
    }

    private Boolean getCheckedValue(EventType eventType) {
        return selectedTypes.contains(eventType);
    }
}

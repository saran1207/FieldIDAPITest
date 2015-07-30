package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.fieldid.service.event.massevent.MassEventService;
import com.n4systems.fieldid.wicket.components.eventtype.GroupedEventTypePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.assetsearch.SearchPage;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class SelectMassEventPage extends FieldIDTemplatePage {

    @SpringBean
    private MassEventService massEventService;

    private IModel<AssetSearchCriteria> criteriaModel;
    private IModel<EventType> eventTypeModel;

    public SelectMassEventPage(IModel<AssetSearchCriteria> criteriaModel) {
        this.criteriaModel = criteriaModel;
        this.eventTypeModel = Model.of(new ThingEventType());

        add(new FIDFeedbackPanel("feedbackPanel"));

        MultiIdSelection selectedAssetIds = criteriaModel.getObject().getSelection();

        Form form;
        add(form = new Form<Void>("form"));

        form.add(new Label("selectMessage",
                new FIDLabelModel("message.select_event_type_to_perform_on_assets", selectedAssetIds.getNumSelectedIds())));

        List<EventType> commonEventTypes = massEventService.getCommonEventTypesForAssets(selectedAssetIds.getSelectedIds());

        form.add(new GroupedEventTypePicker("eventType", eventTypeModel, new ListModel(commonEventTypes)).setRequired(true));

        form.add(new SubmitLink("submitLink") {
            @Override
            public void onSubmit() {
                setResponsePage(new SelectSchedulesPage(criteriaModel, eventTypeModel));
            }
        });

        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new SearchPage(criteriaModel.getObject()));
            }
        });

        form.setVisible(!commonEventTypes.isEmpty());

        add(new WebMarkupContainer("noCommonEventTypesMessage").setVisible(commonEventTypes.isEmpty()));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.select_event_type_to_perform"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                setResponsePage(new SearchPage(criteriaModel.getObject()));
            }
        }.add(new Label(linkLabelId, new FIDLabelModel("label.back_to_x", new FIDLabelModel("label.search_results").getObject())));
    }

}

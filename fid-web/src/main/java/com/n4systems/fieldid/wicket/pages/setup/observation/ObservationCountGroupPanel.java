package com.n4systems.fieldid.wicket.pages.setup.observation;

import com.n4systems.fieldid.service.event.ObservationCountService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.eventform.EditCopyDeleteItemPanel;
import com.n4systems.fieldid.wicket.components.eventform.SortableListPanel;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountGroup;
import com.n4systems.model.Score;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import java.util.ArrayList;
import java.util.List;

public class ObservationCountGroupPanel extends SortableListPanel {

    @SpringBean
    private ObservationCountService observationCountService;

    private ObservationCountGroupForm observationCountGroupForm;
    private IModel<ObservationCountGroup> observationCountGroupModel;
    private SortableAjaxBehavior sortableBehavior;
    private Boolean reorderState = false;


    public ObservationCountGroupPanel(String id, IModel<ObservationCountGroup> model) {
        super(id);
        this.observationCountGroupModel = model;
        setDefaultModel();
        setOutputMarkupPlaceholderTag(true);

        add(new WebMarkupContainer("blankInstructions") {
            @Override
            public boolean isVisible() {
                return !isGroupSelected() && (getObservationCounts() == null || getObservationCounts().size() > 0);
            }
        });

        add(new TwoStateAjaxLink("reorderScoresButton", "Reorder Scores", "Done Reordering") {
            List<ObservationCount> observationCounts;  // transient model backing while we are juggling the list around.
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.add(ObservationCountGroupPanel.this);
                sortableBehavior.setDisabled(true);
                reorderState = false;
                observationCountGroupModel.getObject().setObservationCounts(observationCounts);
                observationCountService.saveOrUpdate(observationCountGroupModel.getObject());
                ObservationCountGroupPanel.this.setDefaultModel();
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.add(ObservationCountGroupPanel.this);
                observationCounts = new ArrayList<ObservationCount>();
                observationCounts.addAll(getObservationCounts());
                ObservationCountGroupPanel.this.setDefaultModel(new PropertyModel<List<Score>>(this, "observationCounts"));
                sortableBehavior.setDisabled(false);
                reorderState = true;
            }

            @Override
            public boolean isVisible() {
                return isGroupSelected();
            }
        });

        WebMarkupContainer sortableObservationCountsContainer = new WebMarkupContainer("sortableObservationCountsContainer");

        sortableObservationCountsContainer.add(new ListView<ObservationCount>("countsList", getObservationCountsModel()) {
            @Override
            protected void populateItem(final ListItem<ObservationCount> item) {
                item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("count", new PropertyModel<String>(item.getModel(), "name"), createSubtitleModel(item.getModel()), false) {
                    {
                        setStoreLabel(new FIDLabelModel("label.save"));
                    }

                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        observationCountService.archiveObservationCount(observationCountGroupModel.getObject(), item.getModelObject());
                        target.add(ObservationCountGroupPanel.this);
                    }

                    @Override
                    protected void onStoreLinkClicked(AjaxRequestTarget target) {
                        observationCountService.updateObservationCount(observationCountGroupModel.getObject(), item.getModelObject());
                        target.add(ObservationCountGroupPanel.this);
                    }

                    @Override
                    protected boolean isReorderState() {
                        return reorderState;
                    }

                    @Override
                    public int getTextDisplayLimit() {
                        return 40;
                    }
                });
            }
        });

        sortableObservationCountsContainer.add(sortableBehavior = makeSortableBehavior());

        add(sortableObservationCountsContainer);

        add(observationCountGroupForm = new ObservationCountGroupForm("observationCountGroupForm", model){
            @Override
            public boolean isVisible() {
                return isGroupSelected();
            }
        });
    }

    private IModel<String> createSubtitleModel(IModel<ObservationCount> model) {
        if (model.getObject().isCounted())
            return new FIDLabelModel("label.included_in_total");
        else
            return new FIDLabelModel("label.not_included_in_total");
    }

    private boolean isGroupSelected() {
        return getDefaultModel().getObject() != null;
    }

    private IModel<List<ObservationCount>> getObservationCountsModel() {
        return (IModel<List<ObservationCount>>) getDefaultModel();
    }

    private List<ObservationCount> getObservationCounts() {
        return (List<ObservationCount>) getDefaultModel().getObject();
    }

    private void setDefaultModel() {
        setDefaultModel(new PropertyModel<List<ObservationCount>>(observationCountGroupModel, "observationCounts"));
    }

    @Override
    protected int getIndexOfComponent(Component component) {
        ObservationCount count = (ObservationCount) component.getDefaultModelObject();
        return getObservationCounts().indexOf(count);
    }

    @Override
    protected void onItemMoving(int oldIndex, int newIndex, AjaxRequestTarget target) {
        ObservationCount movingCount = getObservationCounts().remove(oldIndex);
        getObservationCounts().add(newIndex, movingCount);
    }

    private class ObservationCountGroupForm extends Form<ObservationCountGroup> {

        private ObservationCount observationCount;
        private FeedbackPanel feedbackPanel;
        private IModel<ObservationCountGroup> model;

        public ObservationCountGroupForm(String id, IModel<ObservationCountGroup> groupModel) {
            super(id, groupModel);

            model = groupModel;
            observationCount = new ObservationCount();
            setOutputMarkupId(true);

            add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", ObservationCountGroupForm.this));
            feedbackPanel.setOutputMarkupPlaceholderTag(true);

            add(new NewObservationCountPanel("newObservationCount", new PropertyModel<>(this, "observationCount")));

            add(new AjaxSubmitLink("saveLink") {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    observationCount.setTenant(FieldIDSession.get().getTenant());
                    observationCountService.addObservationCount(getModelObject(), observationCount);
                    observationCount = new ObservationCount();
                    groupModel.detach();
                    target.add(ObservationCountGroupPanel.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });


        }
    }
}

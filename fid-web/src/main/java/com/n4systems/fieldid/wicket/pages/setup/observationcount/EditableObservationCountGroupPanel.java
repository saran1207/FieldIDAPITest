package com.n4systems.fieldid.wicket.pages.setup.observationcount;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.ObservationCountService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.ajax.ConfirmAjaxCallDecorator;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.eventform.EditCopyDeleteItemPanel;
import com.n4systems.fieldid.wicket.components.eventform.SortableListPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.NoBarsValidator;
import com.n4systems.fieldid.wicket.util.NoColonsValidator;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountGroup;
import com.n4systems.model.Score;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import java.util.ArrayList;
import java.util.List;

public class EditableObservationCountGroupPanel extends SortableListPanel {

    @SpringBean
    private ObservationCountService observationCountService;

    private ObservationCountGroupForm observationCountGroupForm;
    private IModel<ObservationCountGroup> observationCountGroupModel;
    private SortableAjaxBehavior sortableBehavior;
    private Boolean reorderState = false;

    private FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer sortableObservationCountsContainer;

    private List<ObservationCount> observationCountList;

    public EditableObservationCountGroupPanel(String id, IModel<ObservationCountGroup> model) {
        super(id);
        this.observationCountGroupModel = model;
        if(observationCountGroupModel.getObject() != null) {
            observationCountList= Lists.newArrayList(observationCountGroupModel.getObject().getObservationCounts());
        }

        setOutputMarkupPlaceholderTag(true);

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupPlaceholderTag(true);

        add(new WebMarkupContainer("blankInstructions") {
            @Override
            public boolean isVisible() {
                return !isGroupSelected() && (observationCountList == null || observationCountList.size() > 0);
            }
        });

        add(new TwoStateAjaxLink("reorderScoresButton", "Reorder Scores", "Done Reordering") {
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.add(sortableObservationCountsContainer);
                sortableBehavior.setDisabled(true);
                reorderState = false;
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.add(sortableObservationCountsContainer);
                sortableBehavior.setDisabled(false);
                reorderState = true;
            }

            @Override
            public boolean isVisible() {
                return isGroupSelected();
            }
        });

        sortableObservationCountsContainer = new WebMarkupContainer("sortableObservationCountsContainer");
        sortableObservationCountsContainer.setOutputMarkupPlaceholderTag(true);

        sortableObservationCountsContainer.add(new ListView<ObservationCount>("countsList", getObservationCountsModel()) {
            @Override
            protected void populateItem(final ListItem<ObservationCount> item) {
                item.setOutputMarkupId(true);
                EditCopyDeleteItemPanel editCopyDeleteItemPanel;
                item.add(editCopyDeleteItemPanel = new EditCopyDeleteItemPanel("count", new PropertyModel<String>(item.getModel(), "name"), createSubtitleModel(item.getModel()), false) {
                    {
                        setStoreLabel(new FIDLabelModel("label.save"));
                        setEditMaximumLength(10);
                    }

                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        observationCountList.remove(item.getModelObject());
                        target.add(sortableObservationCountsContainer, observationCountGroupForm);
                    }

                    @Override
                    protected void onStoreLinkClicked(AjaxRequestTarget target) {
                        target.add(sortableObservationCountsContainer, observationCountGroupForm);
                    }

                    @Override
                    protected boolean isReorderState() {
                        return reorderState;
                    }

                    @Override
                    public int getTextDisplayLimit() {
                        return 40;
                    }

                    @Override
                    protected void onFormValidationError(AjaxRequestTarget target) {
                        target.add(feedbackPanel);
                    }
                });
                editCopyDeleteItemPanel.getTextField().add(new NoBarsValidator());
                editCopyDeleteItemPanel.getTextField().add(new NoColonsValidator());

            }
        });

        sortableObservationCountsContainer.add(sortableBehavior = makeSortableBehavior());

        add(sortableObservationCountsContainer);

        add(observationCountGroupForm = new ObservationCountGroupForm("observationCountGroupForm", model){
            @Override
            public boolean isVisible() {
                return isGroupSelected() && observationCountList.size() < 5;
            }
        });

        add(new AjaxLink<Void>("saveGroupLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ObservationCountGroup group = observationCountGroupModel.getObject();
                if (group.getObservationCounts().isEmpty()) {
                    for (ObservationCount count: observationCountList) {
                        observationCountService.addObservationCount(group, count);
                    }
                } else {//We need to create a new version of the group and retire the old one
                    ObservationCountGroup newGroup = new ObservationCountGroup();
                    newGroup.setTenant(group.getTenant());
                    newGroup.setName(group.getName());
                    newGroup = observationCountService.saveOrUpdate(newGroup);
                    for (ObservationCount count: observationCountList) {
                        if (count.isNew()) {
                            observationCountService.addObservationCount(newGroup, count);
                        } else {
                            ObservationCount newCount = new ObservationCount(count);
                            observationCountService.addObservationCount(newGroup, newCount);
                        }
                    }
                    observationCountService.retireObservationGroup(group.getId());
                    observationCountService.updateEventFormsWithNewObservationGroup(group.getId(), newGroup);
                }
                onSaveObservationCounts(target);
            }

            @Override
            public boolean isVisible() {
                return isGroupSelected();
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new ConfirmAjaxCallDecorator(new FIDLabelModel("message.changing_observation_group").getObject());
            }
        });
    }

    protected void onSaveObservationCounts(AjaxRequestTarget target) {}

    private IModel<String> createSubtitleModel(IModel<ObservationCount> model) {
        if (model.getObject().isCounted())
            return new FIDLabelModel("label.included_in_total");
        else
            return new FIDLabelModel("label.not_included_in_total");
    }

    private boolean isGroupSelected() {
        return observationCountList != null;
    }

    private LoadableDetachableModel<List<ObservationCount>> getObservationCountsModel() {
        return new LoadableDetachableModel<List<ObservationCount>>() {
            @Override
            protected List<ObservationCount> load() {
                return observationCountList != null ?
                        observationCountList: Lists.newArrayList();
            }
        };
    }

    @Override
    protected int getIndexOfComponent(Component component) {
        ObservationCount count = (ObservationCount) component.getDefaultModelObject();
        return observationCountList.indexOf(count);
    }

    @Override
    protected void onItemMoving(int oldIndex, int newIndex, AjaxRequestTarget target) {
        ObservationCount movingCount = observationCountList.remove(oldIndex);
        observationCountList.add(newIndex, movingCount);
    }

    private class ObservationCountGroupForm extends Form<ObservationCountGroup> {

        private ObservationCount observationCount;
        private IModel<ObservationCountGroup> model;

        public ObservationCountGroupForm(String id, IModel<ObservationCountGroup> groupModel) {
            super(id, groupModel);

            model = groupModel;
            observationCount = new ObservationCount(FieldIDSession.get().getTenant());
            setOutputMarkupId(true);

            add(new NewObservationCountPanel("newObservationCount", new PropertyModel<ObservationCount>(this, "observationCount")));

            add(new AjaxSubmitLink("addLink") {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    observationCountList.add(new ObservationCount(observationCount));
                    observationCount = new ObservationCount(FieldIDSession.get().getTenant());
                    target.add(sortableObservationCountsContainer, observationCountGroupForm);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }

            });


        }
    }
}

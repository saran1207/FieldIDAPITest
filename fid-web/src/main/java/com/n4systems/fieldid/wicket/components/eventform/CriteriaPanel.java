package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.service.event.ScoreService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.ClickOnComponentWhenEnterKeyPressedBehavior;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventform.CriteriaTypeDescriptionModel;
import com.n4systems.fieldid.wicket.util.NoDoubleBarsValidator;
import com.n4systems.model.*;
import com.n4systems.model.stateset.StateSetLoader;
import com.n4systems.util.eventform.CriteriaCopyUtil;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CriteriaPanel extends SortableListPanel {

    private FeedbackPanel feedbackPanel;
    private CriteriaAddForm criteriaAddForm;
    private SortableAjaxBehavior sortableAjaxBehavior;
    private boolean reorderState = false;

    private ButtonGroup previouslySelectedButtonGroup;
    private boolean previousSetsResultValue;
    private ScoreGroup previouslySelectedScoreGroup;
    private ObservationCountGroup defaultObservationCountGroup;
    private IModel<EventForm> eventFormModel;
    private boolean isMasterEvent;

    @SpringBean
    private ScoreService scoreService;

    public CriteriaPanel(String id, PropertyModel<EventForm> eventFormModel, boolean isMasterEvent) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        this.eventFormModel = eventFormModel;
        this.isMasterEvent = isMasterEvent;

        WebMarkupContainer sortableCriteriaContainer = new WebMarkupContainer("sortableCriteriaContainer");


        sortableCriteriaContainer.add(new ListView<Criteria>("criteria", new PropertyModel<List<Criteria>>(CriteriaPanel.this, "criteriaSection.availableCriteria")) {
            @Override
            protected void populateItem(final ListItem<Criteria> item) {
                item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("editCopyDeletePanel", new PropertyModel<>(item.getModel(), "displayText"), new CriteriaTypeDescriptionModel(item.getModel())) {
                    { setEditMaximumLength(1000); getTextField().add(new NoDoubleBarsValidator()); }
                    @Override
                    protected void onViewLinkClicked(AjaxRequestTarget target) {
                        currentlySelectedIndex = item.getIndex();
                        onCriteriaSelected(target, item.getModelObject());
                    }

                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        getCriteriaSection().getCriteria().remove(item.getIndex());
                        if (item.getIndex() == currentlySelectedIndex) {
                            currentlySelectedIndex = -1;
                            onCurrentCriteriaDeleted(target);
                        } else if (item.getIndex() < currentlySelectedIndex) {
                            // Shift the selection by one if an item higher in the list was deleted
                            currentlySelectedIndex -= 1;
                        }
                        onCriteriaListUpdated(target);
                    }

                    @Override
                    protected void onCopyLinkClicked(AjaxRequestTarget target) {
                        Criteria copiedCriteria = new CriteriaCopyUtil().copyCriteria(item.getModelObject(), getCriteriaSection().getCriteria());
                        getCriteriaSection().getCriteria().add(copiedCriteria);
                        onCriteriaAdded(target, copiedCriteria, getCriteriaSection().getCriteria().size() - 1);
                    }

                    @Override
                    protected void onFormValidationError(AjaxRequestTarget target) {
                        target.add(feedbackPanel);
                    }

                    @Override
                    protected boolean isReorderState() {
                        return reorderState;
                    }
                });

                item.add(new AppendToClassIfCondition("selectedCriteria", () -> item.getIndex() == currentlySelectedIndex && !reorderState));
            }
        });
        
        add(new TwoStateAjaxLink("reorderCriteriaButton", "Reorder Criteria", "Done Reordering") {
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.add(CriteriaPanel.this);
                sortableAjaxBehavior.setDisabled(true);
                reorderState = false;
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.add(CriteriaPanel.this);
                sortableAjaxBehavior.setDisabled(false);
                reorderState = true;
            }
        });

        criteriaAddForm = new CriteriaAddForm("criteriaAddForm");
        EnclosureContainer enclosureContainer = new EnclosureContainer("addCriteriaFormContainer", criteriaAddForm);
        enclosureContainer.add(criteriaAddForm);
        add(enclosureContainer);
        add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", this));
        sortableCriteriaContainer.add(sortableAjaxBehavior = makeSortableBehavior("#criteriaPanel"));
        add(sortableCriteriaContainer);
        feedbackPanel.setOutputMarkupId(true);
    }

    @Override
    protected void onModelChanged() {
        setSelectedIndex(0);
    }

    public CriteriaSection getCriteriaSection() {
        return (CriteriaSection) getDefaultModelObject();
    }

    class CriteriaAddForm extends Form {
        private List<CriteriaType> criteriaTypes = Arrays.asList(CriteriaType.values());
        protected TextField<String> addTextField;
        private String criteriaName;
        private CriteriaType criteriaType;

        public CriteriaAddForm(String id) {
            super(id);

            if(isMasterEvent) {
                //We want to remove the ObservationCountCriteria from this list.  You can't do that as a Master Event.
                criteriaTypes = criteriaTypes.stream()
                                             .filter(criteriaType ->
                                                     !criteriaType.getReportIdentifier().equalsIgnoreCase("observationcount"))
                                             .collect(Collectors.toList());
            }

            add(new DropDownChoice<>("criteriaType", new PropertyModel<>(this, "criteriaType"), criteriaTypes, new ListableChoiceRenderer<>()).setRequired(true));
            AjaxButton submitButton;
            add(addTextField = new RequiredTextField<>("criteriaName", new PropertyModel<>(this, "criteriaName")));
            addTextField.add(new NoDoubleBarsValidator());
            addTextField.setOutputMarkupId(true);
            addTextField.add(new StringValidator.MaximumLengthValidator(1000));
            add(submitButton = new AjaxButton("submitButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Criteria criteria;

                    try {
                        criteria = criteriaType.getCriteriaClass().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    if (CriteriaType.ONE_CLICK.equals(criteriaType)) {
                        if (!configureDefaultStateSet(target, (OneClickCriteria) criteria)) {
                            return;
                        }
                    } else if (CriteriaType.UNIT_OF_MEASURE.equals(criteriaType)) {
                        ((UnitOfMeasureCriteria) criteria).setPrimaryUnit(getDefaultUnitOfMeasure());
                    } else if (CriteriaType.SCORE.equals(criteriaType)) {
                        if (!configureDefaultScoreGroup(target, (ScoreCriteria) criteria)) {
                            return;
                        }
                    } else if (CriteriaType.OBSERVATION_COUNT.equals(criteriaType)) {
                        if (!configureDefaultObservationCountGroup(target, (ObservationCountCriteria) criteria)) {
                            return;
                        }
                    }

                    if(getCriteriaSection().isRequired()) {
                        if (!(criteria instanceof ObservationCountCriteria) && !(criteria instanceof OneClickCriteria)) {
                            criteria.setRequired();
                        }
                    }

                    criteria.setDisplayText(criteriaName);
                    criteriaName = null;
                    getCriteriaSection().getCriteria().add(criteria);
                    onCriteriaAdded(target, criteria, getCriteriaSection().getCriteria().size() - 1);
                }

                private boolean configureDefaultStateSet(AjaxRequestTarget target, OneClickCriteria criteria) {
                    ButtonGroup buttonGroup = getDefaultStateSet();
                    if (buttonGroup == null) {
                        error(new FIDLabelModel("message.event_form.create_one_click").getObject());
                        target.add(feedbackPanel);
                        return false;
                    }
                    if (previouslySelectedButtonGroup != null) {
                        criteria.setButtonGroup(previouslySelectedButtonGroup);
                    } else {
                        criteria.setButtonGroup(buttonGroup);
                    }
                    criteria.setPrincipal(previousSetsResultValue);
                    return true;
                }

                private boolean configureDefaultScoreGroup(AjaxRequestTarget target, ScoreCriteria criteria) {
                    ScoreGroup scoreGroup = getDefaultScoreGroup();
                    if (scoreGroup == null) {
                        error(new FIDLabelModel("message.event_form.create_score_group").getObject());
                        target.add(feedbackPanel);
                        return false;
                    }
                    if(previouslySelectedScoreGroup != null) {
                    	criteria.setScoreGroup(previouslySelectedScoreGroup);
                    } else {
                    	criteria.setScoreGroup(scoreGroup);
                    }
                    return true;
                }

                private boolean configureDefaultObservationCountGroup(AjaxRequestTarget target, ObservationCountCriteria criteria) {
                    defaultObservationCountGroup = getDefaultObservationCountGroup();
                    if (defaultObservationCountGroup == null) {
                        error(new FIDLabelModel("message.event_form.create_observation_count_group").getObject());
                        target.add(feedbackPanel);
                        return false;
                    } else {
                        criteria.setObservationCountGroup(defaultObservationCountGroup);
                    }
                    return true;
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });

            addTextField.add(new ClickOnComponentWhenEnterKeyPressedBehavior(submitButton));
        }

        @Override
        public boolean isVisible() {
            return !reorderState;
        }
    }

    @Override
    protected int getIndexOfComponent(Component component) {
        Criteria criteria = (Criteria) component.getDefaultModelObject();
        return getCriteriaSection().getCriteria().indexOf(criteria);
    }

    @Override
    protected void onItemMoving(int oldIndex, int newIndex, AjaxRequestTarget target) {
        Criteria movingCriteria = getCriteriaSection().getCriteria().remove(oldIndex);
        getCriteriaSection().getCriteria().add(newIndex, movingCriteria);
        target.add(this);
    }

    protected void onCriteriaAdded(AjaxRequestTarget target, Criteria criteria, int newIndex) { }

    protected void onCriteriaSelected(AjaxRequestTarget target, Criteria criteria) { }

    protected void onCurrentCriteriaDeleted(AjaxRequestTarget target) { }

    protected void onCriteriaListUpdated(AjaxRequestTarget target) { }

    public String getAddTextFieldId() {
        return criteriaAddForm.addTextField.getMarkupId();
    }

    public TextField<String> getAddTextField() {
        return criteriaAddForm.addTextField;
    }

    public void setPreviouslySelectedButtonGroup(ButtonGroup previouslySelectedButtonGroup) {
        this.previouslySelectedButtonGroup = previouslySelectedButtonGroup;
    }

    public void setPreviousSetsResultValue(boolean setsResultValue) {
        this.previousSetsResultValue = setsResultValue;
    }

    private ButtonGroup getDefaultStateSet() {
        StateSetLoader stateSetLoader = new StateSetLoader(FieldIDSession.get().getSessionUser().getSecurityFilter());
        List<ButtonGroup> buttonGroupList =  stateSetLoader.load();
        if (buttonGroupList.isEmpty()) {
            return null;
        }
        return buttonGroupList.get(0);
    }

    private ScoreGroup getDefaultScoreGroup() {
        List<ScoreGroup> scoreGroups = scoreService.getScoreGroups();
        if (scoreGroups.isEmpty()) {
            return null;
        }
        return scoreGroups.get(0);
    }

    private ObservationCountGroup getDefaultObservationCountGroup() {
        //When the user is creating a brand new form.
        if(eventFormModel.getObject() == null) {
            return null;
        } else {
            return eventFormModel.getObject().getObservationCountGroup();
        }
    }

    private UnitOfMeasure getDefaultUnitOfMeasure() {
        UnitOfMeasureListLoader uomListLoader = new UnitOfMeasureListLoader(FieldIDSession.get().getSessionUser().getSecurityFilter());
        List<UnitOfMeasure> uoms = uomListLoader.load();
        if (uoms.isEmpty()) {
            return null;
        }
        return uoms.get(0);
    }

	public void setPreviouslySelectedScoreGroup(ScoreGroup scoreGroup) {
		this.previouslySelectedScoreGroup = scoreGroup;
		
	}
}

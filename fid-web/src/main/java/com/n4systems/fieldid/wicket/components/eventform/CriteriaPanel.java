package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.eventform.util.CriteriaCopyUtil;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.StateSet;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.stateset.StateSetLoader;
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
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import java.util.Arrays;
import java.util.List;

public class CriteriaPanel extends SortableListPanel {

    private FeedbackPanel feedbackPanel;
    private CriteriaAddForm criteriaAddForm;
    private SortableAjaxBehavior sortableAjaxBehavior;
    private boolean reorderState = false;

    public CriteriaPanel(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);

        WebMarkupContainer sortableCriteriaContainer = new WebMarkupContainer("sortableCriteriaContainer");
        sortableCriteriaContainer.add(new ListView<Criteria>("criteria", new PropertyModel<List<Criteria>>(CriteriaPanel.this, "criteriaSection.criteria")) {
            @Override
            protected void populateItem(final ListItem<Criteria> item) {
                item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("editCopyDeletePanel", new PropertyModel<String>(item.getModel(), "displayText"), new PropertyModel<String>(item.getModel(), "typeDescription")) {
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
                    protected boolean isReorderState() {
                        return reorderState;
                    }
                });

                item.add(new AppendToClassIfCondition("selectedCriteria", new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return item.getIndex() == currentlySelectedIndex && !reorderState;
                    }
                }));
            }
        });
        add(new TwoStateAjaxLink("reorderCriteriaButton", "Reorder Criteria", "Done Reordering") {
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.addComponent(CriteriaPanel.this);
                sortableAjaxBehavior.setDisabled(true);
                reorderState = false;
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.addComponent(CriteriaPanel.this);
                sortableAjaxBehavior.setDisabled(false);
                reorderState = true;
            }
        });

        criteriaAddForm = new CriteriaAddForm("criteriaAddForm");
        EnclosureContainer enclosureContainer = new EnclosureContainer("addCriteriaFormContainer", criteriaAddForm);
        enclosureContainer.add(criteriaAddForm);
        add(enclosureContainer);
        add(feedbackPanel = new FeedbackPanel("feedbackPanel"));
        sortableCriteriaContainer.add(sortableAjaxBehavior = makeSortableBehavior());
        add(sortableCriteriaContainer);
        feedbackPanel.setOutputMarkupId(true);
    }

    public CriteriaSection getCriteriaSection() {
        return (CriteriaSection) getDefaultModelObject();
    }

    class CriteriaAddForm extends Form {
        private List<String> criteriaTypes = Arrays.asList("One-Click", "Text Field", "Select");
        protected TextField addTextField;
        private String criteriaName;
        private String criteriaType;

        public CriteriaAddForm(String id) {
            super(id);
            add(new DropDownChoice<String>("criteriaType", new PropertyModel<String>(this, "criteriaType"), criteriaTypes).setRequired(true));
            add(addTextField = new RequiredTextField<String>("criteriaName", new PropertyModel<String>(this, "criteriaName")));
            addTextField.setOutputMarkupId(true);
            add(new AjaxButton("submitButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Criteria criteria = null;
                    if ("One-Click".equals(criteriaType)) {
                        OneClickCriteria oneClickCriteria = new OneClickCriteria();
                        StateSetLoader stateSetLoader = new StateSetLoader(FieldIDSession.get().getSessionUser().getSecurityFilter());
                        List<StateSet> stateSetList =  stateSetLoader.load();
                        if (stateSetList.isEmpty()) {
                            error("You must configure at least one Button Group to use One-Click criteria");
                            target.addComponent(feedbackPanel);
                            return;
                        }
                        oneClickCriteria.setStates(stateSetList.get(0));
                        criteria = oneClickCriteria;
                    } else if ("Text Field".equals(criteriaType)) {
                        criteria = new TextFieldCriteria();
                    } else if ("Select".equals(criteriaType)) {
                        criteria = new SelectCriteria();
                    }
                    
                    criteria.setDisplayText(criteriaName);
                    criteriaName = null;
                    getCriteriaSection().getCriteria().add(criteria);
                    onCriteriaAdded(target, criteria, getCriteriaSection().getCriteria().size() - 1);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedbackPanel);
                }
            });
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
        target.addComponent(this);
    }

    @Override
    protected String getSortableContainmentCss() {
        return "#criteriaPanel";
    }

    protected void onCriteriaAdded(AjaxRequestTarget target, Criteria criteria, int newIndex) { }

    protected void onCriteriaSelected(AjaxRequestTarget target, Criteria criteria) { }

    protected void onCurrentCriteriaDeleted(AjaxRequestTarget target) { }

    protected void onCriteriaListUpdated(AjaxRequestTarget target) { }

    public String getAddTextFieldId() {
        return criteriaAddForm.addTextField.getMarkupId();
    }

}

package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.ClickOnComponentWhenEnterKeyPressedBehavior;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;
import com.n4systems.fieldid.wicket.util.NoBarsValidator;
import com.n4systems.model.CriteriaSection;
import com.n4systems.util.eventform.CriteriaSectionCopyUtil;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import java.util.List;

public class CriteriaSectionsPanel extends SortableListPanel {

    private FeedbackPanel feedbackPanel;
    private boolean reorderState = false;
    SortableAjaxBehavior sortableBehavior;

    public CriteriaSectionsPanel(String id, IModel<List<CriteriaSection>> sectionsModel) {
        super(id, sectionsModel);
        setOutputMarkupId(true);
        add(new TwoStateAjaxLink("reorderSectionsButton", "Reorder Sections", "Done Reordering") {
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.add(CriteriaSectionsPanel.this);
                sortableBehavior.setDisabled(true);
                reorderState = false;
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.add(CriteriaSectionsPanel.this);
                sortableBehavior.setDisabled(false);
                reorderState = true;
            }
        });

        WebMarkupContainer sortableSectionContainer = new WebMarkupContainer("sortableSectionContainer");
        sortableSectionContainer.add(sortableBehavior = makeSortableBehavior("#criteriaSectionsPanel"));
        add(sortableSectionContainer);
        sortableSectionContainer.add(new ListView<CriteriaSection>("criteriaSections", getListModel()) {
            @Override
            protected void populateItem(final ListItem<CriteriaSection> item) {
                item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("editCopyDeletePanel", new PropertyModel<String>(item.getModel(), "title")) {
                    { setEditMaximumLength(2000); getTextField().add(new NoBarsValidator()); }
                    @Override
                    protected void onViewLinkClicked(AjaxRequestTarget target) {
                        currentlySelectedIndex = item.getIndex();
                        onCriteriaSectionSelected(target, currentlySelectedIndex);
                    }

                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        getListModel().getObject().remove(item.getIndex());
                        if (item.getIndex() == currentlySelectedIndex) {
                            currentlySelectedIndex = -1;
                            onCurrentCriteriaSectionDeleted(target);
                        } else if (item.getIndex() < currentlySelectedIndex) {
                            // Shift the selection by one if an item higher in the list was deleted
                            currentlySelectedIndex -= 1;
                        }
                        onCriteriaSectionListUpdated(target);
                    }

                    @Override
                    protected void onCopyLinkClicked(AjaxRequestTarget target) {
                        processCopy(item.getIndex());
                        currentlySelectedIndex = getListModel().getObject().size() - 1;
                        onCriteriaSectionSelected(target, currentlySelectedIndex);
                    }
                    
                    @Override
                    protected void onFormValidationError(AjaxRequestTarget target) {
                        target.add(feedbackPanel);
                    }

                    @Override
                    protected boolean isReorderState() {
                        return reorderState;
                    }

                    @Override
                    protected Component createOptionalPanel(String id) {
                       return new OptionalFlagPanel(id, new PropertyModel<Boolean>(item.getModel(), "optional"));
                    }
                });

                item.add(new AppendToClassIfCondition("selectedSection", new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return item.getIndex() == currentlySelectedIndex && !reorderState;
                    }
                }));
            }
        });

        CriteriaSectionAddForm addForm = new CriteriaSectionAddForm("formSectionAddForm");
        EnclosureContainer addFormContainer = new EnclosureContainer("addCriteriaSectionFormContainer", addForm);
        addFormContainer.add(addForm);
        add(addFormContainer);
        add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", this));
        feedbackPanel.setOutputMarkupId(true);

        if (sectionsModel.getObject().size() > 0) {
            setSelectedIndex(0);
        }
    }

    @Override
    protected int getIndexOfComponent(Component component) {
        CriteriaSection section = (CriteriaSection) component.getDefaultModelObject();
        return getListModel().getObject().indexOf(section);
    }

    @Override
    protected void onItemMoving(int oldIndex, int newIndex, AjaxRequestTarget target) {
        CriteriaSection movingSection = getListModel().getObject().remove(oldIndex);
        getListModel().getObject().add(newIndex, movingSection);
        target.add(CriteriaSectionsPanel.this);
    }

    private void processCopy(int index) {
        List<CriteriaSection> criteriaList = getListModel().getObject();
        CriteriaSection sectionToCopy = getListModel().getObject().get(index);
        CriteriaSection copiedSection = new CriteriaSectionCopyUtil().copySection(sectionToCopy, criteriaList);
        criteriaList.add(copiedSection);
    }

    protected IModel<List<CriteriaSection>> getListModel() {
        return (IModel<List<CriteriaSection>>) getDefaultModel();
    }

    class CriteriaSectionAddForm extends Form {
        private String sectionTitle;
        private AjaxButton submitButton;
        private TextField<String> sectionNameField;

        public CriteriaSectionAddForm(String id) {
            super(id);

            sectionNameField = new RequiredTextField<String>("sectionNameField", new PropertyModel<String>(this, "sectionTitle"));
            sectionNameField.add(new NoBarsValidator());
            add(sectionNameField);
            sectionNameField.add(new StringValidator.MaximumLengthValidator(2000));
            add(submitButton = new AjaxButton("submitButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    CriteriaSection section = new CriteriaSection();

                    section.setTitle(sectionTitle);
                    sectionTitle = null;
                    onCriteriaSectionAdded(target, section);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            sectionNameField.add(new ClickOnComponentWhenEnterKeyPressedBehavior(submitButton));
        }

        @Override
        public boolean isVisible() {
            return !reorderState;
        }
    }

    protected void onCriteriaSectionAdded(AjaxRequestTarget target, CriteriaSection section) { }

    protected void onCriteriaSectionSelected(AjaxRequestTarget target, int index) { }

    protected void onCriteriaSectionListUpdated(AjaxRequestTarget target) { }

    protected void onCurrentCriteriaSectionDeleted(AjaxRequestTarget target) { }

    private class OptionalFlagPanel extends Fragment {
        public OptionalFlagPanel(String id, IModel<Boolean> model) {
            super(id, "optionalFlagPanel", CriteriaSectionsPanel.this, model);
            add(new CheckBox("optionalCheck", model).add(new UpdateComponentOnChange()));
        }
    }

}

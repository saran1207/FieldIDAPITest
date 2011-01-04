package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.eventform.util.CriteriaSectionCopyUtil;
import com.n4systems.model.CriteriaSection;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;
import org.odlabs.wiquery.ui.sortable.SortableBehavior;
import org.odlabs.wiquery.ui.sortable.SortableContainment;
import org.odlabs.wiquery.ui.sortable.SortableRevert;

import java.util.List;

public class CriteriaSectionsPanel extends Panel {

    private int currentlySelectedIndex = -1;
    private FeedbackPanel feedbackPanel;
    private boolean reorderState = false;
    SortableAjaxBehavior sortableBehavior;

    public CriteriaSectionsPanel(String id, IModel<List<CriteriaSection>> sectionsModel) {
        super(id, sectionsModel);
        setOutputMarkupId(true);
        add(new TwoStateAjaxLink("reorderSectionsButton", "Reorder Sections", "Done Reordering") {
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.addComponent(CriteriaSectionsPanel.this);
                sortableBehavior.setDisabled(true);
                reorderState = false;
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.addComponent(CriteriaSectionsPanel.this);
                sortableBehavior.setDisabled(false);
                reorderState = true;
            }
        });

        WebMarkupContainer sortableSectionContainer = new WebMarkupContainer("sortableSectionContainer");
        sortableSectionContainer.add(sortableBehavior = makeSortableBehavior());
        add(sortableSectionContainer);
        sortableSectionContainer.add(new ListView<CriteriaSection>("criteriaSections", getListModel()) {
            @Override
            protected void populateItem(final ListItem<CriteriaSection> item) {
                item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("editCopyDeletePanel", new PropertyModel<String>(item.getModel(), "title")) {
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
                    protected boolean isReorderState() {
                        return reorderState;
                    }
                });

                item.add(new AppendToClassIfCondition("selectedSection", new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return !reorderState && item.getIndex() == currentlySelectedIndex;
                    }
                }));
            }
        });

        CriteriaSectionAddForm addForm = new CriteriaSectionAddForm("formSectionAddForm");
        EnclosureContainer addFormContainer = new EnclosureContainer("criteriaFormContainer", addForm);
        addFormContainer.add(addForm);
        add(addFormContainer);
        add(feedbackPanel = new FeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);
    }

    private SortableAjaxBehavior makeSortableBehavior() {
        SortableAjaxBehavior sortable = new SimpleSortableAjaxBehavior() {
            @Override
            public void onUpdate(Component component, int index, AjaxRequestTarget target) {
                if (component == null) {
                    return;
                }
                List<CriteriaSection> theCriteriaList = getListModel().getObject();
                CriteriaSection section = (CriteriaSection) component.getDefaultModelObject();
                theCriteriaList.remove(section);
                theCriteriaList.add(index, section);
                target.addComponent(CriteriaSectionsPanel.this);
            }
        };
        sortable.getSortableBehavior().setConnectWith(".sortableSectionContainer");
        sortable.getSortableBehavior().setRevert(new SortableRevert(true));
        sortable.getSortableBehavior().setContainment(new SortableContainment("#criteriaSectionsPanel"));
        sortable.getSortableBehavior().setAxis(SortableBehavior.AxisEnum.Y);
        sortable.setDisabled(true);
        return sortable;
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

    protected void setSelectedIndex(int newSelectedIndex) {
        this.currentlySelectedIndex = newSelectedIndex;
    }

    class CriteriaSectionAddForm extends Form {
        private String sectionTitle;

        public CriteriaSectionAddForm(String id) {
            super(id);

            add(new RequiredTextField<String>("sectionNameField", new PropertyModel<String>(this, "sectionTitle")));
            add(new AjaxButton("submitButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    CriteriaSection section = new CriteriaSection();
                    section.setTitle(sectionTitle);
                    sectionTitle = null;
                    onCriteriaSectionAdded(target, section);
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

    protected void onCriteriaSectionAdded(AjaxRequestTarget target, CriteriaSection section) { }

    protected void onCriteriaSectionSelected(AjaxRequestTarget target, int index) { }

    protected void onCriteriaSectionListUpdated(AjaxRequestTarget target) { }

    protected void onCurrentCriteriaSectionDeleted(AjaxRequestTarget target) { }

}

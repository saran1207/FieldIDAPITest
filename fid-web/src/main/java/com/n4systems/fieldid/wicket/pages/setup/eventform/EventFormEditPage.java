package com.n4systems.fieldid.wicket.pages.setup.eventform;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaSectionsPanel;
import com.n4systems.fieldid.wicket.components.eventform.save.SavePanel;
import com.n4systems.fieldid.wicket.components.eventform.util.CriteriaSectionCopyUtil;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.fieldid.wicket.pages.setup.SetupPage;
import com.n4systems.fieldid.wicket.pages.setup.TemplatesPage;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.StateSet;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.event.EventFormSaver;
import com.n4systems.model.eventtype.EventTypeSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import java.util.ArrayList;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EventFormEditPage extends FieldIDLoggedInPage {

    private List<CriteriaSection> criteriaSections;

    private Long eventTypeId;
    private Long oldEventFormId;

    private CriteriaSectionsPanel criteriaSectionsPanel;
    private CriteriaPanel criteriaPanel;
    private CriteriaDetailsPanel criteriaDetailsPanel;

    private SavePanel topSavePanel;
    private SavePanel bottomSavePanel;
    // TODO: Use model
    private transient EventType eventType;

    @Override
    protected void storePageParameters(PageParameters params) {
        eventTypeId = params.getAsLong("uniqueID");
        FilteredIdLoader<EventType> idLoader = new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()).createFilteredIdLoader(EventType.class);
        idLoader.setPostFetchFields("eventForm.sections");
        idLoader.setId(eventTypeId);
        eventType = idLoader.load();
    }

    @Override
    protected void addTitleLabel(String labelId) {
        add(new Label(labelId, new FIDLabelModel("title.manage_event_type_id", eventType.getName())));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("/eventTypes.action").build(),
                aNavItem().label("nav.view").page("/eventType.action").params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.edit").page("/eventTypeEdit.action").params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.event_form").page(EventFormEditPage.class).build(),
                aNavItem().label("nav.asset_type_associations").page("/selectAssetTypes.action").params(param("eventTypeId", eventTypeId)).build(),
                aNavItem().label("nav.add").page("/eventTypeAdd.action").onRight().build(),
                aNavItem().label("nav.import").page("/eventImportExport.action").params(uniqueId(eventTypeId)).onRight().build()));
    }

    public EventFormEditPage(PageParameters params) {
        super(params);

        add(CSSPackageResource.getHeaderContribution("style/fieldid.css"));
        add(CSSPackageResource.getHeaderContribution("style/eventFormEdit.css"));

        add(topSavePanel = createSavePanel("topSavePanel"));
        add(bottomSavePanel = createSavePanel("bottomSavePanel"));

        criteriaSections = new ArrayList<CriteriaSection>();
        if (eventType.getEventForm() != null) {
            oldEventFormId = eventType.getEventForm().getId();
            criteriaSections.addAll(eventType.getEventForm().getAvailableSections());
        }

        add(criteriaSectionsPanel = new CriteriaSectionsPanel("criteriaSectionsPanel", new PropertyModel<List<CriteriaSection>>(this, "criteriaSections"))
        {
            @Override
            public void onCriteriaSectionAdded(AjaxRequestTarget target, CriteriaSection section) {
                section.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                criteriaSections.add(section);
                setSelectedIndex(criteriaSections.size() - 1);
                onCriteriaSectionSelected(target, criteriaSections.size() - 1);
                focusOnCriteriaNameField(target);
            }

            @Override
            public void onCriteriaSectionSelected(AjaxRequestTarget target, int index) {
                updateCriteriaPanel(new Model<CriteriaSection>(criteriaSections.get(index)));
                criteriaPanel.clearSelection();
                criteriaDetailsPanel.setVisible(false);
                refreshAllComponents(target);
            }

            @Override
            protected void onCriteriaSectionListUpdated(AjaxRequestTarget target) {
                refreshAllComponents(target);
            }

            @Override
            protected void onCurrentCriteriaSectionDeleted(AjaxRequestTarget target) {
                criteriaPanel.setVisible(false);
                criteriaDetailsPanel.setVisible(false);
                refreshAllComponents(target);
            }
        });
        add(criteriaPanel = new CriteriaPanel("criteriaPanel") {
            @Override
            public void onCriteriaAdded(AjaxRequestTarget target, Criteria criteria, int newIndex) {
                criteria.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                setSelectedIndex(newIndex);
                onCriteriaSelected(target, criteria);
                focusOnCriteriaNameField(target);
            }

            @Override
            protected void onCriteriaSelected(AjaxRequestTarget target, Criteria criteria) {
                criteriaDetailsPanel.setDefaultModelObject(criteria);
                criteriaDetailsPanel.setVisible(true);
                refreshAllComponents(target);
            }

            @Override
            protected void onCurrentCriteriaDeleted(AjaxRequestTarget target) {
                criteriaDetailsPanel.setVisible(false);
                refreshAllComponents(target);
            }

            @Override
            protected void onCriteriaListUpdated(AjaxRequestTarget target) {
                refreshAllComponents(target);
            }
        });
        criteriaPanel.setVisible(false);

        add(criteriaDetailsPanel = new CriteriaDetailsPanel("criteriaDetailsPanel", new Model<Criteria>()) {
            @Override
            protected void onStateSetSelected(StateSet stateSet) {
                criteriaPanel.setPreviouslySelectedStateSet(stateSet);
            }

            @Override
            protected void onSetsResultSelected(boolean setsResult) {
                criteriaPanel.setPreviousSetsResultValue(setsResult);
            }
        });
        criteriaDetailsPanel.setVisible(false);
    }

    private SavePanel createSavePanel(String panelID) {
        return new SavePanel(panelID) {
            @Override
            protected void onSaveAndContinueClicked(AjaxRequestTarget target) {
                saveEventForm();
                target.addComponent(topSavePanel);
                target.addComponent(bottomSavePanel);
            }

            @Override
            protected void onSaveAndFinishClicked(AjaxRequestTarget target) {
                saveEventForm();
                FieldIDSession.get().storeInfoMessageForStruts("Event Form saved.");
                getRequestCycle().setRequestTarget(new RedirectRequestTarget("/eventType.action?uniqueID="+eventTypeId));
            }
        };
    }

    public void updateCriteriaPanel(IModel<CriteriaSection> newModel) {
        criteriaPanel.setDefaultModel(newModel);
        criteriaPanel.setVisible(true);
    }

    private void refreshAllComponents(AjaxRequestTarget target) {
        target.addComponent(criteriaSectionsPanel);
        target.addComponent(criteriaPanel);
        target.addComponent(criteriaDetailsPanel);
    }

    private void focusOnCriteriaNameField(AjaxRequestTarget target) {
        target.focusComponent(criteriaPanel.getAddTextField());
    }

    private void saveEventForm() {
        Transaction tx = PersistenceManager.startTransaction();

        FilteredIdLoader<EventForm> eventFormLoader = new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()).createFilteredIdLoader(EventForm.class);
        if (oldEventFormId != null) {
            EventForm oldEventForm = eventFormLoader.setId(oldEventFormId).load(tx);
            oldEventForm.setState(Archivable.EntityState.RETIRED);
            new EventFormSaver().update(tx, oldEventForm);
        }

        FilteredIdLoader<EventType> eventTypeLoader = new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()).createFilteredIdLoader(EventType.class);

        EventType eventType = eventTypeLoader.setId(eventTypeId).setPostFetchFields("eventForm.sections").load(tx);

        EventForm eventForm = new EventForm();
        eventForm.setSections(createCopiesOf(criteriaSections));
        eventForm.setTenant(FieldIDSession.get().getSessionUser().getTenant());
        eventType.setEventForm(eventForm);
        eventType.incrementFormVersion();

        new EventFormSaver().save(tx, eventForm);
        new EventTypeSaver().update(tx, eventType);

        tx.commit();

        oldEventFormId = eventForm.getId();
    }

    private List<CriteriaSection> createCopiesOf(List<CriteriaSection> criteriaSections) {
        CriteriaSectionCopyUtil copyUtil = new CriteriaSectionCopyUtil();
        List<CriteriaSection> copiedSections = new ArrayList<CriteriaSection>();
        for (CriteriaSection section : criteriaSections) {
            copiedSections.add(copyUtil.copySection(section));
        }
        return copiedSections;
    }

}

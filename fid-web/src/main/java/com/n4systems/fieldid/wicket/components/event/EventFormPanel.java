package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.model.*;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;
import java.util.Map;

public abstract class EventFormPanel extends Panel {


    private final EventFormHelper eventFormHelper;
    private AbstractEvent.SectionResults currentSection;
    private int currentSectionIndex = 0;
    private int totalSections;
    private IModel<List<AbstractEvent.SectionResults>> results;
    protected IModel<? extends AbstractEvent> event;

    private Map<CriteriaSection, Double> sectionScores;
    private Map<CriteriaSection, Double> sectionScorePercentages;

    private Map<CriteriaSection, Map<ObservationCount, Integer>> sectionObservations;

    private WebMarkupContainer criteriaSectionContainer;
    private boolean isEdit;

    public EventFormPanel(String id, final IModel<? extends AbstractEvent> event, final IModel<List<AbstractEvent.SectionResults>> results, final boolean isEdit) {
        super(id);
        this.results = results;
        this.event = event;
        this.isEdit = isEdit;
        this.eventFormHelper = new EventFormHelper();
        setOutputMarkupId(true);

        totalSections = results.getObject().size();
        if (!results.getObject().isEmpty()) {
            currentSection = results.getObject().get(0);
        }

        PropertyModel<Integer> currentSectionModel = new PropertyModel<Integer>(this, "currentSectionNumber");
        PropertyModel<Integer> totalSectionsModel = new PropertyModel<Integer>(this, "totalSections");

        add(criteriaSectionContainer = new WebMarkupContainer("criteriaSectionsContainer"));
        criteriaSectionContainer.setOutputMarkupId(true);

        criteriaSectionContainer.add(new ListView<AbstractEvent.SectionResults>("criteriaSectionsList", results) {
            @Override
            protected void populateItem(final ListItem<AbstractEvent.SectionResults> item) {
                WebMarkupContainer sectionContainer = new WebMarkupContainer("sectionContainer") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().section.getId().equals(currentSection.section.getId());
                    }
                };

                sectionContainer.add(new Label("sectionName", new PropertyModel<String>(item.getModel(), "section.title")));
                sectionContainer.add(getCriteriaSectionPanel(event.getObject().getClass(), new PropertyModel<List<CriteriaResult>>(item.getModel(), "results")));
                sectionContainer.add(getSectionScore("sectionScore", new PropertyModel<CriteriaSection>(item.getModel(), "section")));
                sectionContainer.add(getSectionScorePercentage("sectionScorePercentage", new PropertyModel<CriteriaSection>(item.getModel(), "section")));
                sectionContainer.add(getSectionObservations("sectionObservation", new PropertyModel<CriteriaSection>(item.getModel(), "section")));

                sectionContainer.add(new AjaxLink<Void>("hideSectionLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        item.getModelObject().disabled = true;
                        target.add(criteriaSectionContainer);
                    }

                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().section.isOptional() && isEdit;
                    }
                });
                item.add(sectionContainer);

                WebMarkupContainer disabledOverlay;
                sectionContainer.add(disabledOverlay = new WebMarkupContainer("disabledOverlay") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().disabled;
                    }
                });
                disabledOverlay.setOutputMarkupPlaceholderTag(true);
                disabledOverlay.add(new Label("name", new PropertyModel<String>(item.getModel(), "section.title")));
                disabledOverlay.add(new AjaxLink<Void>("restoreLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        item.getModelObject().disabled = false;
                        target.add(criteriaSectionContainer);
                    }
                });
            }
        });

        DropDownChoice<AbstractEvent.SectionResults> jumpToSection = new DropDownChoice<AbstractEvent.SectionResults>("jumpToSection", new PropertyModel<AbstractEvent.SectionResults>(this, "currentSection"), results, sectionChoiceRenderer());
        jumpToSection.add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                currentSectionIndex = results.getObject().indexOf(currentSection);
                target.add(EventFormPanel.this);
            }
        });
        add(jumpToSection);

        add(createCriteriaSectionPager("topSectionPager", currentSectionModel, totalSectionsModel));
        add(createCriteriaSectionPager("bottomSectionPager", currentSectionModel, totalSectionsModel));
    }

    protected Component getSectionScore(String id, IModel<CriteriaSection> criteriaSectionModel) {
        return new Label(id).setVisible(false);
    }

    protected Component getSectionScorePercentage(String id, IModel<CriteriaSection> criteriaSectionModel) {
        return new Label(id).setVisible(false);
    }

    protected Component getSectionObservations(String id, IModel<CriteriaSection> criteriaSectionModel) {
        return new Label(id).setVisible(false);
    }

    protected abstract Panel getCriteriaSectionPanel(Class<? extends AbstractEvent> eventClass, PropertyModel<List<CriteriaResult>> results);

    private IChoiceRenderer<AbstractEvent.SectionResults> sectionChoiceRenderer() {
        return new IChoiceRenderer<AbstractEvent.SectionResults>() {
            @Override
            public Object getDisplayValue(AbstractEvent.SectionResults object) {
                return object.section.getTitle();
            }

            @Override
            public String getIdValue(AbstractEvent.SectionResults object, int index) {
                return object.section.getTitle();
            }
        };
    }

    private CriteriaSectionPager createCriteriaSectionPager(String pagerId, IModel<Integer> currentPageModel, final IModel<Integer> totalPagesModel) {
        return new CriteriaSectionPager(pagerId, currentPageModel, totalPagesModel) {
            @Override
            protected void onBack(AjaxRequestTarget target) {
                changeSection(target, -1);
            }

            @Override
            protected void onForward(AjaxRequestTarget target) {
                changeSection(target, 1);
            }
        };
    }

    private void changeSection(AjaxRequestTarget target, int delta) {
        currentSectionIndex = (currentSectionIndex + delta ) % totalSections;
        if (currentSectionIndex < 0) currentSectionIndex = totalSections - 1;
        currentSection = results.getObject().get(currentSectionIndex);
        target.add(EventFormPanel.this);
    }

    public int getCurrentSectionNumber() {
        return currentSectionIndex + 1;
    }

    protected Map<CriteriaSection, Double> getScoresForSections() {
        if(sectionScores == null)
            sectionScores = eventFormHelper.getScoresForSections(event.getObject());
        return sectionScores;
    }

    protected Map<CriteriaSection, Double> getScorePercentageForSections() {
       if(sectionScorePercentages == null)
           sectionScorePercentages = eventFormHelper.getScorePercentageForSections(event.getObject());
        return sectionScorePercentages;
    }

    protected Map<CriteriaSection, Map<ObservationCount, Integer>> getObservationsForSections() {
        if (sectionObservations == null)
            sectionObservations = eventFormHelper.getObservationsForSections(event.getObject());
        return sectionObservations;
    }

    protected Integer getSectionTotal(AbstractEvent event, CriteriaSection section) {
        return eventFormHelper.getObservationSectionTotal(event, section);
    }
}

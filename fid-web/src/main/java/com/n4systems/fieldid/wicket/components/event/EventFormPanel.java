package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

public abstract class EventFormPanel extends Panel {

    private AbstractEvent.SectionResults currentSection;
    private int currentSectionIndex = 0;
    private int totalSections;
    private IModel<List<AbstractEvent.SectionResults>> results;

    public EventFormPanel(String id, final Class<? extends Event> eventClass, final IModel<List<AbstractEvent.SectionResults>> results) {
        super(id);
        this.results = results;
        setOutputMarkupId(true);

        totalSections = results.getObject().size();
        if (!results.getObject().isEmpty()) {
            currentSection = results.getObject().get(0);
        }

        PropertyModel<Integer> currentSectionModel = new PropertyModel<Integer>(this, "currentSectionNumber");
        PropertyModel<Integer> totalSectionsModel = new PropertyModel<Integer>(this, "totalSections");

        add(new ListView<AbstractEvent.SectionResults>("criteriaSections", results) {
            @Override
            protected void populateItem(final ListItem<AbstractEvent.SectionResults> item) {
                WebMarkupContainer sectionContainer = new WebMarkupContainer("sectionContainer") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().section.getId().equals(currentSection.section.getId());
                    }
                };

                sectionContainer.add(new Label("sectionName", new PropertyModel<String>(item.getModel(), "section.title")));
                sectionContainer.add(getCriteriaSectionPanel(eventClass, new PropertyModel<List<CriteriaResult>>(item.getModel(), "results")));

                item.add(sectionContainer);
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

    protected abstract Panel getCriteriaSectionPanel(Class<? extends Event> eventClass, PropertyModel<List<CriteriaResult>> results);

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

}

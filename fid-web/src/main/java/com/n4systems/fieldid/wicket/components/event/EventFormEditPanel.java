package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
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

public class EventFormEditPanel extends Panel {
    
    private AbstractEvent.SectionResults currentSection;
    private int currentSectionNumber = 1;
    private int totalSections;

    public EventFormEditPanel(String id, IModel<List<AbstractEvent.SectionResults>> results) {
        super(id);
        setOutputMarkupId(true);

        totalSections = results.getObject().size();

        if (!results.getObject().isEmpty()) {
            currentSection = results.getObject().get(0);
        }
        
        add(new Label("currentSection", new PropertyModel<Object>(this, "currentSectionNumber")));
        add(new Label("totalSections", new PropertyModel<Object>(this, "totalSections")));

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
                sectionContainer.add(new CriteriaSectionEditPanel("criteriaEditPanel", new PropertyModel<List<CriteriaResult>>(item.getModel(), "results")));
                
                item.add(sectionContainer);
            }
        });

        DropDownChoice<AbstractEvent.SectionResults> jumpToSection = new DropDownChoice<AbstractEvent.SectionResults>("jumpToSection", new PropertyModel<AbstractEvent.SectionResults>(this, "currentSection"), results, sectionChoiceRenderer());
        jumpToSection.add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(EventFormEditPanel.this);
            }
        });
        add(jumpToSection);
    }
    
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

}

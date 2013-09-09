package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.wicket.components.eventtype.GroupedEventTypePicker;
import com.n4systems.fieldid.wicket.components.localization.LocalizedField;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.model.Criteria;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.ScoreCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EventTypeTranslationsPage extends TranslationsPage<EventType> {


    public EventTypeTranslationsPage() {
        super();
    }

    @Override
    protected DropDownChoice<EventType> createChoice(String id) {
        return new GroupedEventTypePicker(id, Model.of(new EventType()), new EventTypesForTenantModel());
    }


    class EventLinks extends Fragment {

        private final IModel<LocalizedField> model;

        public EventLinks(String id, IModel<LocalizedField> model) {
            super(id, "eventLinks", EventTypeTranslationsPage.this);
            this.model = model;
            final Object entity = model.getObject().getEntity();

            add(new AjaxLink("scoreGroups") {
                @Override public void onClick(AjaxRequestTarget target) {

                }
                @Override public boolean isVisible() {
                    return entity instanceof ScoreCriteria;
                }
            });
            add(new AjaxLink("buttonGroups") {
                @Override public void onClick(AjaxRequestTarget target) {

                }

                @Override public boolean isVisible() {
                    return entity instanceof OneClickCriteria;
                }
            });
            add(new AjaxLink("observations") {
                @Override public void onClick(AjaxRequestTarget target) {

                }

                @Override public boolean isVisible() {
                    if (entity instanceof Criteria) {
                        Criteria criteria = (Criteria) entity;
                        return !criteria.getRecommendations().isEmpty() || !criteria.getDeficiencies().isEmpty();
                    }
                    return false;
                }
            });
        }
    }



}

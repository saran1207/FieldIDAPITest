package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.eventtype.GroupedEventTypePicker;
import com.n4systems.fieldid.wicket.components.localization.LocalizationPanel;
import com.n4systems.fieldid.wicket.components.localization.LocalizedField;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.model.*;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.lang.reflect.Field;
import java.util.List;

public class EventTypeTranslationsPage extends TranslationsPage<EventType> {

    public EventTypeTranslationsPage() {
        super();
        add(new RenderHint("criteriaSection.title", "top-level"));
        add(new RenderHint("eventType.name", "top-level"));
    }

    @Override
    protected List<String> initExcludedFields() {
        return Lists.newArrayList("supportedProofTests","infoFieldNames", "supportedProofTests", "group", "eventTypes",
                "recommendations", "deficiencies", "scoreGroup", "buttonGroup");
    }

    @Override
    protected DropDownChoice<EventType> createChoice(String id) {
        return new GroupedEventTypePicker(id, Model.of(new EventType()), new EventTypesForTenantModel());
    }

    @Override
    protected Component createLinksForItem(String id, ListItem<LocalizedField> item) {
        LocalizedField field = item.getModelObject();
        if (field!=null && field.getEntity() instanceof Criteria) {
            return new EventLinks(id, item.getModel());
        }
        return super.createLinksForItem(id, item);
    }


    class EventLinks extends Fragment {

        private final IModel<LocalizedField> model;

        public EventLinks(String id, IModel<LocalizedField> model) {
            super(id, "eventLinks", EventTypeTranslationsPage.this);
            this.model = model;
            final Object entity = model.getObject().getEntity();
            final EntityWithTenant e = (EntityWithTenant) entity;

            add(new AjaxLink("scoreGroups") {
                @Override public void onClick(AjaxRequestTarget target) {
                    showLocalizationDialogFor(e, Lists.newArrayList("scoreGroup"), target);
                }
                @Override public boolean isVisible() {
                    return entity instanceof ScoreCriteria;
                }
            });
            add(new AjaxLink("buttonGroups") {
                @Override public void onClick(AjaxRequestTarget target) {
                    showLocalizationDialogFor(e, Lists.newArrayList("buttonGroup"), target);
                }

                @Override public boolean isVisible() {
                    return entity instanceof OneClickCriteria;
                }
            });
            add(new AjaxLink("observations") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    showLocalizationDialogFor(e, Lists.newArrayList("recommendations", "deficiencies"), target);
                }

                @Override
                public boolean isVisible() {
                    if (entity instanceof Criteria) {
                        Criteria criteria = (Criteria) entity;
                        return !criteria.getRecommendations().isEmpty() || !criteria.getDeficiencies().isEmpty();
                    }
                    return false;
                }
            });
        }
    }

    private void showLocalizationDialogFor(final EntityWithTenant entity, final List<String> fieldsToInclude, AjaxRequestTarget target) {
        LocalizationPanel content = new LocalizationPanel(FIDModalWindow.CONTENT_ID, new EntityModel(entity.getClass(), entity)) {
            @Override protected boolean isFieldIgnored(Object e, Field field) {
                if (entity.equals(e)) {
                    return !fieldsToInclude.contains(field.getName());
                }
                return false;
            }
        };
        dialog.setContent(content);
        dialog.show(target);
    }

}

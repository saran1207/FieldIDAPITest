package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.localization.LocalizationPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

abstract public class TranslationsPage<T extends EntityWithTenant> extends FieldIDFrontEndPage {

    private final DropDownChoice<T> choice;
    private Component localizationPanel;

    protected List<String> excludedNames= Lists.newArrayList("owner", "tenant", "createdBy", "modifiedBy");

    protected TranslationsPage() {
        super();
        excludedNames.addAll(initExcludedFields());
        choice = createChoice("choice");
        choice.setNullValid(true).
                setOutputMarkupPlaceholderTag(true).
                add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override protected void onUpdate(AjaxRequestTarget target) {
                        selected(target);
                    }
                });
        add(new Form("form").add(choice));
        add(localizationPanel = new WebMarkupContainer("localization").setOutputMarkupId(true));
    }

    protected List<String> initExcludedFields() {
        return Lists.newArrayList();
    };

    protected abstract DropDownChoice<T> createChoice(String choice);

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.translations"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("title.manage_asset_type_groups.singular").page(AssetTypeGroupTranslationsPage.class).build(),
                aNavItem().label("title.asset_type").page(AssetTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventtypegroup").page(EventTypeGroupTranslationsPage.class).build(),
                aNavItem().label("label.event_type").page(EventTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventbook").page(EventBookTranslationsPage.class).build()
        ));
    }

    protected void selected(AjaxRequestTarget target) {
        Object modelObject = choice.getDefaultModelObject();
        EntityWithTenant entity = (EntityWithTenant) modelObject;
        replace(localizationPanel = new LocalizationPanel("localization", new EntityModel(entity.getClass(), entity)) {
            @Override
            protected boolean ignoreField(Field field) {
                return TranslationsPage.this.isFiltered(field);
            }
        }.setOutputMarkupId(true));
        target.add(localizationPanel);
    }

    protected boolean isFiltered(Field field) {
        System.out.println("checking field " + field.getName() + " --> " + excludedNames.contains(field.getName()));
        return excludedNames.contains(field.getName());
    }



    class FieldImpl implements Serializable {
        private final String name;
        private final Class<?> declaringClass;
        private final Class<?> type;

        FieldImpl(Field field) {
            this.name = field.getName();
            this.declaringClass = field.getDeclaringClass();
            this.type = field.getType();
        }
    }


}

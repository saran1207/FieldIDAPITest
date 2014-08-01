package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.wicket.components.localization.LocalizationPanel;
import com.n4systems.fieldid.wicket.components.localization.LocalizedField;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

abstract public class TranslationsPage<T extends EntityWithTenant> extends FieldIDFrontEndPage {

    private static final Logger logger= Logger.getLogger(TranslationsPage.class);

    public static final String LOCALIZATION_PANEL_ID = "localization";

    protected Map<String, RenderHint> renderingHintMap = Maps.newHashMap();
    private final DropDownChoice<T> choice;
    private Component localizationPanel;
    private final FeedbackPanel feedback;

    protected List<String> excludedNames= Lists.newArrayList("allInfoOptionsForCasadeDeleteOnlyDoNotInteractWithThisSet");

    protected final ModalWindow dialog;

    protected TranslationsPage() {
        super();

        if(getTenant().getSettings().getTranslatedLanguages().isEmpty()) {
            setResponsePage(LanguageConfigurationPage.class);
        }

        add(feedback = new FeedbackPanel("feedback"));
        feedback.setOutputMarkupPlaceholderTag(true);

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

        dialog = new FIDModalWindow("dialog").setAutoSize(true);
        add(new Form("dialogForm").add(dialog));

        add(localizationPanel = new WebMarkupContainer(LOCALIZATION_PANEL_ID).setOutputMarkupId(true));
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
        boolean hasTranslatedLanguages = !getTenant().getSettings().getTranslatedLanguages().isEmpty();
        add(new NavigationBar(navBarId,
                aNavItem().label("title.asset_type_groups.singular").cond(hasTranslatedLanguages).page(AssetTypeGroupTranslationsPage.class).build(),
                aNavItem().label("title.asset_type").cond(hasTranslatedLanguages).page(AssetTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventtypegroup").cond(hasTranslatedLanguages).page(EventTypeGroupTranslationsPage.class).build(),
                aNavItem().label("label.event_type").cond(hasTranslatedLanguages).page(EventTypeTranslationsPage.class).build(),
                aNavItem().label("label.eventbook").cond(hasTranslatedLanguages).page(EventBookTranslationsPage.class).build(),
                aNavItem().label("label.assetstatus").cond(hasTranslatedLanguages).page(AssetStatusTranslationsPage.class).build(),
                aNavItem().label("label.event_status").cond(hasTranslatedLanguages).page(EventStatusTranslationsPage.class).build(),
                aNavItem().label("label.configure_languages").page(LanguageConfigurationPage.class).onRight().build()
        ));
    }

    protected void selected(AjaxRequestTarget target) {
        Object modelObject = choice.getDefaultModelObject();
        if (modelObject==null) {
            replace(localizationPanel = new WebMarkupContainer(LOCALIZATION_PANEL_ID).setOutputMarkupId(true));
        } else {
            EntityWithTenant entity = (EntityWithTenant) modelObject;
            replace(localizationPanel = new LocalizationPanel(LOCALIZATION_PANEL_ID, new EntityModel(entity.getClass(), entity)) {
                @Override protected boolean isFieldIgnored(Object entity, Field field) {
                    return TranslationsPage.this.isFiltered(entity,field);
                }

                @Override protected String getCssFor(ListItem<LocalizedField> item) {
                    return TranslationsPage.this.getCssFor(item.getModelObject());
                }

                @Override protected Component createLinksForItem(String id, ListItem<LocalizedField> item, IModel<List<Locale>> languages) {
                    Component component = TranslationsPage.this.createLinksForItem(id, item, languages);
                    return component!=null ? component : super.createLinksForItem(id, item, languages);
                }

                @Override protected void onLocalizationsSaved(AjaxRequestTarget target) {
                    info(new FIDLabelModel("label.translations_saved").getObject());
                    target.add(feedback);
                }
            }.setOutputMarkupId(true));
        }
        target.add(get(LOCALIZATION_PANEL_ID));
    }

    protected Component createLinksForItem(String id, ListItem<LocalizedField> item, IModel<List<Locale>> languages) {
        return null;
    }

    @Override
    protected boolean forceDefaultLanguage() {
        return true;
    }

    protected String getCssFor(LocalizedField field) {
        RenderHint renderHint = renderingHintMap.get(field.getOgnl());
        return renderHint==null ? "" : renderHint.css;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
        response.renderCSSReference("style/legacy/pageStyles/localization.css");
    }

    protected boolean isFiltered(Object entity, Field field) {
        return excludedNames.contains(field.getName());
    }

    protected TranslationsPage add(RenderHint renderHint) {
        renderingHintMap.put(renderHint.ognl, renderHint);
        return this;
    }

    protected void showLocalizationDialogFor(final EntityWithTenant entity, final List<String> fieldsToInclude, AjaxRequestTarget target) {
        EntityModel entityModel = new EntityModel(entity.getClass(), entity);
        showLocalizationDialogFor(entityModel, fieldsToInclude, target, new PropertyModel(entityModel,"tenant.settings.translatedLanguages"));
    }

    protected void showLocalizationDialogFor(final IModel<?> model, final List<String> fieldsToInclude, AjaxRequestTarget target, IModel<List<Locale>> languages) {
        LocalizationPanel content = new LocalizationPanel(FIDModalWindow.CONTENT_ID, model, languages) {
            @Override protected boolean isFieldIgnored(Object e, Field field) {
                if (model.getObject().equals(e)) {
                    return !fieldsToInclude.contains(field.getName());
                }
                return false;
            }

            @Override protected void onLocalizationsSaved(AjaxRequestTarget target) {
                dialog.close(target);
                target.add(dialog);
            }
        };
        dialog.setContent(content);
        dialog.show(target);
    }


    class RenderHint implements Serializable {
        String css;
        String ognl;

        public RenderHint(String ognl, String css) {
            this.ognl = ognl;
            this.css = css;
        }
    }


}

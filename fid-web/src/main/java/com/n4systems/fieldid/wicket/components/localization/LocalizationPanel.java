package com.n4systems.fieldid.wicket.components.localization;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.Criteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.services.localization.LocalizationService;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Locale;

public class LocalizationPanel extends Panel {

    private static final Logger logger= Logger.getLogger(LocalizationPanel.class);

    private @SpringBean LocalizationService localizationService;

    private Locale language;
    private final FormComponent<Locale> chooseLanguage;
    private final ListView<LocalizedField> listView;

    public LocalizationPanel(String id, IModel<? extends EntityWithTenant> model) {
        super(id, model);

        add(new Form("form")
                .add(listView = new ListView<LocalizedField>("translations", new LocalizedFieldsModel(LocalizationPanel.this.getDefaultModel(), getLanguages())) {
                    @Override
                    protected void populateItem(ListItem<LocalizedField> item) {
                        item.add(new AttributeAppender("class",Model.of(getCssFor(item))));
                        final LocalizedField field = item.getModelObject();
                        item.add(new Label("label", Model.of(field.getName())));
                        item.add(new Label("defaultValue", Model.of(field.getDefaultValue())));
                        item.add(new TranslationsListView("translations", new PropertyModel(item.getModel(), "translations")));
                        // TODO DD : refactor this into generic templated method..... createLinksForEntity("misc", item.getModel);
                        item.add(createLinksForItem(item));
                    }
                }.setReuseItems(true))
                .add(chooseLanguage = new FidDropDownChoice<Locale>("language", new PropertyModel(this, "language"), getLanguages()).setNullValid(false).setRequired(true))
                .add(new AjaxSubmitLink("submit") {
                    @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        localizationService.save(convertToTranslations());
                    }

                    @Override protected void onError(AjaxRequestTarget target, Form<?> form) {

                    }
                })
        );
        chooseLanguage.add(new AjaxFormSubmitBehavior("onchange") {
            @Override protected void onSubmit(AjaxRequestTarget target) {
                target.add(LocalizationPanel.this);
            }
            @Override protected void onError(AjaxRequestTarget target) {

            }
        });

    }

    protected EventLinks createLinksForItem(ListItem<LocalizedField> item) {
        return new EventLinks("misc", item.getModel());
    }

    protected String getCssFor(ListItem<LocalizedField> item) {
        return item.getModelObject().getOgnl();
    }

    private List<Translation> convertToTranslations() {
        LocalizedFieldsModel model = (LocalizedFieldsModel) listView.getDefaultModel();
        return model.getAsTranslations();
    }

    @Override
    protected void onModelChanged() {
        super.onModelChanged();
        detachModel();
        listView.setDefaultModel(new LocalizedFieldsModel(getDefaultModel(), getLanguages()));
        listView.detach();
    }

    private List<Locale> getLanguages() {
        return FieldIDSession.get().getTenant().getSettings().getLanguages();
    }


    class TranslationsListView extends ListView<Translation> {
        TranslationsListView(String id, IModel<List<Translation>> list) {
            super(id, list);
            setReuseItems(true);
        }

        @Override protected void populateItem(ListItem<Translation> item) {
            Translation translation = item.getModelObject();
            final Locale itemLanguage = getLanguages().get(item.getIndex());
            item.add(new TextField("translation", new PropertyModel(item.getModel(),"value")) {
                @Override public boolean isVisible() {
                    return itemLanguage.equals(language);
                }
            });
        }
    }


    class EventLinks extends Fragment {

        private final IModel<LocalizedField> model;

        public EventLinks(String id, IModel<LocalizedField> model) {
            super(id, "eventLinks", LocalizationPanel.this);
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

package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.OrgEnum;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;
import org.odlabs.wiquery.ui.autocomplete.*;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AutoCompleteSearch extends FormComponentPanel<Asset> {
    
    private @SpringBean AssetService assetService;
    
    public static final WiQueryJavaScriptResourceReference WIQUERY_AUTOCOMPLETE_JS =
            new WiQueryJavaScriptResourceReference(AutocompleteAjaxComponent.class,
                    "wiquery-autocomplete.js");

    private static final String NOT_ENTERED = "NOT_ENTERED";

    private boolean autoUpdate = false;
    private String term = "";
    private InnerOrgAutocompleteBehavior autocompleteBehavior;
    private final Autocomplete<String> autocompleteField;
    private final HiddenField<String> autocompleteHidden;
    private IChoiceRenderer<? super Asset> choiceRenderer;
    private AbstractDefaultAjaxBehavior updateAjax;
    private HashSet<AssetType> categories;
    // this is added because depending on styling (font, padding, etc...) you might want to tweak the width of the menu.
    private int extraWidth = 5;


    public AutoCompleteSearch(String id, final IModel<Asset> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);
        setOutputMarkupId(true);

        autocompleteHidden = new HiddenField<String>("autocompleteHidden", new Model<String>(NOT_ENTERED) {
            @Override
            public String getObject() {
                Asset modelObject = AutoCompleteSearch.this.getModelObject();
                return (modelObject != null) ? super.getObject() : null;
            }
        });
        autocompleteHidden.setOutputMarkupId(true);
        add(autocompleteHidden);

        autocompleteField = new InnerAutocomplete<String>("autocompleteField", new IModel<String>() {
            public String getObject() {
                Asset modelObject = AutoCompleteSearch.this.getModelObject();
                if (modelObject != null) {
                    Asset objectValue = (Asset) choiceRenderer.getDisplayValue(modelObject);
                    Class<Asset> objectClass =
                            (Class<Asset>) (objectValue == null ? null : objectValue.getClass());

                    String displayValue = "";
                    if (objectClass != null) {
                        final IConverter<Asset> converter = getConverter(objectClass);

                        displayValue = converter.convertToString(objectValue, getLocale());
                    } else if (objectValue != null) {
                        displayValue = objectValue.toString();
                    }
                    return displayValue;
                } else {
                    return null;
                }
            }

            public void setObject(String object) {
            }

            public void detach() {
            }
        });
        add(autocompleteField);

        updateAjax = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget target) {
                final String hiddenInput = autocompleteHidden.getInput();
                final String fieldInput = autocompleteField.getInput();
                autocompleteHidden.setConvertedInput(hiddenInput);
                autocompleteField.setConvertedInput(fieldInput);
                validate();
                if (isValid()) {
                    updateModel();
                    onUpdate(target);
                }
            }
        };
        add(updateAjax);
        add(autocompleteBehavior = new InnerOrgAutocompleteBehavior());
    }



    protected List<Asset> getChoices() {
        return getChoices(term);
    }

    public List<Asset> getChoices(String term) {
        return assetService.getAssetsLike(term);
    }
    
    public HiddenField<String> getAutocompleteHidden() {
        return autocompleteHidden;
    }

    public Asset getValueOnSearchFail(String input) {
        return null;
    }

    public IChoiceRenderer<? super Asset> getChoiceRenderer() {
        if (choiceRenderer == null) {
            choiceRenderer = new ChoiceRenderer<Asset>();
        }
        return choiceRenderer;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }


    protected void onBeforeRenderAutocomplete(Autocomplete<?> autocomplete) {
        Asset defaultValue = AutoCompleteSearch.this.getModelObject();
        if (defaultValue != null) {
            AutocompleteJson value = null;
            value = newAutocompleteJson(defaultValue);
            autocomplete.setDefaultModelObject(value.getLabel());
            getAutocompleteHidden().setModelObject(value.getValueId());
        }

        ((InnerAutocomplete)autocomplete).getOptions().putLiteral("source",
                autocompleteBehavior.getCallbackUrl().toString());

    }

    private void clearCategories() {
        categories = new HashSet<AssetType>();
    }


    private class InnerOrgAutocompleteBehavior extends AbstractAjaxBehavior {

        public void onRequest() {
            term = this.getComponent().getRequest().getQueryParameters().getParameterValue("term").toString();

            if (!Strings.isEmpty(term)) {
                StringWriter sw = new StringWriter();
                try {
                    JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);

                    AutocompleteJson value = null;
                    Integer index = 0;
                    List<Object> json = new ArrayList<Object>();

                    clearCategories();
                    List<Asset> choices = getChoices();
                    for (Asset obj : choices) {
                        value = newAutocompleteJson(obj);
                        json.add(value);
                    }

                    new ObjectMapper().writeValue(gen, json);

                } catch (IOException e) {
                    throw new WicketRuntimeException(e);
                }

                RequestCycle.get().scheduleRequestHandlerAfterCurrent(
                        new TextRequestHandler("application/json", "utf-8", sw.toString()));
            }
        }
    }


    private class InnerAutocomplete<E> extends Autocomplete<E> {
        public InnerAutocomplete(String id, IModel<E> model) {
            super(id, model);
            setOutputMarkupId(true);
        }

        public Options getOptions() {
            return super.getOptions();
        }

        @Override
        public void renderHead(IHeaderResponse response) {
            response.renderJavaScriptReference(WicketEventReference.INSTANCE);
            response.renderJavaScriptReference(WicketAjaxReference.INSTANCE);
            response.renderJavaScriptReference(WiQueryAutocompleteJavaScriptResourceReference.get());
        }

        @Override
        protected void onBeforeRender() {
            onBeforeRenderAutocomplete(this);
            super.onBeforeRender();
        }

        @Override
        public Autocomplete<E> setCloseEvent(JsScopeUiEvent close) {
            throw new WicketRuntimeException("You can't define the close event");
        }

        @Override
        public Autocomplete<E> setSelectEvent(JsScopeUiEvent select) {
            throw new WicketRuntimeException("You can't define the select event");
        }

        @Override
        public Autocomplete<E> setChangeEvent(JsScopeUiEvent select) {
            throw new WicketRuntimeException("You can't define the change event");
        }

        @Override
        public Autocomplete<E> setSource(AutocompleteSource source) {
            throw new WicketRuntimeException("You can't define the source");
        }

        @Override
        public JsStatement statement() {
            StringBuilder js = new StringBuilder();
            js.append("$.ui.autocomplete.wiquery.changeEvent(event, ui,").append(
                    JsUtils.quotes(autocompleteHidden.getMarkupId()));
            if (isAutoUpdate()) {
                js.append(",'").append(updateAjax.getCallbackUrl()).append("'");
            }
            js.append(");");
            super.setMinLength(1);
            super.setChangeEvent(JsScopeUiEvent.quickScope(js.toString()));
            super.setSelectEvent(JsScopeUiEvent.quickScope(js.append("$(event.target).blur();")
                    .toString()));
            JsStatement jsStatement = super.statement();
            jsStatement.append(".data('autocomplete')._renderItem = autoCompleteOrgPicker.render");
            return jsStatement;
        }
    }
    
    protected AutocompleteJson newAutocompleteJson(Asset asset) {
        boolean thisOneSelected = asset.equals(getModelObject());
        final String idValue = asset.getId() + "";
        if (thisOneSelected) {
            getAutocompleteHidden().setModelObject(idValue);
        }
        String tooltip = String.format("[ %s,%s,%s]",
                asset.getIdentifier()==null ? "" : asset.getIdentifier(),
                asset.getRfidNumber()==null ? "" : asset.getRfidNumber(),
                asset.getCustomerRefNumber()==null ? "" : asset.getCustomerRefNumber());
        return new OrgAutocompleteJson(asset.getId()+"", asset.getDisplayName(), getCategory(asset), tooltip);
    }

    private String getCategory(Asset asset) {
        AssetType category = asset.getType();
        if (!categories.contains(category)) {
            categories.add(category);
            return category.getDisplayName();
        }
        return "";
    }

    protected void onUpdate(AjaxRequestTarget target) {
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/component/autoCompleteOrgPicker.js");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        response.renderCSSReference("style/component/autoCompleteOrgPicker.css");
        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderOnLoadJavaScript("autoCompleteOrgPicker.init('"+autocompleteField.getMarkupId()+"',"+extraWidth+");");
    }

    @Override
    protected final void convertInput() {
        String valueId = autocompleteHidden.getConvertedInput();
        String input = autocompleteField.getConvertedInput();
        final Asset object = this.getModelObject();
        final IChoiceRenderer<? super Asset> renderer = getChoiceRenderer();

        if (NOT_ENTERED.equals(valueId))
            valueId = null;

        if (valueId == null && Strings.isEmpty(input)) {
            setConvertedInput(null);

        } else if (valueId == null) {
            setConvertedInput(getValueOnSearchFail(input));

        } else if (object == null || input.compareTo((String) renderer.getDisplayValue(object)) != 0) {
            final List<Asset> choices = getChoices();
            boolean found = false;
            for (int index = 0; index < choices.size(); index++) {
                // Get next choice
                final Asset choice = choices.get(index);
                final String idValue = renderer.getIdValue(choice, index + 1);
                if (idValue.equals(valueId)) {
                    setConvertedInput(choice);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // if it is still not entered, then it means this field was not touched
                // so keep the original value
                if (valueId.equals(NOT_ENTERED)) {
                    setConvertedInput(getModelObject());
                } else {
                    setConvertedInput(getValueOnSearchFail(input));
                }
            }
        } else {
            setConvertedInput(object);
        }
    }


}


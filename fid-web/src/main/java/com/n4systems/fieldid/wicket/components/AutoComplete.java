package com.n4systems.fieldid.wicket.components;

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
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class AutoComplete<T> extends FormComponentPanel<T> {

    public static final WiQueryJavaScriptResourceReference WIQUERY_AUTOCOMPLETE_JS =
            new WiQueryJavaScriptResourceReference(AutocompleteAjaxComponent.class,
                    "wiquery-autocomplete.js");

    private static final String NOT_ENTERED = "NOT_ENTERED";

    private boolean autoUpdate = true;
    protected String term = "";
    private InnerAutocompleteBehavior autocompleteBehavior;
    private final Autocomplete<String> autocompleteField;
    private final HiddenField<String> autocompleteHidden;
    private IChoiceRenderer<? super T> choiceRenderer;
    private AbstractDefaultAjaxBehavior updateAjax;
    protected int threshold = 15;


    public AutoComplete(String id, final IModel<T> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);
        setOutputMarkupId(true);

        autocompleteHidden = new HiddenField<String>("autocompleteHidden", new Model<String>(NOT_ENTERED) {
            @Override
            public String getObject() {
                T modelObject = AutoComplete.this.getModelObject();
                return (modelObject != null) ? super.getObject() : null;
            }
        });
        autocompleteHidden.setOutputMarkupId(true);
        add(autocompleteHidden);

        autocompleteField = new InnerAutocomplete<String>("autocompleteField", new IModel<String>() {
            public String getObject() {
                T modelObject = AutoComplete.this.getModelObject();
                if (modelObject != null) {
                    T objectValue = (T) choiceRenderer.getDisplayValue(modelObject);
                    Class<T> objectClass =
                            (Class<T>) (objectValue == null ? null : objectValue.getClass());
                    
                    String displayValue = "";
                      if (objectClass != null) {
                        final IConverter<T> converter = getConverter(objectClass);
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
        add(autocompleteBehavior = new InnerAutocompleteBehavior());
    }

    protected List<T> getChoices() {
        return getChoices(term);
    }

    public HiddenField<String> getAutocompleteHidden() {
        return autocompleteHidden;
    }
    
    public T getValueOnSearchFail(String input) {
        return null;
    }
    
    private IChoiceRenderer<? super T> getChoiceRenderer() {
        if (choiceRenderer == null) {
            choiceRenderer = createChoiceRenderer();
        }
        return choiceRenderer;
    }

    public IChoiceRenderer<? super T> createChoiceRenderer() {
        // NOTE : you may have to override this is you are writing an AutoComplete extension for
        // an entity that doesn't match the ChoiceRenderer parameters (e.g. doesn't have "id").
        return new ChoiceRenderer<T>(null, "id");
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    protected void onBeforeRenderAutocomplete(Autocomplete<?> autocomplete) {
        T defaultValue = AutoComplete.this.getModelObject();
        if (defaultValue != null) {
            AutoCompleteResult value = null;
            value = createAutocompleteJson(defaultValue, normalizeSearchTerm(term));
            autocomplete.setDefaultModelObject(value.getLabel());
            getAutocompleteHidden().setModelObject(value.getValueId());
        }
        
        ((InnerAutocomplete)autocomplete).getOptions().putLiteral("source",
                autocompleteBehavior.getCallbackUrl().toString());
        
    }

    private AutoCompleteResult createNoResultsJson(String search) {
        return new AutoCompleteResult("no results found for '" + search + "'", "no-results");
    }

    private AutoCompleteResult createMaxResultsJson(String term) {
        return new AutoCompleteResult("search limit reached for '" + term +"'", "max-results");
    }

    protected void startRequest() {};

    protected String normalizeSearchTerm(String term) {
        return term;
    }

    private class InnerAutocompleteBehavior extends AbstractAjaxBehavior {

        public void onRequest() {
            startRequest();
            term = this.getComponent().getRequest().getQueryParameters().getParameterValue("term").toString();

            if (!Strings.isEmpty(term)) {
                StringWriter sw = new StringWriter();
                try {
                    JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);

                    Serializable value = null;
                    Integer index = 0;
                    List<Object> json = new ArrayList<Object>();

                    String search = normalizeSearchTerm(term);
                    List<T> choices = getChoices();
                    
                    if (choices.size()==0) {
                        json.add(createNoResultsJson(search));
                    } else { 
                        for (T obj : choices) {
                            value = createAutocompleteJson(obj, search);
                            json.add(value);
                        }
                    }
                    if (choices.size()>=threshold) {
                        json.add(createMaxResultsJson(term));
                    }
                    
                    new ObjectMapper().writeValue(gen, json);
                    
                } catch (IOException e) {
                    throw new WicketRuntimeException(e);
                }

                RequestCycle.get().scheduleRequestHandlerAfterCurrent(
                        new TextRequestHandler("application/json", "utf-8", sw.toString()));

                endRequest();
            }
        }

    }

    private void endRequest() { }

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
            jsStatement.append(".data('autocomplete')._renderItem = autoCompleter.render");
            return jsStatement;
        }
    }
    
    protected void onUpdate(AjaxRequestTarget target) {
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/component/autoComplete.js");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        response.renderCSSReference("style/component/autoComplete.css");
        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderOnLoadJavaScript("autoCompleter.init('"+autocompleteField.getMarkupId()+"');");
    }
    
    @Override
    protected final void convertInput() {
        String valueId = autocompleteHidden.getConvertedInput();
        String input = autocompleteField.getConvertedInput();
        final T object = this.getModelObject();
        final IChoiceRenderer<? super T> renderer = getChoiceRenderer();
        
        if (NOT_ENTERED.equals(valueId))
            valueId = null;
        
        if (valueId == null && Strings.isEmpty(input)) {
            setConvertedInput(null);

        } else if (valueId == null) {
            setConvertedInput(getValueOnSearchFail(input));

        } else if (object == null || input.compareTo(renderer.getDisplayValue(object).toString()) != 0) {
            final List<T> choices = getChoices();
            boolean found = false;
            for (int index = 0; index < choices.size(); index++) {
                // Get next choice
                final T choice = choices.get(index);
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


    protected abstract List<T> getChoices(String term);

    protected abstract AutoCompleteResult createAutocompleteJson(T asset, String term);


}


package com.n4systems.fieldid.wicket.components.autocomplete;

import com.google.gson.Gson;
import com.n4systems.fieldid.wicket.behavior.Watermark;
import com.n4systems.model.parents.AbstractEntity;
import org.apache.commons.lang.StringUtils;
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
import org.apache.wicket.request.Request;
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
import org.odlabs.wiquery.ui.autocomplete.Autocomplete;
import org.odlabs.wiquery.ui.autocomplete.AutocompleteAjaxComponent;
import org.odlabs.wiquery.ui.autocomplete.AutocompleteSource;
import org.odlabs.wiquery.ui.autocomplete.WiQueryAutocompleteJavaScriptResourceReference;
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
    
    private boolean autoUpdate = false;
    protected String term = "";
    private InnerAutocompleteBehavior autocompleteBehavior;
    private final Autocomplete<String> autocompleteField;
    protected final HiddenField<String> autocompleteHidden;
    private IChoiceRenderer<? super T> choiceRenderer;
    protected AbstractDefaultAjaxBehavior updateAjax;
    protected int threshold = 15;
    private String[] containers;


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
                    T objectValue = (T) getChoiceRenderer().getDisplayValue(modelObject);
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
                    onUpdate(target, hiddenInput, fieldInput);
                }
            }
        };
        add(updateAjax);
        add(autocompleteBehavior = new InnerAutocompleteBehavior());
        autocompleteField.add(new Watermark(getWatermarkText()));
    }

    protected String getWatermarkText() {
        // override this to provide more meaningful prompt.
        return "start typing...";
    }

    protected List<T> getChoices() {
        if (StringUtils.isEmpty(term)) {
            return getChoicesForEmptyTerm();
        }        
        return getChoices(term);
    }

    public AutoComplete<T> withAutoUpdate(boolean update) {
        this.autoUpdate = update;
        return this;
    }

    protected List<T> getChoicesForEmptyTerm() {
        return new ArrayList<T>();
    }

    public HiddenField<String> getAutocompleteHidden() {
        return autocompleteHidden;
    }

    public Autocomplete<String> getAutocompleteField() {
        return autocompleteField;
    }

    public T getValueOnSearchFail(String input) {
        return null;
    }
    
    private IChoiceRenderer<? super T> getChoiceRenderer() {
        if (choiceRenderer == null) {
            choiceRenderer = new ChoiceRenderer<T>() {
                @Override public Object getDisplayValue(T object) {
                    return AutoComplete.this.getDisplayValue(object);
                }
                @Override public String getIdValue(T object, int index) {
                    return AutoComplete.this.getIdValue(object, index);
                }
            };
        }
        return choiceRenderer;
    }

    protected String getIdValue(T object, int index) {
        if (object instanceof AbstractEntity) {
            return ((AbstractEntity)object).getId()+"";
        }
        return index+"";
    }

    protected String getDisplayValue(T object) {
        return object==null ? "" : object.toString();
    }

    protected boolean isAutoUpdate() {
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

    public AutoComplete<T> inScrollableContainers(String... selectors) {
        this.containers = selectors;
        return this;
    }
    
    private AutoCompleteResult createNoResultsJson(String search) {
        return new AutoCompleteResult("No Results Found", "no-results");
    }

    private AutoCompleteResult createMaxResultsJson(String term) {
        return new AutoCompleteResult("Search Limit Reached. ", "max-results");
    }

    protected void startRequest(Request request) {}

    protected void endRequest(Request request) { }

    protected String normalizeSearchTerm(String term) {
        return term;
    }


    protected void onUpdate(AjaxRequestTarget target, String hiddenInput, String fieldInput) {
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        String args = "";
        if (containers!=null && containers.length>0) {
            Gson gson = new Gson();
            args = "," + new Gson().toJson(containers);
        }
        response.renderJavaScriptReference("javascript/component/autoComplete.js");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        response.renderCSSReference("style/component/autoComplete.css");
        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderOnLoadJavaScript("autoCompleter.init('"+autocompleteField.getMarkupId()+"'" + args + ");");
   }
    
    @Override
    protected final void convertInput() {
        String valueId = autocompleteHidden.getConvertedInput();
        String input = autocompleteField.getConvertedInput();
        final T object = this.getModelObject();
        final IChoiceRenderer<? super T> renderer = getChoiceRenderer();
        final String selectedValueId =  renderer.getIdValue(object,0);
        
        if (NOT_ENTERED.equals(valueId))
            valueId = null;
        
        if (valueId == null && Strings.isEmpty(input)) {
            setConvertedInput(null);

        } else if (valueId == null) {
            setConvertedInput(getValueOnSearchFail(input));

        } else if (object == null || (selectedValueId!=null && !selectedValueId.equals(valueId))) {
            final List<T> choices = getChoices();
            boolean found = false;
            for (int index = 0; index < choices.size(); index++) {
                // Get next choice
                final T choice = choices.get(index);
                final String idValue = renderer.getIdValue(choice, index);
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

    public String getHiddenMarkupId() {
        return autocompleteHidden.getMarkupId();
    }

    public String getTextMarkupId() {
        return autocompleteField.getMarkupId();
    }


    // ----------------INNER CLASSES------------------


    private class InnerAutocompleteBehavior extends AbstractAjaxBehavior {

        public void onRequest() {
            startRequest(getComponent().getRequest());
            term = this.getComponent().getRequest().getQueryParameters().getParameterValue("term").toString();


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

            endRequest(getComponent().getRequest());
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
            jsStatement.append(".data('autocomplete')._renderItem = autoCompleter.render");
            return jsStatement;
        }
    }


}


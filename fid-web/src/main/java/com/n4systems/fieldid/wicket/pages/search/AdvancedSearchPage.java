package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.form.IndicatingAjaxSubmitLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.IndexField;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AdvancedSearchPage extends FieldIDFrontEndPage {
    private static final String HIDE_LIST_JS = "$('#%s').hide();";
    private static final String SHOW_LIST_JS = "$('#%s').show();";
    private static final String DISABLE_INPUTS_JS = "lockSearchPage();";
    private static final String ENABLE_INPUTS_JS = "unlockSearchPage();";

    public static final int ITEMS_PER_PAGE = 10;

    private @SpringBean PersistenceService persistenceService;
    private @SpringBean S3Service s3Service;

    private String searchText = null;
    private PageableListView resultsListView = null;
    private WebMarkupContainer listViewContainer = null;
    private TextField<String> searchCriteria = null;
    private FIDFeedbackPanel feedbackPanel = null;
    private WebMarkupContainer actions;
    private Form resultForm;
    private Set<String> selectedIds = new HashSet<String>();
    private TextSearchDataProvider provider;
    private Component blankSlate;
    private DataView<SearchResult> dataView;
    private CheckBox groupSelector;
    private WebMarkupContainer suggestionsContainer;
    private final NewSearchForm form;
    private IndexField idField;

    private boolean currentPageSelected = false;
    private IAjaxCallDecorator lockingDecorator = new PageLockingDecorator();

    protected abstract TextSearchDataProvider createDataProvider(IModel<String> searchTextModel);

    public AdvancedSearchPage(IndexField idField) {
        this.idField = idField;
        IModel<String> searchTextModel = new PropertyModel<String>(AdvancedSearchPage.this, "searchText");
        provider = createDataProvider(searchTextModel);
        add(form = new NewSearchForm("form", searchTextModel));

        listViewContainer = new WebMarkupContainer("listViewContainer");
        listViewContainer.setOutputMarkupId(true);
        add(listViewContainer);

        add(resultForm = new Form("resultForm"));

        final WebMarkupContainer searchResults = new WebMarkupContainer("searchResults") {
            @Override public boolean isVisible() {
                return provider.size() > 0;
            }
        };
        searchResults.setOutputMarkupPlaceholderTag(true);
        resultForm.add(searchResults);

        resultForm.add(blankSlate = new WebMarkupContainer("blankSlate") {
            @Override
            public boolean isVisible() {
                return provider.size() == 0;
            }
        }.setOutputMarkupPlaceholderTag(true));

        feedbackPanel = new FIDFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        dataView = new DataView<SearchResult>("results", provider, ITEMS_PER_PAGE) {
            @Override
            protected void populateItem(Item<SearchResult> item) {
                final IsItemSelectedModel itemIsSelectedModel = new IsItemSelectedModel(item.getModel());

                CheckBox check = new CheckBox("check", itemIsSelectedModel);
                item.add(check);
                check.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(listViewContainer, actions, groupSelector);
                    }
                });

                Component detailsContainer = createDetailsPanel("detailsContainer", item.getModel());
                item.add(detailsContainer.setOutputMarkupId(true));

                detailsContainer.add(new AttributeAppender("class", Model.of("selected"), " ") {
                    @Override
                    public boolean isEnabled(Component component) {
                        return itemIsSelectedModel.getObject();
                    }
                });
            }
        };

        searchResults.add(dataView);

        groupSelector = new CheckBox("groupselector", createMultiSelectModel(dataView)) {
            {
                setOutputMarkupId(true);
                add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override protected void onUpdate(AjaxRequestTarget target) {
                        target.add(searchResults, actions);
                    }

                    @Override protected IAjaxCallDecorator getAjaxCallDecorator() {
                        return lockingDecorator;
                    }
                });
            }

            @Override public boolean isVisible() {
                return provider.size() > 0;
            }
        };
        searchResults.add(groupSelector);

        searchResults.add(new FlatLabel("currentlySelectedItems", new PropertyModel<Integer>(this, "numSelectedIds")));
        searchResults.add(new FlatLabel("totalAvailableItems", new PropertyModel<Integer>(this, "totalRows")));

        AjaxLink clearSelectionLink = new AjaxLink("clearSelectionLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                selectedIds.clear();
                target.add(listViewContainer, actions, groupSelector);
            }
            @Override protected IAjaxCallDecorator getAjaxCallDecorator() {
                return lockingDecorator;
            }
        };
        searchResults.add(clearSelectionLink);

        AjaxLink selectAllLink;
        searchResults.add(selectAllLink = new AjaxLink("selectAllLink") {
            @Override public void onClick(AjaxRequestTarget target) {
                selectedIds.clear();

                List<Long> idList = provider.getIdList();
                for (Long id : idList) {
                    selectedIds.add(Long.toString(id));
                }

                target.add(listViewContainer, actions, groupSelector);
            }

            @Override protected IAjaxCallDecorator getAjaxCallDecorator() {
                return lockingDecorator;
            }
        });

        selectAllLink.add(new FlatLabel("totalAvailableItems", new PropertyModel<Integer>(this, "totalRows")));

        resultForm.add(new AjaxPagingNavigator("navigator", dataView) {
            @Override
            public boolean isVisible() {
                return provider.size() > 0;
            }
        });
        listViewContainer.add(resultForm);

        actions = new WebMarkupContainer("actionsContainer") {
            { setOutputMarkupPlaceholderTag(true); }
            @Override public boolean isVisible() {
                return !selectedIds.isEmpty();
            }
        };

        actions.add(createActionsPanel("actions", new PropertyModel<Set<String>>(this, "selectedIds")));

        resultForm.add(actions);
    }

    protected abstract Component createDetailsPanel(String id, IModel<SearchResult> resultModel);
    protected abstract Component createActionsPanel(String id, IModel<Set<String>> selectedItemsModel);

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.assetsearch"));
    }

    private IModel<Boolean> createMultiSelectModel(final DataView<SearchResult> dataView) {
        return new IModel<Boolean>() {
            @Override
            public Boolean getObject() {
                IDataProvider<SearchResult> dataProvider = dataView.getDataProvider();
                int currentPage = dataView.getCurrentPage();
                int itemsPerPage = dataView.getItemsPerPage();
                for (Iterator<? extends SearchResult> it = dataProvider.iterator(currentPage * itemsPerPage, itemsPerPage);it.hasNext();) {
                    String idFieldString = it.next().get(idField.getField());
                    if (!selectedIds.contains(idFieldString)) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public void setObject(Boolean object) {
                IDataProvider<SearchResult> dataProvider = dataView.getDataProvider();
                currentPageSelected = object;
                int currentPage = dataView.getCurrentPage();
                int itemsPerPage = dataView.getItemsPerPage();
                for (Iterator<? extends SearchResult> it = dataProvider.iterator(currentPage * itemsPerPage, itemsPerPage);it.hasNext();) {
                    String idFieldString = it.next().get(idField.getField());
                    if (object) {
                        selectedIds.add(idFieldString);
                    } else {
                        selectedIds.remove(idFieldString);
                    }
                }
            }

            @Override
            public void detach() { }
        };
    }

    class IsItemSelectedModel implements IModel<Boolean> {
        private IModel<SearchResult> resultModel;
        public IsItemSelectedModel(IModel<SearchResult> resultModel) {
            this.resultModel = resultModel;
        }

        @Override
        public Boolean getObject() {
            return selectedIds.contains(resultModel.getObject().get(idField.getField()));
        }

        @Override
        public void setObject(Boolean object) {
            if (object) {
                selectedIds.add(resultModel.getObject().get(idField.getField()));
            } else {
                selectedIds.remove(resultModel.getObject().get(idField.getField()));
            }
        }

        @Override
        public void detach() {
        }
    }

    class NewSearchForm extends Form {

        public NewSearchForm(String id, IModel<String> searchTextModel) {
            super(id);
            setOutputMarkupPlaceholderTag(true);

            searchCriteria = new TextField<String>("searchCriteria", searchTextModel);
            searchCriteria.setRequired(true);

            add(searchCriteria);

            suggestionsContainer = new WebMarkupContainer("suggestionsContainer");
            suggestionsContainer.add( new ListView<String>("suggestions", new SuggestionsModel()) {
                @Override
                protected void populateItem(ListItem<String> item) {
                    final String suggestion = item.getModelObject();
                    item.add(new AjaxLink("link") {
                        @Override public void onClick(AjaxRequestTarget target) {
                            searchText = suggestion;
                            performSearch(target);
                        }
                    }.add(new Label("text",Model.of(suggestion))));
                }
            }).add(new AttributeAppender("class",getSuggestionsCss())).setOutputMarkupPlaceholderTag(true);

            add(suggestionsContainer);

            final AjaxCallDecorator ajaxCallDecorator = new AjaxCallDecorator() {
                @Override public CharSequence decorateScript(Component c, CharSequence script) {
                    return String.format(HIDE_LIST_JS, listViewContainer.getMarkupId()) + super.decorateScript(c, script);
                }
                @Override public CharSequence decorateOnSuccessScript(Component c, CharSequence script) {
                    return String.format(SHOW_LIST_JS, listViewContainer.getMarkupId()) + super.decorateOnSuccessScript(c, script);
                }
                @Override public CharSequence decorateOnFailureScript(Component c, CharSequence script) {
                    return String.format(SHOW_LIST_JS, listViewContainer.getMarkupId()) + super.decorateOnSuccessScript(c, script);
                }
            };

            AjaxSubmitLink submit;
            add( submit = new IndicatingAjaxSubmitLink("searchButtonId") {
                @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    performSearch(target);
                }

                @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                    // TODO DD : replace msg with properties file.
                    error("Error searching based on the following criteria:" + searchCriteria.getModelObject());
                }
            }.withAjaxCallDecorator(ajaxCallDecorator));

            setDefaultButton(submit);
        }
    }

    private IModel<?> getSuggestionsCss() {
        return new Model<String>() {
            @Override public String getObject() {
                return provider.getSuggestions().size()>0 ? "suggestion" : "hide";
            }
        };
    }

    private void performSearch(AjaxRequestTarget target) {
        selectedIds.clear();
        dataView.setCurrentPage(0);
        target.add(listViewContainer, feedbackPanel, blankSlate, form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/actions-menu.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/pageStyles/searchResults.css");

        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");
        response.renderOnDomReadyJavaScript("subMenu.init();");
    }

    class SuggestionsModel implements IModel<List<String>> {
        @Override
        public List<String> getObject() {
            return provider.getSuggestions();
        }

        @Override public void setObject(List<String> object) {
            // do nothing.
        }

        @Override public void detach() {
            // do nothing.
        }
    }

    public Integer getTotalRows() {
        return provider.size();
    }

    public String getTotalRowsLabel() {
        return String.format("(%d items)", provider.size());
    }

    public Integer getNumSelectedIds() {
        if (null == selectedIds) {
            return 0;
        }

        return selectedIds.size();
    }

    public String getNumSelectedIdsLabel() {
        return String.format("(%d items)", getNumSelectedIds());
    }

    class PageLockingDecorator extends AjaxCallDecorator {
        @Override
        public CharSequence decorateOnFailureScript(Component c, CharSequence script) {
            return ENABLE_INPUTS_JS + script;
        }

        @Override
        public CharSequence decorateOnSuccessScript(Component c, CharSequence script) {
            return ENABLE_INPUTS_JS + script;
        }

        @Override
        public CharSequence decorateScript(Component c, CharSequence script) {
            return DISABLE_INPUTS_JS + script;
        }
    }

}

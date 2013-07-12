package com.n4systems.fieldid.wicket.pages.search;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.LatentImage;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.form.IndicatingAjaxSubmitLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.QuickEventPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.Asset;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteriaContainer;
import com.n4systems.services.search.AssetIndexField;
import com.n4systems.services.search.SearchResult;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


public class NewSearchPage extends FieldIDFrontEndPage {

    private static final String HIDE_LIST_JS = "$('#%s').hide();";
    private static final String SHOW_LIST_JS = "$('#%s').show();";

    private static String CUSTOM_ATTR_FORMAT = "<span class='attr'>%s=</span><span class='value'>%s</span>";

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
    private SearchDataProvider provider;
    private Component blankSlate;
    private DataView<SearchResult> dataView;
    private CheckBox groupSelector;
    private WebMarkupContainer suggestionsContainer;
    private final NewSearchForm form;

    private boolean currentPageSelected = false;

    public NewSearchPage() {
        IModel<String> searchTextModel = new PropertyModel<String>(NewSearchPage.this, "searchText");
        provider = new SearchDataProvider(searchTextModel) {
            @Override protected Formatter getFormatter() {
                return new SimpleHTMLFormatter("<span class=\"matched-text\">", "</span>");
            }
        };
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

        final boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        final boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");

        dataView = new DataView<SearchResult>("results", provider, ITEMS_PER_PAGE) {
            @Override
            protected void populateItem(Item<SearchResult> item) {
                final SearchResult result = item.getModelObject();

                final IsItemSelectedModel itemIsSelectedModel = new IsItemSelectedModel(item.getModel());

                final WebMarkupContainer detailsContainer = new WebMarkupContainer("detailsContainer");
                item.add(detailsContainer.setOutputMarkupId(true));

                detailsContainer.add(new AttributeAppender("class", Model.of("selected"), " ") {
                    @Override
                    public boolean isEnabled(Component component) {
                        return itemIsSelectedModel.getObject();
                    }
                });

                BookmarkablePageLink summaryLink;
                final Long assetId = result.getLong(AssetIndexField.ID.getField());
                detailsContainer.add(summaryLink = new BookmarkablePageLink<Void>("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(assetId)));
                summaryLink.add(new Label("summary", getIdentifier(result)).setEscapeModelStrings(false));

                detailsContainer.add(new Label("fixedAttributes", getFixedAttributes(result)).setEscapeModelStrings(false));

                detailsContainer.add(new Label("customAttributes", getCustomAttributes(result)).setEscapeModelStrings(false));

                CheckBox check = new CheckBox("check", itemIsSelectedModel);
                item.add(check);
                check.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(listViewContainer, actions, groupSelector);
                    }
                });

                detailsContainer.add(new Link("viewLink") {
                    @Override public void onClick() {
                        setResponsePage(new AssetSummaryPage(new PageParameters().add("uniqueID",assetId)));
                    }
                });

                detailsContainer.add(new BookmarkablePageLink("startEventLink", QuickEventPage.class, PageParametersBuilder.id(assetId)).setVisible(hasCreateEvent));
                detailsContainer.add(new NonWicketLink("mergeLink", "assetMergeAdd.action?uniqueID=" + assetId, new AttributeAppender("class", "mattButtonRight")).setVisible(hasEditEvent));

                detailsContainer.add(new LatentImage("assetImage") {
                    @Override protected String updateSrc() {
                        Asset asset = persistenceService.find(Asset.class, assetId);
                        // CAVEAT : careful about making assumptions regarding asset Ids retrieved from search results (ie. lucene index).
                        // it is entirely possible that it might be a stale id and therefore might not exist.
                        // e.g. if user deletes an asset and you search for it before the asset index has been updated.
                        // .: lesson learned is always guard against nulls!
                        if (asset==null || StringUtils.isBlank(asset.getImageName())) {
                            return null;
                        } else {
                            return s3Service.getAssetProfileImageThumbnailURL(assetId, asset.getImageName()).toString();
                        }
                    }
                });

            }
        };

        searchResults.add(dataView);

        groupSelector = new CheckBox("groupselector", createMultiSelectModel(dataView)) {
            {
                setOutputMarkupId(true);
                add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(searchResults, actions);
                    }
                });
            }

            @Override
            public boolean isVisible() {
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
        };
        searchResults.add(clearSelectionLink);

        AjaxLink selectAllLink = null;
        searchResults.add(selectAllLink = new AjaxLink("selectAllLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                selectedIds.clear();

                List<Long> idList = provider.getIdList();
                for (Long id : idList) {
                    selectedIds.add(Long.toString(id));
                }

                target.add(listViewContainer, actions, groupSelector);


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

        actions = new WebMarkupContainer("actions") {
            { setOutputMarkupPlaceholderTag(true); }
            @Override public boolean isVisible() {
                return !selectedIds.isEmpty();
            }
        };

        actions.add(new SubmitLink("massEventLink") {
            @Override
            public void onSubmit() {

                AssetSearchCriteria assetSearchCriteria = createAssetSearchCriteria();

                HttpServletRequest httpServletRequest = ((ServletWebRequest) getRequest()).getContainerRequest();
                HttpSession session = httpServletRequest.getSession();

                SearchCriteriaContainer<AssetSearchCriteria> container = new LegacyReportCriteriaStorage().storeCriteria(assetSearchCriteria, session);

                String formattedUrl = String.format("/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", container.getSearchId());
                String destination = ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath() + formattedUrl;

                getRequestCycle().replaceAllRequestHandlers(new RedirectRequestHandler(destination));
            }
        });

        actions.add(new Link("massUpdateLink") {
            @Override
            public void onClick() {
                AssetSearchCriteria assetSearchCriteria = createAssetSearchCriteria();
                setResponsePage(new MassUpdateAssetsPage(new Model(assetSearchCriteria), NewSearchPage.this));
            }
        });

        actions.add(new Link("massScheduleLink") {
            @Override
            public void onClick() {
                AssetSearchCriteria assetSearchCriteria = createAssetSearchCriteria();
                setResponsePage(new MassSchedulePage(new Model(assetSearchCriteria)));
            }
        });

        resultForm.add(actions);
    }

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
                    String idField = it.next().get(AssetIndexField.ID.getField());
                    if (!selectedIds.contains(idField)) {
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
                    String idField = it.next().get(AssetIndexField.ID.getField());
                    if (object) {
                        selectedIds.add(idField);
                    } else {
                        selectedIds.remove(idField);
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
            return selectedIds.contains(resultModel.getObject().get(AssetIndexField.ID.getField()));
        }

        @Override
        public void setObject(Boolean object) {
            if (object) {
                selectedIds.add(resultModel.getObject().get(AssetIndexField.ID.getField()));
            } else {
                selectedIds.remove(resultModel.getObject().get(AssetIndexField.ID.getField()));
            }
        }

        @Override
        public void detach() {
        }
    }

    private String getCustomAttributes(SearchResult result) {
        List<String> fields = Lists.newArrayList();
        for (String field:result.getFields()) {
            AssetIndexField f = AssetIndexField.fromString(field);
            boolean highlighted = result.isHighlighted(field);
            // if no predefined field exists, we know its a custom field .: display it.
            // if it's a predefined field but one that isn't always displayed, display it if it matches the criteria.
            if (f==null || (f.isNonDisplayedFixedAttribute() && highlighted && !f.isInternal())) {
                int index = highlighted ? 0 :fields.size();
                String key = WordUtils.capitalize(field);
                String value = result.get(field);
                fields.add(index, String.format(CUSTOM_ATTR_FORMAT, key, value));
            }
        }
        return Joiner.on("<span class='separator'>|</span>").skipNulls().join(fields.toArray(new String[fields.size()]));
    }

    private String getFixedAttributes(SearchResult result) {
        List<String> fields = Lists.newArrayList();
        for (AssetIndexField field : AssetIndexField.getDisplayedFixedAttributes()) {
            if (!field.isInternal()) {
                fields.add(result.get(field.getField()));
            }
        }
        return Joiner.on(" / ").skipNulls().join(Iterables.filter(fields, new Predicate<String>() {
            @Override public boolean apply(String input) {
                return input != null && StringUtils.isNotBlank(input);
            }
        }));
    }

    private String getIdentifier(SearchResult result) {
        String type = result.get(AssetIndexField.TYPE.getField());
        String id = result.get(AssetIndexField.IDENTIFIER.getField());
        String status = result.get(AssetIndexField.STATUS.getField());
        return Joiner.on(" / ").skipNulls().join(type, id, status);
    }

    private AssetSearchCriteria createAssetSearchCriteria() {

        List<Long> ids = new ArrayList<Long>();

        for (String selectedId : selectedIds) {
            ids.add(Long.parseLong(selectedId));
        }

        AssetSearchCriteria assetSearchCriteria = new AssetSearchCriteria();

        MultiIdSelection multiIdSelection = new MultiIdSelection();
        multiIdSelection.addAllIds(ids);

        assetSearchCriteria.setSelection(multiIdSelection);

        return assetSearchCriteria;
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
            ; // do nothing.
        }

        @Override public void detach() {
            ; // do nothing.
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


}

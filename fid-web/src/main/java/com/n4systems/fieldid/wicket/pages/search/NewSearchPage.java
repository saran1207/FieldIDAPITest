package com.n4systems.fieldid.wicket.pages.search;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.LatentImage;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.form.IndicatingAjaxSubmitLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.Asset;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteriaContainer;
import com.n4systems.services.search.AssetIndexField;
import com.n4systems.services.search.FullTextSearchService;
import com.n4systems.services.search.SearchResult;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
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
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NewSearchPage extends FieldIDFrontEndPage {

    private static final String HIDE_LIST_JS = "$('#%s').hide();";
    private static final String SHOW_LIST_JS = "$('#%s').show();";

    private @SpringBean FullTextSearchService fullTextSearchService;
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
    private IDataProvider<SearchResult> provider;
    private final Component blankSlate;

    public NewSearchPage() {
        IModel<String> searchTextModel = new PropertyModel<String>(NewSearchPage.this, "searchText");
        provider = new SearchDataProvider(searchTextModel) {
            @Override protected Formatter getFormatter() {
                return new SimpleHTMLFormatter("<span class=\"matched-text\">", "</span>");
            }
        };
        add(new NewSearchForm("NewSearchForm", searchTextModel));

        listViewContainer = new WebMarkupContainer("listViewContainer");
        listViewContainer.setOutputMarkupId(true);
        add(listViewContainer);

        add(resultForm = new Form("resultForm"));

        final WebMarkupContainer searchResults = new WebMarkupContainer("searchResults") {
            @Override public boolean isVisible() {
                return provider.size() > 0;
            }
        };
        resultForm.add(searchResults);

        searchResults.add(new CheckBox("groupselector") {
            @Override public boolean isVisible() {
                return provider.size() > 0;
            }
        });

        feedbackPanel = new FIDFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        add(blankSlate = new WebMarkupContainer("blankSlate") {
            @Override public boolean isVisible() {
                return provider.size() == 0;
            }
        }.setOutputMarkupPlaceholderTag(true));

        final boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        final boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");

        DataView<SearchResult> dataView = new DataView<SearchResult>("results", provider, 10) {
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
                        target.add(detailsContainer, actions);
                    }
                });

                detailsContainer.add(new Link("viewLink") {
                    @Override public void onClick() {
                        setResponsePage(new AssetSummaryPage(new PageParameters().add("uniqueID",assetId)));
                    }
                });

                detailsContainer.add(new NonWicketLink("startEventLink", "quickEvent.action?assetId=" + assetId, new AttributeAppender("class", "mattButtonMiddle")).setVisible(hasCreateEvent));
                detailsContainer.add(new NonWicketLink("mergeLink", "assetMergeAdd.action?uniqueID=" + assetId, new AttributeAppender("class", "mattButtonRight")).setVisible(hasEditEvent));

                detailsContainer.add(new LatentImage("assetImage") {
                    @Override protected String updateSrc() {
                        Asset asset = persistenceService.find(Asset.class, assetId);
                        if (StringUtils.isBlank(asset.getImageName())) {
                            return null;
                        } else {
                            return s3Service.getAssetProfileImageThumbnailURL(assetId, asset.getImageName()).toString();
                        }
                    }
                });
            }
        };

        searchResults.add(dataView);


        resultForm.add(new PagingNavigator("navigator", dataView) {
            @Override
            public boolean isVisible() {
                return provider.size() > 0;
            }
        });
        listViewContainer.add(resultForm);

        actions = new WebMarkupContainer("actions") {
            { setOutputMarkupPlaceholderTag(true); }
            @Override public boolean isVisible() {
                return provider.size() > 0;
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
                setResponsePage(new MassUpdateAssetsPage(new Model(assetSearchCriteria)));
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
//                String key = result.getKeyValueStringCapitalized(field);
//                String value = result.get(field);
                // TODO DD : format these. <span class="search_attr">key</span>   <span class="search-value">value</span>
                fields.add(index, result.getKeyValueStringCapitalized(field));
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
                    selectedIds.clear();
                    target.add(listViewContainer, feedbackPanel, blankSlate);
                }

                @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                    // TODO DD : replace msg with properties file.
                    error("Error searching based on the following criteria:" + searchCriteria.getModelObject());
                }
            }.withAjaxCallDecorator(ajaxCallDecorator));

            setDefaultButton(submit);
        }
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

}

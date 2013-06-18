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
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NewSearchPage extends FieldIDFrontEndPage {

    @SpringBean
    protected FullTextSearchService fullTextSearchService;

    private @SpringBean PersistenceService persistenceService;
    private @SpringBean S3Service s3Service;

    protected IModel<Asset> assetModel;
    private String searchText = null;
    private PageableListView resultsListView = null;
    private WebMarkupContainer listViewContainer = null;
    private IModel assetList = null;
    private TextField<String> searchCriteria = null;
    private FIDFeedbackPanel feedbackPanel = null;
    private WebMarkupContainer actions;
    private Link printLink;
    private Link exportLink;
    private Link massEventLink;
    private SubmitLink massUpdateLink;
    private SubmitLink massScheduleLink;
    private Model<AssetSearchCriteria> model = null;
    private AssetSearchCriteria assetSearchCriteria = null;
    private List<Asset> selectedAssets;
    SubmitLink massEventsLink;
    AssetSearchCriteria assetSearchCriteria1 = null;
    final  SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yy");
    private List<SearchResult> results = Lists.newArrayList();
    private Form resultForm;


    public NewSearchPage(PageParameters params) {
        super(params);
    }

    public NewSearchPage() {
        this(new PageParameters());

        add(new NewSearchForm("NewSearchForm"));

        listViewContainer = new WebMarkupContainer("listViewContainer");
        listViewContainer.setOutputMarkupId(true);
        add(listViewContainer);

        add(resultForm = new Form("resultForm"));

        final CheckGroup<Asset> assetCheckGroup = new CheckGroup<Asset>("selected_assets", new ArrayList<Asset>()) {
            @Override public boolean isVisible() {
                return !results.isEmpty();
            }
        };
        resultForm.add(assetCheckGroup);
        assetCheckGroup.add(new CheckGroupSelector("groupselector") {
            @Override public boolean isVisible() {
                return (results != null && !results.isEmpty());
            }
        });


        StringResourceModel stringResourceModel = new StringResourceModel("label.select_assets_all",
                this, null, getLocale());
        assetCheckGroup.add(new Label("selectAssetsAllLabel", stringResourceModel) {
            @Override public boolean isVisible() {
                return (results != null && !results.isEmpty());
            }
        });

        StringResourceModel stringResourceAModel = new StringResourceModel("label.select_assets",
                this, null, getLocale());
        assetCheckGroup.add(new Label("selectAssetsLabel", stringResourceAModel) {
            @Override public boolean isVisible() {
                return (results != null && !results.isEmpty());
            }
        });

        feedbackPanel = new FIDFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        add(new WebMarkupContainer("blankSlate") {
            @Override public boolean isVisible() {
                return results.isEmpty();
            }
        }.setOutputMarkupPlaceholderTag(true));

        final boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        final boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");

        resultsListView = new PageableListView<SearchResult>("results", new PropertyModel<List<? extends SearchResult>>(this, "results"), 10) {
            @Override
            protected void populateItem(ListItem<SearchResult> item) {
                final SearchResult result = item.getModelObject();

                BookmarkablePageLink summaryLink;
                final Long assetId = result.getLong(AssetIndexField.ID.getField());
                item.add(summaryLink = new BookmarkablePageLink<Void>("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(assetId)));
                summaryLink.add(new Label("summary", getIdentifier(result)).setEscapeModelStrings(false));

                item.add(new Label("fixedAttributes", getFixedAttributes(result)).setEscapeModelStrings(false));

                item.add(new Label("customAttributes", getCustomAttributes(result)).setEscapeModelStrings(false));

                item.add(new Check<SearchResult>("check", item.getModel()));

                item.add(new Link("viewLink") {
                    @Override public void onClick() {
                        setResponsePage(new AssetSummaryPage(new PageParameters().add("uniqueID",assetId)));
                    }
                });

                item.add(new NonWicketLink("startEventLink", "quickEvent.action?assetId=" + assetId, new AttributeAppender("class", "mattButtonMiddle")).setVisible(hasCreateEvent));
                item.add(new NonWicketLink("mergeLink", "assetMergeAdd.action?uniqueID=" + assetId, new AttributeAppender("class", "mattButtonRight")).setVisible(hasEditEvent));

                // TODO DD ;
                item.add(new LatentImage("assetImage") {
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
        //assetListView.setReuseItems(true);
        assetCheckGroup.add(resultsListView);


        resultForm.add(new PagingNavigator("navigator", resultsListView){
            @Override
            public boolean isVisible() {
                return (results!= null && !results.isEmpty());
            }
        });
        listViewContainer.add(resultForm);

        actions=new WebMarkupContainer("actions"){
            @Override public boolean isVisible() {
                return (results != null && !results.isEmpty());
            }
        };

        actions.add(new SubmitLink("massEventLink") {
            @Override
            public void onSubmit() {

                AssetSearchCriteria assetSearchCriteria3 = transformToAssetSearchCriteria(assetCheckGroup.getModelObject());

                HttpServletRequest httpServletRequest = ((ServletWebRequest) getRequest()).getContainerRequest();
                HttpSession session = httpServletRequest.getSession();

                SearchCriteriaContainer<AssetSearchCriteria> container = new LegacyReportCriteriaStorage().storeCriteria(assetSearchCriteria3, session);

                String formattedUrl = String.format("/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", container.getSearchId());
                String destination = ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath() + formattedUrl;

                getRequestCycle().replaceAllRequestHandlers(new RedirectRequestHandler(destination));
            }
        });

        actions.add(massUpdateLink = new SubmitLink("massUpdateLink") {
            @Override public void onSubmit() {
                AssetSearchCriteria assetSearchCriteria1 = transformToAssetSearchCriteria(assetCheckGroup.getModelObject());
                setResponsePage(new MassUpdateAssetsPage(new Model(assetSearchCriteria1)));
            }
        });

        actions.add(massScheduleLink = new SubmitLink("massScheduleLink") {
            @Override public void onSubmit() {
                AssetSearchCriteria assetSearchCriteria1 = transformToAssetSearchCriteria(assetCheckGroup.getModelObject());
                setResponsePage(new MassSchedulePage(new Model(assetSearchCriteria1)));
            }
        });

        resultForm.add(actions);
    }

    private String getCustomAttributes(SearchResult result) {
        List<String> fields = Lists.newArrayList();
        for (String field:result.getFields()) {
            AssetIndexField f = AssetIndexField.fromString(field);
            if (f==null || (f.isNonDisplayedFixedAttribute() && result.isHighlighted(f.getField()))) {
                fields.add(result.getKeyValueString(field));
            }
        }
        return Joiner.on(" | ").join(fields.toArray(new String[fields.size()]));
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

    private AssetSearchCriteria transformToAssetSearchCriteria(Collection<Asset> modelObject) {

        List<Long> ids = new ArrayList<Long>();
        for(Asset asset: modelObject) {
            ids.add(asset.getID());
        }

        AssetSearchCriteria assetSearchCriteria1 = new AssetSearchCriteria();

        MultiIdSelection multiIdSelection = new MultiIdSelection();
        multiIdSelection.addAllIds(ids);

        assetSearchCriteria1.setSelection(multiIdSelection);

        return assetSearchCriteria1;
    }

    class NewSearchForm extends Form {

        public NewSearchForm(String id) {
            super(id);
            setOutputMarkupPlaceholderTag(true);

            searchCriteria = new TextField<String>("searchCriteria",new PropertyModel<String>(NewSearchPage.this, "searchText"));
            searchCriteria.setRequired(true);
            add(searchCriteria);

            add(new AjaxSubmitLink("searchButtonId") {
                @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    results = fullTextSearchService.search(searchText,new SimpleHTMLFormatter("<span class=\"matched-text\">", "</span>")).getResults();
                    target.add(NewSearchPage.this);
                    target.add(NewSearchPage.this.feedbackPanel);
                }

                @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                    error("Error searching based on the following criteria:" + searchCriteria.getModelObject());
                }
            });
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/actions-menu.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/pageStyles/searchResults.css");

        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnDomReadyJavaScript("subMenu.init();");
    }



}

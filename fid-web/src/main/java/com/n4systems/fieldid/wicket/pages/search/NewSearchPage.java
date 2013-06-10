package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.asset.AssetToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.asset.summary.AssetImagePanel;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.Asset;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteriaContainer;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewSearchPage extends FieldIDFrontEndPage {

    @SpringBean
    protected AssetService assetService;

    @SpringBean
    private S3Service s3Service;

    protected IModel<Asset> assetModel;
    private String searchText = "Type search here.";
    private List<Asset> assets = null;
    private PageableListView assetListView = null;
    private WebMarkupContainer listViewContainer = null;
    private IModel assetList = null;
    private TextField<String> searchCriteria = null;
    private FeedbackPanel feedbackPanel = null;
    private WebMarkupContainer actions;
    private Link printLink;
    private Link exportLink;
    private Link massEventLink;
    private SubmitLink massUpdateLink;
    private SubmitLink massScheduleLink;
    private Model<AssetSearchCriteria> model = null;
    private AssetSearchCriteria assetSearchCriteria = null;
    private List<Asset> selectedAssets;
    String link = "";
    SubmitLink massEventsLink;
    AssetSearchCriteria assetSearchCriteria1 = null;
    final  SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yy");



    public NewSearchPage(PageParameters params) {
        super(params);
    }

    public NewSearchPage() {
        this(new PageParameters());
       addStuff();
    }

    private void addStuff() {

     /* TODO - look at using detachable model */
     //get the list of items to display from provider (database, etc)
     //in the form of a LoadableDetachableModel
//       assetList =  new LoadableDetachableModel()
//        {
//            protected Object load() {
//                return getAssetList();
//            }
//        };

//        assetListView = new ListView<Asset>("assets", assetList) {
//            @Override
//            protected void populateItem(ListItem<Asset> item) {
//                Asset asset = item.getModelObject();
//                item.add(new Label("identifier", asset.getIdentifier()));
//                item.add(new Label("name", asset.getRfidNumber()));
//
//            }
//        };

        add(new NewSearchForm("NewSearchForm"));

        listViewContainer = new WebMarkupContainer("listViewContainer");
        listViewContainer.setOutputMarkupId(true);
        add(listViewContainer);

        final CheckGroup<Asset> assetCheckGroup = new CheckGroup<Asset>("selected_assets", new ArrayList<Asset>());

        Form resultForm = new Form("resultForm"){
            @Override
            protected void onSubmit() {
                info("Languages : " + assetCheckGroup.getDefaultModelObjectAsString());
                AssetSearchCriteria assetSearchCriteria1 = transformToAssetSearchCriteria(assetCheckGroup.getModelObject());
               if (link.equals("update")) {
                setResponsePage(new MassUpdateAssetsPage(new Model(assetSearchCriteria1)));
               } else if (link.equals("schedule")) {
                    setResponsePage(new MassSchedulePage(new Model(assetSearchCriteria1)));
               }
            }
        };

        add(resultForm);
        resultForm.add(assetCheckGroup);
        assetCheckGroup.add(new CheckGroupSelector("groupselector"){
            @Override
            public boolean isVisible() {
                return (assets != null && !assets.isEmpty());
            }

        });

        assetListView = new PageableListView<Asset>("assets", new PropertyModel<List<? extends Asset>>(NewSearchPage.this, "assets"), 10) {
            @Override
            protected void populateItem(ListItem<Asset> item) {
                Asset asset = item.getModelObject();
                AssetToViewConverter assetToViewConverter = new AssetToViewConverter();
                AssetView assetView = null;
                try {
                    assetView = assetToViewConverter.toView(asset);
                } catch (ConversionException e) {
                    e.printStackTrace();
                }

                BookmarkablePageLink summaryLink;
                item.add(summaryLink = new BookmarkablePageLink<Void>("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(asset.getId())));

                item.add(new Check<Asset>("check", item.getModel()));
                summaryLink.add(new Label("assetIdentifier", assetView.getIdentifier()));

                item.add(new Label("assetType", (asset.getType()==null) ? "":asset.getType().getDisplayName()));
                item.add(new Label("assetDescription", asset.getDescription()));
                item.add(new Label("assetStatus", assetView.getStatus()));
                item.add(new Label("customer", assetView.getCustomer()));
                item.add(new Label("location", assetView.getLocation()));

//                item.add(new Label("dateIdentified", assetView.getIdentified().toString()));
                item.add(new Label("dateIdentified", (assetView.getIdentified()==null) ? "": sf.format(assetView.getIdentified())));

                Map<String, String> attrs = assetView.getAttributes();
                String attributes = attrs.toString();
                item.add(new Label("attributes", attributes));

                item.add(new Label("orderNumber", assetView.getPurchaseOrder()));
        //      item.add(new Label("orderNumber",(asset.getOrderNumber()==null) ? "":asset.getOrderNumber()));
                item.add(new Label("rfidNumber",  assetView.getRfidNumber()));


                BaseOrg owner = asset.getOwner();
                item.add(new Label("ownerInfo", getOwnerLabel(owner, asset.getAdvancedLocation())));



//                add image
                boolean imageExists;
                final String imageUrl;
                if(asset.getImageName() == null) {
                    imageUrl = ContextAbsolutizer.toContextAbsoluteUrl("/file/downloadAssetTypeImage.action?uniqueID=" + asset.getType().getId());
                    if(asset.getType().getImageName() != null)
                        imageExists = new File(PathHandler.getAssetTypeImageFile(asset.getType()), asset.getType().getImageName()).exists();
                    else
                        imageExists = false;
                } else {
                    imageExists = s3Service.assetProfileImageExists(asset.getId(), asset.getImageName());
                    imageUrl = s3Service.getAssetProfileImageThumbnailURL(asset.getId(), asset.getImageName()).toString();
                }


                final ModalWindow modalWindow = new FIDModalWindow("assetImageModalWindow");
                modalWindow.setInitialHeight(500);
                modalWindow.setInitialWidth(540);
                item.add(modalWindow);

                ExternalImage assetImage;
                item.add(assetImage = new ExternalImage("assetImage", imageUrl));
                assetImage.setVisible(imageExists);
                assetImage.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        modalWindow.setContent(new AssetImagePanel(modalWindow.getContentId(), Model.of(imageUrl)));
                        modalWindow.show(target);
                    }
                });
            }
        };
        assetListView.setReuseItems(true);
        assetCheckGroup.add(assetListView);

        resultForm.add(new PagingNavigator("navigator", assetListView){
            @Override
            public boolean isVisible() {
                return (assets != null && !assets.isEmpty());
            }
        });
        listViewContainer.add(resultForm);

        feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        actions=new WebMarkupContainer("actions"){
            @Override
            public boolean isVisible() {
                return (assets != null && !assets.isEmpty());
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
                link = "update";
//                setResponsePage(new MassUpdateAssetsPage(model));
            }
        });

        actions.add(massScheduleLink = new SubmitLink("massScheduleLink") {
            @Override public void onSubmit() {
                link = "schedule";
//                setResponsePage(new MassSchedulePage(model));
            }
        });

        resultForm.add(actions);

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
//            TextField<String> searchCriteria = new TextField<String>("searchCriteria",new PropertyModel<String>(NewSearchPage.this, "searchCriteria"));

            add(new AjaxSubmitLink("searchButtonId") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    // do stuff...
//                    assets = NewSearchPage.getDummyAssets();

                  assets = assetService.getBogusAssets();
                  target.add(listViewContainer);
                  target.add(NewSearchPage.this.feedbackPanel);

//                  user entered search criteria
//                  System.out.println("" + searchCriteria.getModelObject());


                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    error("Error searching based on the following criteria:" + searchCriteria.getModelObject());
                }
            });
        }
    }

    private List<Asset> getAssetList() {
        List assets = assetService.getBogusAssets();
        return assets;
    }

    public static List<Asset> getDummyAssets() {
        List assets = new ArrayList();
        Asset asset1 = new Asset();
        asset1.setRfidNumber("123");
        assets.add(asset1);
        return assets;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
//        response.renderCSSReference("style/newCss/asset/asset.css");
        response.renderCSSReference("style/newCss/asset/actions-menu.css");
//        response.renderCSSReference("style/newCss/asset/header.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/pageStyles/searchResults.css");

        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnDomReadyJavaScript("subMenu.init();");

    }

    private String getOwnerLabel(BaseOrg owner, Location advancedLocation) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(owner.getHierarchicalDisplayName());

        if(advancedLocation != null && !advancedLocation.getFullName().isEmpty()) {
            stringBuilder.append(", ").append(advancedLocation.getFullName());
        }
        return stringBuilder.toString();
    }

    public static String markSearchResults(String searchCriteria, HashMap<String,String> fdoc) {

     // loop through fdoc
       // Pattern pattern = Pattern.compile("/\\{\\w+\\}/");
        Pattern pattern = Pattern.compile(searchCriteria);

        for (Map.Entry<String, String> entry : fdoc.entrySet())
        {
            // if matches search criteria add span around value
            //System.out.println(entry.getKey() + "/" + entry.getValue());

            Matcher matcher = pattern.matcher(entry.getValue());
            if (matcher.find()) {
                System.out.println(matcher.group(0) + " " + entry.getValue()); //prints /{item}/
                entry.setValue("<span>" + entry.getValue() + "</span>");
            //    return matcher.group(0);
            } else {
                System.out.println("Match not found");
            }



        }

        // mark matching search with span tage - return string of all Map.toString

        return "";


    }


    class FDocument {

        private HashMap<String,String> fields;

        public FDocument(HashMap<String,String> fields) {
            this.fields = fields;
        }

        public HashMap<String,String> getFields() {
            return fields;
        }


    }

    public static void main(String[] args) {

      HashMap<String,String> fdoc = new HashMap<String, String>();
        fdoc.put("colour","blue");
        fdoc.put("size","large");
        fdoc.put("shape","round");
        fdoc.put("song", "blue skies");
        fdoc.put("song1", "overhead light-blue skies");

        String result = NewSearchPage.markSearchResults("blue", fdoc);

        System.out.println("Result is " + result);
        System.out.println("Map is " + fdoc.toString());

    }


}

package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.ejb.AssetManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.table.StyledAjaxPagingNavigator;
import com.n4systems.fieldid.wicket.data.AssetSearchForAssetMergeDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.AssetMergeTask;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;

import java.util.Date;
import java.util.Locale;

@UserPermissionFilter(userRequiresOneOf={Permissions.TAG})
public class AssetMergePage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(AssetMergePage.class);
    private final int searchPageSize = 10;

    @SpringBean
    private AssetManager assetManager;
    @SpringBean
    private UserService userService;

    private LoaderFactory loaderFactory;

    private Long losingAssetId;
    private Asset losingAsset;

    private CompoundPropertyModel<Asset> winningAsset;

    private WebMarkupContainer stepsContainer;

    private WebMarkupContainer step1Container;
    private WebMarkupContainer step1ToggledContainer;

    private WebMarkupContainer step2Container;
    private WebMarkupContainer step2ToggledContainer;
    private AjaxButton assetSearchButton;
    private TextField assetSearchTerm;

    private WebMarkupContainer step3Container;
    private WebMarkupContainer step3ToggledContainer;

    private AjaxLink cancelButton;

    private WebMarkupContainer hints;

    private WebMarkupContainer completedMessageContainer;

    public AssetMergePage(PageParameters params) {
        super(params);
        losingAsset = assetManager.findAssetAllFields(losingAssetId, getSecurityFilter());
        if (losingAsset == null) {
            logger.error("AssetMerge cannot find losing asset " + losingAssetId);
            throw new MissingEntityException();
        }
        winningAsset = new CompoundPropertyModel<>((Asset)null);
        addComponents();
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        losingAssetId = params.get("uniqueID").toLong();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSS(".emptyList h2 {color: #333;}", "legacyStyle");
        response.renderCSSReference("style/legacy/steps.css");
        /* Function to enable/disable the winner asset search button based on the search term field not being empty */
        response.renderJavaScript("function setAssetSearchButtonStatus(searchTermField) {" +
                        "var searchButton = document.getElementById('" + assetSearchButton.getMarkupId() + "'); " +
                        "if (searchTermField.value.length < 1) {" +
                            "searchButton.setAttribute('disabled','disabled');} " +
                            "else {searchButton.removeAttribute('disabled');} }",
                null);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.merge_assets"));
    }

    private void addComponents() {

        stepsContainer = createHideableWebMarkupContainer("steps");
        stepsContainer.setVisible(true);
        add(stepsContainer);

        createStep1Content();
        createStep2Content();
        createStep3Content();

        cancelButton = new AjaxLink("cancelMergeAssetsPage") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(IdentifyOrEditAssetPage.class,
                        PageParametersBuilder.param("id", losingAssetId));
            }
        };
        stepsContainer.add(cancelButton);

        hints = createHideableWebMarkupContainer("hints");
        hints.setVisible(true);
        add(hints);

        createConfirmationPageContent();
    }

    private void createStep1Content() {

        step1Container = new WebMarkupContainer("step1Container");
        step1Container.setOutputMarkupId(true);
        stepsContainer.add(step1Container);
        step1ToggledContainer = createHideableWebMarkupContainer("step1ToggledContainer");
        step1Container.add(step1ToggledContainer);

        step1ToggledContainer.add(new Label("identifierLabel", getIdentifierLabel(losingAsset)));
        step1ToggledContainer.add(new Label("losingAsset.identifier", losingAsset.getIdentifier()));
        step1ToggledContainer.add(new Label("losingAsset.rfidNumber", losingAsset.getRfidNumber()));
        step1ToggledContainer.add(new Label("losingAsset.owner.name", losingAsset.getOwner().getName()));
        step1ToggledContainer.add(new Label("losingAsset.type.name", losingAsset.getType().getName()));
        step1ToggledContainer.add(new Label("losingAsset.identified", formatDate(losingAsset.getIdentified(), false)));
        step1ToggledContainer.add(new AjaxLink("step1Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                closeContainer(target, step1Container, step1ToggledContainer);
                openContainer(target, step2Container, step2ToggledContainer);
            }
        });
    }

    private void createStep2Content() {

        step2Container = new WebMarkupContainer("step2Container");
        step2Container.setOutputMarkupId(true);
        stepsContainer.add(step2Container);
        step2ToggledContainer = createHideableWebMarkupContainer("step2ToggledContainer");
        step2ToggledContainer.setVisible(false);
        step2Container.add(step2ToggledContainer);

        final AssetSearchForAssetMergeDataProvider searchDataProvider =
                new AssetSearchForAssetMergeDataProvider(searchPageSize);
        final WebMarkupContainer foundSearchResult = createHideableWebMarkupContainer("foundSearchResult");

        final WebMarkupContainer notFoundSearchResult = createHideableWebMarkupContainer("notFoundSearchResult");

        final DataView<Asset> dataView =
                new DataView<Asset>("result.dataView", searchDataProvider) {
                    @Override
                    public void populateItem(final Item<Asset> item) {
                        Asset asset = item.getModelObject();
                        final Long selectedAssetId = asset.getID();
                        item.add(new AjaxLink("selectedWinningAsset") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                winningAsset.setObject(assetManager.findAssetAllFields(selectedAssetId, getSecurityFilter()));
                                closeContainer(target, step2Container, step2ToggledContainer);
                                openContainer(target, step3Container, step3ToggledContainer);
                            }
                        });
                        item.add(new Label("result.assetType", asset.getType().getName()));
                        item.add(new Label("result.identifier", asset.getIdentifier()));
                        item.add(new Label("result.rfidnumber", asset.getRfidNumber()));
                        item.add(new Label("result.referenceNumber", asset.getCustomerRefNumber()));
                        item.add(new Label("result.owner", asset.getOwner().getName()));
                        item.add(new Label("result.assetStatus",
                                asset.getAssetStatus() != null ? asset.getAssetStatus().getName() : ""));
                        item.add(new Label("result.nextScheduledDate",
                                formatDate(getNextScheduledEventDate(asset.getId()), false)));
                    }
                };
        dataView.setOutputMarkupId(true);
        dataView.setItemsPerPage(searchPageSize);

        final WebMarkupContainer multipleResultsMsg = new WebMarkupContainer("multipleResultsLabel");
        multipleResultsMsg.setOutputMarkupId(true);
        multipleResultsMsg.setOutputMarkupPlaceholderTag(true);

        Form assetSearchForm = new Form("assetSearchForm");
        assetSearchTerm = new TextField("assetSearchTerm", new Model<String>(""));
        assetSearchTerm.add(new AttributeAppender("onkeyup", new Model("setAssetSearchButtonStatus(this);"), ";"));
        // onpaste and oncut events are called before the element is updated so a brief pause is added
        // to wait till after the update is done
        assetSearchTerm.add(new AttributeAppender("onpaste",
                new Model("var e = this; setTimeout(function(){setAssetSearchButtonStatus(e);}, 4);"), ";"));
        assetSearchTerm.add(new AttributeAppender("oncut",
                new Model("var e = this; setTimeout(function(){setAssetSearchButtonStatus(e);}, 4);"), ";"));
        assetSearchForm.add(assetSearchTerm);
        assetSearchButton = new AjaxButton("performAssetSearch", assetSearchForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                processAssetSearch(searchDataProvider, foundSearchResult, assetSearchTerm, foundSearchResult,
                        notFoundSearchResult, multipleResultsMsg, target);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };
        assetSearchForm.add(assetSearchButton);

        step2ToggledContainer.add(assetSearchForm);

        foundSearchResult.add(multipleResultsMsg);
        foundSearchResult.add(new Label("searchIdentifierLabel", getPrimaryOrg().getIdentifierLabel()));
        foundSearchResult.add(dataView);
        foundSearchResult.add(new StyledAjaxPagingNavigator("resultTableNavigator", dataView, foundSearchResult) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(foundSearchResult);
            }
        });

        step2ToggledContainer.add(foundSearchResult);
        foundSearchResult.setVisible(false);
        step2ToggledContainer.add(notFoundSearchResult);
        notFoundSearchResult.setVisible(false);

        step2ToggledContainer.add(new AjaxLink("backToStep1Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                closeContainer(target, step2Container, step2ToggledContainer);
                openContainer(target, step1Container, step1ToggledContainer);
            }
        });
    }

    private void createStep3Content() {

        step3Container = new WebMarkupContainer("step3Container");
        step3Container.setOutputMarkupId(true);
        stepsContainer.add(step3Container);
        step3ToggledContainer = createHideableWebMarkupContainer("step3ToggledContainer");
        step3ToggledContainer.setVisible(false);
        step3Container.add(step3ToggledContainer);

        step3ToggledContainer.add(new Label("identiferLabel", getPrimaryOrg().getIdentifierLabel()));

        step3ToggledContainer.add(new Label("confirm.losingAsset.type.name", losingAsset.getType().getName()));
        step3ToggledContainer.add(new Label("confirm.losingAsset.identifier", losingAsset.getIdentifier()));
        step3ToggledContainer.add(new Label("confirm.losingAsset.rfidNumber", losingAsset.getRfidNumber()));
        step3ToggledContainer.add(new Label("confirm.losingAsset.referenceNumber", losingAsset.getCustomerRefNumber()));
        step3ToggledContainer.add(new Label("confirm.losingAsset.owner.name", losingAsset.getOwner().getName()));
        step3ToggledContainer.add(new Label("confirm.losingAsset.assetStatus",
                losingAsset.getAssetStatus() != null ? losingAsset.getAssetStatus().getName() : ""));
        step3ToggledContainer.add(new Label("confirm.losingAsset.nextScheduledDate",
                formatDate(getNextScheduledEventDate(losingAsset.getId()), false)));

        step3ToggledContainer.add(new Label("confirm.winningAsset.type.name", winningAsset.bind("type.name")));
        step3ToggledContainer.add(new Label("confirm.winningAsset.identifier", winningAsset.bind("identifier")));
        step3ToggledContainer.add(new Label("confirm.winningAsset.rfidNumber", winningAsset.bind("rfidNumber")));
        step3ToggledContainer.add(new Label("confirm.winningAsset.referenceNumber", winningAsset.bind("customerRefNumber")));
        step3ToggledContainer.add(new Label("confirm.winningAsset.owner.name", winningAsset.bind("owner.name")));
        step3ToggledContainer.add(new Label("confirm.winningAsset.assetStatus", winningAsset.bind("assetStatus.name")));
        step3ToggledContainer.add(new Label("confirm.winningAsset.nextScheduledDate",
                winningAsset.bind("id")) {
            public IConverter getConverter(Class type) {
                return new IConverter() {
                    public String convertToString(Object value, Locale locale) {
                        return formatDate(getNextScheduledEventDate((Long) value), false);
                    }
                    public Object convertToObject(String value, Locale locale) {
                        // Only used by FormComponents
                        return null;
                    }
                };
            }
        });

        step3ToggledContainer.add(new AjaxLink("mergeAssets") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                processAssetMerge(losingAsset, winningAsset.getObject());
                stepsContainer.setVisible(false);
                target.add(stepsContainer);
                hints.setVisible(false);
                target.add(hints);
                completedMessageContainer.setVisible(true);
                target.add(completedMessageContainer);
            }
        });

        step3ToggledContainer.add(new AjaxLink("backToStep2Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                closeContainer(target, step3Container, step3ToggledContainer);
                openContainer(target, step2Container, step2ToggledContainer);
            }
        });
    }

    private void createConfirmationPageContent() {
        completedMessageContainer = createHideableWebMarkupContainer("completedMessage");
        completedMessageContainer.setVisible(false);
        add(completedMessageContainer);

        AjaxLink goToWinningAssetLink = new AjaxLink("goToWinningAssetButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(AssetSummaryPage.class,
                        PageParametersBuilder.param("uniqueID", winningAsset.getObject().getId()));
            }
        };
        completedMessageContainer.add(goToWinningAssetLink);
        goToWinningAssetLink.add(new Label("goToWinningAssetLabel", winningAsset.bind("identifier")));
    }

    private void processAssetMerge(Asset losingAsset, Asset winningAsset) {
        //test required entities
        if (losingAsset == null || losingAsset.isNew()) {
            logger.error("AssetMerge failure on missing losing asset");
            throw new MissingEntityException();
        }
        if (winningAsset == null) {
            logger.error("AssetMerge failure on missing winning asset");
            throw new MissingEntityException();
        }

        AssetMergeTask task = new AssetMergeTask(winningAsset, losingAsset,
                userService.getUser(FieldIDSession.get().getSessionUser().getId()));
        TaskExecutor.getInstance().execute(task);
        stepsContainer.setVisible(false);
    }

    private void processAssetSearch(AssetSearchForAssetMergeDataProvider searchDataProvider, Component parent,
                                    TextField assetSearchTerm, Component foundSearchResult,
                                    Component notFoundSearchResult, Component multipleResultsMsg, AjaxRequestTarget target) {

        boolean resultsFound = false;
        boolean multipleResultsFound = false;

        if (assetSearchTerm.getModelObject() != null) {
            String searchTerm = assetSearchTerm.getModelObject().toString();
            searchDataProvider.setParams(losingAsset.getId(), searchTerm, losingAsset.getType());
            int resultSize = searchDataProvider.size();
            if (resultSize > 0)
                resultsFound = true;
            if (resultSize > 1)
                multipleResultsFound = true;
        }

        if (resultsFound) {
            setComponentVisibility(foundSearchResult, true, target);
            setComponentVisibility(notFoundSearchResult, false, target);
            target.add(foundSearchResult); // Ensure data display gets refreshed
            if (multipleResultsFound)
                setComponentVisibility(multipleResultsMsg, true, target);
            else
                setComponentVisibility(multipleResultsMsg, false, target);
        }
        else {
            setComponentVisibility(foundSearchResult, false, target);
            setComponentVisibility(notFoundSearchResult, true, target);
        }
    }

    private void closeContainer(AjaxRequestTarget target, Component mainContainer, Component toggledContainer) {
        toggledContainer.setVisible(false);
        mainContainer.add(AttributeModifier.append("class", "stepClosed"));
        target.add(mainContainer);
        target.add(toggledContainer);
    }

    private void openContainer(AjaxRequestTarget target, Component mainContainer, Component toggledContainer) {
        toggledContainer.setVisible(true);
        removeCssAttribute(mainContainer, "stepClosed");
        target.add(mainContainer);
        target.add(toggledContainer);
    }

    private void setComponentVisibility(Component component, boolean visibilityState, AjaxRequestTarget target) {
        if (component.isVisible() != visibilityState) {
            component.setVisible(visibilityState);
            target.add(component);
        }
    }

    private void removeCssAttribute(Component component, String cssClass) {
        String currentAttributes = component.getMarkupAttributes().get("class").toString().trim();
        if (currentAttributes.isEmpty())
            return;
        String newAttributes;
        if (currentAttributes.equals(cssClass))
            /* Specified class is the only class currently specified */
            newAttributes = "";
        else
        if (currentAttributes.startsWith(cssClass + " "))
            /* Specified class is the first class of several */
            newAttributes = currentAttributes.replaceFirst(cssClass + " ", "");
        else
            /* Specified class is after the first class or does not appear at all */
            newAttributes = currentAttributes.replaceFirst(" " + cssClass, "");

        component.add(AttributeModifier.replace("class", newAttributes));
    }

    private String getIdentifierLabel(Asset asset) {
        AssetType assetType = asset.getType();
        if (assetType != null && assetType.isIdentifierOverridden())
            return assetType.getIdentifierLabel();
        else
            return getPrimaryOrg().getIdentifierLabel();
    }

    private WebMarkupContainer createHideableWebMarkupContainer(String id) {
        WebMarkupContainer container = new WebMarkupContainer(id);
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        return container;
    }

    private PrimaryOrg getPrimaryOrg() {
        return getSecurityGuard().getPrimaryOrg();
    }

    private Date getNextScheduledEventDate(Long id) {
        Event schedule = new NextEventScheduleLoader().setAssetId(id).load();
        return schedule==null ? null : schedule.getDueDate();
    }

    private String formatDate(Date date, boolean convertTimeZone) {
        return formatAnyDate(date, convertTimeZone, false);
    }

    private String formatAnyDate(Date date, boolean convertTimeZone, boolean showTime) {
        return new FieldIdDateFormatter(date, getSessionUser(), convertTimeZone, showTime).format();
    }

    public LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }
}

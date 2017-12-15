package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.exporting.AssetExporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.io.*;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.widgets.DownloadExportNotificationPage;
import com.n4systems.fieldid.wicket.pages.widgets.EntityImportInitiator;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultPage;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultStatus;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadCoordinator;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.AssetImportFailureNotification;
import com.n4systems.notifiers.notifications.AssetImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ArrayUtils;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.string.StringValue;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.web.helper.SessionUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Component to handle asset export/import with the following three functions
 * 1) generate a sample input Excel file for the selected asset type
 * 2) generate an input Excel file containing the data for all Assets of the selected asset type, filtered by owner
 * 3) start an import using a specified input file
 */
public class AssetImportPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AssetImportPanel.class);

    private static final String COLORBOX_CLASS = "colorboxLink";
    private static final String JQUERY_COLORBOX_CMD = "jQuery('."+COLORBOX_CLASS+"').colorbox({maxHeight: '600px', width: '600px', height:'360px', ajax: true, iframe: true});";

    private IModel<DownloadLink> downloadLinkModel;

    private LoaderFactory loaderFactory;
    private IModel<AssetType> selectedAssetType;
    private IModel<BaseOrg> selectedOrgModel;
    private IModel<User> currentUserModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<WebSessionMap> webSessionMapModel;
    private IModel<SecurityContext> nonProxySecurityContextModel;
    private WebMarkupContainer loadingWheel;

    public AssetImportPanel(String id, StringValue preSelectedAssetTypeId, IModel<User> currentUserModel,
                            IModel<SessionUser> sessionUserModel, IModel<SecurityFilter> securityFilterModel,
                            IModel<WebSessionMap> webSessionMapModel, IModel<SecurityContext> nonProxySecurityContextModel) {
        super(id);
        this.currentUserModel = currentUserModel;
        this.sessionUserModel = sessionUserModel;
        this.securityFilterModel = securityFilterModel;
        this.webSessionMapModel = webSessionMapModel;
        this.nonProxySecurityContextModel = nonProxySecurityContextModel;
        if (preSelectedAssetTypeId != null && !preSelectedAssetTypeId.isEmpty()) {
            selectedAssetType = Model.of(getAssetType(new Long(preSelectedAssetTypeId.toString())));
        }
        else {
            selectedAssetType = Model.of((AssetType)null);
        }
        selectedOrgModel = Model.of((BaseOrg)null);
        downloadLinkModel = new Model<DownloadLink>().of((DownloadLink) null);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSS(".disabled {opacity: 0.5; pointer-events: none}", null);
        response.renderCSS(".downloadContainer {margin-top: 5px; margin-bottom: 15px; clear:both;}", null);
        response.renderCSS(".busyIndicator {display: block; position: fixed; left: 50%; top: 50%; opacity : 0.8;}", null);
        response.renderCSS(".hideElement {display: none;}", null);
    }

    private void addComponents() {

        // Section 1
        final DropDownChoice<AssetType> assetTypeSelection = new DropDownChoice<AssetType>("assetTypesSelectionList",
                selectedAssetType,
                new LoadableDetachableModel<List<AssetType>>() {
                    @Override
                    protected List<AssetType> load() {
                        List<AssetType> types = getLoaderFactory().createAssetTypeListLoader().load();
                        if (selectedAssetType.getObject() == null) {
                            // If no selection default to first item in this list
                            selectedAssetType.setObject(types.get(0));
                        }
                        return types;
                    }
                },
                new IChoiceRenderer<AssetType>() {
                    @Override
                    public Object getDisplayValue(AssetType object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(AssetType object, int index) {
                        return object.getId().toString();
                    }
                }
        );
        assetTypeSelection.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
              /* This behavior added to make wicket update the selection list model through ajax as opposed to the
                 regular http request/response cycle which will lose any selection in the upload file field. */
                target.addChildren(getPage(), FeedbackPanel.class);
            }
        });
        assetTypeSelection.setOutputMarkupId(true);
        add(assetTypeSelection);

        // Section 2

        Model<Integer> choiceSelectionModel = new Model<Integer>(2);
        RadioGroup downloadChoiceSelectionGroup = new RadioGroup("downloadChoiceContainer", choiceSelectionModel);

        final Component downloadDataContainer = createDownloadDataDetails("downloadDataContainer");
        final Component downloadTemplateContainer = createDownloadTemplateContainer("downloadTemplateContainer");

        Radio dataRadioChoice = new Radio("downloadDataChoice", new Model<Integer>(1));
        dataRadioChoice.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.addChildren(getPage(), FeedbackPanel.class);
                downloadTemplateContainer.add(AttributeModifier.append("class","disabled"));
                removeCssClass(downloadDataContainer, "disabled");
                target.add(downloadTemplateContainer);
                target.add(downloadDataContainer);
                target.appendJavaScript(JQUERY_COLORBOX_CMD); // Enable JQuery colorbox on link
            }
        });
        downloadChoiceSelectionGroup.add(dataRadioChoice);
        downloadChoiceSelectionGroup.add(downloadDataContainer);

        Radio templateRadioChoice = new Radio("downloadTemplateChoice", new Model<Integer>(2));
        templateRadioChoice.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.addChildren(getPage(), FeedbackPanel.class);
                removeCssClass(downloadTemplateContainer, "disabled");
                downloadDataContainer.add(AttributeModifier.append("class", "disabled"));
                target.add(downloadTemplateContainer);
                target.add(downloadDataContainer);
            }
        });
        downloadChoiceSelectionGroup.add(templateRadioChoice);
        downloadChoiceSelectionGroup.add(downloadTemplateContainer);

        add(downloadChoiceSelectionGroup);

        // Hide based on initial selection
        downloadDataContainer.add(AttributeModifier.append("class", "disabled"));

        // Section 3
        final FileUploadField fileUploadField = new FileUploadField("fileToUpload");

        Form fileUploadForm = new Form("fileUploadForm");

        AjaxButton fileUploadButton =new AjaxButton("fileUploadSubmit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                ImportResultStatus result;
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    try {
                        logger.info("Beginning import of file " + fileUpload.getClientFileName());
                        InputStream inputStream = fileUpload.getInputStream();
                        EntityImportInitiator importService = new EntityImportInitiator(getWebSessionMap(), getCurrentUser(), getSessionUser(), getSecurityFilter()) {
                            @Override
                            protected ImportSuccessNotification createSuccessNotification() {
                                return new AssetImportSuccessNotification(getCurrentUser(), getSelectedAssetType());
                            }
                            @Override
                            protected ImportFailureNotification createFailureNotification() {
                                return new AssetImportFailureNotification(getCurrentUser(), getSelectedAssetType());
                            }
                            @Override
                            protected Importer createImporter(MapReader reader) {
                                return getImporterFactory().createAssetImporter(reader, getCurrentUser(), getSelectedAssetType(), nonProxySecurityContextModel.getObject());
                            }
                        };
                        result = importService.doImport(inputStream);
                    } catch (IOException ex) {
                        logger.error("Exception reading input file", ex);
                        result = new ImportResultStatus(false, null,
                                new FIDLabelModel("error.io_error_reading_import_file").getObject(), null);
                    }
                    finally {
                        logger.info("Import validation phase completed");
                    }
                }
                else {
                    logger.error("Input file required");
                    result = new ImportResultStatus(false, null,
                            new FIDLabelModel("error.file_required").getObject(), null);
                }
                String selectedAssetTypeId = selectedAssetType.getObject().getId().toString();
                ImportResultPage resultPage = new ImportResultPage(result) {

                    @Override
                    protected PageParameters getRerunParameters() {
                        return PageParametersBuilder.param(
                                AssetImportPage.ASSET_TYPE_ID_KEY, selectedAssetTypeId);
                    }
                    @Override
                    protected Class<? extends IRequestablePage> getRerunPageClass() {
                        return AssetImportPage.class;
                    }
                };
                setResponsePage(resultPage);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxCallDecorator() {
                    @Override
                    public CharSequence decorateScript(Component component, CharSequence script) {
                        /* When this button is clicked show the busy indicator */
                        return "document.getElementById('"+ loadingWheel.getMarkupId()+"').className='busyIndicator';"+script;
                    }
                };
            }
        };

        fileUploadForm.add(fileUploadButton);
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);
        add(fileUploadForm);

        loadingWheel = new WebMarkupContainer("loadingWheel");
        loadingWheel.setOutputMarkupId(true);
        loadingWheel.setOutputMarkupPlaceholderTag(true);
        add(loadingWheel);
    }

    private Component createDownloadDataDetails(String id) {

        WebMarkupContainer container = new WebMarkupContainer(id);
        container.setOutputMarkupId(true);

        final Link downloadDataLink = new Link("downloadDataLink") {

            @Override
            public void onClick() {
                submitAssetExport();
            }
        };

        downloadDataLink.setOutputMarkupId(true);
        downloadDataLink.add(new AttributeModifier("class", "disabled") {
            @Override
            public boolean isEnabled(Component component) {
                return selectedOrgModel.getObject() == null;
            }
        });
        downloadDataLink.add(new AttributeAppender("class", new Model<>(COLORBOX_CLASS), " "));

        OrgLocationPicker orgPicker = new OrgLocationPicker("owner", selectedOrgModel) {
            @Override
            protected boolean showClearIcon() {
                return true;
            }
            @Override
            protected void onChanged(AjaxRequestTarget target) {
                /* Workaround - OrgLocationPicker does not set model to null if clear icon is clicked so check to
                * see if input field is empty and clear out model based on that */
                String text = getTextString();
                if (text == null || text.isEmpty())
                    AssetImportPanel.this.selectedOrgModel.setObject(null);
                else
                    target.appendJavaScript(JQUERY_COLORBOX_CMD);
                target.add(downloadDataLink);
            }
        }.withAutoUpdate();

        container.add(orgPicker);
        container.add(downloadDataLink);
        return container;
    }

    private Component createDownloadTemplateContainer(String id) {

        WebMarkupContainer container = new WebMarkupContainer(id);
        container.setOutputMarkupId(true);

        Link downloadTemplateLink = new Link("downloadTemplateLink") {

            @Override
            public void onClick() {
                AssetType assetType = getSelectedAssetType();
                AbstractResourceStreamWriter rStream = doDownloadExample(assetType);
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(assetType));
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };
        container.add(downloadTemplateLink);
        return container;
    }

    private void submitAssetExport() {

        try {
            String downloadFileName = new FIDLabelModel("label.export_file.asset", selectedAssetType.getObject().getName()).getObject();
            DownloadLink downloadLink = getDownloadCoordinator().generateAssetExport(downloadFileName, getDownloadLinkUrl(),
                    createAssetListLoader(selectedAssetType.getObject(), selectedOrgModel.getObject()),
                    getAssetTypeWithPrefetches(selectedAssetType.getObject().getId()));
            downloadLinkModel.setObject(downloadLink);
            DownloadExportNotificationPage downloadWindow = new DownloadExportNotificationPage(
                    downloadLinkModel);
            setResponsePage(downloadWindow);
        } catch (RuntimeException e) {
            logger.error("Unable to execute Asset data export", e);
            throw new RestartResponseException(OopsPage.class,
                    PageParametersBuilder.param(OopsPage.PARAM_ERROR_TYPE_KEY, new FIDLabelModel("error.export_failed.asset").getObject()));
        }
    }

    private DownloadCoordinator getDownloadCoordinator() {
        DownloadCoordinator downloadCoordinator = new DownloadCoordinator(getCurrentUser(), new SaverFactory().createDownloadLinkSaver());
        return downloadCoordinator;
    }
    private ListLoader<Asset> createAssetListLoader(AssetType assetType, BaseOrg baseOrg) {
        return getLoaderFactory().createAssetListLoader(assetType, baseOrg);
    }

    private String getDownloadLinkUrl() {
        return new ActionURLBuilder(getBaseURI(), getConfigContext()).setAction("showDownloads").build() + "?fileId=";
    }

    private URI getBaseURI() {

        // creates a URI based on the current url, and resolved against the context path which should be /fieldid.  We add on the extra / since we currently need it.
        String fullPath = getRequestCycle().getUrlRenderer().renderFullUrl(Url.parse(getRequest().getContextPath()));
         return URI.create(fullPath + "/");
    }

    private ConfigurationProvider getConfigContext() {
        return ConfigService.getInstance();
    }

    private AssetType getSelectedAssetType() {
        Long id = selectedAssetType.getObject().getId();
        // Get fully populated AssetType object
        return getAssetTypeWithPrefetches(id);
    }

    private AssetType getAssetTypeWithPrefetches(Long id) {
        return getLoaderFactory().createAssetTypeLoader().setStandardPostFetches().setId(id).load();
    }

    private AssetType getAssetType(Long id) {
        return getLoaderFactory().createAssetTypeLoader().setId(id).load();
    }

    private AbstractResourceStreamWriter doDownloadExample(final AssetType assetType) {
        AssetExporter exporter = new AssetExporter(getLoaderFactory().createPassthruListLoader(Arrays.asList(createExampleAsset(assetType))));

        AbstractResourceStreamWriter rStream = new AbstractResourceStreamWriter() {
            @Override
            public void write(Response output) {
                MapWriter writer = null;
                try {
                    writer = new ExcelXSSFMapWriter(new DateTimeDefiner(getCurrentUser()));
                    exporter.export(writer);
                    ((ExcelXSSFMapWriter)writer).writeToStream(getResponse().getOutputStream());

                } catch (Exception e) {
                    logger.error("Failed generating example asset export", e);
                    throw new RuntimeException(e);
                } finally {
                    StreamUtils.close(writer);
                }
            }
            @Override
            public String getContentType() {
                return ContentType.EXCEL.getMimeType();
            }
        };

        return rStream;
    }

    private Asset createExampleAsset(AssetType assetType) {
        Asset example = new Asset();
        example.setType(assetType);

        example.setIdentifier(new FIDLabelModel("example.asset.identifier").getObject());
        example.setRfidNumber(new FIDLabelModel("example.asset.rfidNumber").getObject());
        example.setCustomerRefNumber(new FIDLabelModel("example.asset.customerRefNumber").getObject());
        example.setOwner(getCurrentUser().getOwner());

        if (getSessionUserOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.AdvancedLocation)) {
            PredefinedLocation location1 = new PredefinedLocation();
            location1.setName("Location 1");
            PredefinedLocation location2 = new PredefinedLocation();
            location2.setName("Location 2");
            location2.setParent(location1);
            PredefinedLocation location3 = new PredefinedLocation();
            location3.setName("Location 3");
            location3.setParent(location2);
            example.setAdvancedLocation(new Location(location3, new FIDLabelModel("label.freeform_location").getObject()));
        } else {
            example.setAdvancedLocation(Location.onlyFreeformLocation(new FIDLabelModel("example.asset.location").getObject()));
        }

        example.setPurchaseOrder(new FIDLabelModel("example.asset.purchaseOrder").getObject());
        example.setComments(new FIDLabelModel("example.asset.comments").getObject());
        example.setIdentified(new Date());

        List<AssetStatus> statuses = getLoaderFactory().createAssetStatusListLoader().load();

        AssetStatus exampleStatus =  (statuses.isEmpty()) ? null : statuses.get(0);
        if (exampleStatus != null) {
            example.setAssetStatus(exampleStatus);
        }

        InfoOptionBean exampleOption;
        for (InfoFieldBean field: assetType.getInfoFields()) {
            exampleOption = new InfoOptionBean();
            exampleOption.setInfoField(field);
            exampleOption.setStaticData(false);
            exampleOption.setName("");
            example.getInfoOptions().add(exampleOption);
        }

        return example;
    }

    private String getExportFileName(AssetType assetType) {
        return ContentType.EXCEL.prepareFileName(new FIDLabelModel("label.export_file.asset",
                ArrayUtils.newArray(assetType.getName())).getObject());
    }

    private void removeCssClass(Component component, String cssClass) {
        Object markupClasses = component.getMarkupAttributes().get("class");
        if (markupClasses == null)
            return; // No classes
        String currentClasses = markupClasses.toString().trim();
        if (currentClasses.isEmpty())
            return;
        String newClasses;
        if (currentClasses.equals(cssClass))
            /* Specified class is the only class currently specified */
            newClasses = "";
        else
        if (currentClasses.startsWith(cssClass + " "))
            /* Specified class is the first class of several */
            newClasses = currentClasses.replaceFirst(cssClass + " ", "");
        else
            /* Specified class is after the first class or does not appear at all */
            newClasses = currentClasses.replaceFirst(" " + cssClass, "");

        component.add(AttributeModifier.replace("class", newClasses));
    }

    private BaseOrg getSessionUserOwner() {
        return getSessionUser().getOwner();
    }

    public LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }

    private WebSessionMap getWebSessionMap() {
        return webSessionMapModel.getObject();
    }

    private User getCurrentUser() {
        return currentUserModel.getObject();
    }
    private SessionUser getSessionUser() {
        return sessionUserModel.getObject();
    }
    private SecurityFilter getSecurityFilter() {
         return securityFilterModel.getObject();
    }
}

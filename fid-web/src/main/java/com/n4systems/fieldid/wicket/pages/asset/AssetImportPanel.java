package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.exporting.AssetExporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.io.*;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.widgets.EntityImportInitiator;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultPage;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultStatus;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.downloadlink.ContentType;
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
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Component to handle asset import, including starting the import as well as generating a sample input Excel file
 * for the selected asset type.
 */
public class AssetImportPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AssetImportPanel.class);

    private LoaderFactory loaderFactory;
    private IModel<AssetType> selectedAssetType;
    private IModel<User> currentUserModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<WebSessionMap> webSessionMapModel;

    public AssetImportPanel(String id, StringValue preSelectedAssetTypeId, IModel<User> currentUserModel,
                            IModel<SessionUser> sessionUserModel, IModel<SecurityFilter> securityFilterModel,
                            IModel<WebSessionMap> webSessionMapModel) {
        super(id);
        if (preSelectedAssetTypeId != null && !preSelectedAssetTypeId.isEmpty()) {
            selectedAssetType = Model.of(getAssetType(new Long(preSelectedAssetTypeId.toString())));
        }
        else {
            selectedAssetType = Model.of((AssetType)null);
        }
        this.currentUserModel = currentUserModel;
        this.sessionUserModel = sessionUserModel;
        this.securityFilterModel = securityFilterModel;
        this.webSessionMapModel = webSessionMapModel;
        addComponents();
    }

    private void addComponents() {

        final DropDownChoice<AssetType> assetTypeSelection = new DropDownChoice<AssetType>("assetTypesSelectionList",
                selectedAssetType,
                new LoadableDetachableModel<List<AssetType>>() {
                    @Override
                    protected List<AssetType> load() {
                        return getLoaderFactory().createAssetTypeListLoader().load();
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
        ) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

        };
        add(assetTypeSelection);

        Link downloadTemplateLink = new Link("downloadTemplateLink") {

            @Override
            public void onClick() {
                AssetType assetType = getSelectedAssetType();
                AbstractResourceStreamWriter rStream = doDownloadExample(assetType);
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(assetType));
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };
        add(downloadTemplateLink);

        final FileUploadField fileUploadField = new FileUploadField("fileToUpload");
        Form fileUploadForm = new Form("fileUploadForm") {
            @Override
            protected void onSubmit() {
                ImportResultStatus result;
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    try {
                        InputStream inputStream = fileUploadField.getFileUpload().getInputStream();
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
                                return getImporterFactory().createAssetImporter(reader, getCurrentUser(), getSelectedAssetType());
                            }
                        };
                        result = importService.doImport(inputStream);
                    } catch (IOException ex) {
                        logger.error("Exception reading input file", ex);
                        result = new ImportResultStatus(false, null,
                                new FIDLabelModel("error.io_error_reading_import_file").getObject(), null);
                    }
                }
                else {
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
        };
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);
        add(fileUploadForm);
    }

    private AssetType getSelectedAssetType() {
        Long id = selectedAssetType.getObject().getId();
        // Get fully populated AssetType object
        return getAssetType(id);
    }

    private AssetType getAssetType(Long id) {
        return getLoaderFactory().createAssetTypeLoader().setStandardPostFetches().setId(id).load();
    }

    private AbstractResourceStreamWriter doDownloadExample(AssetType assetType) {
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
                    //TODO handle this error?
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

    private BaseOrg getSessionUserOwner() {
        return getSessionUser().getOwner();
    }

    public LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }

    public WebSessionMap getWebSessionMap() {
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

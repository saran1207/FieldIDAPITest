package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.exporting.*;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.BaseImportExportPanel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.notifications.AutoAttributeImportFailureNotification;
import com.n4systems.notifiers.notifications.AutoAttributeImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.loaders.ListLoader;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.web.helper.SessionUser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Panel to handle export/import of auto attribute data.
 */
public class AutoAttributeImportPanel extends BaseImportExportPanel {

    private static final Logger logger = Logger.getLogger(AutoAttributeImportPanel.class);

    private IModel<AutoAttributeCriteria> selectedAutoAttributeCriteria;

    public AutoAttributeImportPanel(String id, IModel<User> currentUserModel,
                                    IModel<SessionUser> sessionUserModel,
                                    IModel<SecurityFilter> securityFilterModel,
                                    IModel<WebSessionMap> webSessionMapModel) {
        super(id, currentUserModel, sessionUserModel, securityFilterModel, webSessionMapModel);
        selectedAutoAttributeCriteria = Model.of((AutoAttributeCriteria) null);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/import.css");
    }

    private void addComponents() {

        final DropDownChoice<AutoAttributeCriteria> autoAttributeSelection = new DropDownChoice<AutoAttributeCriteria>("autoAttributeSelectionList",
                selectedAutoAttributeCriteria,
                new LoadableDetachableModel<List<AutoAttributeCriteria>>() {
                    @Override
                    protected List<AutoAttributeCriteria> load() {
                        List<AutoAttributeCriteria> autoAttributeCriteria = new ArrayList<>();
                        List<AssetType> allAssetTypes = getLoaderFactory().createAssetTypeListLoader().setPostFetchFields("autoAttributeCriteria").load();
                        autoAttributeCriteria.addAll(allAssetTypes.stream().filter(
                                assetType -> assetType.getAutoAttributeCriteria() != null).map(a -> a.getAutoAttributeCriteria()).collect(Collectors.toList()));
                        if (selectedAutoAttributeCriteria.getObject() == null && !autoAttributeCriteria.isEmpty()) {
                            // If no selection default to first item in this list
                            setSelection(autoAttributeCriteria.get(0).getId());
                        }
                        return autoAttributeCriteria;
                    }
                },
                new IChoiceRenderer<AutoAttributeCriteria>() {
                    @Override
                    public Object getDisplayValue(AutoAttributeCriteria object) {
                        return object.getAssetType().getName();
                    }

                    @Override
                    public String getIdValue(AutoAttributeCriteria object, int index) {
                        return object.getId().toString();
                    }
                }
        ) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(AutoAttributeCriteria newSelection) {
                super.onSelectionChanged(newSelection);
                setSelection(newSelection.getId());
            }
        };
        add(autoAttributeSelection);

        Link downloadDataLink = new Link("downloadDataLink") {

            @Override
            public void onClick() {
                performDataExport();
            }

            @Override
            public boolean isEnabled() {
                return selectedAutoAttributeCriteria.getObject() != null;
            }
        };
        setLinkToColorbox(downloadDataLink);
        add(downloadDataLink);

        Link downloadTemplateLink = new Link("downloadTemplateLink") {

            @Override
            public void onClick() {
                AbstractResourceStreamWriter rStream = performTemplateExport();
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream,
                        ContentType.EXCEL.prepareFileName(getTemplateExportFileName()));
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }

            @Override
            public boolean isEnabled() {
                return selectedAutoAttributeCriteria.getObject() != null;
            }
        };
        add(downloadTemplateLink);

        // Add file upload section
        final FileUploadField fileUploadField = new FileUploadField("fileToUpload");

        Form fileUploadForm = new Form("fileUploadForm");

        AjaxButton fileUploadButton = addAjaxUploadButton("fileUploadSubmit", fileUploadField);

        fileUploadForm.add(fileUploadButton);
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);
        add(fileUploadForm);

        addBusyIndicator(this);
    }

    protected void setSelection(Long id) {
        selectedAutoAttributeCriteria.setObject(
                getLoaderFactory().createFilteredIdLoader(AutoAttributeCriteria.class).
                        setId(id).setPostFetchFields(
                        "inputs", "outputs", "assetType.name", "definitions.outputs").load());
    }

    @Override
    protected ImportSuccessNotification getImportSuccessNotification() {
        return new AutoAttributeImportSuccessNotification(getCurrentUser());
    }

    @Override
    protected ImportFailureNotification getImportFailureNotification() {
        return new AutoAttributeImportFailureNotification(getCurrentUser());
    }

    @Override
    protected Importer getImporter(MapReader reader, ImporterFactory importerFactory) {
        return importerFactory.createAutoAttributeImporter(reader, selectedAutoAttributeCriteria.getObject());
    }

    @Override
    protected Class<? extends IRequestablePage> getImportRerunPageClass() {
        return AutoAttributeActionsPage.class;
    }

    @Override
    protected PageParameters getParametersForImportRerun() {
        return new PageParameters();
    }

    @Override
    protected Exporter getTemplateExporter() {

        Exporter exporter = new AutoAttributeExporter(getLoaderFactory().createPassthruListLoader(Arrays.asList(createExampleDefinition())));
        return exporter;
    }

    @Override
    protected String getTemplateExportFailureMessage() {
        return "Failed generating example Auto Attribute export";
    }

    @Override
    protected DownloadLink getDataDownloadLink() {
        String downloadFileName = getTemplateExportFileName();
        ListLoader<AutoAttributeDefinition> attribLoader = getLoaderFactory().createPassthruListLoader(selectedAutoAttributeCriteria.getObject().getDefinitions());
        DownloadLink downloadLink = getDownloadCoordinator().generateAutoAttributeExport(downloadFileName,
                getDownloadLinkUrl(), attribLoader);
        return downloadLink;
    }

    @Override
    protected String getDataDownloadLoggerErrorText() {
        return "Unable to execute Auto Attribute Data export";
    }

    @Override
    protected String getDataDownloadOopsErrorText() {
        return new FIDLabelModel("error.export_failed.autoattrib").getObject();
    }

    @Override
    protected boolean isUploadButtonEnabled() {
        return selectedAutoAttributeCriteria.getObject() != null;
    }

    public AutoAttributeDefinition createExampleDefinition() {

        AutoAttributeDefinition def = new AutoAttributeDefinition();
        def.setCriteria(selectedAutoAttributeCriteria.getObject());

        InfoOptionBean option;
        for (InfoFieldBean field: selectedAutoAttributeCriteria.getObject().getInputs()) {
            option = new InfoOptionBean();
            option.setInfoField(field);
            option.setName("");
            option.setStaticData(false);
            def.getInputs().add(option);
        }

        for (InfoFieldBean input: selectedAutoAttributeCriteria.getObject().getOutputs()) {
            option = new InfoOptionBean();
            option.setInfoField(input);
            option.setName("");
            option.setStaticData(false);
            def.getOutputs().add(option);
        }

        return def;
    }

    private String getTemplateExportFileName() {
        String exportName = selectedAutoAttributeCriteria.getObject().getAssetType().getName();
        String pattern = getString("label.export_file.autoattrib");
        String fileName = MessageFormat.format(pattern, exportName);
        return fileName;
    }

}

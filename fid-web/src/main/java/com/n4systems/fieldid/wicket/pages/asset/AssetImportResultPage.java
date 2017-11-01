package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.ImportTaskRegistry;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.taskscheduling.task.ImportTask;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.ui.progressbar.ProgressBar;

import java.util.List;

public class AssetImportResultPage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(AssetImportResultPage.class);

    public AssetImportResultPage(Long assetTypeId, ImportResultStatus importResultsStatus) {
        super(null);
        System.out.println("AssetImportResultPage - " + assetTypeId + ", " + importResultsStatus);
        addComponents(assetTypeId, importResultsStatus);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/import.css");
        response.renderCSS(".ui-progressbar-value {background: #79c5e5;}", "legacyStyle");
    }

    private void addComponents(final Long assetTypeId, final ImportResultStatus importResultsStatus) {

        /* Existing top feedback panel is in the correct place for our error message but doesn't
           seem to get recognized as a feedback panel for our error message. */
        remove(getTopFeedbackPanel());
        FeedbackPanel feedbackPanel = new FeedbackPanel("topFeedbackPanel");
        feedbackPanel.add(new AttributeAppender("style", new Model("text-align: center; color:red; padding: 0px 10px"), " "));
        add(feedbackPanel);

        if (!importResultsStatus.isSuccess()) {
            System.out.println("error message to write");
            error(importResultsStatus.getErrorMessage());
        }

        WebMarkupContainer validationErrorsSection = new WebMarkupContainer("validationErrorsSection");
        if (importResultsStatus.getValidationResults() != null && importResultsStatus.getValidationResults().size() > 0) {
            ListView<ValidationResult> validationErrorsListView =
                    new ListView<ValidationResult>("validationErrors", importResultsStatus.getValidationResults()) {
              protected void populateItem(ListItem<ValidationResult> item) {
                  item.add(new Label("validationErrorRow", new Integer(item.getModelObject().getRow()).toString()));
                  item.add(new Label("validationErrorMsg", item.getModelObject().getMessage()));
              }
            };
            validationErrorsSection.add(validationErrorsListView);
        }
        else {
            validationErrorsSection.setVisible(false);
        }
        AjaxLink reloadImportFileLink = new AjaxLink("reuploadImportFile") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(AssetImportPage.class,
                        PageParametersBuilder.param(AssetImportPage.ASSET_TYPE_ID_KEY, assetTypeId.toString()));
            }
        };
        validationErrorsSection.add(reloadImportFileLink);
        add(validationErrorsSection);

        WebMarkupContainer sucessProgressSection = new WebMarkupContainer("importProgressSection");
        WebMarkupContainer progressBarContainer = new WebMarkupContainer("progressBarContainer");
        sucessProgressSection.add(progressBarContainer);

        if (importResultsStatus.isSuccess()) {
            ImportTaskRegistry taskRegistry = new ImportTaskRegistry();
            ImportTask importTask = taskRegistry.get(importResultsStatus.getTaskId());

            IModel<String> taskStatusModel = new IModel<String>() {
                    public String getObject() {
                        System.out.println("taskStatusModel.getObject");
                        ImportTask importTask = new ImportTaskRegistry().get(importResultsStatus.getTaskId());
                        return new FIDLabelModel(importTask.getStatus().getLabel()).getObject();
                    }
                    public void setObject(final String object) { }
                    public void detach() {}
            };
            final Label taskStatusLabel = new Label("taskStatusLabel", taskStatusModel);
            taskStatusLabel.setOutputMarkupId(true);
            progressBarContainer.add(taskStatusLabel);

            final ProgressBar importProgressBar = new ProgressBar("importProgressBar");
            importProgressBar.setValue(getCurrentPercentDone(importTask));
            importProgressBar.setOutputMarkupId(true);
            progressBarContainer.add(importProgressBar);

            IModel<String> taskCountModel = new IModel<String>() {
                @Override
                public void detach() {}
                @Override
                public String getObject() {
                    ImportTask importTask = new ImportTaskRegistry().get(importResultsStatus.getTaskId());
                    return new FIDLabelModel("message.import_task_count",
                            new Integer(importTask.getCurrentRow()), new Integer(importTask.getTotalRows())).getObject();
                }
                @Override
                public void setObject(String object) {}
            };

            final Label taskCountLabel = new Label("importTaskCount", taskCountModel);
            taskCountLabel.setOutputMarkupId(true);
            progressBarContainer.add(taskCountLabel);

            final AbstractAjaxTimerBehavior timer = new AbstractAjaxTimerBehavior(Duration.ONE_SECOND) {

                @Override
                protected void onTimer(AjaxRequestTarget target)
                {
                    System.out.println("AbstractAjaxTimerBehavior tick");
                    ImportTaskRegistry taskRegistry = new ImportTaskRegistry();
                    // ImportTask is not seriaiizable
                    ImportTask importTask = taskRegistry.get(importResultsStatus.getTaskId());
                    int newValue = getCurrentPercentDone(importTask);
                    System.out.println("new value is " + newValue);
                    importProgressBar.setValue(newValue);
                    target.add(importProgressBar);
                    target.add(taskStatusLabel);
                    target.add(taskCountLabel);
                    if (newValue > 99)
                        stop();
                }
            };
            importProgressBar.add(timer);
        }
        else {
            /* This section will not shown */
            progressBarContainer.add(new Label("taskStatusLabel", Model.of("")));
            progressBarContainer.add(new Label("importProgressBar", Model.of("")));
            progressBarContainer.add(new Label("importTaskCount", Model.of("")));
        }
        sucessProgressSection.setVisible(importResultsStatus.isSuccess());
        add(sucessProgressSection);
    }

    private int getCurrentPercentDone(ImportTask importTask) {
        return importTask.getCurrentRow() * 100 / importTask.getTotalRows();
    }
}

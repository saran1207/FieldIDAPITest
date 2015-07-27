package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.setup.loto.LotoWarningTemplateModalInputPanel;
import com.n4systems.fieldid.wicket.components.setup.loto.LotoWarningsActionCell;
import com.n4systems.fieldid.wicket.components.setup.loto.LotoWarningsListPanel;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.procedure.LotoSettings;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Iterator;
import java.util.List;

public class LotoDetailsSetupPage extends FieldIDTemplatePage {

    private static final int TEXTAREA_MAXLENGTH = 2500;

    @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    WarningTemplateService warningTemplateService;

    private IModel<LotoSettings> customLotoDetailsModel;
    private FIDFeedbackPanel feedbackPanel;
    private LotoWarningTemplateModalInputPanel inputPanel;
    private LotoWarningsListPanel listPanel;

    private ModalWindow addTemplateModal;

    public LotoDetailsSetupPage() {}

    @Override
    public void onInitialize() {
        super.onInitialize();
        customLotoDetailsModel = Model.of(getLotoSettings());

        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                LotoSettings customLotoDetails = customLotoDetailsModel.getObject();

                if(customLotoDetails.getApplicationProcess() == null &&
                        customLotoDetails.getRemovalProcess() == null &&
                        customLotoDetails.getTestingAndVerification() == null) {

                    //If the ID is null, it doesn't exist... so we can't delete it.
                    if(customLotoDetails.getId() != null) {
                        procedureDefinitionService.deleteLotoSettings(customLotoDetails);
                    }
                } else {
                    procedureDefinitionService.saveOrUpdateLotoSettings(customLotoDetails);
                }
                setResponsePage(LotoSetupPage.class);
            }
        };

        add(addTemplateModal = new DialogModalWindow("addTemplateModal").setInitialHeight(500).setInitialWidth(400));


        form.add(new TextArea<String>("applicationProcess", new PropertyModel<>(customLotoDetailsModel, "applicationProcess"))
                .add(new StringValidator.MaximumLengthValidator(TEXTAREA_MAXLENGTH))
                .setLabel(new FIDLabelModel("label.lockout_application_process")));
        form.add(new TextArea<String>("removalProcess", new PropertyModel<>(customLotoDetailsModel, "removalProcess"))
                .add(new StringValidator.MaximumLengthValidator(TEXTAREA_MAXLENGTH))
                .setLabel(new FIDLabelModel("label.lockout_removal_process")));
        form.add(new TextArea<String>("testingAndVerification", new PropertyModel<>(customLotoDetailsModel, "testingAndVerification"))
                .add(new StringValidator.MaximumLengthValidator(TEXTAREA_MAXLENGTH))
                .setLabel(new FIDLabelModel("label.testing_and_verification_detail_panel")));


        form.add(new SubmitLink("saveLink"));
        form.add(new BookmarkablePageLink<LotoSetupPage>("cancelLink", LotoSetupPage.class));
        add(form);

        //We want a FeedbackPanel...
        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        //Warning Template Stuff.
        add(listPanel = new LotoWarningsListPanel("warningTemplateList",
                                                  getDataProvider()) {

            @Override
            protected FIDFeedbackPanel getFeedbackPanel() {
                return feedbackPanel;
            }

            @Override
            protected void addActionColumn(List<IColumn<WarningTemplate>> columnList) {
                columnList.add(new AbstractColumn<WarningTemplate>(new FIDLabelModel("")) {
                    @Override
                    public void populateItem(Item<ICellPopulator<WarningTemplate>> item,
                                             String id,
                                             IModel<WarningTemplate> model) {
                        item.add(new LotoWarningsActionCell(id, model) {
                            @Override
                            protected void doEdit(AjaxRequestTarget target) {
                                addTemplateModal.setContent(inputPanel = new LotoWarningTemplateModalInputPanel(addTemplateModal.getContentId(), Model.of(thisTemplate)) {
                                    @Override
                                    protected void doSave(AjaxRequestTarget target) {
                                        warningTemplateModel.setObject(saveAction(target, warningTemplateModel.getObject()));
                                    }

                                    @Override
                                    protected void doCancel(AjaxRequestTarget target) {
                                        cancelAction(target);
                                    }
                                });
                                target.add(inputPanel);
                                addTemplateModal.show(target);
                            }

                            @Override
                            protected void doDelete(AjaxRequestTarget target) {
                                deleteAction(target, thisTemplate);
                            }
                        }.setOutputMarkupId(true));

                        item.setOutputMarkupId(true);
                    }
                });
//                columnList.add(new LotoWarningsActionColumn(this));
            }

            @Override
            protected void doAddAction(AjaxRequestTarget target) {
                WarningTemplate createMe = new WarningTemplate();
                createMe.setTenant(getTenant());
                addTemplateModal.setContent(inputPanel = new LotoWarningTemplateModalInputPanel(addTemplateModal.getContentId(), Model.of(createMe)) {
                    @Override
                    protected void doCancel(AjaxRequestTarget target) {
                        target.add(addTemplateModal);
                        cancelAction(target);
                    }

                    @Override
                    protected void doSave(AjaxRequestTarget target) {
                        warningTemplateModel.setObject(saveAction(target, warningTemplateModel.getObject()));
                    }
                });
                target.add(inputPanel);
                target.add(addTemplateModal);
                addTemplateModal.show(target);
            }
        });

        listPanel.setOutputMarkupId(true);
    }

    private LotoSettings getLotoSettings() {
        LotoSettings customLotoDetails =  procedureDefinitionService.getLotoSettings();
        return customLotoDetails != null ? customLotoDetails : new LotoSettings(getTenant());
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.loto_detail_fields"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new BookmarkablePageLink<LotoSetupPage>(linkId, LotoSetupPage.class)
                .add(new Label(linkLabelId, new FIDLabelModel("label.back_to_setup")));
    }

    /**
     * Since the DataProvider is only used in this one place, we can just create an anonymous one here...
     *
     * @return A <b>FieldIDDataProvider</b> typed to <b>WarningTemplate</b> which will return all Warning Templates for the DataTable.
     */
    private FieldIDDataProvider<WarningTemplate> getDataProvider() {
        return new FieldIDDataProvider<WarningTemplate>() {
            @Override
            public Iterator<? extends WarningTemplate> iterator(int first, int count) {
                return warningTemplateService.getAllTemplatesForTenant().iterator();
            }

            @Override
            public int size() {
                return warningTemplateService.getAllTemplatesForTenant().size();
            }

            @Override
            public IModel<WarningTemplate> model(WarningTemplate object) {
                return new AbstractReadOnlyModel<WarningTemplate>() {
                    @Override
                    public WarningTemplate getObject() {
                        return object;
                    }
                };
            }
        };
    }

    private void deleteAction(AjaxRequestTarget target, WarningTemplate template) {
        //The model has become detached by this point, which is unacceptable to Hibernate.
        //That means we're going to have to renew our reference to it by reading it again.
        WarningTemplate deleteMe = warningTemplateService.loadById(template.getId());
        warningTemplateService.delete(deleteMe);
        target.add(listPanel);
    }

    private WarningTemplate saveAction(AjaxRequestTarget target, WarningTemplate warningTemplate) {
        if(warningTemplate.isNew()) {
            warningTemplate = warningTemplateService.save(warningTemplate);
        } else {
            WarningTemplate fromDB = warningTemplateService.loadById(warningTemplate.getId());

            fromDB.setName(warningTemplate.getName());
            fromDB.setWarning(warningTemplate.getWarning());

            warningTemplate = warningTemplateService.update(fromDB);
        }
        addTemplateModal.close(target);
//        addTemplateModal.setContent(inputPanel = new LotoWarningTemplateModalInputPanel(addTemplateModal.getContentId()));
        target.add(listPanel); //We need to add this target so that the list refreshes.  Might be nice.
        target.add(inputPanel);
        target.add(addTemplateModal);

        return warningTemplate;
    }

    private void cancelAction(AjaxRequestTarget target) {
        addTemplateModal.close(target);
    }
}

package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.loto.ProcedureTitleLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureDefinitionListPage;
import com.n4systems.model.Asset;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ProcedureDefinitionPage extends FieldIDFrontEndPage{

    private @SpringBean ProcedureDefinitionService procedureDefinitionService;
    private @SpringBean PersistenceService persistenceService;

    protected Label assetNameLabel;
    protected Label pageTileLabel;
    protected Label isolationPointLabel;
    private IModel<ProcedureDefinition> model;

    enum ProcedureDefinitionSection { Details, Content, Publish };

    private Navigation navigation;
    private ProcedureDefinitionSection currentSection = ProcedureDefinitionSection.Details;
    private ProcedureDefinitionFormContainer form;
    private boolean contentFinished = false;

    public ProcedureDefinitionPage(Asset asset) {
        super(new PageParameters());

        ProcedureDefinition pd = new ProcedureDefinition();
        pd.setProcedureCode("New Procedure");
        pd.setAsset(asset);
        pd.setTenant(asset.getTenant());
        pd.setPublishedState(PublishedState.DRAFT);
        pd.setDevelopedBy(getCurrentUser());
        pd.setEquipmentNumber(asset.getIdentifier());
        pd.setEquipmentLocation(asset.getAdvancedLocation().getFullName());
        pd.setEquipmentDescription(asset.getType().getDisplayName());

        init(Model.of(pd));
    }

    public ProcedureDefinitionPage(IModel<ProcedureDefinition> model) {
        super(new PageParameters());
        init(model);
    }

    public ProcedureDefinitionPage(PageParameters params) {
        super(params);
        init(createEntityModel());
    }

    private IModel<ProcedureDefinition> createEntityModel() {
        Preconditions.checkState(getPageParameters().get("id") != null && getPageParameters().get("id").toString() != null, "you must specify an id for a ProcedureDefinition.");
        return createEntityModel(getPageParameters().get("id").toLong());
    }

    private IModel<ProcedureDefinition> createEntityModel(Long id) {
        ProcedureDefinition procedureDefinition = persistenceService.find(ProcedureDefinition.class, id);
        return Model.of(procedureDefinition);
    }

    private void init(IModel<ProcedureDefinition> model) {
        // this business requirement may change, but for now only edit DRAFT. even WAITING_FOR_AUTHORIZATION shouldn't be done.
        Preconditions.checkState(model.getObject().getPublishedState().isPreApproval(), "you are only allowed to edit PRE APPROVAL copies!");
        this.model = model;
        add(navigation = new Navigation("navigation"));
        add(form = new ProcedureDefinitionFormContainer("formSections", model));

        add(new AttributeAppender("class", Model.of("procedure-definition")));
    }

    @Override
    protected Component createTitleLabel(String labelId, boolean isTopTitle) {
        if(isTopTitle)
            return new Label(labelId, new FIDLabelModel("label.procedures"));
        else
            return new ProcedureTitleLabel(labelId, new PropertyModel<Asset>(model, "asset"));
    }
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/procedureDefinition.css");
        response.renderJavaScriptReference("javascript/procedureDefinitionPage.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    private void sectionChanged(AjaxRequestTarget target) {
        form.updateSection(target);
    }

    class ProcedureDefinitionFormContainer extends WebMarkupContainer {

        private final Component details;
        private final Component content;
        private final Component publish;

        ProcedureDefinitionFormContainer(String id, final IModel<ProcedureDefinition> model) {
            super(id, model);

            setOutputMarkupId(true);

            add(details = new DetailsPanel("details", model) {
                @Override protected void doCancel(AjaxRequestTarget target) {
                    ProcedureDefinitionPage.this.doCancel(target);
                }

                @Override protected void doContinue(AjaxRequestTarget target) {
                    currentSection = ProcedureDefinitionSection.Content;
                    updateSection(target);
                }
            });

            add(content = new ContentPanel("content",model) {
                @Override protected void doCancel(AjaxRequestTarget target) {
                    ProcedureDefinitionPage.this.doCancel(target);
                }

                @Override protected void doContinue(AjaxRequestTarget target) {
                    currentSection = ProcedureDefinitionSection.Publish;
                    updateSection(target);
                }

                @Override protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {
                    super.doAdd(target, sourceType);
                }
            }.setVisible(false));

            add(publish = new PublishPanel("publish", model) {
                @Override protected void doCancel(AjaxRequestTarget target) {
                    // leave the model as a detached object....don't save it.   nothing to "undo".
                    ProcedureDefinitionPage.this.doCancel(target);
                }

                @Override protected void doPublish() {
                    procedureDefinitionService.saveProcedureDefinition(model.getObject());
                    gotoProceduresPage();
                }

                @Override protected void doSave() {
                    procedureDefinitionService.saveProcedureDefinitionDraft(model.getObject());
                    gotoProceduresPage();
                }
            }.setVisible(false));

        }

        public void updateSection(AjaxRequestTarget target) {
            if (ProcedureDefinitionSection.Publish.equals(currentSection)) {
                contentFinished = true;
            }

            switch (currentSection) {
                case Details:
                    details.setVisible(true);
                    content.setVisible(false);
                    publish.setVisible(false);
                    break;
                case Content:
                    content.setVisible(true);
                    publish.setVisible(false);
                    details.setVisible(false);
                    break;
                case Publish:
                    publish.setVisible(true);
                    content.setVisible(false);
                    details.setVisible(false);
                    break;
            }
            target.add(form,navigation);
        }
    }

    private void gotoProceduresPage() {
        setResponsePage(new ProcedureDefinitionListPage(new PageParameters().add("uniqueID", model.getObject().getAsset().getId())));
    }

    private void doCancel(AjaxRequestTarget target) {
        gotoProceduresPage();
    }

    class Navigation extends Form {

        public Navigation(String id) {
            super(id);
            List<ProcedureDefinitionSection> sections = Lists.newArrayList(ProcedureDefinitionSection.values());
            add(new ListView<ProcedureDefinitionSection>("sections", sections) {
                @Override
                protected void populateItem(final ListItem<ProcedureDefinitionSection> item) {
                    ProcedureDefinitionSection section = item.getModelObject();
                    Button button = new AjaxButton("section") {
                        @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                            if(currentSection.equals(ProcedureDefinitionSection.Details)) {
                                target.appendJavaScript("document.getElementById('detailsContinueLink').click();");
                            } else {
                                currentSection = item.getModelObject();
                                sectionChanged(target);
                            }
                        }
                        @Override protected void onError(AjaxRequestTarget target, Form<?> form) { }
                    };
                    if (section.equals(currentSection)) {
                        button.add(new AttributeAppender("class", "active"));
                    }
                    button.setMarkupId(item.getModelObject().name());
                    item.add(button);
                    if (currentSection.equals(ProcedureDefinitionSection.Details)
                            && ProcedureDefinitionSection.Publish.equals(item.getModel().getObject())) {
                        button.setEnabled(false);
                        button.add(new AttributeAppender("class", "disabled"));
                    }
                    item.add(new Label("label", Model.of(item.getModel().getObject().name())));
                }
            });
        }

    }


}

package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.Score;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.util.List;

public class ProcedureDefinitionPage extends FieldIDFrontEndPage implements IVisitor<FormComponent,Void> {

    private @SpringBean ProcedureSearchService procedureService;
    private @SpringBean PersistenceService persistenceService;

    protected Label assetNameLabel;
    protected Label pageTileLabel;
    protected Label isolationPointLabel;
    private IModel<ProcedureDefinition> model;


    enum ProcedureDefinitionSection { Details, Content, Publish };

    private Navigation navigation;
    private ProcedureDefinitionSection currentSection = ProcedureDefinitionSection.Details;
    private ProcedureDefinitionForm form;
    private boolean contentFinished = false;

    public ProcedureDefinitionPage(IModel<ProcedureDefinition> model) {
        super(new PageParameters());
        init(createEntityModel(model.getObject().getId()));
    }

    public ProcedureDefinitionPage(PageParameters params) {
        super(params);
        init(createEntityModel());
    }

    public ProcedureDefinitionPage(ProcedureDefinition procedureDefinition) {
        super(new PageParameters());
        init(Model.of(procedureDefinition));
    }

    private IModel<ProcedureDefinition> createEntityModel() {
        Preconditions.checkState(getPageParameters().get("id")!=null && getPageParameters().get("id").toString()!=null,"you must specify an id for a ProcedureDefinition.");
        return createEntityModel(getPageParameters().get("id").toLong());
    }

    private IModel<ProcedureDefinition> createEntityModel(Long id) {
        return new EntityModel<ProcedureDefinition>(ProcedureDefinition.class,id) {
            @Override protected ProcedureDefinition load() {
                ProcedureDefinition entity = super.load();
                entity.getIsolationPoints();
                return entity;
            }
        };
    }

    private void init(IModel<ProcedureDefinition> model) {
        this.model = model;
        add(assetNameLabel = new Label("assetName", Model.of("Big Machine")));
        add(pageTileLabel = new Label("pageTitle",Model.of("Author Procedure")));
        add(isolationPointLabel = new Label("isolationPoint",Model.of(": Isolation Point E-1")));

        add(navigation = new Navigation("navigation", new PropertyModel<Score>(this, "currentSection")));
        add(form = new ProcedureDefinitionForm("form", model));

        add(new AttributeAppender("class", Model.of("procedure-definition")));

        visitChildren(FormComponent.class, this);
    }

    @Override  // part of IVisitor interface.  to avoid serialization issues, don't make anonymous class.
    public void component(FormComponent formComponent, IVisit<Void> visit) {
        formComponent.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                // TODO : this is hook to save draft mode when anything changes...for now it's just here so model is updated.
                // makes the form very chatty but gives you lots of saving options.
            }
        });
    }

    protected Label createTitleLabel(String labelId) {
         Label label = new Label(labelId, Model.of("THIS SHOULDN'T BE SHOWN"));
         label.setVisible(false);
         return label;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/procedureDefinition.css");
        response.renderJavaScriptReference("javascript/procedureDefinitionPage.js");
    }

    private void sectionChanged(AjaxRequestTarget target) {
        form.updateSection(target);
    }

    private ProcedureDefinition getProcedureDefinition() {
        StringValue idParam = getPageParameters().get("id");
        Preconditions.checkState(idParam!=null && idParam.toString()!=null,"must pass id of procedure definition for page creation.");
        return persistenceService.find(ProcedureDefinition.class, idParam.toLong());
    }


    class ProcedureDefinitionForm extends Form {

        private final Component details;
        private final Component content;
        private final Component publish;

        ProcedureDefinitionForm(String id, IModel<ProcedureDefinition> model) {
            super(id, model);

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
            }.setVisible(false));

            add(publish = new PublishPanel("publish", model) {
                @Override
                protected void doCancel(AjaxRequestTarget target) {
                    if (!getProcedureDefinition().isNew()) {
                        procedureService.reset(getProcedureDefinition());
                    }
                }

                @Override
                protected void doPublish(AjaxRequestTarget target) {
                    procedureService.save(getProcedureDefinition());
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

    private void doCancel(AjaxRequestTarget target) {

    }

    class Navigation extends RadioGroup<ProcedureDefinitionSection> {

        public Navigation(String id, IModel model) {
            super(id, model);
            add(new AjaxFormChoiceComponentUpdatingBehavior() {
                @Override protected void onUpdate(AjaxRequestTarget target) {
                    sectionChanged(target);
                }
            });
            List<ProcedureDefinitionSection> sections = Lists.newArrayList(ProcedureDefinitionSection.values());
            add(new ListView<ProcedureDefinitionSection>("sections", sections) {
                @Override
                protected void populateItem(ListItem<ProcedureDefinitionSection> item) {
                    Radio<ProcedureDefinitionSection> radio = new Radio<ProcedureDefinitionSection>("section", item.getModel());
                    item.add(radio);
                    if (!contentFinished && ProcedureDefinitionSection.Publish.equals(item.getModel().getObject())) {
                        radio.setEnabled(false);
                    }
                    item.add(new Label("label", Model.of(item.getModel().getObject().name())));
                }
            });
        }

    }


}

package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.search.ProcedureService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.Score;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ProcedureDefinitionPage extends FieldIDFrontEndPage {

    private @SpringBean ProcedureService procedureService;
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
        super(new PageParameters().add("id",model.getObject().getId()==null?"":model.getObject().getId()));
        init(model);
    }

    public ProcedureDefinitionPage(PageParameters params) {
        super(params);
        init(getProcedureDefinition(params));
    }

    private IModel<ProcedureDefinition> getProcedureDefinition(PageParameters params) {
        // TODO : load procedure definition by ID.   for now i'm just handling new ones.
        return Model.of(new ProcedureDefinition());
    }

    private void init(IModel<ProcedureDefinition> model) {
        this.model = model;
        add(assetNameLabel = new Label("assetName", Model.of("Big Machine")));
        add(pageTileLabel = new Label("pageTitle",Model.of("Author Procedure")));
        add(isolationPointLabel = new Label("isolationPoint",Model.of(": Isolation Point E-1")));

        add(navigation = new Navigation("navigation", new PropertyModel<Score>(this, "currentSection")));
        add(form = new ProcedureDefinitionForm("form", model));

        add(new AttributeAppender("class", Model.of("procedure-definition")));
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


    protected ProcedureDefinition getProcedureDefinition() {
        return model.getObject();
    }



    class ProcedureDefinitionForm extends Form {

        private final Component details;
        private final Component content;
        private final Component publish;

        ProcedureDefinitionForm(String id, IModel<ProcedureDefinition> model) {
            super(id,model);

            add(details = new DetailsPanel("details", model) {
                @Override
                protected void doCancel(AjaxRequestTarget target) {
                }

                @Override
                protected void doContinue(AjaxRequestTarget target) {
                    currentSection = ProcedureDefinitionSection.Content;
                    updateSection(target);
                }
            }.setVisible(true));

            add(content = new ContentPanel("content",model) {
                    @Override protected void doCancel(AjaxRequestTarget target) {
                }
                @Override protected void doContinue(AjaxRequestTarget target) {
                    currentSection = ProcedureDefinitionSection.Publish;
                    updateSection(target);
                }
            }.setVisible(false));


            add(publish = new PublishPanel("publish",model) {
                @Override protected void doCancel(AjaxRequestTarget target) {
                    if (!getProcedureDefinition().isNew()) {
                        procedureService.resetProcedureDefinition(getProcedureDefinition());
                    }
                }
                @Override protected void doPublish(AjaxRequestTarget target) {
                    procedureService.saveProcedureDefinition(getProcedureDefinition());
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

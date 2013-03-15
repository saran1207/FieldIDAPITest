package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.Score;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
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

import java.util.List;

public class ProcedureDefinitionPage extends FieldIDFrontEndPage {


    enum ProcedureDefinitionSection { Details, Content, Publish };

    private final Navigation navigation;
    private ProcedureDefinitionSection currentSection = ProcedureDefinitionSection.Details;
    private final ProcedureDefinitionForm form;
    private boolean contentFinished = false;

    public ProcedureDefinitionPage(PageParameters params) {
        super(params);

        add(navigation = new Navigation("navigation", new PropertyModel<Score>(this, "currentSection")));
        add(form = new ProcedureDefinitionForm("form"));
    }

     protected Label createTitleLabel(String labelId) {
        return new Label(labelId,Model.of("Asset Name : Author Procedure"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/procedureDefinition.css");
    }

    private void sectionChanged(AjaxRequestTarget target) {
        form.updateSection(target);
    }


    class ProcedureDefinitionForm extends Form {

        private final Component details;
        private final Component content;
        private final Component publish;

        ProcedureDefinitionForm(String id) {
            super(id);
            add(details = new DetailsPanel("details") {
                @Override protected void doCancel(AjaxRequestTarget target) {
                }
                @Override protected void doContinue(AjaxRequestTarget target) {
                    currentSection = ProcedureDefinitionSection.Content;
                    updateSection(target);
                }
            }.setVisible(true));

            add(content = new ContentPanel("content") {
                    @Override protected void doCancel(AjaxRequestTarget target) {
                }
                @Override protected void doContinue(AjaxRequestTarget target) {
                    currentSection = ProcedureDefinitionSection.Publish;
                    updateSection(target);
                }
            }.setVisible(false));


            add(publish = new PublishPanel("publish") {
                @Override protected void doCancel(AjaxRequestTarget target) {
                }
                @Override protected void doPublish(AjaxRequestTarget target) {
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

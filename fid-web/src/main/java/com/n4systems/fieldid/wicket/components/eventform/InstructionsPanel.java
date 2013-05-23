package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;
import com.n4systems.fieldid.wicket.components.richText.RichText;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Criteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.StringValidator;

import static ch.lambdaj.Lambda.on;

public class InstructionsPanel extends Panel {

    private RichText text;
    private ContainerFeedbackPanel feedbackPanel;

    public InstructionsPanel(final String id, final Model<Criteria> criteriaModel) {
        super(id, criteriaModel);
        add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", this));
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.setOutputMarkupPlaceholderTag(true);

        add(new InstructionsForm("form", (Model<Criteria>) getDefaultModel()));
        add(new TooltipImage("tooltip", new StringResourceModel("label.tooltip.criteria_instructions",this,null)));
    }

    class InstructionsForm extends Form {

        public InstructionsForm(String id, final Model<Criteria> criteriaModel) {
            super(id, criteriaModel);
            add(text = new RichText("text", ProxyModel.of(criteriaModel, on(Criteria.class).getInstructions())) {
                @Override
                protected String getImagePath() {
                    String path = (criteriaModel.getObject() == null) ? "empty" : criteriaModel.getObject().getId().toString();
                    return String.format("/criteria/%s/instructions/images/", path);
                }

                @Override
                protected void onRichTextError(AjaxRequestTarget target, RuntimeException e) {
                    feedbackPanel.setVisible(true);
                    target.add(feedbackPanel);
                }

                @Override
                protected void onRichTextUpdate(AjaxRequestTarget target) {
                    if(feedbackPanel.isVisible()) {
                        feedbackPanel.setVisible(false);
                        target.add(feedbackPanel);
                    }

                }

            }.withAutoUpdate().withWidth("310px"));
            text.withValidationBehavior();
            text.addBehavior(new StringValidator.MaximumLengthValidator(5000) {
                @Override
                protected String resourceKey() {
                    return "InstructionsPanel.StringValidator.maximum";
                }
            });
       }
    }

}

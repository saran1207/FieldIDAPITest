package com.n4systems.fieldid.wicket.components.setup.loto;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This Panel is used to populate a Modal window used for Adding or Editing Warning Templates.
 *
 * Created by Jordan Heath on 14-11-21.
 */
public class LotoWarningTemplateModalInputPanel extends Panel {

    @SpringBean
    private WarningTemplateService warningTemplateService;

    protected Model<WarningTemplate> warningTemplateModel;
    private FIDFeedbackPanel feedbackPanel;
    private RequiredTextField<String> nameField;
    private TextArea<String> warningArea;

    private static final Logger logger = Logger.getLogger(LotoWarningTemplateModalInputPanel.class);

    public LotoWarningTemplateModalInputPanel(String id, Model<WarningTemplate> warningTemplateModel) {
        super(id, warningTemplateModel);
        this.warningTemplateModel = warningTemplateModel;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setVisible(true);
        add(feedbackPanel);

        Form<WarningTemplate> form = new Form<>("templateForm");

        form.add(nameField = new RequiredTextField<>("name", new PropertyModel<>(warningTemplateModel, "name")));
        nameField.setOutputMarkupId(true);

        form.add(warningArea = new TextArea<>("warning", new PropertyModel<>(warningTemplateModel, "warning")));
        warningArea.setRequired(true);
        warningArea.setOutputMarkupId(true);

        form.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                doSave(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                logger.error("Error when saving " + (warningTemplateModel.getObject().isNew() ? "a new WarningTemplate!" : "a WarningTemplate with ID " + warningTemplateModel.getObject().getId()));
                target.add(feedbackPanel);
            }
        });

        form.add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });

        form.setOutputMarkupId(true);

        add(form);

    }

    protected void doCancel(AjaxRequestTarget target) {}

    protected void doSave(AjaxRequestTarget target) {}
}

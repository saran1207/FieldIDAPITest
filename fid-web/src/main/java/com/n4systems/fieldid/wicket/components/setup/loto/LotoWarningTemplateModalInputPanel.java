package com.n4systems.fieldid.wicket.components.setup.loto;

import com.n4systems.fieldid.service.warningtemplates.WarningTemplateService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.text.LabelledRequiredTextField;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;

/**
 * This Panel is used to populate a Modal window used for Adding or Editing Warning Templates.
 *
 * Created by Jordan Heath on 14-11-21.
 */
public class LotoWarningTemplateModalInputPanel extends Panel {

    @SpringBean
    private WarningTemplateService warningTemplateService;

    protected WarningTemplate warningTemplate;
    private FIDFeedbackPanel feedbackPanel;
    private LabelledRequiredTextField<String> nameField;
    private LabelledTextArea<String> warningArea;

    private static final Logger logger = Logger.getLogger(LotoWarningTemplateModalInputPanel.class);

    public LotoWarningTemplateModalInputPanel(String id) {
        super(id);

        warningTemplate = new WarningTemplate();
        warningTemplate.setTenant(FieldIDSession.get().getTenant());
    }

    public LotoWarningTemplateModalInputPanel(String id,
                                              WarningTemplate warningTemplate) {
        super(id);

        this.warningTemplate = warningTemplate;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setVisible(true);

        //Add both fields as Labelled Required Fields (if possible... can do that at least for name).
        Form<WarningTemplate> form = new Form<>("templateForm");
        form.add(feedbackPanel);
        nameField = new LabelledRequiredTextField<>("name", "Name:", ProxyModel.of(warningTemplate, on(WarningTemplate.class).getName()));
        nameField.add(new AttributeAppender("style", new Model<>("padding-left: 15px"), " "));
        form.add(nameField);
        nameField.setOutputMarkupId(true);
        form.add(warningArea = new LabelledTextArea<>("warning", "Warning:", new PropertyModel<>(warningTemplate, "warning")));
        warningArea.setOutputMarkupId(true);
        form.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                doSave(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                logger.error("Error when saving " + (warningTemplate.isNew() ? "a new WarningTemplate!" : "a WarningTemplate with ID " + warningTemplate.getId()));
                target.add(feedbackPanel);
                error(new FIDLabelModel("label.error"));
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

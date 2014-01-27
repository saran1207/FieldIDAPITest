package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ConfirmArchivePage extends FieldIDTemplatePage {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    private IModel<PriorityCode> priorityCodeModel;

    private PriorityCode newPriority;

    private FIDFeedbackPanel feedbackPanel;

    public ConfirmArchivePage(PageParameters params) {
        priorityCodeModel = new EntityModel<PriorityCode>(PriorityCode.class, params.get("id").toLong());

        Long numAssignedActions = priorityCodeService.countOpenActionsWithPriorityCode(priorityCodeModel.getObject());

        add(new Label("message", new FIDLabelModel("msg.delete_priority_code", numAssignedActions, priorityCodeModel.getObject().getDisplayName())));

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        Form form;
        add(form = new Form<Void>("form"));
        form.add(new FidDropDownChoice<PriorityCode>("priorityCodes", new PropertyModel<PriorityCode>(this, "newPriority"),
                createPriorityCodesModel(), new ListableChoiceRenderer<PriorityCode>()).setRequired(true));

        form.add(new AjaxSubmitLink("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                priorityCodeService.archiveAndUpdateActions(priorityCodeModel.getObject(), newPriority);
                setResponsePage(PriorityCodePage.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        form.add(new BookmarkablePageLink<Void>("cancel", PriorityCodePage.class));

    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, PriorityCodePage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_priority_codes")));
        return pageLink;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.archive_priority_code", priorityCodeModel.getObject().getDisplayName()));
    }

    private LoadableDetachableModel<List<PriorityCode>> createPriorityCodesModel() {
        return new LoadableDetachableModel<List<PriorityCode>>() {
           @Override
           protected List<PriorityCode> load() {
               List<PriorityCode> codes = priorityCodeService.getActivePriorityCodes();
               codes.remove(priorityCodeModel.getObject());
               return codes;
           }
       };
    }
}

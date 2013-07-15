package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeArchivedListPanel;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeListPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PriorityCodePage extends FieldIDFrontEndPage {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    @SpringBean
    private PersistenceService persistenceService;

    private AjaxSubmitLink addPriorityCodeButton;

    private AjaxLink viewAll;
    private AjaxLink viewArchived;
    private PriorityCodeListPanel priorityCodeListPanel;
    private PriorityCodeArchivedListPanel priorityCodeArchivedListPanel;

    private ModalWindow addOrEditPriorityCodeWindow;
    private AddEditPriorityCodePanel addEditPriorityCodePanel;

    public PriorityCodePage() {
        add(addOrEditPriorityCodeWindow = new DialogModalWindow("priorityCodeModalWindow").setInitialWidth(365).setInitialHeight(280));
        addOrEditPriorityCodeWindow.setContent(addEditPriorityCodePanel = createAddEditPanel(Model.of(new PriorityCode())));
        add(viewAll = new AjaxLink<Void>("viewAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                viewAll.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));
                viewArchived.add(new AttributeModifier("class", "mattButtonRight"));
                priorityCodeListPanel.setVisible(true);
                priorityCodeArchivedListPanel.setVisible(false);
                target.add(viewAll, viewArchived, priorityCodeListPanel, priorityCodeArchivedListPanel);
                refreshTipsy(target);
            }
        });
        viewAll.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));

        add(viewArchived = new AjaxLink<Void>("viewArchived") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                viewArchived.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));
                viewAll.add(new AttributeModifier("class", "mattButtonLeft"));
                priorityCodeListPanel.setVisible(false);
                priorityCodeArchivedListPanel.setVisible(true);
                target.add(viewAll, viewArchived, priorityCodeListPanel, priorityCodeArchivedListPanel);
                refreshTipsy(target);
            }
        });

        Form openForm = new Form("openForm");
        openForm.add(addPriorityCodeButton = new AjaxSubmitLink("addPriorityCodeButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                addEditPriorityCodePanel.setPriorityCodeModel(Model.of(new PriorityCode()));
                addOrEditPriorityCodeWindow.show(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        add(openForm);

        add(priorityCodeListPanel = new PriorityCodeListPanel("priorityCodeList") {
            @Override
            protected void onEditPriorityCodeClicked(AjaxRequestTarget target, PriorityCode priorityCode) {
                addOrEditPriorityCodeWindow.setContent(createAddEditPanel(Model.of(priorityCode)));
                addOrEditPriorityCodeWindow.show(target);
            }
        });
        priorityCodeListPanel.setOutputMarkupPlaceholderTag(true);

        add(priorityCodeArchivedListPanel = new PriorityCodeArchivedListPanel("priorityCodeArchivedList"));
        priorityCodeArchivedListPanel.setOutputMarkupPlaceholderTag(true);
        priorityCodeArchivedListPanel.setVisible(false);

    }

    private AddEditPriorityCodePanel createAddEditPanel(IModel<PriorityCode> priorityCode) {
        return new AddEditPriorityCodePanel(addOrEditPriorityCodeWindow.getContentId(), priorityCode) {
            @Override
            protected void onEditComplete(AjaxRequestTarget target, PriorityCode priorityCode) {
                persistenceService.saveOrUpdate(priorityCode);
                setPriorityCodeModel(Model.of(new PriorityCode()));
                target.add(priorityCodeListPanel, priorityCodeArchivedListPanel);
                addOrEditPriorityCodeWindow.close(target);
                refreshTipsy(target);
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/newCss/priority/priority_codes.css");

        response.renderCSSReference("style/newCss/setup/prettyItemList.css");

        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        // CAVEAT : https://github.com/jaz303/tipsy/issues/19
        // after ajax call, tipsy tooltips will remain around so need to remove them explicitly.
        response.renderOnDomReadyJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
    }

    private void refreshTipsy(AjaxRequestTarget target) {
        target.appendJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new PriorityCodeTitleLabel(labelId);
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.priority_codes"));
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }
}

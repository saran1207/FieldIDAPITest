package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeArchivedListPanel;
import com.n4systems.fieldid.wicket.components.actions.PriorityCodeListPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PriorityCodePage extends FieldIDTemplatePage {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    @SpringBean
    private PersistenceService persistenceService;

    private AjaxLink viewAll;
    private AjaxLink viewArchived;
    private PriorityCodeListPanel priorityCodeListPanel;
    private PriorityCodeArchivedListPanel priorityCodeArchivedListPanel;

    private ModalWindow addOrEditPriorityCodeWindow;

    public PriorityCodePage() {
        add(addOrEditPriorityCodeWindow = new DialogModalWindow("priorityCodeModalWindow").setInitialWidth(365).setInitialHeight(280));
        addOrEditPriorityCodeWindow.setContent(createAddEditPanel(Model.of(new PriorityCode())));

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
        return new TitleLabel(labelId);
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

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    private class TitleLabel extends Fragment {

        public TitleLabel(String id) {
            super(id, "priorityCodesTitleLabel", PriorityCodePage.this);
            ContextImage tooltip;

            add(tooltip = new ContextImage("tooltip", "images/tooltip-icon.png"));
            tooltip.add(new AttributeAppender("title", new FIDLabelModel("msg.priority_codes")));
        }
    }

    private class ActionGroup extends Fragment {

        public ActionGroup(String id) {
            super(id, "priorityCodesActionGroup", PriorityCodePage.this);

            add(viewAll = new AjaxLink<Void>("viewAll") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    viewAll.add(new AttributeAppender("class", "selected").setSeparator(" "));
                    viewArchived.add(new AttributeModifier("class", "btn-secondary"));
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
                    viewArchived.add(new AttributeAppender("class", "selected").setSeparator(" "));
                    viewAll.add(new AttributeModifier("class", "btn-secondary"));
                    priorityCodeListPanel.setVisible(false);
                    priorityCodeArchivedListPanel.setVisible(true);
                    target.add(viewAll, viewArchived, priorityCodeListPanel, priorityCodeArchivedListPanel);
                    refreshTipsy(target);
                }
            });

            add(new AjaxLink<Void>("addPriorityCodeButton") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    addOrEditPriorityCodeWindow.setContent(createAddEditPanel(Model.of(new PriorityCode())));
                    addOrEditPriorityCodeWindow.show(target);
                }
            });
        }
    }
}

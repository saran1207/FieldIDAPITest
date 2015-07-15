package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.procedure.LockoutReasonService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.setup.loto.AddEditLockoutReasonPanel;
import com.n4systems.fieldid.wicket.components.setup.loto.LockoutReasonActionPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.procedure.LockoutReason;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class LockoutReasonsPage extends FieldIDTemplatePage {

    @SpringBean
    protected LockoutReasonService lockoutReasonsService;

    protected ModalWindow addOrEditModalWindow;
    protected WebMarkupContainer listContainer;

    protected LockoutReasonsPage() {
        super();
        add(addOrEditModalWindow = new DialogModalWindow("addOrEditModalWindow").setInitialHeight(400).setInitialWidth(400));
    }

    protected void redrawList(AjaxRequestTarget target) {
        target.add(listContainer);
    };

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
            aNavItem().label(new FIDLabelModel("nav.view_all.count", lockoutReasonsService.getNumberOfActiveLockoutReasons())).page(LockoutReasonsListPage.class).build(),
            aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", lockoutReasonsService.getNumberOfArchivedLockoutReasons())).page(ArchivedLockoutReasonsListPage.class).build()
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_lockout_reasons"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, LotoSetupPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new LockoutReasonActionPanel(actionGroupId) {
            @Override
            protected void onAddClicked(AjaxRequestTarget target) {
                addOrEditModalWindow.setContent(getAddEditPanel(addOrEditModalWindow.getContentId(), Model.of(new LockoutReason())));
                addOrEditModalWindow.show(target);
            }
        };
    }

    protected AddEditLockoutReasonPanel getAddEditPanel(String id, IModel<LockoutReason> model) {
        return new AddEditLockoutReasonPanel(id, model) {
            @Override
            protected void onSaveLockoutReason(AjaxRequestTarget target) {
                redrawList(target);
                addOrEditModalWindow.close(target);
            }

            @Override
            protected void onCancelClicked(AjaxRequestTarget target) {
                addOrEditModalWindow.close(target);
            }
        };
    }
}

package com.n4systems.fieldid.wicket.components.setup.assetstatus;

import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.assetstatus.EditAssetStatusPage;
import com.n4systems.model.AssetStatus;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This is the action cell for the Asset Status list page in the Setup section.  It's used by the
 * <b>AssetStatusListPanel</b> to display context-specific actions for <b>AssetStatus</b> entities.
 *
 * When an <b>AssetStatus</b> has a state of <i>Archivable.EntityState.ACTIVE</i>, "Edit" and "Archive" links will be
 * displayed.  If the <b>AssetStatus</b> has a state of <i>Archivable.EntityState.ARCHIVED</i>, an "Unarchive" link will
 * be displayed.
 *
 * Created by Jordan Heath on 31/07/14.
 */
public class AssetStatusActionCell extends Panel {

    @SpringBean
    private AssetStatusService assetStatusService; //Used for data access... despite the warning, it IS used.

    private AssetStatus thisStatus; //Internal reference to the AssetStatus represented by this cell.

    /**
     * This is the main constructor.  It will build your class based on a String "id" parameter (which must be equal
     * to the wicket:id attribute) and an <b>IModel<AssetStatus></b> object which represents the AssetStatus for which
     * the cell is being built.
     *
     * @param id - A <b>String</b> value representing the wicket:id of the component.
     * @param model - An <b>IModel</b> object typed to <b>AssetStatus</b>, representing the AssetStatus that needs a cell.
     */
    public AssetStatusActionCell(String id, final IModel<AssetStatus> model) {
        super(id);

        this.thisStatus = model.getObject();

        Link editLink;

        add(editLink = new BookmarkablePageLink("editLink",
                                                EditAssetStatusPage.class,
                                                PageParametersBuilder.param("assetStatusId",
                                                                            thisStatus.getId())));

        editLink.setVisible(thisStatus.isActive());


        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        //We don't need to worry about controlling the visibility of this link, because it lives inside of a
        //wicket:enclosure, with editLink set as its child.  This means that any time the visibility of editLink
        //changes, the visibility of the whole enclosure changes.
        optionsContainer.add(new AjaxLink("archiveLink") {
            /**
             * This method simply provides the click functionality for the Archive link, tying it to an internal method
             * in the class.
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                AssetStatus archiveMe = assetStatusService.getStatusById(thisStatus.getId());
                assetStatusService.archiveStatus(archiveMe);

                FieldIDSession.get()
                              .info(new FIDLabelModel("message.archive_asset_status",
                                    thisStatus.getDisplayName()).getObject());

                onAction(target);
            }
        });

        add(optionsContainer);

        AjaxLink unarchiveLink;

        add(unarchiveLink = new AjaxLink("unarchiveLink") {
            /**
             * This method simply provides the click functionality for the Unarchive link, tying it to an internal
             * method in the class.
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                AssetStatus unarchiveMe = assetStatusService.getStatusById(thisStatus.getId());
                assetStatusService.unarchiveStatus(unarchiveMe);

                FieldIDSession.get()
                              .info(new FIDLabelModel("message.unarchive_asset_status",
                                    thisStatus.getDisplayName()).getObject());

                onAction(target);
            }
        });

        unarchiveLink.setVisible(thisStatus.isArchived());
    }

    protected void onAction(AjaxRequestTarget target) {
        //This sucker needs to be overridden.
    }
}

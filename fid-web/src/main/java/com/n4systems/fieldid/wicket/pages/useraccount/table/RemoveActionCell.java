package com.n4systems.fieldid.wicket.pages.useraccount.table;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.model.Asset;
import com.n4systems.model.offlineprofile.OfflineProfile;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RemoveActionCell extends Panel {

    private IModel<Asset> assetModel;
    private OfflineProfile offlineProfile;

    @SpringBean
    private OfflineProfileService offlineProfileService;

    public RemoveActionCell(String id, IModel<Asset> model, OfflineProfile profile) {
        super(id, model);
        this.assetModel = model;
        this.offlineProfile = profile;

        add(new AjaxLink<Void>("remove") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                offlineProfile.getAssets().remove(assetModel.getObject().getMobileGUID());
                offlineProfileService.update(offlineProfile);
                onRemoveAsset(target);
            }
        });
    }

    public void onRemoveAsset(AjaxRequestTarget target) {}
}

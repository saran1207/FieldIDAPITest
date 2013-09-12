package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.AssetStatus;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetStatusTranslationsPage extends TranslationsPage<AssetStatus> {

    @SpringBean
    private AssetStatusService assetStatusService;

    public AssetStatusTranslationsPage() {
        super();
    }

    @Override
    protected DropDownChoice<AssetStatus> createChoice(String id) {
        return new FidDropDownChoice<AssetStatus>(id, Model.of(new AssetStatus()), assetStatusService.getActiveStatuses(), getChoiceRenderer());
    }

    public IChoiceRenderer<AssetStatus> getChoiceRenderer() {
        return new IChoiceRenderer<AssetStatus>() {
            @Override
            public Object getDisplayValue(AssetStatus status) {
                return status.getDisplayName();
            }

            @Override
            public String getIdValue(AssetStatus status, int index) {
                return status.getId()+"";
            }
        };
    }

}

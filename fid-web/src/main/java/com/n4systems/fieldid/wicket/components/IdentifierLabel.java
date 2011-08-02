package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.AssetType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

public class IdentifierLabel extends Label {

    public IdentifierLabel(String id) {
        this(id, new Model<AssetType>(null));
    }

    public IdentifierLabel(String id, IModel<AssetType> assetTypeModel) {
        super(id, new IdentifierLabelModel(assetTypeModel));
        setRenderBodyOnly(true);
    }

    static class IdentifierLabelModel extends LoadableDetachableModel<String> {

        private IModel<AssetType> assetTypeModel;

        public IdentifierLabelModel(IModel<AssetType> assetTypeModel) {
            this.assetTypeModel = assetTypeModel;
        }

        @Override
        protected String load() {
            if (assetTypeModel.getObject() != null) {
                return assetTypeModel.getObject().getIdentifierLabel();
            }
            return FieldIDSession.get().getPrimaryOrg().getIdentifierLabel();
        }
    }

}

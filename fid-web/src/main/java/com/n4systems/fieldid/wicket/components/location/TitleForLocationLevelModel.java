package com.n4systems.fieldid.wicket.components.location;

import com.n4systems.uitags.views.HierarchicalNode;
import org.apache.wicket.model.IModel;

import java.util.List;

public class TitleForLocationLevelModel implements IModel<String> {

    private IModel<List<HierarchicalNode>> nodeListModel;

    public TitleForLocationLevelModel(IModel<List<HierarchicalNode>> nodeListModel) {
        this.nodeListModel = nodeListModel;
    }

    @Override
    public String getObject() {
        if (nodeListModel.getObject().isEmpty()) {
            return null;
        }
        return nodeListModel.getObject().get(0).getLevelName();
    }

    @Override
    public void setObject(String object) {
    }

    @Override
    public void detach() {
    }
}

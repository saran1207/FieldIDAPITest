package com.n4systems.fieldid.actions.columnlayout;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;

public class EditColumnLayoutAction extends AbstractAction {

    private String layoutType;

    public EditColumnLayoutAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    public String doEdit() {
        return SUCCESS;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

}

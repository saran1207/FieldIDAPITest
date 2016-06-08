package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.event.ButtonGroupService;
import com.n4systems.model.ButtonGroup;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * Super thin DataProvider for the Button Groups Page.
 *
 * Created by Jordan Heath on 2016-03-11.
 */
public class ButtonGroupDataProvider extends FieldIDDataProvider<ButtonGroup> {

    @SuppressWarnings("unused")
    @SpringBean
    private ButtonGroupService buttonGroupService;

    private List<ButtonGroup> groupList;

    public ButtonGroupDataProvider() {
        this.groupList = buttonGroupService.getButtonGroups();
    }

    @Override
    public Iterator<? extends ButtonGroup> iterator(int first, int count) {
        return groupList.subList(first, count).iterator();
    }

    @Override
    public int size() {
        return groupList.size();
    }

    @Override
    public IModel<ButtonGroup> model(ButtonGroup buttonGroup) {
        return new AbstractReadOnlyModel<ButtonGroup>() {
            @Override
            public ButtonGroup getObject() {
                return buttonGroup;
            }
        };
    }

    public void refreshGroupList() {
        this.groupList = buttonGroupService.getButtonGroups();
    }

    public List<ButtonGroup> getCurrentGroupList() {
        return groupList;
    }
}

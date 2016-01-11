package com.n4systems.fieldid.wicket.components.eventform.details;

import com.n4systems.fieldid.wicket.components.eventform.SortableStringListEditor;
import com.n4systems.model.SelectCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class SelectDetailsPanel extends Panel {
	
    private SortableStringListEditor selectOptionsEditor;

	public SelectDetailsPanel(String id, IModel<SelectCriteria> selectCriteria) {
		super(id, selectCriteria);
		
		add(selectOptionsEditor = new SortableStringListEditor("selectOptionsEditor", new PropertyModel<>(selectCriteria, "options"),
                new Model<>("Drop Down Options"), true) {
            @Override
            protected void onAddLogicLinkClicked(AjaxRequestTarget target, String selectValue) {
                onConfigureCriteriaLogic(target, selectValue);
            }

            @Override
            protected boolean isExistingRule(String selectValue) {
                return isRuleExists(selectValue);
            }
        });
	}

	protected void onConfigureCriteriaLogic(AjaxRequestTarget target, String selectValue) {}

    protected boolean isRuleExists(String selectValue) {
        return false;
    }
}

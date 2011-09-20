package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;
import org.odlabs.wiquery.ui.sortable.SortableBehavior;
import org.odlabs.wiquery.ui.sortable.SortableContainment;

public abstract class SortableListPanel extends Panel {

    protected int currentlySelectedIndex = -1;

    public SortableListPanel(String id) {
        super(id);
    }

    public SortableListPanel(String id, IModel<?> model) {
        super(id, model);
    }

    public void setSelectedIndex(int newSelectedIndex) {
        this.currentlySelectedIndex = newSelectedIndex;
    }

    public void clearSelection() {
        currentlySelectedIndex = -1;
    }

    private void updateSelectedIndex(int currentIndexOfMovingItem, int newIndexOfMovingItem) {
        if (currentIndexOfMovingItem == currentlySelectedIndex) {
            // If we're moving the selected item, we need to update the selected index to reflect its new position
            currentlySelectedIndex = newIndexOfMovingItem;
        } else if (currentIndexOfMovingItem < currentlySelectedIndex && newIndexOfMovingItem >= currentlySelectedIndex) {
            // If the item was before the selected item, and we're moving it below the selected item, the selected index goes down by 1
            currentlySelectedIndex -= 1;
        } else if (currentIndexOfMovingItem > currentlySelectedIndex && newIndexOfMovingItem <= currentlySelectedIndex) {
            // Similarly for items after the selected item when moving above the selected item
            currentlySelectedIndex += 1;
        }
    }

    protected SortableAjaxBehavior makeSortableBehavior(String containmentCss) {
        SortableAjaxBehavior sortable = new SimpleSortableAjaxBehavior() {
            @Override
            public void onUpdate(Component component, int newIndexOfMovingItem, AjaxRequestTarget target) {
                if (component == null) {
                    return;
                }
                int oldIndexOfMovingItem = getIndexOfComponent(component);
                updateSelectedIndex(oldIndexOfMovingItem, newIndexOfMovingItem);
                onItemMoving(oldIndexOfMovingItem, newIndexOfMovingItem, target);
            }
        };
        sortable.getSortableBehavior().setContainment(new SortableContainment(containmentCss));
        sortable.getSortableBehavior().setAxis(SortableBehavior.AxisEnum.Y);
        sortable.setDisabled(true);
        return sortable;
    }

    protected abstract int getIndexOfComponent(Component component);

    protected abstract void onItemMoving(int oldIndex, int newIndex, AjaxRequestTarget target);

}

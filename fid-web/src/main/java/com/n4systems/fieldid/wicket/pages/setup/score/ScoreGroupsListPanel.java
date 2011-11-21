package com.n4systems.fieldid.wicket.pages.setup.score;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.eventform.EditCopyDeleteItemPanel;
import com.n4systems.fieldid.wicket.components.score.ScoreGroupCopyUtil;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ScoreGroupsListPanel extends Panel {

    private int currentlySelectedIndex = -1;

    @SpringBean
    private PersistenceService persistenceService;

    public ScoreGroupsListPanel(String id, final IModel<List<ScoreGroup>> scoreGroupsModel) {
        super(id, scoreGroupsModel);
        setOutputMarkupId(true);

        add(new ListView<ScoreGroup>("scoreGroups", scoreGroupsModel) {
            @Override
            protected void populateItem(final ListItem<ScoreGroup> item) {
                item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("editCopyDeletePanel", new PropertyModel<String>(item.getModel(), "name")) {
                    {
                        setStoreLabel(new FIDLabelModel("label.save"));
                        setEditMaximumLength(1024);
                    }

                    @Override
                    protected void onViewLinkClicked(AjaxRequestTarget target) {
                        currentlySelectedIndex = item.getIndex();
                        target.addComponent(ScoreGroupsListPanel.this);
                        onScoreGroupSelected(item.getModel(), target);
                    }

                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        persistenceService.archive(item.getModelObject());
                        if (item.getIndex() == currentlySelectedIndex) {
                            onScoreGroupSelected(new Model<ScoreGroup>(null), target);
                            currentlySelectedIndex = -1;
                        } else if (currentlySelectedIndex != -1 && item.getIndex() < currentlySelectedIndex) {
                            currentlySelectedIndex = currentlySelectedIndex - 1;
                        }
                        scoreGroupsModel.detach();
                        target.addComponent(ScoreGroupsListPanel.this);
                    }

                    @Override
                    protected void onCopyLinkClicked(AjaxRequestTarget target) {
                        ScoreGroup copiedGroup = new ScoreGroupCopyUtil().copy(item.getModelObject());
                        persistenceService.save(copiedGroup);
                        scoreGroupsModel.detach();
                        target.addComponent(ScoreGroupsListPanel.this);
                    }

                    @Override
                    protected void onStoreLinkClicked(AjaxRequestTarget target) {
                        persistenceService.update(item.getModelObject());
                    }

                    @Override
                    protected void onFormValidationError(AjaxRequestTarget target) {
                        onValidationError(target);
                    }
                });
                item.add(new AppendToClassIfCondition("selectedGroup", new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return item.getIndex() == currentlySelectedIndex ;
                    }
                }));
            }
        });
    }

    protected IModel<List<ScoreGroup>> getListModel() {
        return (IModel<List<ScoreGroup>>) getDefaultModel();
    }

    protected void onScoreGroupSelected(IModel<ScoreGroup> model, AjaxRequestTarget target) { }
    protected void onValidationError(AjaxRequestTarget target) { }

}

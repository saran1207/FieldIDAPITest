package com.n4systems.fieldid.wicket.components.location;

import com.n4systems.fieldid.viewhelpers.LocationHelper;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class PredefinedLocationsPanel extends Panel {

    private Long selectedPredefinedLocationId = -1L;

    private List<HierarchicalNode> expandedLevels = new ArrayList<HierarchicalNode>();
    private List<HierarchicalNode> selectedLevels = new ArrayList<HierarchicalNode>();

    public PredefinedLocationsPanel(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);

        List<HierarchicalNode> predefinedLocationTree = new LocationHelper(new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()), new PersistenceManagerTransactor()).getPredefinedLocationTree();

        HierarchicalNode artificialRootNode = new HierarchicalNode();
        artificialRootNode.addChild(createNoneChoice(predefinedLocationTree));
        artificialRootNode.addChildren(predefinedLocationTree);

        expandedLevels.add(artificialRootNode);
        
        ListView<HierarchicalNode> levelList = new ListView<HierarchicalNode>("levelList", new PropertyModel<List<HierarchicalNode>>(this, "expandedLevels")) {
            @Override
            protected void populateItem(ListItem item) {
                int levelIndex = item.getIndex();

                PropertyModel<List<HierarchicalNode>> childrenModel = new PropertyModel<List<HierarchicalNode>>(item.getModel(), "children");

                item.add(new FlatLabel("levelLabel", new TitleForLocationLevelModel(childrenModel)));
                item.add(createNodeList(childrenModel, levelIndex));

                if (item.getIndex() > 0) {
                    item.add(createAbsolutePositionModifier(levelIndex));
                }
            }
        };

        add(levelList);
    }

    private HierarchicalNode createNoneChoice(List<HierarchicalNode> predefinedLocationTree) {
        HierarchicalNode noneChoice = new HierarchicalNode();
        noneChoice.setName("None");
        noneChoice.setId(-1L);
        if (!predefinedLocationTree.isEmpty()) {
            noneChoice.setLevelName(predefinedLocationTree.get(0).getLevelName());
        }
        return noneChoice;
    }

    private ListView<HierarchicalNode> createNodeList(final IModel<List<HierarchicalNode>> locationsModel, final int levelIndex) {
        return new ListView<HierarchicalNode>("nodeList", locationsModel) {
            @Override
            protected void populateItem(final ListItem<HierarchicalNode> item) {
                AjaxLink clickLink = new AjaxLink("nodeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        selectedPredefinedLocationId = item.getModelObject().getId();
                        target.add(PredefinedLocationsPanel.this);

                        for (int i = expandedLevels.size() - 1; i >= Math.max(1, levelIndex + 1); i--) {
                            expandedLevels.remove(i);
                        }

                        selectedLevels.clear();
                        for (int i = 1; i < expandedLevels.size(); i++) {
                            selectedLevels.add(expandedLevels.get(i));
                        }

                        if (!item.getModelObject().isLeaf()) {
                            expandedLevels.add(item.getModelObject());
                        }

                        selectedLevels.add(item.getModelObject());

                        target.appendJavaScript("$('"+ PredefinedLocationsPanel.this.getMarkupId()+"').scrollLeft = 10000;");
                    }
                };
                clickLink.add(createHighlightSelectedModifier(item, levelIndex));
                clickLink.add(createArrowIfHasChildrenModifier(item));
                clickLink.add(new FlatLabel("nodeLinkLabel", new PropertyModel<String>(item.getModel(), "name")));
                clickLink.add(createArrowImage(item));

                item.setRenderBodyOnly(true);

                item.add(clickLink);
            }
        };
    }

    private ContextImage createArrowImage(final ListItem<HierarchicalNode> item) {
        return new ContextImage("arrowImage", "images/tree-arrow.png") {
            @Override
            public boolean isVisible() {
                return !item.getModelObject().isLeaf();
            }
        };
    }

    private AttributeAppender createArrowIfHasChildrenModifier(final ListItem<HierarchicalNode> item) {
        return new AttributeAppender("class", new Model<String>("hasChildMenu"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                return !item.getModelObject().isLeaf();
            }
        };
    }

    private AttributeAppender createHighlightSelectedModifier(final ListItem<HierarchicalNode> item, final int levelIndex) {
        return new AttributeAppender("class", new Model<String>("active"), " ") {
            @Override
            public boolean isEnabled(Component component) {
                // Must add one to index to account for artificial root level.
                if (levelIndex >= selectedLevels.size()) {
                    return false;
                }
                return selectedLevels.get(levelIndex).getId().equals(item.getModelObject().getId());
            }
        };
    }

    private AttributeAppender createAbsolutePositionModifier(final int levelIndex) {
        return new AttributeAppender("style", new IModel<String>() {
            @Override
            public String getObject() {
                return "top: 0px; left: " +(levelIndex*200) + "px";
            }

            @Override
            public void setObject(String object) {
            }

            @Override
            public void detach() {
            }
        }, " ");
    }

    public Long getSelectedPredefinedLocationId() {
        return selectedPredefinedLocationId;
    }


}

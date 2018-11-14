package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.service.asset.AutoAttributeService;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.List;

/**
 * Created by agrabovskis on 2018-11-12.
 */
public class AutoAttributeDefinitionsListPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeDefinitionsListPanel.class);

    @SpringBean
    private AutoAttributeService autoAttributeService;

    private IModel<Long> autoAttributeCriteriaProvidedIdModel;

    private LoadableDetachableModel<AutoAttributeCriteria> autoAttributeCriteriaModel;
    private LoadableDetachableModel<List<AutoAttributeDefinition>> autoAttributeDefinitionModel;
    private WebMarkupContainer resultsPanel;
    private WebMarkupContainer noResultsPanel;

    public AutoAttributeDefinitionsListPanel(String id, IModel<Long> autoAttributeCriteriaProvidedIdModel) {
        super(id);
        this.autoAttributeCriteriaProvidedIdModel = autoAttributeCriteriaProvidedIdModel;
        createDataModels();
        addComponents();
    }

    private void addComponents() {

        /* Create the result section, hidden if no results were found */
        resultsPanel = new WebMarkupContainer("resultList") {
            @Override
            public boolean isVisible() {
                return showResultSection();
            }
        };
        resultsPanel.setOutputMarkupId(true);
        resultsPanel.setOutputMarkupPlaceholderTag(true);

        final DataView<InfoFieldBean> criteriaInputsList =
                new DataView<InfoFieldBean>("criteriaList", new ListDataProvider<InfoFieldBean>() {
                    @Override
                    protected List<InfoFieldBean> getData() {
                        return autoAttributeCriteriaModel.getObject().getInputs();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<InfoFieldBean> item) {
                        item.add(new Label("inputName", item.getModelObject().getName()));
                    }
                };
        resultsPanel.add(criteriaInputsList);

        final DataView<AutoAttributeDefinition> definitionList =
                new DataView<AutoAttributeDefinition>("definitionList", new ListDataProvider<AutoAttributeDefinition>() {
                    @Override
                    protected List<AutoAttributeDefinition> getData() {
                        return autoAttributeDefinitionModel.getObject();
                    }
                }) {
                    @Override
                    protected void populateItem(Item<AutoAttributeDefinition> item) {
                        final AutoAttributeDefinition definition = item.getModelObject();
                        final DataView<InfoOptionBean> definitionInputList =
                                new DataView<InfoOptionBean>("definitionInputList", new ListDataProvider<InfoOptionBean>() {
                                    @Override
                                    protected List<InfoOptionBean> getData() {
                                        return definition.getInputs();
                                    }
                                }) {
                                    @Override
                                    protected void populateItem(Item<InfoOptionBean> item) {
                                        item.add(new Label("inputName", item.getModelObject().getName()));
                                    }
                                };
                        item.add(definitionInputList);
                        Link autoAttributeDefinitionEditLink = new Link("autoAttributeDefinitionEdit") {
                            @Override
                            public void onClick() {
                                // templates/html/autoAttributeDefinition/form.ftl
                                System.out.println("Edit definition link clicked");
                            }
                        };
                        item.add(autoAttributeDefinitionEditLink);

                        Link autoAttributeDefinitionRemoveLink = new Link("autoAttributeDefinitionRemove") {
                            @Override
                            public void onClick() {
                                System.out.println("Remove definition link clicked");
                                try {
                                    //autoAttributeManager.removeDefinition( autoAttributeDefinition );
                                    logger.info("AutoAttributesDefinition ");
                                    Session.get().info("message.definition_removed");
                                } catch (Exception e) {
                                    logger.error("Removal of AutoAttributesDefinition failed", e);
                                    Session.get().error("error.remove_definition_failed");
                                }
                            }
                        };
                        item.add(autoAttributeDefinitionRemoveLink);
                    }
                };


        resultsPanel.add(definitionList);

        /* Create the section to display a message if no result */
        noResultsPanel = new WebMarkupContainer("emptyListResult"){
            @Override
            public boolean isVisible() {
                return !showResultSection();
            }
        };
        noResultsPanel.setOutputMarkupId(true);
        noResultsPanel.setOutputMarkupPlaceholderTag(true);

        add(resultsPanel);
        add(noResultsPanel);
    }

    protected boolean showResultSection() {
        return autoAttributeCriteriaModel.getObject().getInputs().size() > 0 &&
                autoAttributeDefinitionModel.getObject().size() > 0;
    }

    private void createDataModels() {

        autoAttributeCriteriaModel = new LoadableDetachableModel<AutoAttributeCriteria>() {

            protected AutoAttributeCriteria load() {
                return autoAttributeService.getAutoAttributeCriteriaWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
            }
        };

        autoAttributeDefinitionModel = new LoadableDetachableModel<List<AutoAttributeDefinition>>() {
            @Override
            protected List<AutoAttributeDefinition> load() {
                return autoAttributeService.getAutoAttributeDefinitionsWithPostFetches(
                        autoAttributeCriteriaProvidedIdModel.getObject());
            }
        };
    }
}

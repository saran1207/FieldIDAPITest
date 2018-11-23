package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.asset.AutoAttributeService;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.security.Permissions;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.List;


/**
 * Created by agrabovskis on 2018-11-08.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
public class AutoAttributeDefinitionsPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeDefinitionsPanel.class);

    @SpringBean
    private AutoAttributeService autoAttributeService;

    private IModel<Long> autoAttributeCriteriaProvidedIdModel;
    private IModel<Long> autoAttributeDefinitionIdModel;

    private IModel<SessionUser> sessionUserModel;
    private boolean displayingViewAllDefinitions;

    private WebMarkupContainer definitionsPanels;
    private WebMarkupContainer buttonsContainer;
    private AutoAttributeDefinitionsListPanel definitionsListPanel;
    private AutoAttributeDefinitionAddPanel definitionAddPanel;

    public AutoAttributeDefinitionsPanel(String id, IModel<Long> autoAttributeCriteriaProvidedIdModel,
                                         IModel<SessionUser> sessionUserModel) {
        super(id);
        displayingViewAllDefinitions = true;
        autoAttributeDefinitionIdModel = new Model(null);
        this.autoAttributeCriteriaProvidedIdModel = autoAttributeCriteriaProvidedIdModel;
        this.sessionUserModel = sessionUserModel;
        addComponents();
    }

    @Override
    protected void onBeforeRender() {
        System.out.println("AutoAttributeDefinitionsPanel.onBeforeRender");
        //autoAttributeCriteriaModel.detach(); // force refresh of criteria
        super.onBeforeRender();
    }

    public void handleSelectionChange() {
        System.out.println("DefinitionsPanel.handleSelectionChange");
        definitionsListPanel.handleSelectionChange();
        definitionAddPanel.handleSelectionChange();
    }

    private void addComponents() {

        definitionsPanels = new WebMarkupContainer("definitionsPanels");
        definitionsPanels.setOutputMarkupId(true);
        definitionsPanels.setOutputMarkupPlaceholderTag(true);
        add(definitionsPanels);

        buttonsContainer = new WebMarkupContainer("buttonsContainer");
        buttonsContainer.setOutputMarkupId(true);
        add(buttonsContainer);

        AjaxLink viewDefinitions = new AjaxLink("viewDefinitions") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("clicked viewDefinitions");
                handleSwitchBackToListPanel(target);
            }

            @Override
            public boolean isVisible() {
                return !displayingViewAllDefinitions;
            }
        };
        viewDefinitions.setOutputMarkupId(true);
        buttonsContainer.add(viewDefinitions);

        AjaxLink addDefinition = new AjaxLink("addDefinition") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("Clicked addDefinition");
                displayingViewAllDefinitions = false;
                target.add(buttonsContainer);
                target.add(definitionsPanels);
            }
            @Override
            public boolean isVisible() {
                return displayingViewAllDefinitions;
            }
        };
        addDefinition.setOutputMarkupId(true);
        buttonsContainer.add(addDefinition);

        definitionsListPanel = new AutoAttributeDefinitionsListPanel(
                "definitionsListPanel",
                autoAttributeCriteriaProvidedIdModel,
                autoAttributeDefinitionIdModel) {

            @Override
            protected void editActionInvoked(AjaxRequestTarget target) {
                displayingViewAllDefinitions = false;
                target.add(buttonsContainer);
                target.add(definitionsPanels);
            }

            @Override
            public boolean isVisible() {
                return displayingViewAllDefinitions;
            }
        };
        definitionsListPanel.setOutputMarkupPlaceholderTag(true);
        definitionsListPanel.setOutputMarkupId(true);
        definitionsPanels.add(definitionsListPanel);

        definitionAddPanel = new AutoAttributeDefinitionAddPanel(
                "definitionAddPanel",
                autoAttributeCriteriaProvidedIdModel,
                autoAttributeDefinitionIdModel,
                sessionUserModel) {
            @Override
            public boolean isVisible() {
                return !displayingViewAllDefinitions;
            }

            @Override
            protected void addActionCompleted(AjaxRequestTarget target) {
                handleSwitchBackToListPanel(target);
            }

            @Override
            protected void cancelActionCompleted(AjaxRequestTarget target) {
                handleSwitchBackToListPanel(target);
            }
        };
        definitionAddPanel.setOutputMarkupPlaceholderTag(true);
        definitionsListPanel.setOutputMarkupId(true);
        definitionsPanels.add(definitionAddPanel);
    }

    private void handleSwitchBackToListPanel(AjaxRequestTarget target) {
        displayingViewAllDefinitions = true;
        definitionsListPanel.handleSelectionChange();
        definitionAddPanel.handleSelectionChange();
        target.add(buttonsContainer);
        target.add(definitionsPanels);
    }

}

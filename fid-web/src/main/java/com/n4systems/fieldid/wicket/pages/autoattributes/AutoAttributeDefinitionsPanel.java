package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;


/**
 * Created by agrabovskis on 2018-11-08.
 */
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
public class AutoAttributeDefinitionsPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeDefinitionsPanel.class);

    private IModel<Long> autoAttributeCriteriaProvidedIdModel;
    private IModel<SessionUser> sessionUserModel;
    private boolean displayingViewAllDefinitions;

    public AutoAttributeDefinitionsPanel(String id, IModel<Long> autoAttributeCriteriaProvidedIdModel,
                                         IModel<SessionUser> sessionUserModel) {
        super(id);
        displayingViewAllDefinitions = true;
        this.autoAttributeCriteriaProvidedIdModel = autoAttributeCriteriaProvidedIdModel;
        this.sessionUserModel = sessionUserModel;
        addComponents();
    }

    private void addComponents() {

        WebMarkupContainer definitionsPanels = new WebMarkupContainer("definitionsPanels");
        definitionsPanels.setOutputMarkupId(true);
        definitionsPanels.setOutputMarkupPlaceholderTag(true);
        add(definitionsPanels);

        WebMarkupContainer buttonsContainer = new WebMarkupContainer("buttonsContainer");
        buttonsContainer.setOutputMarkupId(true);
        add(buttonsContainer);

        AjaxLink viewDefinitions = new AjaxLink("viewDefinitions") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("clicked viewDefinitions");
                displayingViewAllDefinitions = true;
                target.add(buttonsContainer);
                target.add(definitionsPanels);
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

        Panel definitionsListPanel = new AutoAttributeDefinitionsListPanel("definitionsListPanel", autoAttributeCriteriaProvidedIdModel) {
            @Override
            public boolean isVisible() {
                return displayingViewAllDefinitions;
            }
        };
        definitionsListPanel.setOutputMarkupPlaceholderTag(true);
        definitionsListPanel.setOutputMarkupId(true);
        definitionsPanels.add(definitionsListPanel);

        Panel definitionAddPanel = new AutoAttributeDefinitionAddPanel("definitionAddPanel", autoAttributeCriteriaProvidedIdModel, sessionUserModel) {
            @Override
            public boolean isVisible() {
                return !displayingViewAllDefinitions;
            }
        };
        definitionAddPanel.setOutputMarkupPlaceholderTag(true);
        definitionsListPanel.setOutputMarkupId(true);
        definitionsPanels.add(definitionAddPanel);
    }

}

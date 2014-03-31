package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.components.CloseImagePanel;
import com.n4systems.fieldid.wicket.components.SimplePanel;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.List;

public class OrgPicker extends Panel {

    private IModel<BaseOrg> orgModel;
    private WebMarkupContainer orgPickerContainer;
    private AjaxLink chooseLink;

    private BrowsePanel browsePanel;
    private SearchPanel searchPanel;

    // A selector for the relative container that the picker is contained in.
    // This is necessary for the modal windows where the translate function doesn't position
    // the orgpicker properly and translateWithin is required.
    private String translateContainerSelector;

    public OrgPicker(String id, final IModel<BaseOrg> orgModel) {
        super(id, orgModel);

        setOutputMarkupPlaceholderTag(true);

        this.orgModel = orgModel;

        WebMarkupContainer orgNameDisplay = new WebMarkupContainer("orgNameInput");
        orgNameDisplay.add(new AttributeModifier("value", true, new PropertyModel<String>(orgModel, "displayName")));
        add(orgNameDisplay);

        add(orgPickerContainer = new WebMarkupContainer("orgPickerContainer"));
        orgPickerContainer.setVisible(false);
        orgPickerContainer.setOutputMarkupPlaceholderTag(true);

        addClearAndCloseLinks();

        final List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTab(new StringResourceModel("label.browse", this, null)) {
            @Override
            public Panel getPanel(String panelId) {
                browsePanel = new BrowsePanel(panelId, orgModel) {
                    @Override
                    protected void onOrgSelected(AjaxRequestTarget target) {
                        closePicker(target);
                        OrgPicker.this.onOrgSelected(target);
                    }

                    @Override
                    protected void onCancelClicked(AjaxRequestTarget target) {
                        closePicker(target);
                    }
                };
                return browsePanel;
            }
        });
        tabs.add(new AbstractTab(new StringResourceModel("label.search", this, null)) {
            @Override
            public Panel getPanel(String panelId) {
                searchPanel = new SearchPanel(panelId, orgModel) {
                    @Override
                    protected void onOrgSelected(AjaxRequestTarget target) {
                        closePicker(target);
                        OrgPicker.this.onOrgSelected(target);
                    }
                };
                return searchPanel;
            }
        });
        tabs.add(new AbstractTab(new Model<String>("CLOSE")) {
            @Override
            public Panel getPanel(String panelId) {
                return new SimplePanel(panelId);
            }
        });

        AjaxTabbedPanel tabbedPanel = new AjaxTabbedPanel("tabbedPanel", tabs) {
            @Override
            protected String getTabContainerCssClass() {
                return "selections";
            }

            @Override
            protected Component newTitle(String titleId, IModel<?> titleModel, int index) {
                if ("CLOSE".equals(titleModel.getObject())) {
                    return new CloseImagePanel(titleId);
                }
                Component title = super.newTitle(titleId, titleModel, index);
                title.setRenderBodyOnly(true);
                return title;
            }

            @Override
            protected WebMarkupContainer newLink(String linkId, int index) {
                if (index + 1 == tabs.size()) {
                    return new AjaxLink(linkId) {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            closePicker(target);
                        }
                    };
                }
                return super.newLink(linkId, index);
            }
        };
        orgPickerContainer.add(tabbedPanel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/featureStyles/orgPickerNew.css");
    }

    private void addClearAndCloseLinks() {
        add(new AjaxLink("clearLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                cancelPicker(target);
            }
		
        });

        add(chooseLink = new AjaxLink("chooseLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                orgPickerContainer.setVisible(true);
                target.add(OrgPicker.this);
                if (translateContainerSelector != null) {
                    target.appendJavaScript("translateWithin($('#"+orgPickerContainer.getMarkupId()+"'), $('#"+chooseLink.getMarkupId()+"'), $('"+translateContainerSelector+"'), 0, 0);");
                }
            }
        });
        chooseLink.setOutputMarkupPlaceholderTag(true);
    }

    protected void closePicker(AjaxRequestTarget target) {
        orgPickerContainer.setVisible(false);
        target.add(OrgPicker.this);
        onPickerClosed(target);
    }
    
	protected void cancelPicker(AjaxRequestTarget target) {
		orgModel.setObject(null);
        if (browsePanel != null) {
            browsePanel.clearSelections();
        }
        target.add(OrgPicker.this);
	}

    protected void onPickerClosed(AjaxRequestTarget target) {}
    protected void onOrgSelected(AjaxRequestTarget target) {}

    public void setTranslateContainerSelector(String selector) {
        this.translateContainerSelector = selector;
    }

}

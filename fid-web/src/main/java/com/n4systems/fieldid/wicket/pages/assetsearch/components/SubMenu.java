package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import java.util.List;


public abstract class SubMenu<T extends SearchCriteria> extends Panel {

	public static final String HIDE_JS = "fieldIdWidePage.hideLeftMenu()";
	public static final String SHOW_JS = "fieldIdWidePage.showLeftMenu()";

	protected static final String FILTERS_ID = "filters";
	protected static final String COLUMNS_ID = "columns";

    private static final String COLORBOX_CLASS = "colorboxLink";

    private List<Link> lightBoxLinks = Lists.newArrayList();

    protected Model<T> model;
    protected Integer maxUpdate;
    private Integer maxSchedule;
    protected Integer maxPrint;
    protected Integer maxExport;
    private Integer maxEvent;
    protected Label msg;
    private Link saveAsLink;
    private Link saveLink;
    private WebMarkupContainer saveAsMenu;
    protected boolean filtersDisabled;
    private WebMarkupContainer buttonsContainer;


    public SubMenu(String id, final Model<T> model) {
		super(id);
		this.model = model;
		
        add(createHeader());

        add(buttonsContainer = new WebMarkupContainer("buttonsContainer"));
        buttonsContainer.setOutputMarkupPlaceholderTag(true);
        addMattBar();

        add(msg = new Label("msg", new StringResourceModel(getNoneSelectedMsgKey(), this, null)));
		add(new AttributeAppender("class", "sub-menu clearfix"));

        setOutputMarkupId(true);
	}

    protected void addMattBar() {
        MattBar mattBar = createMattBar();
        buttonsContainer.addOrReplace(mattBar);
        mattBar.addLink(new Model<>(""), COLUMNS_ID, "images/col.png", "label.tooltip_columns");
        if (!filtersDisabled) {
            mattBar.addLink(new Model<>(""), FILTERS_ID, "images/filter.png", "label.tooltip_filters");
            mattBar.setCurrentState(COLUMNS_ID);
        } else {
            mattBar.setCurrentState(FILTERS_ID);
        }
    }

    protected void setFiltersDisabled(AjaxRequestTarget target, boolean disabled) {
        filtersDisabled = disabled;
        addMattBar();
        target.add(buttonsContainer);
    }

    private Label createHeader() {
        return new Label("header", getHeaderModel());
    }

    protected IModel<String> getHeaderModel() { return new Model<>("title of saveditem"); }
    

    private MattBar createMattBar() {
        return new MattBar("columnsOrFiltersButtons") {
            @Override
            protected void onEnterState(AjaxRequestTarget target, Object state) {
                target.appendJavaScript("fieldIdWidePage.showConfig(" + FILTERS_ID.equals(state) + ");");
            }
        }.setCurrentState(1);
    }

    protected abstract String getNoneSelectedMsgKey();

    protected void initializeLimits() {
        Long tenantId = FieldIDSession.get().getSessionUser().getTenant().getId();
        maxUpdate = ConfigService.getInstance().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxExport = ConfigService.getInstance().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxPrint = ConfigService.getInstance().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxEvent = ConfigService.getInstance().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxSchedule = ConfigService.getInstance().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
    }

    protected <T extends Link> T makeLinkLightBoxed(T link) {
        link.setOutputMarkupId(true);
        link.add(new AttributeAppender("class", new Model<>(COLORBOX_CLASS), " "));
        lightBoxLinks.add(link);
        return link;
    }

    @Override
    protected void onBeforeRender() {
        updateMenuBeforeRender(model.getObject());
        super.onBeforeRender();
    }

    protected void updateMenuBeforeRender(T criteria) {
        int selected = criteria.getSelection().getNumSelectedIds();
        msg.setVisible(selected==0);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnLoadJavaScript("subMenu.init();");
        if (!lightBoxLinks.isEmpty()) {
            response.renderOnLoadJavaScript("jQuery('."+COLORBOX_CLASS+"').colorbox({maxHeight: '600px', width: '600px', height:'360px', ajax: true, iframe: true});");
        }
        super.renderHead(response);
    }

    protected abstract Link createSaveAsLink(String saveAs);

    protected abstract Link createSaveLink(String save);
}

package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.ConfigContext;
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


    public SubMenu(String id, final Model<T> model) {
		super(id);
		this.model = model;
		
        add(createHeader());
        
        MattBar mattBar = createMattBar();
        add(mattBar);
        mattBar.addLink(new Model<String>(""), COLUMNS_ID, "images/col.png", "label.tooltip_columns");
        mattBar.addLink(new Model<String>(""), FILTERS_ID, "images/filter.png", "label.tooltip_filters");
        mattBar.setCurrentState(FILTERS_ID);

        add(msg = new Label("msg", new StringResourceModel(getNoneSelectedMsgKey(), this, null)));
		add(new AttributeAppender("class", "sub-menu"));

        setOutputMarkupId(true);
	}

    private Label createHeader() {
        return new Label("header", getHeaderModel());
    }

    protected IModel<String> getHeaderModel() { return new Model<String>("title of saveditem"); }
    

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
        maxUpdate = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxExport = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxPrint = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxEvent = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
        maxSchedule = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, tenantId);
    }

    protected <T extends Link> T makeLinkLightBoxed(T link) {
        link.setOutputMarkupId(true);
        link.add(new AttributeAppender("class", new Model<String>(COLORBOX_CLASS), " "));
        lightBoxLinks.add(link);
        return link;
    }

    @Override
    protected void onBeforeRender() {
        int selected = model.getObject().getSelection().getNumSelectedIds();
        updateMenuBeforeRender(selected);
        super.onBeforeRender();
    }

    protected void updateMenuBeforeRender(int selected) {
        msg.setVisible(selected == 0);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnLoadJavaScript("subMenu.init();");
        if (!lightBoxLinks.isEmpty()) {
            response.renderOnLoadJavaScript("jQuery('."+COLORBOX_CLASS+"').colorbox({ ajax:true });");
        }
        super.renderHead(response);
    }

    protected abstract Link createSaveAsLink(String saveAs);

    protected abstract Link createSaveLink(String save);
}

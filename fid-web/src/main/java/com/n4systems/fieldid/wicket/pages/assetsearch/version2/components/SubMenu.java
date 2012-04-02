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
        mattBar.addLinkWithImage(new Model<String>(""), COLUMNS_ID, "images/col.png");
        mattBar.addLinkWithImage(new Model<String>(""), FILTERS_ID, "images/filter.png");
        mattBar.setCurrentState(COLUMNS_ID);
        add(mattBar);
        
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
        };
    }

    protected abstract String getNoneSelectedMsgKey();

    protected void initializeLimits() {
        // XXX : the actions limits here should roll into one single "maxMassActionLimit"???    ask matt.
        Long tenantId = FieldIDSession.get().getSessionUser().getTenant().getId();
        maxUpdate = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_UPDATE, tenantId);
        maxExport = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_EXCEL_EXPORT, tenantId);
        maxPrint = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_PDF_PRINT_OUTS, tenantId);
        maxEvent = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, tenantId);
        maxSchedule = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_SCHEDULE, tenantId);
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
        if (!lightBoxLinks.isEmpty()) {
            response.renderOnLoadJavaScript("jQuery('."+COLORBOX_CLASS+"').colorbox({ ajax:true });");
        }
        super.renderHead(response);
    }

    protected WebMarkupContainer createSaveMenu(String saveMenu) {
        WebMarkupContainer menu;
        add(menu = new WebMarkupContainer("saveMenu"));
        saveLink = createSaveLink("save");
        menu.add(saveLink);
        saveAsLink = createSaveAsLink("saveAs");
        menu.add(saveAsLink);
        add(menu);
        
        saveAsMenu = new WebMarkupContainer("saveAsMenu");
        saveAsMenu.add(new AttributeAppender("class", new Model<String>(getSaveAsCssClass())));
        menu.add(saveAsMenu);

        saveLink.add(new AttributeAppender("class", new Model<String>(getSaveLinkCssClass())) );
        return menu;
    }

    private String getSaveAsCssClass() {
        return saveAsLink.isVisible() ? " " : " hide";
    }

    private String getSaveLinkCssClass() {
        return saveAsLink.isVisible() ? " mattBarLeft" : " ";
    }

    protected abstract Link createSaveAsLink(String saveAs);

    protected abstract Link createSaveLink(String save);
}

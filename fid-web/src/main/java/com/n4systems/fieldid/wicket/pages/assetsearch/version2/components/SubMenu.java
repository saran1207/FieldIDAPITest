package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import java.util.List;


public abstract class SubMenu<T extends SearchCriteria> extends Panel {

	public static final String HIDE_JS = "fieldIdWidePage.hideLeftMenu()";
	public static final String SHOW_JS = "fieldIdWidePage.showLeftMenu()";

	protected static final String FILTERS_ID = "filters";
	protected static final String COLUMNS_ID = "columns";

    private static final String COLORBOX_CLASS = "colorboxLink";

    private List<Link> lightBoxLinks = Lists.newArrayList();

    protected Model<T> model;
    private SubMenuLink columns;
    private SubMenuLink filters;
    protected Integer maxUpdate;
    private Integer maxSchedule;
    protected Integer maxPrint;
    protected Integer maxExport;
    private Integer maxEvent;
    protected Label msg;



    public SubMenu(String id, final Model<T> model) {
		super(id);
		this.model = model;
		add(columns = new SubMenuLink(COLUMNS_ID));
		add(filters = new SubMenuLink(FILTERS_ID));
        add(msg = new Label("msg", new StringResourceModel(getNoneSelectedMsgKey(), this, null)));
		add(new AttributeAppender("class", "sub-menu"));
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

    //  ------------------------------------------------------------------------------------

    class SubMenuLink extends WebMarkupContainer  {

		public SubMenuLink(final String id) {
			super(id);
			add(createToggleBehavior(FILTERS_ID.equals(id)));
		}

		 private Behavior createToggleBehavior(final boolean showFilters) {
				return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					@Override public JsScope callback() {
						return JsScopeUiEvent.quickScope("fieldIdWidePage.showConfig("+showFilters+");");
					}
				});
			}
	}
}

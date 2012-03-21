package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;


public abstract class SubMenu<T extends SearchCriteria> extends Panel {

	public static final String HIDE_JS = "fieldIdWidePage.hideLeftMenu()";
	public static final String SHOW_JS = "fieldIdWidePage.showLeftMenu()";

	protected static final String FILTERS_ID = "filters";
	protected static final String COLUMNS_ID = "columns";

    protected Model<T> model;
	private SubMenuLink columns;
	private SubMenuLink filters;


	public SubMenu(String id, final Model<T> model) {
		super(id);
		this.model = model;
		add(columns = new SubMenuLink(COLUMNS_ID));
		add(filters = new SubMenuLink(FILTERS_ID));
		add(new AttributeAppender("class", "sub-menu"));
	}

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

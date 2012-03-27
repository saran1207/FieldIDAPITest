package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;


public abstract class AbstractCriteriaPanel<T extends SearchCriteria> extends Panel {

    private Model<T> model;
    protected AbstractColumnsPanel columns;
    protected Panel filters;


    public AbstractCriteriaPanel(String id, final Model<T> model) {
		super(id, new CompoundPropertyModel<T>(model));
        this.model = model;
        setOutputMarkupId(true);

		SearchConfigForm form = new SearchConfigForm("form",model);
        form.add(new SubmitLink("submit"));
		form.add(new WebMarkupContainer("close").add(createCloseBehavior()));        

		form.add(columns = createColumnsPanel("columns", model));
		form.add(filters = createFiltersPanel("filters", model));

		add(form);		
	}

    protected abstract Panel createFiltersPanel(String filters, final Model<T> model);

    protected abstract AbstractColumnsPanel createColumnsPanel(String columns, Model<T> model);

    protected void onNoDisplayColumnsSelected() { }
	
	protected void onSearchSubmit() { }
	
	protected Behavior createCloseBehavior() {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope(SubMenu.HIDE_JS);
			}
		});
	}	

	 public class SearchConfigForm extends Form<T> {

      	public SearchConfigForm(String id, final IModel<T> model) {
        	super(id, model);
          	setOutputMarkupId(true); 
      	}
      	
      	@Override
      	protected void onSubmit() {
            // XXX : put this in form validator.
//			 if (model.getObject().getSortedStaticAndDynamicColumns().isEmpty()) {
//                error(new FIDLabelModel("error.nocolumnsselected").getObject());
//                onNoDisplayColumnsSelected();
//                return;
//	         }

			 model.getObject().setReportAlreadyRun(true);
			 model.getObject().getSelection().clear();
			 onSearchSubmit();
      	}
      	
	 }	
	      	
	
}

package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.LeftPanelController;
import com.n4systems.fieldid.wicket.pages.HasLeftPanelController;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.io.Serializable;


public abstract class AbstractCriteriaPanel<T extends SearchCriteria> extends Panel implements HasLeftPanelController {

    private Model<T> model;
    protected AbstractColumnsPanel columns;
    protected Panel filters;
    private FIDFeedbackPanel feedBack;


    public AbstractCriteriaPanel(String id, final Model<T> model) {
		super(id, new CompoundPropertyModel<T>(model));
        this.model = model;
        setOutputMarkupId(true);

		SearchConfigForm form = new SearchConfigForm("form",model);
        form.add(new SubmitLink("submit"));

		form.add(columns = createColumnsPanel("columns", model));
		form.add(filters = createFiltersPanel("filters", model));        
        form.add(new AtLeastOneColumnSelectedValidator());
        form.add(feedBack = new FIDFeedbackPanel("feedback"));
        add(new AttributeAppender("class", " filters")); // default mode is show filters, not columns.

		add(form);
	}

    protected abstract Panel createFiltersPanel(String filters, final Model<T> model);

    protected abstract AbstractColumnsPanel createColumnsPanel(String columns, Model<T> model);

	protected void onSearchSubmit() { }

    protected void onSearchError() { }
	
    @Override
    public Component getLeftPanelController(String id) {
        return new LeftPanelController(id);
    }

    public class SearchConfigForm extends Form<T> {

      	public SearchConfigForm(String id, final IModel<T> model) {
        	super(id, model);
          	setOutputMarkupId(true); 
      	}

        @Override
        protected void onError() {
            super.onError();
            onSearchError();
        }

        @Override
      	protected void onSubmit() {
			 model.getObject().setReportAlreadyRun(true);
			 model.getObject().getSelection().clear();
			 onSearchSubmit();
      	}
      	
	 }

    private class AtLeastOneColumnSelectedValidator extends AbstractFormValidator implements Serializable {

        private CheckBoxSelectionVisitor visitor;

        public AtLeastOneColumnSelectedValidator() {
            super();
            visitor = new CheckBoxSelectionVisitor();
        }

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent<?>[0];  
        }

        @Override
        public void validate(Form<?> form) {
            form.visitChildren(CheckBox.class,visitor);
            if (!visitor.detectedSelection()) {
                form.error(getString("label.select.column.validation"));
            }
        }

        class CheckBoxSelectionVisitor implements IVisitor<CheckBox, Void>, Serializable {

            private boolean anySelected;

            public CheckBoxSelectionVisitor() {
                this.anySelected = false;
            }
            @Override public void component(CheckBox checkBox, IVisit<Void> visit) {
                if (checkBox.getValue()!=null) {
                    anySelected = true;
                    visit.stop();
                }
            }

            public boolean detectedSelection() {
                return anySelected;
            }
        }
    }

}

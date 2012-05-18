package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.event.criteria.factory.CriteriaEditorFactory;
import com.n4systems.fieldid.wicket.components.event.observations.DeficienciesEditPanel;
import com.n4systems.fieldid.wicket.components.event.observations.ObservationEditPanel;
import com.n4systems.fieldid.wicket.components.event.observations.ObservationsButton;
import com.n4systems.fieldid.wicket.components.event.observations.RecommendationsEditPanel;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Observation;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class CriteriaSectionEditPanel extends Panel {

    private IModel<List<CriteriaResult>> results;
    private FIDModalWindow recommendationsWindow;

    public CriteriaSectionEditPanel(String id, IModel<List<CriteriaResult>> results) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new CriteriaEditForm("criteriaEditForm", results));
        add(recommendationsWindow = new FIDModalWindow("modalWindow"));
        recommendationsWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                target.add(CriteriaSectionEditPanel.this);
                return true;
            }
        });
    }
    
    class CriteriaEditForm extends Form<List<CriteriaResult>> {

        public CriteriaEditForm(String id, IModel<List<CriteriaResult>> results) {
            super(id, results);
            
            add(new ListView<CriteriaResult>("criteria", results) {
                @Override
                protected void populateItem(final ListItem<CriteriaResult> item) {
                    item.add(new Label("criteriaName", new PropertyModel<String>(item.getModel(), "criteria.displayText")));
                    item.add(CriteriaEditorFactory.createEditorFor("criteriaEditor", item.getModel()));
                    final PropertyModel<List<? extends Observation>> recommendations = new PropertyModel<List<? extends Observation>>(item.getModel(), "recommendations");
                    item.add(new ObservationsButton("recommendationsButton", recommendations, "images/rec.png", "images/rec-plus.png") {
                        @Override
                        protected void onClick(AjaxRequestTarget target) {
                            recommendationsWindow.setTitle(new FIDLabelModel("label.recommendations"));
                            recommendationsWindow.setContent(new RecommendationsEditPanel(recommendationsWindow.getContentId(), item.getModel()));
                            recommendationsWindow.show(target);
                        }
                    });
                    final PropertyModel<List<? extends Observation>> deficiencies = new PropertyModel<List<? extends Observation>>(item.getModel(), "deficiencies");
                    item.add(new ObservationsButton("deficienciesButton", deficiencies, "images/def.png", "images/def-plus.png") {
                        @Override
                        protected void onClick(AjaxRequestTarget target) {
                            recommendationsWindow.setTitle(new FIDLabelModel("label.deficiencies"));
                            recommendationsWindow.setContent(new DeficienciesEditPanel(recommendationsWindow.getContentId(), item.getModel()));
                            recommendationsWindow.show(target);
                        }
                    });
                }
            });
        }

    }
    
}

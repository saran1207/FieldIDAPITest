package com.n4systems.fieldid.wicket.pages.setup.score;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ScoreGroupPanel extends Panel {

    @SpringBean
    private PersistenceService persistenceService;

    public ScoreGroupPanel(String id, IModel<ScoreGroup> model) {
        super(id, model);
        setOutputMarkupId(true);

        add(new ScoreGroupForm("scoreGroupForm", model));
    }

    class ScoreGroupForm extends Form<ScoreGroup> {

        public ScoreGroupForm(String id, final IModel<ScoreGroup> model) {
            super(id, model);

            add(new TextField<String>("name", new PropertyModel<String>(model, "name")));

            add(new ListView<Score>("scoreList", new PropertyModel<List<Score>>(model, "scores")) {
                @Override
                protected void populateItem(ListItem<Score> item) {
                    item.add(new ScorePanel("score", item.getModel()));
                }
            });

            add(new AjaxButton("addButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Score score = new Score();
                    score.setTenant(FieldIDSession.get().getTenant());
                    model.getObject().getScores().add(score);
                    target.addComponent(ScoreGroupPanel.this);
                }
            });
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            persistenceService.update(getModel().getObject());
        }

//        @Override
//        protected void onError() {
//            System.out.println("There was an error wtf");
//        }
    }

}

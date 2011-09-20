package com.n4systems.fieldid.wicket.pages.setup.score;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class ScoreBlankSlatePanel extends Panel {

    public ScoreBlankSlatePanel(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new CreateFirstGroupForm("createFirstGroupForm"));
    }
    
    class CreateFirstGroupForm extends Form {

        public CreateFirstGroupForm(String id) {
            super(id);
            add(new AjaxButton("createFirstScoreGroup") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onCreateFirstGroupClicked(target);
                }
            });

        }

    }

    protected void onCreateFirstGroupClicked(AjaxRequestTarget target) { }

}

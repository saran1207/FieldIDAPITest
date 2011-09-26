package com.n4systems.fieldid.wicket.pages.setup.score;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.ScoreService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventform.EditCopyDeleteItemPanel;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreGroup;
import com.n4systems.util.DoubleFormatter;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ScoreGroupPanel extends Panel {

    @SpringBean
    private ScoreService scoreService;

    @SpringBean
    private PersistenceService persistenceService;

    private ScoreGroupForm scoreGroupForm;
    private IModel<ScoreGroup> scoreGroupModel;

    public ScoreGroupPanel(String id, IModel<ScoreGroup> model) {
        super(id, model);
        this.scoreGroupModel = model;
        setOutputMarkupPlaceholderTag(true);

        add(new WebMarkupContainer("blankInstructions") {
            @Override
            public boolean isVisible() {
                return !isScoreGroupSelected();
            }
        });

        add(scoreGroupForm = new ScoreGroupForm("scoreGroupForm", model) {
            @Override
            public boolean isVisible() {
                return isScoreGroupSelected();
            }
        });
    }

    class ScoreGroupForm extends Form<ScoreGroup> {

        private Score score;
        private FeedbackPanel feedbackPanel;

        public ScoreGroupForm(String id, final IModel<ScoreGroup> scoreGroupModel) {
            super(id, scoreGroupModel);
            score = new Score();
            setOutputMarkupId(true);

            add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", ScoreGroupForm.this));
            feedbackPanel.setOutputMarkupPlaceholderTag(true);

            add(new Label("nameLabel", new PropertyModel<String>(scoreGroupModel, "name")));

            WebMarkupContainer sortableScoresContainer = new WebMarkupContainer("sortableScoresContainer");

            sortableScoresContainer.add(new ListView<Score>("scoreList", new PropertyModel<List<Score>>(scoreGroupModel, "scores")) {
                @Override
                protected void populateItem(final ListItem<Score> item) {
                    item.setOutputMarkupId(true);
                    item.add(new EditCopyDeleteItemPanel("score", new PropertyModel<String>(item.getModel(), "name"), createSubtitleModel(item.getModel()), false) {
                        {setStoreLabel(new FIDLabelModel("label.save"));}
                        @Override
                        protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                            scoreService.archiveScore(scoreGroupModel.getObject(), item.getModelObject());
                            target.addComponent(ScoreGroupPanel.this);
                        }

                        @Override
                        protected void onStoreLinkClicked(AjaxRequestTarget target) {
                            persistenceService.update(item.getModelObject());
                            target.addComponent(ScoreGroupPanel.this);
                        }
                    });
                }
            });

            add(sortableScoresContainer);

            add(new NewScorePanel("newScore", new PropertyModel<Score>(this, "score")));
            add(new AjaxButton("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    score.setTenant(FieldIDSession.get().getTenant());
                    scoreService.addScore(ScoreGroupForm.this.getModelObject(), score);
                    score = new Score();
                    scoreGroupModel.detach();
                    target.addComponent(ScoreGroupPanel.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedbackPanel);
                }
            });
        }

        @Override
        protected void onSubmit() {
        }

    }

    private IModel<String> createSubtitleModel(final IModel<Score> model) {
        return new IModel<String>() {
            @Override
            public String getObject() {
                if (model.getObject().isNa()) {
                    return new FIDLabelModel("label.indicates_na").getObject();
                }
                String hasAValueOf = new FIDLabelModel("label.has_a_value_of").getObject();
                return hasAValueOf + " " + DoubleFormatter.simplifyDouble(model.getObject().getValue());
            }

            @Override
            public void setObject(String object) {
            }

            @Override
            public void detach() {
            }
        };

    }

    private boolean isScoreGroupSelected() {
        return getDefaultModelObject() != null;
    }

}

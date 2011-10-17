package com.n4systems.fieldid.wicket.pages.setup.score;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.ScoreService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.eventform.EditCopyDeleteItemPanel;
import com.n4systems.fieldid.wicket.components.eventform.SortableListPanel;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreGroup;
import com.n4systems.util.DoubleFormatter;

@SuppressWarnings("serial")
public class ScoreGroupPanel extends SortableListPanel {

    @SpringBean
    private ScoreService scoreService;

    @SpringBean
    private PersistenceService persistenceService;

    private ScoreGroupForm scoreGroupForm;
    private IModel<ScoreGroup> scoreGroupModel;
    private SortableAjaxBehavior sortableBehavior;
    private List<Score> scores;

	private ListView<Score> listView;

	public ScoreGroupPanel(String id, IModel<ScoreGroup> model) {
        super(id);
        if (model.getObject()==null) {
        	model.setObject(new ScoreGroup());
        }
        this.scoreGroupModel = model;
        scores = new ArrayList<Score>();
        scores.addAll(scoreGroupModel.getObject().getScores()); 
        setDefaultModel(new PropertyModel<List<Score>>(this, "scores"));
        setOutputMarkupPlaceholderTag(true);
        add(new WebMarkupContainer("blankInstructions") {
            @Override
            public boolean isVisible() {
                return !isScoreGroupSelected();
            }
        });

        add(new TwoStateAjaxLink("reorderSectionsButton", "Reorder Sections", "Done Reordering") {
        	private Boolean reorderState = null;

			@Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.addComponent(ScoreGroupPanel.this);
                sortableBehavior.setDisabled(true);
                reorderState = false;
                scoreGroupModel.getObject().setScores(scores);
                persistenceService.update(scoreGroupModel.getObject());
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.addComponent(ScoreGroupPanel.this);
                sortableBehavior.setDisabled(false);
                reorderState = true;
            }
        });
        
        WebMarkupContainer sortableScoresContainer = new WebMarkupContainer("sortableScoresContainer");
        	 
        sortableScoresContainer.add(listView = new ListView<Score>("scoreList", getScoresModel()) {
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
        
        sortableScoresContainer.add(sortableBehavior = makeSortableBehavior());
        
        add(sortableScoresContainer);


        
        add(scoreGroupForm = new ScoreGroupForm("scoreGroupForm", model) {
            @Override
            public boolean isVisible() {
                return isScoreGroupSelected();
            }
        });
        
        
        
    }


     
	@SuppressWarnings("unchecked")
	private IModel<List<Score>> getScoresModel() {
		return (IModel<List<Score>>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	private List<Score> getScores() {
		return (List<Score>) getDefaultModel().getObject();
	}
    
	@Override
	protected void onItemMoving(int oldIndex,	int newIndex, AjaxRequestTarget target) {
		ScoreGroup x = scoreGroupModel.getObject();
		IModel<? extends List<Score>> xx = listView.getModel();
		IModel<?> ll = listView.getDefaultModel();
		Score movingScore = getScores().remove(oldIndex);
movingScore.setName(movingScore.getName()+newIndex);
		getScores().add(newIndex, movingScore);
		target.addComponent(this);		
	}
	

	@Override
	protected int getIndexOfComponent(Component component) {
		Score score = (Score) component.getDefaultModelObject();
		return getScores().indexOf(score);		
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

	class ScoreGroupForm extends Form<ScoreGroup> {
    	
    	private Score score;
    	private FeedbackPanel feedbackPanel;
    	private IModel<ScoreGroup> model;
    	
    	public ScoreGroupForm(String id, final IModel<ScoreGroup> scoreGroupModel) {
    		super(id, scoreGroupModel);
    		model = scoreGroupModel;
    		score = new Score();
    		setOutputMarkupId(true);
    		
    		add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", ScoreGroupForm.this));
    		feedbackPanel.setOutputMarkupPlaceholderTag(true);
    		
    		add(new Label("nameLabel", new PropertyModel<String>(scoreGroupModel, "name")));
    		
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
}

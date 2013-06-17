package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.comment.CommentService;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.List;

@SuppressWarnings("serial")
public class Comment extends Panel implements IEventBehavior {

	@SpringBean private CommentService commentService;
	
	private CommentTemplate comment;
	
	private List<IEventBehavior> onChangeBehaviors = Lists.newArrayList(); 

	private IModel<String> model;

	private DropDownChoice<CommentTemplate> select;
	private TextArea<String> text;

	public Comment(String id, final IModel<String> model) {
		super(id,model);		
		this.model = model;
		onChangeBehaviors.add(this);
		
        CommentsModel commentsModel = new CommentsModel();

        text = new TextArea<String>("commentText", model);
        text.setOutputMarkupId(true);
        select = new DropDownChoice<CommentTemplate>("commentSelect", new PropertyModel<CommentTemplate>(this,"comment"), commentsModel);
		select.setNullValid(true);
		select.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override protected void onUpdate(AjaxRequestTarget target) {
				fireOnChange(target);
			}			
		});
		
		text.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override protected void onUpdate(AjaxRequestTarget target) {
                fireOnChange(target);
				target.add(text);
			}			
		});
		
		add(text);
		add(select);
	}
	
	private void addComment(CommentTemplate comment) {
		String value = model.getObject();
		if (value==null) {
			value = "";
		} else { 
			value = value.endsWith("\n") ? value : value+"\n";
		}
		model.setObject(value + comment.getComment()+"\n");
	}
	
	public void addChangeBehavior(IEventBehavior onChangeBehavior) {
		Preconditions.checkArgument(onChangeBehavior!=null, "can't add null behavior to " + getClass().getSimpleName());
		onChangeBehaviors.add(onChangeBehavior);
	}	

	private void fireOnChange(AjaxRequestTarget target) {
		for (IEventBehavior behavior:onChangeBehaviors) { 
			behavior.onEvent(target);
		}
	}

    public Comment addMaxLengthValidation(int maxLength ) {
        text.add(new StringValidator.MaximumLengthValidator(maxLength));
        return this;
    }
	
	@Override
	public void onEvent(AjaxRequestTarget target) {
		CommentTemplate comment = select.getModel().getObject();
		if (comment!=null) { 
			addComment(comment);
			target.add(text);
		}		
	}
	
	
	
	public DropDownChoice<CommentTemplate> getSelect() {
		return select;
	}


	class CommentsModel extends LoadableDetachableModel<List<CommentTemplate>> {

		@Override
		protected List<CommentTemplate> load() {
			return commentService.getCommentTemplates();
		} 
		
	}

	
	
}



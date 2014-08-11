package com.n4systems.fieldid.wicket.pages.setup.comment;

import com.n4systems.fieldid.service.comment.CommentService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.fieldid.wicket.components.text.LabelledTextField;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.TemplatesPage;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page allows the user to create a new Comment Template.
 *
 *
 * Created by Jordan Heath on 08/08/14.
 */
public class AddCommentTemplatePage extends FieldIDTemplatePage {

    @SpringBean
    protected CommentService commentService;

    protected IModel<CommentTemplate> thisTemplate;

    protected FIDFeedbackPanel feedbackPanel;

    public AddCommentTemplatePage() {
        this.thisTemplate = Model.of(new CommentTemplate());
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        final CommentTemplate template = thisTemplate.getObject();

        Form<Void> form = new Form<Void>("form") {
            @Override
            public void onSubmit() {
                if(isEdit()) {
                    commentService.update(thisTemplate.getObject(), getCurrentUser());
                } else {
                    template.setTenant(getTenant());
                    commentService.save(template, getCurrentUser());
                }

                //Do we need to have a message pop up on the other page??

                setResponsePage(new CommentTemplateListPage());
            }
        };

        form.add(new LabelledTextField<String>("nameField",
                                               "label.name",
                                               new PropertyModel<String>(thisTemplate,
                                                                         "name"))
                            .required()
                            .add(new CommentTemplateUniqueNameValidator(template.getId())));

        form.add(new LabelledTextArea<String>("commentField",
                                              "label.comment",
                                              new PropertyModel<String>(thisTemplate,
                                                                        "comment"))
                            .required());

        Button submitButton;
        form.add(submitButton = new Button("saveButton"));
        submitButton.setOutputMarkupId(true);

        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new CommentTemplateListPage());
            }
        });

        add(form);

        add(new FIDFeedbackPanel("feedbackPanel"));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_comment_templates"));
    }


    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, TemplatesPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                        aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                         commentService.getCommentTemplateCount()))
                                  .page(CommentTemplateListPage.class)
                                  .build(),

                        aNavItem().label(new FIDLabelModel("nav.add"))
                                  .page(AddCommentTemplatePage.class)
                                  .onRight()
                                  .build()
                )
        );
    }


    /**
     * This method determines whether or not this page is being used for Editing or adding a new Comment Template.
     *
     * By default, this method returns false.  It should be Overridden and a value of <b>true</b> returned in the Edit page.
     *
     * @return False, because the return is static.
     */
    protected boolean isEdit() {
        return false;
    }

    /**
     * This is the unique name validator for the Comment Template page.  Since it's only used on this page and the
     * Edit Template page, we're just going to create an internal class.
     */
    private class CommentTemplateUniqueNameValidator extends AbstractValidator<String> {
        private Long id;

        public CommentTemplateUniqueNameValidator(Long id) {
            this.id = id;
        }

        @Override
        protected void onValidate(IValidatable<String> stringIValidatable) {
            String name = stringIValidatable.getValue();

            if(commentService.exists(name, id)) {
                ValidationError error = new ValidationError();
                error.addMessageKey("error.commenttemplateduplicate");
                stringIValidatable.error(error);
            }
        }
    }
}

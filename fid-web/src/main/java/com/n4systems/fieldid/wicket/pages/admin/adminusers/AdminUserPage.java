package com.n4systems.fieldid.wicket.pages.admin.adminusers;


import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.BooleanModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.fieldid.wicket.util.EnumPropertyChoiceRenderer;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.model.admin.AdminUserType;
import com.n4systems.services.admin.AdminUserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import java.util.Arrays;
import java.util.List;

public class AdminUserPage extends FieldIDAdminPage {

	@SpringBean
	private AdminUserService adminUserService;
	private FIDFeedbackPanel feedbackPanel;
	private WebMarkupContainer userList;

	public AdminUserPage() {
		add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
		feedbackPanel.setOutputMarkupId(true);

		add(userList = new WebMarkupContainer("userList"));
		userList.setOutputMarkupId(true);
		userList.add(new ListView<AdminUser>("user", getAdminUserListModel()) {
			@Override
			protected void populateItem(final ListItem<AdminUser> item) {
				item.add(new Label("type", new PropertyModel<String>(item.getModel(), "type")));
				item.add(new Label("email", new PropertyModel<String>(item.getModel(), "email")));
				item.add(new AjaxLink<Void>("remove") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						adminUserService.removeAdminUser(item.getModelObject());
						userList.detach();
						target.add(userList);

					}
				});

				AjaxLink<?> enabledLink = new AjaxLink<Void>("toggle_enabled") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						adminUserService.enableUser(item.getModelObject(), !item.getModelObject().isEnabled());
						userList.detach();
						target.add(userList);
					}
				};
				enabledLink.add(new Label("toggle_enabled_label", new BooleanModel(new PropertyModel<Boolean>(item.getModel(), "enabled"), new FIDLabelModel("label.disable"), new FIDLabelModel("label.enable"))));
				item.add(enabledLink);

				item.add(new AjaxLink<Void>("reset_password") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						adminUserService.resetUserPasswordAndNotify(item.getModelObject());
					}
				});
			}
		});

		add(new AddUserForm("form"));
	}

	private LoadableDetachableModel<List<AdminUser>> getAdminUserListModel() {
		return new LoadableDetachableModel<List<AdminUser>>() {
			@Override
			protected List<AdminUser> load() {
				List<AdminUser> users = adminUserService.findAllAdminUsers();
				return users;
			}
		};
	}

	private class AddUserForm extends Form<Void> {
		private AdminUserType type;
		private String email;

		public AddUserForm(String id) {
			super(id);

			final DropDownChoice<AdminUserType> typeDropDown;
			add(typeDropDown = new DropDownChoice<AdminUserType>("type", new PropertyModel<AdminUserType>(this, "type"), Arrays.asList(AdminUserType.values()), new EnumPropertyChoiceRenderer<AdminUserType>()));

			final RequiredTextField<String> emailTextField;
			add(emailTextField = new RequiredTextField<String>("email", new PropertyModel<String>(this, "email")));
			emailTextField.add(EmailAddressValidator.getInstance());
			emailTextField.add(new AbstractValidator<String>() {
				@Override
				protected void onValidate(IValidatable<String> validatable) {
					if(adminUserService.adminUserExists(validatable.getValue())) {
						ValidationError error = new ValidationError();
						error.addMessageKey("error.duplicate_admin_user_email");
						validatable.error(error);
					}
				}
			});
			add(new AjaxSubmitLink("add") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					adminUserService.createAndNotifyAdminUserWithRandomPass(email, type);
					type = null;
					email = null;
					typeDropDown.clearInput();
					emailTextField.clearInput();
					target.add(userList, feedbackPanel);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
			});
		}
	}
}

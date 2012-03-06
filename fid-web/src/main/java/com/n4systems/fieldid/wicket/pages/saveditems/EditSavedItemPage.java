package com.n4systems.fieldid.wicket.pages.saveditems;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.behavior.validation.UniquelyNamedSavedItemValidator;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedItem;

public class EditSavedItemPage extends FieldIDFrontEndPage {
	
	private SavedItem savedItem;
	
    @SpringBean
    private PersistenceService persistenceService;

	public EditSavedItemPage(PageParameters params){

		add(new FIDFeedbackPanel("feedbackPanel"));
		
		Long id = params.get("id").toLong();
		savedItem = persistenceService.find(SavedItem.class, id);
		
		Form<Void> form = new Form<Void>("form") {
			
			@Override
			protected void onSubmit() {
				persistenceService.update(savedItem);
				setResponsePage(ManageSavedItemsPage.class);
			}
			
		};
		
		RequiredTextField<SavedItem> nameText;
		form.add(nameText = new RequiredTextField<SavedItem>("name", new PropertyModel<SavedItem>(savedItem, "name")));
		nameText.add(new UniquelyNamedSavedItemValidator(savedItem.getId()));
		
		form.add(new TextArea<SavedItem>("description", new PropertyModel<SavedItem>(savedItem, "description")));
		form.add(new BookmarkablePageLink<Void>("cancelLink", ManageSavedItemsPage.class));
		
		add(form);
	}
	
	@Override
	protected Label createTitleLabel(String labelId) {
		return new Label(labelId, "Edit Saved Item");
	}
}

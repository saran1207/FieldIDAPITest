package com.n4systems.fieldid.wicket.pages.saveditems;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.behavior.validation.UniquelyNamedEnityValidator;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedItem;

public class EditSavedItemPage extends FieldIDFrontEndPage {
	
	private SavedItem savedItem;
	
    @SpringBean
    private PersistenceService persistenceService;

	public EditSavedItemPage(PageParameters params){
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
		nameText.add(new UniquelyNamedEnityValidator(SavedItem.class));
		
		form.add(new TextArea<SavedItem>("description", new PropertyModel<SavedItem>(savedItem, "description")));
		form.add(new BookmarkablePageLink<Void>("cancelLink", ManageSavedItemsPage.class));
		
		add(form);
	}
}

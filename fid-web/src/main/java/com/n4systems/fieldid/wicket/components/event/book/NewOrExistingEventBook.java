package com.n4systems.fieldid.wicket.components.event.book;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.model.EventBook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class NewOrExistingEventBook extends FormComponentPanel<EventBook> {
    
    private EventBook newEventBook = new EventBook();
    private EventBook existingEventBook;
    
    private boolean selectExistingState;

    DropDownChoice<EventBook> existingBookSelect;
    TextField<String> newEventBookName;

    public NewOrExistingEventBook(String id, IModel<EventBook> eventBook) {
        super(id, eventBook);

        setOutputMarkupPlaceholderTag(true);

        existingBookSelect = new DropDownChoice<EventBook>("existingEventBook", new PropertyModel<EventBook>(this, "existingEventBook"), new EventBooksForTenantModel(), new ListableChoiceRenderer<EventBook>());
        existingBookSelect.setNullValid(true);
        existingBookSelect.add(new UpdateComponentOnChange());
        add(existingBookSelect);
        
        newEventBookName = new TextField<String>("newEventBookName", new PropertyModel<String>(newEventBook, "name"));
        newEventBookName.add(new UpdateComponentOnChange());
        add(newEventBookName);

        setSelectExistingState(true);
        
        add(new AjaxLink<Void>("addNewBookLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setSelectExistingState(false);
                target.add(NewOrExistingEventBook.this);
            }
        });
        add(new AjaxLink<Void>("selectExistingBookLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setSelectExistingState(true);
                target.add(NewOrExistingEventBook.this);
            }
        });
    }
    
    private void setSelectExistingState(boolean selectExistingState) {
        this.selectExistingState = selectExistingState;
        existingBookSelect.setVisible(selectExistingState);
        newEventBookName.setVisibilityAllowed(!selectExistingState);
    }

    @Override
    protected void convertInput() {
        if (selectExistingState) {
            setConvertedInput(existingEventBook);
        } else {
            setConvertedInput(newEventBook);
        }
    }

}

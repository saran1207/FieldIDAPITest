package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;
import com.n4systems.util.StringUtils;

public class StringOrDatePropertyColumn extends PropertyColumn<Object> {

    public StringOrDatePropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
		super(displayModel, sortProperty, propertyExpression);
	}

	public StringOrDatePropertyColumn(IModel<String> displayModel, String propertyExpression) {
		super(displayModel, propertyExpression);
	}

	@Override
    protected IModel<?> createLabelModel(IModel<Object> rowModel) {
        IModel<?> labelModel = super.createLabelModel(rowModel);
        Object cellValue = labelModel.getObject();

        String cellString;
        if (cellValue instanceof Date) {
        	cellString = new FieldidDateFormatter((Date) cellValue, getDateTimeDefinition()).format();
        } else {
        	cellString = StringUtils.stringOrEmpty(cellValue);
        }
        
        return new Model<String>(cellString);
    }

    protected DateTimeDefinition getDateTimeDefinition() {
    	return FieldIDSession.get().getSessionUser();
    }
}

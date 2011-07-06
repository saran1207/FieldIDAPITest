package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Date;
import java.util.TimeZone;

public class DateTimePropertyColumn<T> extends PropertyColumn<T> implements DateTimeDefinition {

    public DateTimePropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<T> rowModel) {
        IModel<?> labelModel = super.createLabelModel(rowModel);
        Date date = (Date) labelModel.getObject();

        String cellString = new FieldidDateFormatter(date, this, true, true).format();

        return new Model<String>(cellString);
    }

    @Override
    public String getDateFormat() {
        return FieldIDSession.get().getSessionUser().getDateFormat();
    }

    @Override
    public String getDateTimeFormat() {
        return FieldIDSession.get().getSessionUser().getDateTimeFormat();
    }

    @Override
    public TimeZone getTimeZone() {
        return FieldIDSession.get().getSessionUser().getTimeZone();
    }

}

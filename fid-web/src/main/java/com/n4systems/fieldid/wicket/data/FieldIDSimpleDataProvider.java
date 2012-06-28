package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.download.StringRowPopulator;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.TableGenerationContextImpl;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.RowModel;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.util.views.RowView;
import com.n4systems.util.views.TableView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;

import java.util.Iterator;
import java.util.List;

public abstract class FieldIDSimpleDataProvider extends FieldIDDataProvider<RowView> implements ISortableDataProvider<RowView> {

    private List<RowView> results;
    List<ColumnMappingView> columns;
    private Integer size;
    private Integer pageSize;

    protected abstract int getResultCount();
    protected abstract PageHolder<TableView> runSearch(int page, int pageSize);

    public FieldIDSimpleDataProvider(Integer pageSize,  List<ColumnMappingView> columns) {
        this.pageSize = pageSize;
        this.columns = columns;
    }

    @Override
    public Iterator<? extends RowView> iterator(int first, int count) {
        return getResults(first).iterator();
    }

    private List<RowView> getResults(int first) {
        if (results == null) {
            int page = first / pageSize;
            PageHolder<TableView> pageHolder = runSearch(page, pageSize);
            results = pageHolder.getPageResults().getRows();
        }
        return results;
    }

    @Override
    public int size() {
        if (size == null) {
            size = getResultCount();
        }
        return size;    }

    @Override
    public IModel<RowView> model(RowView row) {
        fillInStringValues(row);
        return new RowModel(row);
    }

    private void fillInStringValues(RowView row) {
        SessionUser user = FieldIDSession.get().getSessionUser();
        TableGenerationContext exportContextProvider = new TableGenerationContextImpl(user.getTimeZone(), user.getOwner().getPrimaryOrg().getDateFormat(), user.getOwner().getPrimaryOrg().getDateFormat() + " h:mm a", user.getOwner());
        StringRowPopulator.populateRowWithConvertedStrings(row, columns, exportContextProvider);
    }
}

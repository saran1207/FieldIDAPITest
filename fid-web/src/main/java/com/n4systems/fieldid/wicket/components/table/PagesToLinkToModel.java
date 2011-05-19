package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class PagesToLinkToModel extends LoadableDetachableModel<List<Integer>> {

    protected DataTable<?> table;

    public PagesToLinkToModel(DataTable<?> table) {
        this.table = table;
    }

    @Override
    protected List<Integer> load() {
        return calculatePagesToLinkTo();
    }

    private List<Integer> calculatePagesToLinkTo() {
        // Same logic as the old template.... weird...
        int startingPage = getTable().getCurrentPage() - 4;
        if (startingPage < 1) {
            startingPage = 1;
        }
        if (startingPage + 9 > getTable().getPageCount()) {
            startingPage = getTable().getPageCount() - 9;
        }
        if (startingPage < 1) {
            startingPage = 1;
        }
        int endingPage = startingPage + 9;
        if (endingPage > getTable().getPageCount()) {
            endingPage = getTable().getPageCount();
        }

        List<Integer> pages = new ArrayList<Integer>();
        for (int i = startingPage; i <= endingPage; i++) {
            pages.add(i);
        }

        return pages;
    }

    protected DataTable<?> getTable() {
        return table;
    }

}

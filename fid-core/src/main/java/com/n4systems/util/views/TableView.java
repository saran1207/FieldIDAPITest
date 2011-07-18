package com.n4systems.util.views;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TableView extends AbstractCollection<List<Object>> {
	private static final Object DEFAULT_VALUE = null;
	
	final private List<List<Object>> table;
	final private List<Long> idList;
	final private List<Object> entityList;
	final private int columns;
	
	public TableView(int rowSize, int columnSize) {
		// initalize the table
		columns = columnSize;
		table = new ArrayList<List<Object>>(rowSize);
		for (int r = 0; r < rowSize; r++) {
			addRow();
		}
		
		// initialize the id list
		idList = new ArrayList<Long>(rowSize);
		for (int r = 0; r < rowSize; r++) {
			idList.add(null);
		}
		
		//init the original entity list
		entityList = new ArrayList<Object>(rowSize);
		for (int r = 0; r < rowSize; r++) {
			entityList.add(null);
		}
	}
	
	public void append(TableView other) {
		for (int row = 0; row < other.getRowSize(); row++) {
			table.add(other.getRow(row));
			idList.add(other.getId(row));
			entityList.add(other.getEntity(row));
		}
	}
	
	private void addRow() {
		List<Object> row = new ArrayList<Object>(columns);
		row.addAll(Collections.nCopies(columns, DEFAULT_VALUE));
		table.add(row);
	}
	
	public void setCell(int row, int col, Object value) {
		table.get(row).set(col, value);
	}
	
	public Object getCell(int row, int col) {
		return table.get(row).get(col);
	}
	
	public List<Object> getColumn(int column) {
		List<Object> cols = new ArrayList<Object>(table.size());
		for (List<Object> col: table) {
			cols.add(col.get(column));
		}
		return cols;
	}
	
	public List<Object> getRow(int row) {
		return new ArrayList<Object>(table.get(row));
	}

	public void setId(int row, Long id) {
		idList.set(row, id);
	}
	
	public Long getId(int row) {
		return idList.get(row);
	}
	
	public Long getRowForId(Long id){
		return new Long(idList.indexOf(id));
	}
	
	public List<Long> getIdList() {
		return idList;
	}

	public void setEntity(int row, Object entity) {
		entityList.set(row, entity);
	}
	
	public Object getEntity(int row) {
		return entityList.get(row);
	}
	
	public List<Object> getEntityList() {
		return entityList;
	}
	
	@Override
	public Iterator<List<Object>> iterator() {
		return table.iterator();
	}

	@Override
	public int size() {
		return table.size();
	}
	
	public int getRowSize() {
		return size();
	}
	
	public int getColumnSize() {
		return columns;
	}

    public List<RowView> getRows() {
        List<RowView> rows = new ArrayList<RowView>(table.size());

        Iterator<List<Object>> tableIterator = table.iterator();
        Iterator<Object> entityIterator = entityList.iterator();
        Iterator<Long> idListIterator = idList.iterator();

        while (idListIterator.hasNext()) {
            RowView row = new RowView();

            row.setEntity(entityIterator.next());
            row.setValues(tableIterator.next());
            row.setId(idListIterator.next());

            rows.add(row);
        }

        return rows;
    }

}

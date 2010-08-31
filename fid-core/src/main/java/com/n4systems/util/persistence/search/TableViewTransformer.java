package com.n4systems.util.persistence.search;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.util.reflection.ReflectionException;
import com.n4systems.util.reflection.ReflectionFilter;
import com.n4systems.util.reflection.Reflector;
import com.n4systems.util.reflection.SimpleReflectionFilter;
import com.n4systems.util.views.TableView;

/**
 * A result transformer used to transform query results into a TableView.
 * @see #TableViewTransformer(String, List)
 */
public class TableViewTransformer implements ResultTransformer<TableView>{
	private static final long serialVersionUID = 1L;
	private static final String EMPTY_STRING = new String();
	
	private Logger logger = Logger.getLogger(ResultTransformer.class);
	
	private String idField;
	private List<String> columnPaths;
	private List<ReflectionFilter<?>> filters;
	private List<String> postFilterPaths;
	
	/**
	 * Initializes this ResultTransformer with the id field of the entity and a list of column expressions.  Each
	 * column expression will become a column in the resulting TableView.  Column expressions are {@link Reflector} and
	 * {@link SimpleReflectionFilter} paths. <p/>
	 * Column Expression Format: relative.pre-filter.path{filter.target=filtervalue}.post.filer.path<p/>
	 * Column Expression Examples: <br/>
	 * <ul>
	 * 	<li>name - returns the name field of the target entity</li>
	 *	<li>child.name - returns the name field of child on the target entity </li>
	 * 	<li>childList{id=12345} - returns the childList element with the id field matching 12345</li>
	 * 	<li>childList{id=12345}.name - returns the childList elements name with the id field matching 12345</li>
	 * </ul>
	 * @see SimpleReflectionFilter
	 * @param idField				The id field of the entity, used for reflecting into the TableView.  eg/ id or uniqueID
	 * @param columnExpressions		A list of column expressions.
	 * @throws ParseException		If a column or filter path could not be parsed by 
	 */
	public TableViewTransformer(String idField, List<String> columnExpressions) throws ParseException {
		parseColumns(columnExpressions.toArray(new String[columnExpressions.size()]));
		this.idField = idField;
	}
	
	/**
	 * Parses an array of column expressions into their column paths, filter expressions and post filter paths.
	 * @param columnExpressions	String array of column expressions
	 * @throws ParseException	On any syntactical expression error
	 */
	private void parseColumns(String ... columnExpressions) throws ParseException {
		
		columnPaths = new ArrayList<String>(columnExpressions.length);
		filters = new ArrayList<ReflectionFilter<?>>(columnExpressions.length);
		postFilterPaths = new ArrayList<String>(columnExpressions.length);
		
		int openFilterIdx, closeFilterIdx;
		for(String column: columnExpressions) {
			
			openFilterIdx = column.indexOf("{");
			closeFilterIdx = column.indexOf("}");
			
			if(openFilterIdx == -1) {
				// path has no open filter char .. assume no filter, and thus no post filter
				columnPaths.add(column);
				filters.add(null);
				postFilterPaths.add(null);
			} else {
				if(closeFilterIdx == -1) {
					// if we have an open, we MUST have a close
					throw new ParseException("Missing closing curley brace on filter expression", column.length());
				}
				
				// if the close index is not the last part, we have further reflecting to do
				if(closeFilterIdx != (column.length() - 1)) {
					// post filter paths will start with a '.'.  We'll remove it by adding 2 to the start substring.  /eg myobject{filter.path=exp}.post.path
					postFilterPaths.add(column.substring(closeFilterIdx + 2, column.length()));
				} else {
					postFilterPaths.add(null);
				}
				
				// add the path section
				columnPaths.add(column.substring(0, openFilterIdx));
				filters.add(new SimpleReflectionFilter(column.substring(openFilterIdx + 1, closeFilterIdx)));
			}
		}
	}
	
	/**
	 * Implements the ResultTransformer transform interface.  Converts the List of entity object
	 * into a TableView using the column expressions defined at instantation.
	 */
	public TableView transform(List<?> list) {
		TableView view = new TableView(list.size(), columnPaths.size());
		
		Object finalValue;
		for (int rowIdx = 0; rowIdx < list.size(); rowIdx++) {
			// pull the full entity, into the row list
			view.setEntity(rowIdx, list.get(rowIdx));
			
			// id's will get reflected separately 
			try {
				view.setId(rowIdx, (Long)Reflector.getPathValue(list.get(rowIdx), idField));
			} catch (ReflectionException e) {
				logger.warn("Exception thrown while reflecting id field [" + idField + "]", e);
			} 
			
			
			for (int col = 0; col < columnPaths.size(); col++) {
				finalValue = getCellValue(list.get(rowIdx), col);
				
				// lets make sure we don't have nulls in our list (they're a pain to deal with in struts
				finalValue = (finalValue != null) ? finalValue : EMPTY_STRING;
				view.setCell(rowIdx, col, finalValue);
			}
		}
		
		return view;
	}

	/**
	 * reflects a single entity with it's column expression and applies appropriate filter.  Also
	 * converts single element collections to the single element itself.  Finally runs 
	 * the post filter.
	 * @see #getCellPostFilterValue(Object, int)
	 * @param entity	The row entity
	 * @param column	The column to reflect
	 * @return			The resulting value after pre-reflection, filtering and post-reflection.
	 */
	private Object getCellValue(Object entity, int column) {
		Object cell = EMPTY_STRING;
		
		try {
			cell = Reflector.getPathValue(entity, columnPaths.get(column), filters.get(column));
		} catch (Exception e) {
			logger.warn("Exception thrown while reflecting path [" + columnPaths.get(column) + "]", e);
		}
		
		// if the cell contains a collection with only one value, just return that value
		Object value;
		if (cell instanceof Collection<?>) {
			Collection<?> cellAsCollection = (Collection<?>)cell;
			
			if (cellAsCollection.size() == 1) {
				// if the collection has only one entry, use that entry directly rather then in the collection
				value = cellAsCollection.iterator().next();
			} else if(cellAsCollection.isEmpty()) {
				// empty lists are set null
				value = null;
			} else {
				// lists with more then one element will be passed through
				value = cellAsCollection;
			}
			
		} else {
			// if it's not a collection just pass the value through
			value = cell;
		}
		
		return getCellPostFilterValue(value, column);
	}
	
	/**
	 * Runs final post-filtering reflection.
	 * @param object	The reflected and filtered value
	 * @param column	The current column
	 * @return			The final reflected value
	 */
	private Object getCellPostFilterValue(Object object, int column) {
		if(postFilterPaths.get(column) == null) {
			// get out now if this column has no post filter path
			return object;
		}
		
		Object value = EMPTY_STRING;
		try {
			value = Reflector.getPathValue(object, postFilterPaths.get(column));
		} catch (ReflectionException e) {
			logger.warn("Exception thrown while reflecting post filter path value", e);
		}
		return value;
	}

}

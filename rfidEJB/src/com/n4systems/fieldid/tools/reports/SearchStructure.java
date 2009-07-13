package com.n4systems.fieldid.tools.reports;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Use {@link ColumnMapping} and {@link ColumnMappingFactory}
 */
@Deprecated
public class SearchStructure extends TableStructure {
	
	public SearchStructure() {
		super();
	}
	
	/**
	 * creates the layout for the only search screen.
	 * @param isAnEndUser
	 * @param usingSerialNumber
	 * @return
	 */
	public static SearchStructure getSearchStructure( boolean isAnEndUser, boolean usingSerialNumber ) {
		SearchStructure searchLayout = new SearchStructure();
			
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.Productname );
		columns.add( getSerialNumber( usingSerialNumber ) );
		if( isAnEndUser ) {
			columns.add( ColumnDefinition.CustomerReferenceNumber );
		} else {
			columns.add( ColumnDefinition.EndUserName );
		}
		
		columns.add( ColumnDefinition.OrderNumber );
		columns.add( ColumnDefinition.ProductType );
		columns.add( ColumnDefinition.ProductStatus );
		columns.add( ColumnDefinition.ProductLastInspectionDate );
		
		columns.add( ColumnDefinition.InspectionsLink );
		searchLayout.setColumns( columns );
		
		return searchLayout;
	}
	
}

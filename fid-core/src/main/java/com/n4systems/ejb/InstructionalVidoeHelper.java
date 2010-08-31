package com.n4systems.ejb;

import java.util.List;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.InstructionalVideo;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

public class InstructionalVidoeHelper {
	
	private PersistenceManager persistenceManager;

	public InstructionalVidoeHelper( PersistenceManager persistenceManager ) {
		super();
		this.persistenceManager = persistenceManager;
	}
	
	
	public Pager<InstructionalVideo> getPage( int pageNumber, int pageSize ) throws InvalidQueryException {
		QueryBuilder<InstructionalVideo> queryBuilder = new QueryBuilder<InstructionalVideo>( InstructionalVideo.class, new OpenSecurityFilter());
		queryBuilder.addOrder( "created", false );
		
		return  persistenceManager.findAllPaged( queryBuilder, pageNumber, pageSize );
	}
	
	
	public List<InstructionalVideo> getSummary() throws InvalidQueryException{
		Pager<InstructionalVideo> page =  getPage( 1, 5 );
		if( page != null ) {
			return page.getList();
		} 
		return null;
	}
}

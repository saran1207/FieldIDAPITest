package com.n4systems.fieldid.actions;

import org.apache.log4j.Logger;

import rfid.web.helper.Constants;

import com.n4systems.ejb.InstructionalVidoeHelper;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.InstructionalVideo;
import com.n4systems.tools.Pager;

public class InstructionalVideoAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger( InstructionalVideoAction.class );

	private PersistenceManager persistenceManager;
	
	private Pager<InstructionalVideo> page;
	protected Integer currentPage;
	
	public InstructionalVideoAction( PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.persistenceManager = persistenceManager;
	}

	
	public String doList() {
		try {
			page = new InstructionalVidoeHelper( persistenceManager ).getPage( getCurrentPage(), Constants.PAGE_SIZE );
			return SUCCESS;
		} catch( Exception e ) {
			logger.error( "couldn't load the list of instructionalvideos ", e );
		}
		addActionError( getText( "error.failedtoload" ) );
		return ERROR;
	}



	
	
	
	public Integer getCurrentPage() {
		if (currentPage == null ) {
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage( Integer pageNumber ) {
		this.currentPage = pageNumber;
	}

	public Pager<InstructionalVideo> getPage() {
		return page;
	}

	
	
}

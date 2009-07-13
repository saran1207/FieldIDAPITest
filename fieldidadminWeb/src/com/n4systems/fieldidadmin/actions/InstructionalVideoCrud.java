package com.n4systems.fieldidadmin.actions;

import org.apache.log4j.Logger;

import com.n4systems.ejb.InstructionalVidoeHelper;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.InstructionalVideo;
import com.n4systems.tools.Pager;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class InstructionalVideoCrud extends ActionSupport implements Preparable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger( InstructionalVideoCrud.class );
	
	private PersistenceManager persistenceManager;
	
	private Long id;
	
	private InstructionalVideo video;
	
	private Pager<InstructionalVideo> page;
	private Integer pageNumber;
	
	private String currentAction;
	
	public void prepare() throws Exception {
		if( id != null ) {
			video = persistenceManager.find( InstructionalVideo.class, id );
		} else {
			video = new InstructionalVideo();
		}
	}

	public String doList(){
		try {
			page = new InstructionalVidoeHelper( persistenceManager ).getPage( getPageNumber(), 10 );
			return SUCCESS;
		} catch( InvalidQueryException iqe ) {
			logger.error( "couldn't load the list of instructionalvideos ", iqe );
		} catch( Exception e ) {
			logger.error( "couldn't load the list of instructionalvideos ", e );
		}
		addActionError( getText( "error.failedtoload" ) );
		return ERROR;
	}
	
	public String doAdd(){
		return SUCCESS;
	}
	
	public String doEdit(){
		return SUCCESS;
	}
	
	public String doSave(){
		try {
			if( video.isNew() ) {
				persistenceManager.save( video );
			} else {
				video = persistenceManager.update( video );
			}
			
			return SUCCESS;
		} catch (Exception e) {
			addActionError( "failed to save video" );
			logger.error( "could not save video", e );
		}
		return ERROR;
	}
	
	public String doDelete(){
		try {
			persistenceManager.delete( video );
			return SUCCESS;
		} catch (Exception e) {
			logger.error( "could not delete video", e );
			addActionError( "failed to delete" );
			return ERROR;
		}
	}

	public void setPersistenceManager( PersistenceManager persistenceManager ) {
		this.persistenceManager = persistenceManager;
	}

	public Integer getPageNumber() {
		if( pageNumber == null ) {
			pageNumber = 1;
		}
		return pageNumber;
	}

	public void setPageNumber( Integer pageNumber ) {
		this.pageNumber = pageNumber;
	}

	public InstructionalVideo getVideo() {
		return video;
	}

	public Pager<InstructionalVideo> getPage() {
		return page;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction( String currentAction ) {
		this.currentAction = currentAction;
	}

	
	
}

package com.n4systems.fieldid.actions.downloaders;



import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Organization;
import com.n4systems.model.TenantOrganization;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class DownloadOrganizationImages extends DownloadAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( DownloadOrganizationImages.class );

	private Organization organization;
	private TenantOrganization tenant;
	
	private String tenantName;
	
	public DownloadOrganizationImages(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}


	@Override
	public String doDownload() {
		if( uniqueID != null ) {
			tenant = persistenceManager.find(TenantOrganization.class, uniqueID);
		} else if( tenantName != null ) {
			QueryBuilder<TenantOrganization> queryBuilder = new QueryBuilder<TenantOrganization>( TenantOrganization.class );
			queryBuilder.addWhere(WhereParameter.Comparator.EQ, "name", "name", tenantName, WhereParameter.IGNORE_CASE);
			queryBuilder.setSimpleSelect();
			try {
				tenant = persistenceManager.find( queryBuilder );
			} catch( InvalidQueryException i ) {
				logger.error( "failed find tenant" );
				return ERROR;
			}
			
		}
		
		if(tenant != null) {
			File logo = PathHandler.getTenantLogo(tenant);
			if (!logo.exists()) {
				logger.error("No logo image found at [" + logo.getAbsolutePath() + "]");
				return ERROR;
			}
			
			InputStream input = null;
			try {
				fileName = "tenant.gif";
				
				input = new FileInputStream(logo);
				sendFile( input );
			} catch (IOException ioe ) {
				logger.error("Failed sending Tenant logo image", ioe);
				return ERROR;
			} finally {
				IOUtils.closeQuietly(input);
			}
			
			
		} else {
			logger.error("Tenant was null");
			return ERROR;
		}
		
		return null;
	}

	
	public String doDownloadCertLogo() {
		if( uniqueID != null ) {
			try {
				QueryBuilder<Organization> builder = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().setDefaultTargets());
				organization = persistenceManager.find(builder.addSimpleWhere("id", uniqueID));
			} catch(InvalidQueryException e) {
				logger.error("Unable to load Organization", e);
			}
			
			if(organization != null) {
				File certLogoFile = PathHandler.getCertificateLogo(organization);
				if (!certLogoFile.exists()) {
					logger.error("No certificate logo file found at [" + certLogoFile.getAbsolutePath() + "]");
					return ERROR;
				}
	
				InputStream input = null;
				try {
					fileName = "certlogo.gif";
					
					input = new FileInputStream(certLogoFile);
					sendFile( input );
				} catch (IOException ioe) {
					logger.error("Failed sending certificate logo image", ioe);
					return ERROR;
				} finally {
					IOUtils.closeQuietly(input);
				}
			} else {
				logger.error( "failed find organization" );
				return ERROR;
			}
		}
		return null;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName( String tenantName ) {
		this.tenantName = tenantName;
	}
	
}

package com.n4systems.fieldid.actions.downloaders;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.n4systems.services.TenantFinder;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.reporting.PathHandler;

public class DownloadOrganizationImages extends DownloadAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( DownloadOrganizationImages.class );

	private InternalOrg organization;
	private Tenant tenant;
	
	private String tenantName;
	
	public DownloadOrganizationImages(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doDownload() {
		if( uniqueID != null ) {
			tenant = TenantFinder.getInstance().findTenant(uniqueID);
		} else if( tenantName != null ) {
			tenant = TenantFinder.getInstance().findTenant(tenantName);
		}
		
		if(tenant != null) {
			File logo = PathHandler.getTenantLogo(tenant);
			if (!logo.exists()) {
				logger.debug("No logo image found at [" + logo.getAbsolutePath() + "]");
				logo = getHouseAccountLogoPath(); 
				
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
	
	private File getHouseAccountLogoPath() {
		Tenant houseAccount = TenantFinder.getInstance().findTenant(getHouseAccountName());
		return PathHandler.getTenantLogo(houseAccount);
	}

	public String doDownloadCertLogo() {
		if( uniqueID != null ) {
			organization = (InternalOrg)getLoaderFactory().createFilteredIdLoader(BaseOrg.class).setId(uniqueID).load();
			
			if(organization != null) {
				File certLogoFile = PathHandler.getCertificateLogo(organization, false);
				
				if (!certLogoFile.exists()) {
					logger.debug("No certificate logo file found at [" + certLogoFile.getAbsolutePath() + "]");
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
				logger.error("Could not find organization");
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

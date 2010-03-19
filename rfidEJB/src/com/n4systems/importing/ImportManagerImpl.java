package com.n4systems.importing;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.FileImportException;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.persistence.QueryBuilder;

@Interceptors({TimingInterceptor.class})
@Stateless
public class ImportManagerImpl implements ImportManager {
	private Logger logger = Logger.getLogger(ImportManager.class);
	
	@Resource
	private SessionContext context;
	
	@PersistenceContext (unitName="rfidEM")
	private EntityManager em;
	
	@EJB
	private PersistenceManager persistenceManager;
	
	/**
	 * Imports observations from a CSV formatted file.<br/>
	 * Format: <code>inspection type name, section name, criteria name, &lt;R/D&gt;, observation text</code>
	 * 
	 * @param tenantId			The id of the Tenant
	 * @param observationsFile	The observation csv file
	 */
	public long importObservations(Long tenantId, File observationsFile) throws FileImportException {
		Reader fRead = null;
		CSVReader csvRead = null;
		
		InspectionType type;
		CriteriaSection section;
		Criteria criteria;
		String observationType;
		String observationText;
		
		logger.info("Starting observation import of file " + observationsFile.getName());

		String[] lineParts;
		Long lineNumber = 0L;
		Long observationsImported = 0L;
		try {
			fRead = new FileReader(observationsFile);
			csvRead = new CSVReader(fRead);
			
			// we're going to need all the inspection types for this tenant.  Let's find them now.
			SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
			
			QueryBuilder<InspectionType> builder = new QueryBuilder<InspectionType>(InspectionType.class, filter);
			builder.setSimpleSelect();
			List<InspectionType> inspectionTypes = persistenceManager.findAll(builder);
			
			// read to end of file
			while((lineParts = csvRead.readNext()) != null) {
				lineNumber++;
				
				logger.debug("Started processing Line: " + lineNumber);
				
				// check that we have enough parts.
				if(lineParts.length != OBS_LINE_PARTS) {
					throw new FileImportException("Bad Line Format.  Observation lines must have exactly " + OBS_LINE_PARTS + " columns");
				}
				
				// let's try and find our inspection type
				type = FuzzyResolver.resolve(lineParts[OBS_INSPECTION_TYPE_NAME], inspectionTypes, "name");
				
				if(type == null) {
					throw new FileImportException("Unable to find InspectionType named [" + lineParts[OBS_INSPECTION_TYPE_NAME] + "] for Tenant [" + tenantId + "]");
				}
				
				logger.debug("Found InspectionType [" + type.getName() + "]");
				
				// now let's try and resolve the section
				section = FuzzyResolver.resolve(lineParts[OBS_SECTION_NAME], type.getSections(), "title");
				
				if(section == null) {
					throw new FileImportException("Unable to find CriteriaSection named [" + lineParts[OBS_SECTION_NAME] + "] for Tenant [" + tenantId + "] and InspectionType [" + type.getName() + "]");
				}
				
				logger.debug("Found CriteriaSection [" + section.getTitle() + "]");
				
				// and now the criteria
				criteria = FuzzyResolver.resolve(lineParts[OBS_CRITERIA_NAME], section.getCriteria(), "displayText");
				
				if(criteria == null) {
					throw new FileImportException("Unable to find Criteria named [" + lineParts[OBS_CRITERIA_NAME] + "] for Tenant [" + tenantId + "], InspectionType [" + type.getName() + "] and Section [" + section.getTitle() + "]");
				}
				
				logger.debug("Found Criteria [" + criteria.getDisplayText() + "]");
				
				// we should also make sure the observation text is not empty
				observationText = lineParts[OBS_TEXT].trim();
				
				if(observationText.length() == 0) {
					throw new FileImportException("Observation text cannot be blank");
				}
				
				// we've found all the parts we need, now let's add our observation to the criteria
				observationType = lineParts[OBS_TYPE].toUpperCase();
				if(observationType.equals(OBS_TYPE_REC)) {
					
					logger.debug("Adding Recommendation [" + observationText + "]");
					criteria.getRecommendations().add(observationText);
					
				} else if(observationType.equals(OBS_TYPE_DEF)) {
					
					logger.debug("Adding Deficiency [" + observationText + "]");
					criteria.getDeficiencies().add(observationText);
					
				} else {
					throw new FileImportException("Unknown Observation type [" + lineParts[OBS_TYPE] + "].  Type must be one of '" + OBS_TYPE_REC + "' or '" + OBS_TYPE_DEF + "'");
				}
				
				logger.debug("Updating critieria");
				// now we can update our criteria
				em.merge(criteria);
				observationsImported++;
				
				logger.debug("Updating InspectionType");
				// we should also update the mod time on our inspection type so that mobile knows to get a new one
				type.touch();
				em.merge(type);
				
				logger.debug("Completed processing Line: " + lineNumber);
			}
			
			logger.info("Completed processing file [" + observationsFile.getName() + "]");
			
		} catch(FileImportException e) {
			// set the line number and rethrow
			e.setLineNumber(lineNumber);
			logger.error(e);
			
			// also need to roll back the transaction
			context.setRollbackOnly();
			throw e;
			
		} catch(Exception e) {
			
			// wrap anything else and throw it
			logger.error(e);
			// also need to roll back the transaction
			context.setRollbackOnly();
			throw new FileImportException("Failed observation import", e, lineNumber);
			
		} finally {
			IOUtils.closeQuietly(fRead);
		}
		
		logger.info(observationsImported + " observations imported");
		return observationsImported;
	}
	
}

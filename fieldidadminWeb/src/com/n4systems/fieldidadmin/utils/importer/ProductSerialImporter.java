package com.n4systems.fieldidadmin.utils.importer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import javax.naming.NamingException;

import org.ho.yaml.Yaml;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.ProductManager;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.FindOrCreateCustomerOrgHandler;
import com.n4systems.model.orgs.FindOrCreateDivisionOrgHandler;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.producttype.ProductTypeLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.DivisionOrgByCustomerListLoader;
import com.n4systems.persistence.loaders.TenantFilteredListLoader;
import com.n4systems.util.ServiceLocator;

public class ProductSerialImporter extends Importer {

	public static final String FILE_NAME_PREFIX = "productSerials";
	public static final String SERIAL_NUMBER = "serialNumber";
	public static final String ENDUSER_IDENTIFIER = "endUserIdentifier";
	public static final String PRODUCT_TYPE = "productType";
	public static final String COMMENT = "comment";
	public static final String INFO_FIELDS = "infoFields";
	public static final String DIVISION = "divisionIdentifier";
	public static final String INFO_NAME = "name";
	public static final String INFO_VALUE = "value";
	public static final String FAILURE_REASON = "failureReason";
	public static final String DESCRIPTION = "description";
	public static final String IDENTIFIED_DATE = "identified";
	public static final String JOB_SITE = "jobSite";
	public static final String RFID = "rfid";

	private LegacyProductType productTypeManager;
	private LegacyProductSerial productSerialManager;
	private ProductManager productManager;

	public ProductSerialImporter(File importerBaseDirectory, PrimaryOrg primaryOrg, boolean createMissingDivisions)
			throws NamingException {
		super(importerBaseDirectory, primaryOrg, createMissingDivisions);

		this.productTypeManager = ServiceLocator.getProductType();
		this.productSerialManager = ServiceLocator.getProductSerialManager();
		this.productManager = ServiceLocator.getProductManager();
	}

	public static Collection<File> filesProcessing(Tenant tenant, File importerBaseDirectory) {
		File processingDirectory = new File(processingDirectoryName(tenant, importerBaseDirectory));

		Collection<File> availableFiles = new ArrayList<File>();
		File[] files = processingDirectory.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().startsWith(FILE_NAME_PREFIX)) {
					availableFiles.add(files[i]);
				}
			}
		}
		return availableFiles;
	}

	public static Collection<File> filesAvailableForProcessing(Tenant tenant, File importerBaseDirectory) {
		File uploadDirectory = new File(uploadDirectoryName(tenant, importerBaseDirectory));

		Collection<File> availableFiles = new ArrayList<File>();
		File[] files = uploadDirectory.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().startsWith(FILE_NAME_PREFIX)) {
					availableFiles.add(files[i]);
				}
			}
		}
		return availableFiles;
	}

	@SuppressWarnings("unchecked")
	public int processFile() {

		try {
			Collection<Map<String, Object>> productSerials = (Collection<Map<String, Object>>) Yaml.load(currentFile);

			for (Map<String, Object> productSerial : productSerials) {
				try {
					Product ps = new Product();
					
					// initially default the ps to the primary org, this may or may not get overriden 
					ps.setOwner(primaryOrg);
					
					String serialNumber = (String) productSerial.get(SERIAL_NUMBER);
					
					TenantFilteredListLoader<CustomerOrg> customerLoader = new TenantFilteredListLoader<CustomerOrg>(primaryOrg.getTenant(), CustomerOrg.class);
					FindOrCreateCustomerOrgHandler customerSearcher = new FindOrCreateCustomerOrgHandler(customerLoader, new OrgSaver());
					
					CustomerOrg customer = customerSearcher.findOnly(primaryOrg, (String)productSerial.get(ENDUSER_IDENTIFIER));
					
					if (customer == null) {
						throw new Exception("no customer");
					} else {
						ps.setOwner(customer);
					}

					if (productManager.findProductBySerialNumber(serialNumber, primaryOrg.getTenant().getId(), (ps.getOwner() != null) ? ps.getOwner().getId() : null) == null) {

						ProductType productType = productTypeManager.findProductTypeForItemNum((String) productSerial.get(PRODUCT_TYPE), primaryOrg.getTenant().getId());
						if (productType == null) {
							throw new Exception("no product type");
						} else {
							productType = new ProductTypeLoader(primaryOrg.getTenant().getId()).setId(productType.getId()).setStandardPostFetches().load();
							ps.setType(productType);
						}

						if ((String) productSerial.get(DIVISION) != null) {
							
							DivisionOrgByCustomerListLoader divisionLoader = new DivisionOrgByCustomerListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant()));
							divisionLoader.setCustomer(customer);
							
							FindOrCreateDivisionOrgHandler divisionSearcher = new FindOrCreateDivisionOrgHandler(divisionLoader, new OrgSaver());
							divisionSearcher.setFindOnly(!createMissingDivisions);
		
							DivisionOrg division = divisionSearcher.findOrCreate(customer, (String)productSerial.get(DIVISION));

							if (division == null) {
								if (createMissingDivisions) {
									throw new Exception("no division");
								}
							} else {
								ps.setOwner(division);
							}
						}

						ps.setSerialNumber(serialNumber);
						ps.setComments((String) productSerial.get(COMMENT));
						ps.setTenant(primaryOrg.getTenant());
						if (productSerial.get(IDENTIFIED_DATE) != null) {
							ps.setIdentified((Date) productSerial.get(IDENTIFIED_DATE));
						}

						ps.setRfidNumber((String) productSerial.get(RFID));

						Collection<InfoOptionBean> options = processInfoFields(productSerial, ps);
						ps.setInfoOptions(new TreeSet(options));
						ps = productSerialManager.create(ps);

						successes.add(productSerial);
					} else {
						matches.add(productSerial);
					}
				} catch (Exception e) {

					StringWriter message = new StringWriter();
					e.printStackTrace(new PrintWriter(message));

					productSerial.put(FAILURE_REASON, message.toString());
					exceptions.add(productSerial);
					e.printStackTrace();

				}
			}
			return exceptions.size();
		} catch (Exception e) {
			return -1;
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<InfoOptionBean> processInfoFields(Map<String, Object> productSerial, Product ps)
			throws Exception {
		Collection<Map<String, Object>> infoFields = (Collection<Map<String, Object>>) productSerial.get(INFO_FIELDS);
		Collection<InfoFieldBean> infoFieldBeans = ps.getType().getInfoFields();
		Collection<InfoOptionBean> infoOptions = new ArrayList<InfoOptionBean>();
		if (infoFields != null) {
			for (Map<String, Object> infoField : infoFields) {
				InfoFieldBean targetInfoField = null;
				InfoOptionBean targetInfoOption = null;
				String name = (String) infoField.get(INFO_NAME);
				String value = (String) infoField.get(INFO_VALUE);
				boolean foundField = false;
				boolean foundOption = false;

				for (InfoFieldBean infoFieldBean : infoFieldBeans) {

					if (name.equalsIgnoreCase(infoFieldBean.getName())) {
						foundField = true;
						targetInfoField = infoFieldBean;

						// match static data.
						if (targetInfoField.hasStaticInfoOption()) {
							for (InfoOptionBean infoOptionBean : targetInfoField.retrieveInfoOptions()) {
								if (value != null && value.equalsIgnoreCase(infoOptionBean.getName())) {
									foundOption = true;
									targetInfoOption = infoOptionBean;
									infoOptions.add(targetInfoOption);
									break;
								}
							}
						}

						// fill in dynamic data
						if (foundOption == false) {
							if (infoFieldBean.acceptsDyanmicInfoOption()) {
								targetInfoOption = new InfoOptionBean();
								targetInfoOption.setInfoField(infoFieldBean);
								targetInfoOption.setName(value);
								targetInfoOption.setStaticData(false);
								infoOptions.add(targetInfoOption);
								foundOption = true;
							}
						}
						break;
					}
				}

				if (foundField == false || foundOption == false) {
					if (name != null && value != null) {
						ps.setComments(ps.getComments() + "\n\n" + name + ": " + value);
					}
				}
			}
		}
		return infoOptions;

	}
}

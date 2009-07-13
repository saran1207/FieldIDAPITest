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

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.JobSite;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.TenantOrganization;
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
	private CustomerManager customerManager;
	private LegacyProductSerial productSerialManager;
	private ProductManager productManager;
	private PersistenceManager persistenceManager;

	public ProductSerialImporter(File importerBaseDirectory, TenantOrganization tenant, boolean createMissingDivisions)
			throws NamingException {
		super(importerBaseDirectory, tenant, createMissingDivisions);

		this.productTypeManager = ServiceLocator.getProductType();
		this.customerManager = ServiceLocator.getCustomerManager();
		this.productSerialManager = ServiceLocator.getProductSerialManager();
		this.productManager = ServiceLocator.getProductManager();
		this.persistenceManager = ServiceLocator.getPersistenceManager();
	}

	public static Collection<File> filesProcessing(TenantOrganization tenant, File importerBaseDirectory) {
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

	public static Collection<File> filesAvailableForProcessing(TenantOrganization tenant, File importerBaseDirectory) {
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
					String serialNumber = (String) productSerial.get(SERIAL_NUMBER);
					if (tenant.hasExtendedFeature(ExtendedFeature.JobSites)) {
						if (productSerial.get(JOB_SITE) != null) {
							JobSite jobSite = persistenceManager.findByName(JobSite.class, tenant.getId(), (String)productSerial.get(JOB_SITE));
							if (jobSite != null) {
								ps.setJobSite(jobSite);
								ps.setOwner(jobSite.getCustomer());
								ps.setDivision(jobSite.getDivision());
							}
						}
						
						if (ps.getJobSite() == null) {
							throw new Exception("no job site");
						}
					} else {
						Customer customer = customerManager.findCustomerFussySearch((String) productSerial
								.get(ENDUSER_IDENTIFIER), (String) productSerial.get(ENDUSER_IDENTIFIER), tenant.getId(),
								null);
						if (customer == null) {
							throw new Exception("no customer");
						} else {
							ps.setOwner(customer);
						}
					}

					if (productManager.findProductBySerialNumber(serialNumber, tenant.getId(), (ps.getOwner() != null) ? ps.getOwner().getId() : null) == null) {

						ProductType productType = productTypeManager.findProductTypeForItemNum((String) productSerial
								.get(PRODUCT_TYPE), tenant.getId());
						if (productType == null) {
							throw new Exception("no product type");
						} else {
							productType = productTypeManager.findProductTypeAllFields(productType.getId(), tenant
									.getId());
							ps.setType(productType);
						}

						if ((String) productSerial.get(DIVISION) != null) {
							Division division;
							if (createMissingDivisions) {
								division = customerManager.findOrCreateDivision((String) productSerial.get(DIVISION),
										ps.getOwner().getId(), null);
							} else {
								division = customerManager.findDivision((String) productSerial.get(DIVISION), ps.getOwner(),
										null);
							}

							if (division == null) {
								if (createMissingDivisions) {
									throw new Exception("no division");
								}
							} else {
								ps.setDivision(division);
							}
						}

						ps.setSerialNumber(serialNumber);

						ps.setOrganization(tenant);

						ps.setComments((String) productSerial.get(COMMENT));
						ps.setTenant(tenant);
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

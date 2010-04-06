package com.n4systems.fieldidadmin.utils.importer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.ho.yaml.Yaml;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.User;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.Status;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspectionbook.InspectionBookByNameLoader;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypesLoader;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.FindOrCreateCustomerOrgHandler;
import com.n4systems.model.orgs.FindOrCreateDivisionOrgHandler;
import com.n4systems.model.orgs.FindOrCreateExternalOrgHandler;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.TenantFilteredListLoader;
import com.n4systems.util.ServiceLocator;

public class InspectionImporter extends Importer {

	
	public static final String FILE_NAME_PREFIX = "inspections";

	public static final String SERIAL_NUMBER = "serialNumber";
	public static final String ENDUSER_IDENTIFIER = "endUserIdentifier";
	public static final String EVENT_TYPE = "type";
	public static final String COMMENT = "comment";
	public static final String DIVISION = "divisionIdentifier";
	public static final String FAILURE_REASON = "failureReason";
	public static final String INSPECTION_DATE = "inspectionDate";
	public static final String LOCATION = "location";
	public static final String INSPECTOR = "inspector";
	public static final String DEFAULT_INSPECTOR = "n4systems";
	public static final String RESULT = "result";
	public static final String PASS = "PASS";
	public static final String INSPECTION_BOOK = "inspectionBook";
	public static final String NEXT_INSPECTION_DATE = "nextInspectionDate";
	public static final String PRODUCT_STATUS = "productStatus";
	
	private LegacyProductSerial productSerialManager;
	private ProductManager productManager;
	private User userManager;
	private InspectionManager inspectionManager;

	public InspectionImporter(File importerBaseDirectory, PrimaryOrg primaryOrg, boolean createMissingDivisions)
			throws NamingException {

		super(importerBaseDirectory, primaryOrg, createMissingDivisions);

		this.productSerialManager = ServiceLocator.getProductSerialManager();
		this.productManager = ServiceLocator.getProductManager();
		this.userManager = ServiceLocator.getUser();
		this.inspectionManager = ServiceLocator.getInspectionManager();

	}

	public static Collection<File> filesProcessing(Tenant man, File importerBaseDirectory) {
		File processingDirectory = new File(processingDirectoryName(man, importerBaseDirectory));

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
			Collection<Map<String, Object>> inspections = (Collection<Map<String, Object>>) Yaml.load(currentFile);

			for (Map inspectionMap : inspections) {
				try {

					Inspection inspection = new Inspection();
					inspection.setTenant(primaryOrg.getTenant());

					String serialNumber = (String) inspectionMap.get(SERIAL_NUMBER);

					TenantFilteredListLoader<CustomerOrg> customerLoader = new TenantFilteredListLoader<CustomerOrg>(primaryOrg.getTenant(), CustomerOrg.class);
					FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> customerSearcher = new FindOrCreateCustomerOrgHandler(customerLoader, new OrgSaver());

					CustomerOrg endUser = customerSearcher.find(primaryOrg, (String)inspectionMap.get(ENDUSER_IDENTIFIER));
					
					if (endUser == null) {
						throw new Exception("no end user");
					}
					
					inspection.setOwner(endUser);


					Product product = productManager.findProductBySerialNumber(serialNumber, primaryOrg.getTenant().getId(), endUser.getId());
					if (product == null) {
						throw new Exception("no product ");
					}

					if ((String) inspectionMap.get(PRODUCT_STATUS) != null) {
						try {
							inspection.setProductStatus(productSerialManager.FindProductStatusByName(primaryOrg.getTenant().getId(), (String)inspectionMap.get(PRODUCT_STATUS)));
						} catch (Exception e) {
							throw new Exception("failed to load product status");
						}
					} else {
						inspection.setProductStatus(product.getProductStatus());
					}

					Date inspectionDate = null;

					try {
						inspectionDate = (Date) inspectionMap.get(INSPECTION_DATE);
					} catch (ClassCastException cce) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						try {
							inspectionDate = df.parse((String) inspectionMap.get(INSPECTION_DATE));
						} catch (Exception e) {
							System.out.println("failed to parse inspection date.");
						}
					}

					if (inspectionDate == null) {
						throw new Exception("invalid date");
					}

					Date nextInspectionDate = null;

					try {
						nextInspectionDate = (Date) inspectionMap.get(NEXT_INSPECTION_DATE);
					} catch (ClassCastException cce) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						try {
							nextInspectionDate = df.parse((String) inspectionMap.get(NEXT_INSPECTION_DATE));
						} catch (Exception e) {
							throw new Exception("invalid next date");
						}

					}

					String inspectionBookName = (String) inspectionMap.get(INSPECTION_BOOK);
					if (inspectionBookName != null) {
						try {
							// TODO there should be a security filter created in the importer.
							SecurityFilter filter = new OwnerAndDownFilter(primaryOrg);
							
							InspectionBookByNameLoader inspectionBookLoader = new InspectionBookByNameLoader(filter);
							inspectionBookLoader.setName(inspectionBookName);
							inspectionBookLoader.setOwner(endUser);
							
							InspectionBook inspectionBook = inspectionBookLoader.load();
							
							if (inspectionBook != null) {
								inspection.setBook(inspectionBook);
							}
						} catch (Exception e) {
							throw new Exception("invalid inspection book");
						}

					}

					if ((String)inspectionMap.get(DIVISION) != null) {

						DivisionOrgByCustomerListLoader divisionLoader = new DivisionOrgByCustomerListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant()));
						divisionLoader.setCustomer(endUser);
						
						FindOrCreateExternalOrgHandler<DivisionOrg, CustomerOrg> divisionSearcher = new FindOrCreateDivisionOrgHandler(divisionLoader, new OrgSaver());
						DivisionOrg division;
						if (createMissingDivisions) {
							division = divisionSearcher.findOrCreate(endUser, (String)inspectionMap.get(DIVISION));
						} else {
							division = divisionSearcher.find(endUser, (String)inspectionMap.get(DIVISION));
						}
						
						if (division == null) {
							if (createMissingDivisions) {
								throw new Exception("no division");
							}

						} else {
							inspection.setOwner(division);
						}
					}
					
					inspection.setLocation((String) inspectionMap.get(LOCATION));
					inspection.setDate(inspectionDate);
					inspection.setProduct(product);

					String inspectionTypeName = (String) inspectionMap.get(EVENT_TYPE);
					InspectionType inspectionType = findInspectionType(product, inspectionTypeName);
					if (inspectionType == null) {
						throw new Exception("Could not find InspectionType: [" + inspectionTypeName
								+ "] for ProductType: [" + product.getType().getName() + "]");
					}

					UserBean inspector = userManager.findUserBeanByID(primaryOrg.getTenant().getName(), (String) inspectionMap
							.get(INSPECTOR));
					if (inspector == null) {
						inspector = userManager.findUserBeanByID(primaryOrg.getTenant().getName(), DEFAULT_INSPECTOR);
						if (inspector == null) {
							throw new Exception("no Inspector found, default also not found.");
						}
					}

					inspection.setType(inspectionType);
					inspection.setInspector(inspector);
					inspection.setComments((String) inspectionMap.get(COMMENT));

					if (inspectionMap.get(RESULT) != null) {
						if (((String) inspectionMap.get(RESULT)).equalsIgnoreCase(PASS)) {
							inspection.setStatus(Status.PASS);
						} else {
							inspection.setStatus(Status.FAIL);
						}
					}

					inspectionManager.createInspection(inspection, nextInspectionDate, inspector.getUniqueID());
					successes.add(inspectionMap);

				} catch (Exception e) {

					StringWriter message = new StringWriter();
					e.printStackTrace(new PrintWriter(message));
					inspectionMap.put(FAILURE_REASON, message.toString());
					exceptions.add(inspectionMap);
					e.printStackTrace();
				}
			}
			return exceptions.size();
		} catch (Exception e) {
			return -1;
		}
	}

	private InspectionType findInspectionType(Product product, String name) throws NamingException {
		TenantOnlySecurityFilter tenantFilter = new TenantOnlySecurityFilter(product.getTenant().getId());
		
		List<AssociatedInspectionType> associatedInspectionTypes = new AssociatedInspectionTypesLoader(tenantFilter)
																		.setProductType(product.getType())
																		.load();
		
		for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes) {
			if (associatedInspectionType.getInspectionType().getName().equalsIgnoreCase(name)) {
				return associatedInspectionType.getInspectionType();
			}
		}

		return null;
	}
}

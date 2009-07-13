package com.n4systems.fieldidadmin.utils.importer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.naming.NamingException;

import org.ho.yaml.Yaml;

import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.User;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.Status;
import com.n4systems.model.TenantOrganization;
import com.n4systems.util.SecurityFilter;
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

	private CustomerManager endUserManager;
	private LegacyProductSerial productSerialManager;
	private ProductManager productManager;
	private User userManager;
	private InspectionManager inspectionManager;

	public InspectionImporter(File importerBaseDirectory, TenantOrganization tenant, boolean createMissingDivisions)
			throws NamingException {

		super(importerBaseDirectory, tenant, createMissingDivisions);

		this.endUserManager = ServiceLocator.getCustomerManager();
		this.productSerialManager = ServiceLocator.getProductSerialManager();
		this.productManager = ServiceLocator.getProductManager();
		this.userManager = ServiceLocator.getUser();
		this.inspectionManager = ServiceLocator.getInspectionManager();

	}

	public static Collection<File> filesProcessing(TenantOrganization man, File importerBaseDirectory) {
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
			Collection<Map<String, Object>> inspections = (Collection<Map<String, Object>>) Yaml.load(currentFile);

			for (Map inspectionMap : inspections) {
				try {

					Inspection inspection = new Inspection();
					inspection.setTenant(tenant);

					String serialNumber = (String) inspectionMap.get(SERIAL_NUMBER);

					Customer endUser = endUserManager.findCustomerFussySearch((String) inspectionMap
							.get(ENDUSER_IDENTIFIER), (String) inspectionMap.get(ENDUSER_IDENTIFIER), tenant.getId(),
							null);
					if (endUser == null) {
						throw new Exception("no end user");
					}

					Product product = productManager.findProductBySerialNumber(serialNumber, tenant.getId(), endUser
							.getId());
					if (product == null) {
						throw new Exception("no product ");
					}

					ProductStatusBean productStatus = null;
					if ((String) inspectionMap.get(PRODUCT_STATUS) != null) {
						try {
							productStatus = productSerialManager.FindProductStatusByName(tenant.getId(),
									(String) inspectionMap.get(PRODUCT_STATUS));
						} catch (Exception e) {
							throw new Exception("failed to load product status");
						}
					} else {
						productStatus = product.getProductStatus();
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
							// look up inspection book by name and customer.
							SecurityFilter filter = new SecurityFilter(tenant.getId(), endUser.getId());
							InspectionBook inspectionBook = inspectionManager.findInspectionBook(inspectionBookName,
									filter);
							if (inspectionBook != null) {
								inspection.setBook(inspectionBook);
							}
						} catch (Exception e) {
							throw new Exception("invalid inspection book");
						}

					}

					if ((String) inspectionMap.get(DIVISION) != null) {
						Division division;
						if (createMissingDivisions) {
							division = endUserManager.findOrCreateDivision((String) inspectionMap.get(DIVISION),
									endUser.getId(), null);
						} else {
							division = endUserManager.findDivision((String) inspectionMap.get(DIVISION), endUser
									.getId(), null);
						}

						if (division == null) {
							if (createMissingDivisions) {
								throw new Exception("no division");
							}

						} else {
							inspection.setDivision(division);
						}
					}

					inspection.setOrganization(tenant);
					inspection.setLocation((String) inspectionMap.get(LOCATION));
					inspection.setCustomer(endUser);
					inspection.setDate(inspectionDate);
					inspection.setProduct(product);

					String inspectionTypeName = (String) inspectionMap.get(EVENT_TYPE);
					InspectionType inspectionType = findInspectionType(product, inspectionTypeName);
					if (inspectionType == null) {
						throw new Exception("Could not find InspectionType: [" + inspectionTypeName
								+ "] for ProductType: [" + product.getType().getName() + "]");
					}

					UserBean inspector = userManager.findUserBeanByID(tenant.getName(), (String) inspectionMap
							.get(INSPECTOR));
					if (inspector == null) {
						inspector = userManager.findUserBeanByID(tenant.getName(), DEFAULT_INSPECTOR);
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

					inspectionManager.createInspection(inspection, productStatus, nextInspectionDate, inspector
							.getUniqueID());
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
		InspectionType type = null;

		ProductType prouductType = ServiceLocator.getProductType().findProductTypeAllFields(product.getType().getId(),
				product.getTenant().getId());
		for (InspectionType inspectionType : prouductType.getInspectionTypes()) {
			if (inspectionType.getName().equalsIgnoreCase(name)) {
				type = inspectionType;
				break;
			}
		}

		return type;
	}
}

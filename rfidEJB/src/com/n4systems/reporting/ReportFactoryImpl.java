package com.n4systems.reporting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.User;

import com.n4systems.ejb.DownloadManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.NonPrintableManufacturerCert;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.JobSite;
import com.n4systems.model.LineItem;
import com.n4systems.model.Organization;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubInspection;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;
import com.n4systems.util.FileHelper;
import com.n4systems.util.ReportMap;

@Interceptors( { TimingInterceptor.class })
@Stateless
public class ReportFactoryImpl implements ReportFactory {

	private static final String n4LogoFileName = "n4_logo.gif";
	private static final String pdfExt = ".pdf";

	private Logger logger = Logger.getLogger(ReportFactory.class);

	@EJB
	private DownloadManager downloadManager;
	@EJB
	private ProductManager productManager;
	@EJB
	private PersistenceManager persistenceManager;
	@EJB
	private LegacyProductType productTypeManger;

	@EJB
	private InspectionScheduleManager inspectionScheduleManager;
	@EJB
	private User userManager;

	/**
	 * @return A new jasper JRPdfExporter with character encoding set to UTF8
	 */
	private JRPdfExporter getPdfExporter() {
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

		return exporter;
	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#generateInspectionCertificateReport(com.n4systems.reporting.InspectionReportType,
	 *      java.util.Collection, java.lang.String, java.lang.String,
	 *      rfid.ejb.entity.UserBean)
	 */
	public String generateInspectionCertificateReport(InspectionReportType type, Collection<Long> inspectionIds, String packageName, UserBean user) throws ReportException, EmptyReportException {
		// XXX - this could probable be improved now the the rest is a little
		// cleaner
		int reportsInFile = 0;
		int totalReports = 0;
		int reportFileCount = 1;
		List<JasperPrint> printList = new ArrayList<JasperPrint>();
		File reportOutputDir = null;
		File reportFile;
		OutputStream reportOut = null;
		File zipFile = null;

		try {
			reportOutputDir = FileHelper.getTempDir(downloadManager.getBaseAccessDir(user));

			reportFile = new File(reportOutputDir, packageName + "_" + reportFileCount + pdfExt);
			reportOut = new FileOutputStream(reportFile);

			List<Inspection> inspections = persistenceManager.findAll(Inspection.class, new HashSet<Long>(inspectionIds), user.getTenant());

			for (Inspection inspection : inspections) {
				logger.debug("Processing Report for Inspection [" + inspection.getId() + "]");

				if (reportsInFile >= ConfigContext.getCurrentContext().getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE)) {
					logger.debug("Exporing report pdf to [" + reportFile.getPath() + "]");
					printToPDF(printList, reportOut);
					reportFileCount++;

					reportsInFile = 0;
					printList = new ArrayList<JasperPrint>();

					reportOut.close();
					reportFile = new File(reportOutputDir, packageName + "_" + reportFileCount + pdfExt);
					reportOut = new FileOutputStream(reportFile);
				}

				try {
					JasperPrint cert = generateInspectionCertificate(type, inspection, user);
					printList.add(cert);
					reportsInFile += cert.getPages().size();
					totalReports += cert.getPages().size();
				} catch (NonPrintableEventType npe) {
					logger.debug("Report for Inspection [" + inspection.getId() + "] is not Printable");
				} catch (ReportException e) {
					logger.warn("Failed generating report for Inspection [" + inspection.getId() + "].  Moving on to next Inspection.", e);
				}
			}

			// export any remaining certs
			if (printList.size() > 0) {
				printToPDF(printList, reportOut);
				reportOut.close();
			} else {
				// there were no certs left, the file created will be empty and
				// should be deleted
				reportOut.close();
				reportFile.delete();
			}

			if (totalReports == 0) {
				throw new EmptyReportException("No printable reports found");
			}

			zipFile = FileHelper.zipDirectory(reportOutputDir, packageName);
		} catch (IOException e) {
			throw new ReportException("error with file access", e);
		} finally {
			// close any streams that haven't been closed yet and clean up the
			// temp dir
			IOUtils.closeQuietly(reportOut);

			if (reportOutputDir != null) {
				try {
					FileUtils.deleteDirectory(reportOutputDir);
				} catch (IOException e) {
					logger.error("could not delete the directory " + reportOutputDir.toString(), e);
				}
			}
		}

		return downloadManager.resolveRelativePath(zipFile);
	}

	public String generateProductCertificateReport(Collection<Long> productIds, String packageName, UserBean user) throws ReportException, EmptyReportException {
		// XXX - this could probable be improved now the the rest is a little
		// cleaner
		int reportsInFile = 0;
		int totalReports = 0;
		int reportFileCount = 1;
		List<JasperPrint> printList = new ArrayList<JasperPrint>();
		File reportOutputDir = null;
		File reportFile;
		OutputStream reportOut = null;
		File zipFile = null;

		try {
			reportOutputDir = FileHelper.getTempDir(downloadManager.getBaseAccessDir(user));

			reportFile = new File(reportOutputDir, packageName + "_" + reportFileCount + pdfExt);
			reportOut = new FileOutputStream(reportFile);

			// builds sets of pdfs.
			for (Long productId : productIds) {
				logger.debug("Processing Report for Product [" + productId + "]");

				if (reportsInFile >= ConfigContext.getCurrentContext().getInteger(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE)) {
					logger.debug("Exporing report pdf to [" + reportFile.getPath() + "]");
					printToPDF(printList, reportOut);
					reportFileCount++;

					reportsInFile = 0;
					printList = new ArrayList<JasperPrint>();

					reportOut.close();
					reportFile = new File(reportOutputDir, packageName + "_" + reportFileCount + pdfExt);
					reportOut = new FileOutputStream(reportFile);
				}

				try {
					printList.add(generateProductCertificate(productId, user));
					reportsInFile++;
					totalReports++;
				} catch (ReportException e) {
					logger.debug("Report for Product [" + productId + "] is NonPrintable");
				}
			}

			// export any remaining certs
			if (printList.size() > 0) {
				printToPDF(printList, reportOut);
				reportOut.close();
			} else {
				// there were no certs left, the file created will be empty and
				// should be deleted
				reportOut.close();
				reportFile.delete();
			}

			if (totalReports == 0) {
				throw new EmptyReportException("No printable reports found");
			}

			zipFile = FileHelper.zipDirectory(reportOutputDir, packageName);
		} catch (IOException e) {
			throw new ReportException("File access product occured", e);
		} finally {
			// close any streams that haven't been closed yet and clean up the
			// temp dir
			IOUtils.closeQuietly(reportOut);

			try {
				FileUtils.deleteDirectory(reportOutputDir);
			} catch (IOException e) {
				logger.error("could not delete the directory " + reportOutputDir.toString(), e);
			}
		}

		return downloadManager.resolveRelativePath(zipFile);

	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#printToPDF(java.util.List,
	 *      java.io.OutputStream)
	 */
	public void printToPDF(List<JasperPrint> printList, OutputStream outputStream) throws ReportException {
		JRPdfExporter exporter = getPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, printList);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new ReportException("Failed to print report", e);
		}
	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#printToPDF(java.util.List)
	 */
	public byte[] printToPDF(List<JasperPrint> printList) throws ReportException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		printToPDF(printList, byteBuffer);
		return byteBuffer.toByteArray();
	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#printToPDF(net.sf.jasperreports.engine.JasperPrint,
	 *      java.io.OutputStream)
	 */
	public void printToPDF(JasperPrint jasperPrint, OutputStream outputStream) throws ReportException {
		JRPdfExporter exporter = getPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new ReportException("Failed to print report", e);
		}
	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#printToPDF(net.sf.jasperreports.engine.JasperPrint)
	 */
	public byte[] printToPDF(JasperPrint jasperPrint) throws ReportException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		printToPDF(jasperPrint, byteBuffer);
		return byteBuffer.toByteArray();
	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#generateInspectionCertificate(com.n4systems.reporting.InspectionReportType,
	 *      java.lang.Long, java.lang.String)
	 */
	public JasperPrint generateInspectionCertificate(InspectionReportType type, Long inspectionId, UserBean user) throws NonPrintableEventType, ReportException {
		Inspection inspection = null;
		try {
			inspection = persistenceManager.find(Inspection.class, inspectionId);
		} catch (Exception e) {
			throw new ReportException("Failed loading Inspection", e);
		}

		return generateInspectionCertificate(type, inspection, user);
	}

	/**
	 * Generates a JasperPrint object for an Inspection report of type
	 * InspectionReportType.
	 * 
	 * @param type
	 *            The type of report to generate
	 * @param inspection
	 *            The inspection to generate the report for
	 * @param dateFormat
	 *            The date format to use in the report
	 * @return A JasperPrint object for the generated report
	 * @throws NonPrintableEventType
	 *             When the inspection is set to not printable
	 *             {@link Inspection#setPrintable(boolean)}
	 * @throws ReportException
	 *             On any problem generating the report.
	 */
	private JasperPrint generateInspectionCertificate(InspectionReportType type, Inspection inspection, UserBean user) throws NonPrintableEventType, ReportException {
		JasperPrint jPrint = null;
		switch (type) {
		case INSPECTION_CERT:
			if (inspection.getType().getGroup().getPrintOut().isWithSubInspections()) {
				jPrint = generateInspectionFullCertificate(inspection, user);
			} else {
				jPrint = generateInspectionCertificate(inspection, user);
			}

			break;
		case OBSERVATION_CERT:
			jPrint = generateObservationCertificate(inspection, user);
			break;
		default:
			throw new ReportException("Unsupported Report Type: " + type.name());
		}
		return jPrint;
	}

	/*
	 * XXX - generateInspectionCertificate, generateInspectionCertificate and
	 * generateInspectionSummaryReport should be extracted further. Too much
	 * duplicate code - mf
	 */

	/**
	 * Generates a JasperPrint object for an Observation report.
	 * 
	 * @param inspection
	 *            The Inspection object to create the report for.
	 * @param dateFormat
	 *            A String date format to be used in the report
	 * @return A jasper print object for the generated report
	 * @throws ReportException
	 *             On any problem generating the report.
	 */
	private JasperPrint generateObservationCertificate(Inspection inspection, UserBean user) throws NonPrintableEventType, ReportException {
		if (!inspection.getType().getGroup().hasObservationPrintOut()) {
			throw new NonPrintableEventType();
		}
		// observation reports are always printable
		File jasperFile = PathHandler.getPrintOutFile(inspection.getType().getGroup().getObservationPrintOut());

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No report file for tenant [" + inspection.getTenant().getName() + "] and Inspection Type Group [" + inspection.getType().getGroup().getName() + "] found at ["
					+ jasperFile.getAbsolutePath() + "]");
		}
		ReportMap<Object> reportMap = new ReportMap<Object>();

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");

		addImageStreams(reportMap, inspection.getTenant(), inspection.getOrganization());

		addTenantParams(reportMap, inspection.getTenant());
		addOrganizationParams(reportMap, inspection.getOrganization());
		addUserParams(reportMap, inspection.getInspector());
		reportMap.putAll(new ProductReportMapProducer(inspection.getProduct(), new DateTimeDefiner(user)).produceMap());
		addOrderParams(reportMap, inspection.getProduct().getShopOrder());
		addProductTypeParams(reportMap, inspection.getProduct().getType());
		addCustomerParams(reportMap, inspection.getCustomer());
		addDivisionParams(reportMap, inspection.getDivision());
		reportMap.putAll(new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap());

		JasperPrint jasperPrint = null;
		try {

			JRDataSource jrDataSource = (JRDataSource) new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap().get("observations");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	private JasperPrint generateInspectionFullCertificate(Inspection inspection, UserBean user) throws NonPrintableEventType, ReportException {
		// If the inspection is not printable, stop immediately
		if (!inspection.isPrintable() || !inspection.getType().getGroup().hasPrintOut()) {
			throw new NonPrintableEventType();
		}

		File jasperFile = PathHandler.getPrintOutFile(inspection.getType().getGroup().getPrintOut());

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No report file for tenant [" + inspection.getTenant().getName() + "] and Inspection Type Group [" + inspection.getType().getGroup().getName() + "] found at ["
					+ jasperFile.getAbsolutePath() + "]");
		}

		JasperPrint jasperPrint = null;
		try {
			ReportMap<Object> inspectionReportMap = new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap();

			ReportMap<Object> reportMap = new ReportMap<Object>();
			reportMap.put("mainInspection", inspectionReportMap);
			reportMap.put("product", inspectionReportMap.get("product"));
			addInspectionTypeGroupParams(reportMap, inspection.getType().getGroup());
			reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
			addImageStreams(reportMap, inspection.getTenant(), inspection.getOrganization());
			addTenantParams(reportMap, inspection.getTenant());
			addOrganizationParams(reportMap, inspection.getOrganization());
			addUserParams(reportMap, inspection.getInspector());
			addCustomerParams(reportMap, inspection.getCustomer());
			addDivisionParams(reportMap, inspection.getDivision());
			addInspectionScheduleParams(reportMap, inspectionScheduleManager.getNextScheduleFor(inspection.getProduct(), inspection.getType()), user);

			List<ReportMap<Object>> inspectionResultMaps = new ArrayList<ReportMap<Object>>();
			inspectionResultMaps.add(inspectionReportMap);
			for (SubInspection subInspection : inspection.getSubInspections()) {
				inspectionResultMaps.add(new InspectionReportMapProducer(subInspection, new DateTimeDefiner(user)).produceMap());
			}

			JRDataSource jrDataSource = new JRMapCollectionDataSource(inspectionResultMaps);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	/**
	 * Generates a JasperPrint object for an Inspection certificate.
	 * 
	 * @param inspection
	 *            The Inspection object to create the report for.
	 * @param dateFormat
	 *            A String date format to be used in the report
	 * @return A jasper print object for the generated report
	 * @throws NonPrintableEventType
	 *             When the inspection is set to not printable
	 *             {@link Inspection#setPrintable(boolean)}
	 * @throws ReportException
	 *             On any problem generating the report.
	 */
	private JasperPrint generateInspectionCertificate(Inspection inspection, UserBean user) throws NonPrintableEventType, ReportException {
		// If the inspection is not printable, stop immediately
		if (!inspection.isPrintable() || !inspection.getType().getGroup().hasPrintOut()) {
			throw new NonPrintableEventType();
		}

		File jasperFile = PathHandler.getPrintOutFile(inspection.getType().getGroup().getPrintOut());

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No report file for tenant [" + inspection.getTenant().getName() + "] and Inspection Type Group [" + inspection.getType().getGroup().getName() + "] found at ["
					+ jasperFile.getAbsolutePath() + "]");
		}
		ReportMap<Object> reportMap = createInspectionReportMap(inspection, user, jasperFile);

		JasperPrint jasperPrint = null;
		try {

			JRDataSource jrDataSource = (JRDataSource) new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap().get("results");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	private ReportMap<Object> createInspectionReportMap(Inspection inspection, UserBean user, File jasperFile) throws ReportException {
		ReportMap<Object> reportMap = createAbstractInspectionReportMap(inspection, user);

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		addImageStreams(reportMap, inspection.getTenant(), inspection.getOrganization());
		addTenantParams(reportMap, inspection.getTenant());
		addOrganizationParams(reportMap, inspection.getOrganization());
		addUserParams(reportMap, inspection.getInspector());
		addCustomerParams(reportMap, inspection.getCustomer());
		addDivisionParams(reportMap, inspection.getDivision());
		addProofTestInfoParams(reportMap, inspection);

		addInspectionScheduleParams(reportMap, inspectionScheduleManager.getNextScheduleFor(inspection.getProduct(), inspection.getType()), user);

		return reportMap;
	}

	private ReportMap<Object> createAbstractInspectionReportMap(AbstractInspection inspection, UserBean user) throws ReportException {
		ReportMap<Object> reportMap = new ReportMap<Object>();

		reportMap.putAll(new ProductReportMapProducer(inspection.getProduct(), new DateTimeDefiner(user)).produceMap());
		addProductTypeParams(reportMap, inspection.getProduct().getType());
		addOrderParams(reportMap, inspection.getProduct().getShopOrder());
		reportMap.putAll(new InspectionReportMapProducer(inspection, new DateTimeDefiner(user)).produceMap());
		addInspectionTypeGroupParams(reportMap, inspection.getType().getGroup());

		return reportMap;

	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#generateProductCertificate(java.lang.Long,
	 *      java.lang.String)
	 */
	public JasperPrint generateProductCertificate(Long productId, UserBean user) throws ReportException, NonPrintableManufacturerCert {

		Product productSerial = null;
		try {
			productSerial = productManager.findProduct(productId);
		} catch (Exception e) {
			throw new ReportException("Failed loading ProductSerial", e);
		}

		if (!productSerial.getType().isHasManufactureCertificate()) {
			throw new NonPrintableManufacturerCert("no cert for product.");
		}

		File jasperFile = PathHandler.getReportFile(productSerial);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No Product report file for tenant [" + productSerial.getTenant().getName() + "]");
		}

		ReportMap<Object> reportMap = new ReportMap<Object>();

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");

		addImageStreams(reportMap, productSerial.getTenant(), productSerial.getOrganization());

		addTenantParams(reportMap, productSerial.getTenant());
		addOrganizationParams(reportMap, productSerial.getOrganization());
		addUserParams(reportMap, productSerial.getIdentifiedBy());

		reportMap.putAll(new ProductReportMapProducer(productSerial, new DateTimeDefiner(user)).produceMap());
		addProductTypeParams(reportMap, productSerial.getType());
		addOrderParams(reportMap, productSerial.getShopOrder());

		addCustomerParams(reportMap, productSerial.getOwner());
		addDivisionParams(reportMap, productSerial.getDivision());

		List<Product> reportCollection = new ArrayList<Product>();
		reportCollection.add(productSerial);

		JasperPrint jasperPrint = null;
		try {
			JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(reportCollection);

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);

		} catch (JRException e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	/**
	 * @see com.n4systems.reporting.ReportFactory#generateInspectionReport(com.n4systems.util.ReportCriteria,
	 *      java.lang.String, com.n4systems.model.TenantOrganization)
	 */
	public JasperPrint generateInspectionReport(ReportDefiner reportDefiner, UserBean user, TenantOrganization tenant) throws ReportException {
		File jasperFile = PathHandler.getSummaryReportFile(tenant);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException("No Product report file summary report ");
		}

		List<Long> inspectionIds = persistenceManager.idSearch(reportDefiner);

		ReportMap<Object> reportMap = criteriaMap(reportDefiner, tenant, jasperFile);
		List<ReportMap<Object>> collection = new ArrayList<ReportMap<Object>>();

		// we don't have access to a user here so we'll always use the tenant's
		// image
		addImageStreams(reportMap, tenant, null);

		try {
			Inspection inspection;
			for (Long inspectionId : inspectionIds) {
				inspection = persistenceManager.find(Inspection.class, inspectionId);

				ReportMap<Object> inspectionMap = new ReportMap<Object>();
				inspectionMap.put("date", inspection.getDate());
				inspectionMap.put("productType", inspection.getProduct().getType().getName());
				inspectionMap.put("serialNumber", inspection.getProduct().getSerialNumber());
				inspectionMap.put("description", inspection.getProduct().getDescription());
				inspectionMap.put("organization", inspection.getOrganization().getDisplayName());
				inspectionMap.put("division", (inspection.getDivision() != null) ? inspection.getDivision().getDisplayName() : null);
				inspectionMap.put("inspectionType", inspection.getType().getName());
				inspectionMap.put("dateFormat", new DateTimeDefiner(user).getDateFormat());
				inspectionMap.put("inspector", inspection.getInspector().getUserLabel());
				inspectionMap.put("result", inspection.getStatus().getDisplayName());

				if (inspection.getProofTestInfo() != null) {
					inspectionMap.put("peakLoad", inspection.getProofTestInfo().getPeakLoad());
				}

				collection.add(inspectionMap);
			}
		} catch (Exception e) {
			throw new ReportException("Failed to load the criteria", e);
		}

		JasperPrint jasperPrint = null;
		try {
			JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(collection);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);

		} catch (JRException e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	// XXX - document me
	private ReportMap<Object> criteriaMap(ReportDefiner reportDefiner, TenantOrganization tenant, File jasperFile) {
		ReportMap<Object> reportMap = new ReportMap<Object>();

		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		reportMap.put("serialNumber", reportDefiner.getSerialNumber());
		reportMap.put("rfidNumber", reportDefiner.getRfidNumber());
		reportMap.put("orderNumber", reportDefiner.getOrderNumber());
		reportMap.put("purchaseOrder", reportDefiner.getPurchaseOrder());
		reportMap.put("toDate", reportDefiner.getToDate());
		reportMap.put("fromDate", reportDefiner.getFromDate());
		reportMap.put("hasIntegration", tenant.hasExtendedFeature(ExtendedFeature.Integration));
		reportMap.put("hasJobSites", tenant.hasExtendedFeature(ExtendedFeature.JobSites));

		if (reportDefiner.getProductType() != null) {
			reportMap.put("productType", productTypeManger.findProductType(reportDefiner.getProductType()).getName());
		}

		if (reportDefiner.getInspectionBook() != null) { 
			if (reportDefiner.getInspectionBook() != 0L) {
			reportMap.put("inspectionBook", persistenceManager.find(InspectionBook.class, reportDefiner.getInspectionBook()).getName());
			} else {
				reportMap.put("inspectionBook", "no inspection book");
			}
		}
		if (reportDefiner.getJobSite() != null) {
			reportMap.put("jobSite", persistenceManager.find(JobSite.class, reportDefiner.getJobSite()).getName());
		}

		if (reportDefiner.getInspectionTypeGroup() != null) {
			reportMap.put("inspectionTypeGroup", persistenceManager.find(InspectionTypeGroup.class, reportDefiner.getInspectionTypeGroup()).getName());
		}

		if (reportDefiner.getInspector() != null) {
			reportMap.put("inspector", userManager.findUserBean(reportDefiner.getInspector()).getUserLabel());
		}

		if (reportDefiner.getCustomer() != null) {
			reportMap.put("customer", persistenceManager.find(Customer.class, reportDefiner.getCustomer()).getName());
		}

		if (reportDefiner.getDivision() != null) {
			reportMap.put("division", persistenceManager.find(Division.class, reportDefiner.getDivision()).getName());
		}
		return reportMap;
	}

	private void addImageStreams(ReportMap<Object> params, TenantOrganization tenant, Organization org) throws ReportException {
		InputStream logoImage = resolveCertificateMainLogo(org, tenant);
		InputStream n4LogoImage = getImageFileStream(PathHandler.getCommonImageFile(n4LogoFileName));

		params.put("n4LogoImage", n4LogoImage);
		params.put("logoImage", logoImage);
	}

	private void addTenantParams(ReportMap<Object> params, TenantOrganization tenant) {
		params.putEmpty("manName", "manAddress");

		params.put("manName", tenant.getDisplayName());
		if (tenant.getAddressInfo() != null) {
			params.put("manAddress", tenant.getAddressInfo().getDisplay());
		}
	}

	private void addCustomerParams(ReportMap<Object> params, Customer customer) {
		params.putEmpty("customerNumber", "endUserName", "customerAddress", "customerCity", "customerState", "customerPostalCode", "customerPhoneNumber", "customerFaxNumber");

		if (customer != null) {
			params.put("customerNumber", customer.getCustomerId());
			params.put("endUserName", customer.getName());

			AddressInfo addressInfo = customer.getAddressInfo();
			if (addressInfo != null) {
				params.put("customerAddress", addressInfo.getStreetAddress());
				params.put("customerCity", addressInfo.getCity());
				params.put("customerState", addressInfo.getState());
				params.put("customerPostalCode", addressInfo.getZip());
				params.put("customerPhoneNumber", addressInfo.getPhone1());
				params.put("customerPhoneNumber2", addressInfo.getPhone2());
				params.put("customerFaxNumber", addressInfo.getFax1());
			}
		}
	}

	private void addDivisionParams(ReportMap<Object> params, Division division) {
		params.putEmpty("division");

		if (division != null) {
			params.put("division", division.getName());
			params.put("divisionID", division.getDivisionID());
			params.put("divisionContactName", division.getContact().getName());
			params.put("divisionContactEmail", division.getContact().getEmail());
			AddressInfo addressInfo = division.getAddressInfo();
			if (addressInfo != null) {
				params.put("divisionAddress", addressInfo.getStreetAddress());
				params.put("divisionCity", addressInfo.getCity());
				params.put("divisionState", addressInfo.getState());
				params.put("divisionPostalCode", addressInfo.getZip());
				params.put("divisionPhoneNumber", addressInfo.getPhone1());
				params.put("divisionPhoneNumber2", addressInfo.getPhone2());
				params.put("divisionFaxNumber", addressInfo.getFax1());
			}
		}
	}

	private void addOrderParams(ReportMap<Object> params, LineItem lineItem) {
		params.putEmpty("orderNumber", "customerPartDescription", "partNumber", "productName");

		if (lineItem != null) {
			params.put("orderNumber", lineItem.getOrder().getOrderNumber());
			params.put("customerPartDescription", lineItem.getDescription());
			params.put("productName", lineItem.getProductCode());
		}
	}

	private void addUserParams(ReportMap<Object> params, UserBean user) {
		params.putEmpty("inspectorName", "identifiedBy", "position", "initials");

		if (user != null) {
			params.put("identifiedBy", user.getUserLabel());
			params.put("inspectorName", user.getUserLabel());
			params.put("position", user.getPosition());
			params.put("initials", user.getInitials());

			File userImagePath = PathHandler.getSignatureImage(user);

			if (userImagePath.exists()) {
				try {
					params.put("signatureImage", new FileInputStream(userImagePath));
				} catch (FileNotFoundException fne) {
					// since we check to see that the file does exist first,
					// we'll just log these and move on, rather then throwing it
					// up the stack
					logger.warn("Unable to open user signature image stream", fne);
				}
			}
		}
	}

	private void addOrganizationParams(ReportMap<Object> params, Organization org) {
		params.putEmpty("organizationalPrintName", "organizationalAddress", "organizationalCity", "organizationalState", "organizationalPostalCode", "organizationalPhoneNumber",
				"organizationalFaxNumber");

		if (org != null) {
			params.put("organizationalPrintName", org.getCertificateName());

			AddressInfo addressInfo = org.getAddressInfo();
			if (addressInfo != null) {
				params.put("organizationalAddress", addressInfo.getStreetAddress());
				params.put("organizationalCity", addressInfo.getCity());
				params.put("organizationalState", addressInfo.getState());
				params.put("organizationalPostalCode", addressInfo.getZip());
				params.put("organizationalPhoneNumber", addressInfo.getPhone1());
				params.put("organizationalPhoneNumber2", addressInfo.getPhone2());
				params.put("organizationalFaxNumber", addressInfo.getFax1());
			}
		}
	}

	private void addProductTypeParams(ReportMap<Object> params, ProductType productType) {
		params.put("productType", productType.getName());
		params.put("itemNumber", productType.getName());
		params.put("productWarning", productType.getWarnings());
		params.put("certificateText", productType.getManufactureCertificateText());
	}

	private void addProofTestInfoParams(ReportMap<Object> params, Inspection inspection) {
		params.putEmpty("proofTest", "peakLoad", "testDuration", "peakLoadDuration");

		if (inspection.getProofTestInfo() != null) {
			params.put("peakLoad", inspection.getProofTestInfo().getPeakLoad());
			params.put("testDuration", inspection.getProofTestInfo().getDuration());
			params.put("chartPath", PathHandler.getChartImageFile(inspection).getAbsolutePath());
			params.put("peakLoadDuration", inspection.getProofTestInfo().getPeakLoadDuration());
		}
	}

	private void addInspectionScheduleParams(ReportMap<Object> params, InspectionSchedule schedule, UserBean user) {
		params.putEmpty("nextDate");
		params.putEmpty("nextDate_date");
		if (schedule != null) {
			params.put("nextDate", formatDate(schedule.getNextDate(), new DateTimeDefiner(user), false));
			params.put("nextDate_date", schedule.getNextDate());
		}
	}

	private void addInspectionTypeGroupParams(ReportMap<Object> params, InspectionTypeGroup group) {
		params.put("reportTitle", group.getReportTitle());
	}

	/**
	 * Opens an InputStream for a file.
	 * 
	 * @param imageFile
	 *            The file to open
	 * @return An InputStream for this file or null if the file could not be
	 *         found.
	 */
	private InputStream getImageFileStream(File imageFile) {
		InputStream imageStream = null;

		if (imageFile != null) {
			try {
				imageStream = new FileInputStream(imageFile);
			} catch (FileNotFoundException e) {
				logger.warn("Cannot open stream for image file", e);
			}
		}

		return imageStream;
	}

	/**
	 * Returns an input stream of the logo for an Organization. If the
	 * organization is not null and has a logo, that logo is returned. Failing
	 * that, the tenants logo is returned. If the tenant has no logo, null is
	 * returned.
	 * 
	 * @param organization
	 *            An Organization
	 * @param tenant
	 *            The Tenant
	 * @return An InputStream or null if no logo could be resolved.
	 * @throws ReportException
	 */
	private InputStream resolveCertificateMainLogo(Organization organization, TenantOrganization tenant) throws ReportException {
		InputStream logoStream = null;
		File tenantLogo = PathHandler.getCertificateLogo(tenant);

		try {
			if (organization != null) {
				File orgLogo = PathHandler.getCertificateLogo(organization);

				if (orgLogo.exists()) {
					logoStream = new FileInputStream(orgLogo);
				}
			}

			if (logoStream == null && tenantLogo.exists()) {
				logoStream = new FileInputStream(tenantLogo);
			}
		} catch (FileNotFoundException e) {
			throw new ReportException("Failed creating certificate logo input stream", e);
		}

		return logoStream;
	}

	/**
	 * Wrapper method for {@link DateHelper#date2String(String, Date)}
	 * 
	 * @see DateHelper#date2String(String, Date)
	 * @param format
	 *            A SimpleDateFormat String date format
	 * @param date
	 *            Date object
	 * @return The formatted date
	 */
	private String formatDate(Date date, DateTimeDefinition dateTimeDefinition, boolean showTime) {
		if (date instanceof PlainDate) {
			return new FieldidDateFormatter(date, dateTimeDefinition, false, showTime).format();
		}
		return new FieldidDateFormatter(date, dateTimeDefinition, true, showTime).format();

	}

}

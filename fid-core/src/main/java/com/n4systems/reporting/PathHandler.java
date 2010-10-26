package com.n4systems.reporting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.UUID;


import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.Project;
import com.n4systems.model.State;
import com.n4systems.model.SubInspection;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.product.AssetAttachment;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;

public class PathHandler {
	// XXX - All these constants should be moved into a properties file
	private static final String PRIVATE_PATH_BASE = "private";
	private static final String COMMON_PATH_BASE = "common";
	private static final String TEMP_PATH_BASE = "tmp";
	private static final String REPORT_FILE_EXT = ".jasper";
	private static final String PROPERTIES_FILE_EXT = ".properties";
	private static final String PACKAGE_PROPERTIES_FILE = "package.properties";
	private static final String SUMMARY_REPORT_FILE_NAME = "inspection_summary_report" + REPORT_FILE_EXT;
	private static final String PRODUCT_REPORT_FILE_NAME = "asset" + REPORT_FILE_EXT;
	private static final String CHART_FILE_NAME = "proof_test_chart.png";
	private static final String PROOF_TEST_FILE_NAME = "proof_test.pt";
	private static final String LOGO_IMAGE_FILE_NAME = "logo.gif";
	private static final String CERTIFICATE_LOGO_IMAGE_FILE_NAME = "certlogo.gif";
	private static final String SIGNATURE_IMAGE_FILE_NAME = "signature.gif";
	private static final String ORGANIZATION_PATH_PART =  "orgs";
	private static final String TENANT_IMAGE_PATH_BASE = PRIVATE_PATH_BASE + "/images";
	private static final String INSPECTION_PATH_BASE = PRIVATE_PATH_BASE + "/inspections";
	private static final String PRODUCT_PATH_BASE = PRIVATE_PATH_BASE + "/products";
	private static final String PRODUCT_TYPE_PATH_BASE = PRIVATE_PATH_BASE + "/productTypes";
	private static final String PROJECT_PATH_BASE = PRIVATE_PATH_BASE + "/projects";
	private static final String CONF_PATH_BASE = PRIVATE_PATH_BASE + "/conf";
	private static final String USERS_PATH_BASE = PRIVATE_PATH_BASE + "/users";
	private static final String PRODUCT_TYPE_ATTACHMENT_PATH_BASE = PRODUCT_TYPE_PATH_BASE + "/attachments";
	private static final String PRODUCT_TYPE_IMAGE_PATH_BASE = PRODUCT_TYPE_PATH_BASE + "/images";
	private static final String PROJECT_ATTACHMENT_PATH_BASE = PROJECT_PATH_BASE + "/attachments";
	private static final String PRODUCT_ATTACHMENT_PATH_BASE = PRODUCT_PATH_BASE + "/attachments";
	private static final String ATTACHMENT_PATH_BASE = INSPECTION_PATH_BASE + "/attachments";
	private static final String PROOF_TEST_PATH_BASE = INSPECTION_PATH_BASE + "/prooftests";
	private static final String CHART_IMAGE_PATH_BASE = INSPECTION_PATH_BASE + "/chartimages";
	private static final String REPORT_PATH_BASE = PRIVATE_PATH_BASE + "/reports";
	private static final String ALL_TENANT_REPORT_PATH = REPORT_PATH_BASE + "/all_tenants";
	private static final String DEFAULT_INSPECTION_REPORTNAME = "default_inspection_cert" + REPORT_FILE_EXT;
	private static final String COMMON_IMAGE_PATH_BASE = COMMON_PATH_BASE + "/images";
	private static final String COMMON_TEMPLATE_BASE = COMMON_PATH_BASE + "/templates";
	private static final String COMMON_CONFIG_BASE = COMMON_PATH_BASE + "/conf";
	private static final String BUTTON_IMAGE_PATH_BASE = COMMON_IMAGE_PATH_BASE + "/buttons";
	private static final String BUTTON_IMAGE_EXT = ".png";
	private static final String N4_LOGO_IMAGE = COMMON_IMAGE_PATH_BASE + "/n4_logo.gif";
	private static final String RESERVED_TENANT_NAMES_CONFIG_FILE = COMMON_CONFIG_BASE + "/reservedTenantNames.txt";
	
	// paths are in the format <tenant id>/<created year>/<created month>/<inspection id>
	private static final String CREATED_DATE_PATH_FORMAT = "yy/MM";
	
	/**
	 * Merges path parts into a file system path.
	 * @see File#pathSeparatorChar
	 * @param pathParts	An array of path parts to merge
	 * @return			The merged path.
	 */
	private static String mergePaths(String...pathParts) {
		// we do it this way so we don't add a leading '/'
		StringBuilder mergedPath = new StringBuilder(pathParts[0]);
		for(int i = 1; i < pathParts.length; i++) {
			mergedPath.append(File.separatorChar);
			mergedPath.append(pathParts[i]);
		}
		return mergedPath.toString();
	}
	
	/**
	 * Returns a new file object with the path parented by parent
	 * @see File#File(File, String)
	 * @param parent	parent directory
	 * @param path		child path
	 * @return			File
	 */
	private static File parentize(File parent, String path) {
		return new File(parent, path);
	}
	
	/**
	 * Returns a new file object with the path parented by the application root
	 * @see #parentize(File, String)
	 * @see #getAppRoot()
	 * @param path		child path
	 * @return			File
	 */
	private static File absolutize(String path) {
		return parentize(getAppRoot(), path);
	}
	
	/**
	 * Returns the application root directory
	 * @return	A File object for this directory
	 */
	private static File getAppRoot() {
		return ConfigContext.getCurrentContext().getAppRoot();
	}
	
	/**
	 * Constructs a File object to the temporary directory root
	 * @return					A File object for this directory
	 */
	public static File getTempRoot() {
		return absolutize(TEMP_PATH_BASE);
	}
	
	/**
	 * Generates a random file name.
	 * @see UUID#randomUUID()
	 * @return A string representing this file name
	 */
	private static String getTempFileName() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Constructs a File object for a temporary directory.
	 * @see #getTempRoot()
	 * @see #getTempFileName()
	 * @return					A File object for this directory
	 */
	public static File getTempDir() {
		File tempDir = getTempFile();
		tempDir.mkdirs();
		return tempDir;
	}
	
	/**
	 * Constructs temp File with a random name
	 * @see #getTempDir()
	 * @return				A File object for this file
	 */
	public static File getTempFile() {
		return parentize(getTempRoot(), getTempFileName());
	}
	
	/**
	 * Constructs a File object for a file in a temporary directory
	 * @see #getTempDir()
	 * @param fileName			The name of the file
	 * @return					A File object for this file
	 */
	public static File getTempFile(String fileName) {
		return parentize(getTempDir(), fileName);
	}
	
	/**
	 * Construct the File for a report in the all tenant directory.
	 * @see #getAllTenantReportFile(File)
	 * @param reportName		The name of the report
	 * @return	 				An absolute File object for this report
	 */
	public static File getAllTenantReportFile(String reportName) {
		return absolutize(mergePaths(ALL_TENANT_REPORT_PATH, reportName));
	}
	
	/**
	 * Construct the File for a report in the all tenant directory.
	 * @see #getDefaultReportFile(File)
	 * @return	 				An absolute File object for this report
	*/
	public static File getDefaultReportFile() {
		return absolutize(mergePaths(ALL_TENANT_REPORT_PATH, DEFAULT_INSPECTION_REPORTNAME));
	}
	
	/**
	 * Constructs a relative path for a tenants private directory.
	 * @param 	tenant	A Tenant
	 * @return			A string path representing a Tenants private path
	 */
	private static String getTenantPathPart(Tenant tenant) {
		return tenant.getName();
	}
	
	/**
	 * Constructs a relative path for a organizations private directory.  If the Organization is a TenantOrganization,
	 * the path will be the same as {@link #getTenantPathPart(Tenant)}.
	 * @param 	organization	An Organization
	 * @return					A string path representing an Organizations private path
	 */
	private static String getOrganizationPathPart(InternalOrg organization) {
		String path;
		if (organization.isSecondary()) {
			path = mergePaths(getTenantPathPart(organization.getTenant()), ORGANIZATION_PATH_PART, organization.getId().toString());
		} else {
			path = getTenantPathPart(organization.getTenant());
		}
		return path;
	}
	
	/**
	 * Converts an absolute File to a path relative to {@link #getAppRoot()}.  Warning: using a path that is not a sub-directory of
	 * {@link #getAppRoot()} will yield unexpected results.
	 * @param path	Path to convert.
	 * @return		relative path
	 */
	public static String makeRelative(File path) {
		return path.getAbsolutePath().substring((int)getAppRoot().getAbsoluteFile().length());
	}
	
//	/**
//	 * Constructs the private path for a user within a tenant directory.
//	 * @param user	A UserBean
//	 * @return		A string path representing the users private path
//	 */
//	private static String getUserSignaturePathPart(UserBean user) {
//		return mergePaths(getTenantUserBasePath(user.getTenant()), user.getId().toString());
//	}
	
	/**
	 * Returns the base directory for user files for the given tenant.
	 */
	public static File getTenantUserBaseFile(Tenant tenant) {
		return absolutize(getTenantUserBasePath(tenant));
	}
	
	private static String getTenantUserBasePath(Tenant tenant) {
		return mergePaths(USERS_PATH_BASE, getTenantPathPart(tenant));
	}
	
	/**
	 * Finds the relative Tenant specific path to Asset report.  Uses the Tenant from the Asset to resolve the Teanant path part
	 * @see 	#getTenantPathPart(Tenant)
	 * @see 	#getReportPath(com.n4systems.model.Asset)
	 * @param 	product	A Asset
	 * @return	A string path relative to the application root
	 */
	private static String getReportPath(Asset product) {
		return mergePaths(REPORT_PATH_BASE, getTenantPathPart(product.getTenant()), PRODUCT_REPORT_FILE_NAME);
	}
	
	/**
	 * Finds a report file for an Asset.  Defaults to the all Tenant report directory if the Tenant specific report does not exist.
	 * @see 	#getReportPath(com.n4systems.model.Asset)
	 * @param	getAppRoot()	A base directory to resolve the file from
	 * @param 	product			A Asset
	 * @return					A File object for the resolved report
	 */
	public static File getReportFile(Asset product) {
		File tenantReport = absolutize(getReportPath(product));
		return (tenantReport.exists()) ? tenantReport : getAllTenantReportFile(PRODUCT_REPORT_FILE_NAME);
	}
	
	/**
	 * Resolves an InspectionTypeGroup to its report file name.  Appends the master suffix if isMaster is true.
	 * @see 	InspectionTypeGroup#getFileSystemName()
	 * @param 	group		An InspectionTypeGroup
	 * @param	isMaster	Appends the master suffix when true.
	 * @return				A string representing the reports file name
	 */
	private static String getReportFileName(PrintOut printOut) {
		return printOut.getPdfTemplate() + REPORT_FILE_EXT;
	}
	
	/**
	 * Finds the relative Tenant specific path to an InspectionTypeGroup's report.  Uses the Tenant from the InspectionTypeGroup to resolve the Teanant path part. Appends the master suffix if isMaster is true.
	 * @see 	#getTenantPathPart(Tenant)
	 * @see 	#getReportFileName(InspectionTypeGroup)
	 * @param 	group		An InspectionTypeGroup
	 * @param 	isMaster	Appends the master suffix when true.
	 * @return				A string path relative to the application root
	 */
	private static String getReportPath(PrintOut printOut) {
		return mergePaths(getReportPathBase(printOut), getReportFileName(printOut));
	}
	
	private static String getPrintOutPreveiwPath(PrintOut printOut) {
		return mergePaths(getReportPathBase(printOut), printOut.getFullPdfImage());
	}
	private static String getPrintOutPreveiwThumbPath(PrintOut printOut) {
		return mergePaths(getReportPathBase(printOut), printOut.getThumbNailImage());
	}

	private static String getReportPathBase(PrintOut printOut) {
		String printOutPath = ALL_TENANT_REPORT_PATH;
		if (printOut.isCustom()) {
			printOutPath = mergePaths(REPORT_PATH_BASE, getTenantPathPart(printOut.getTenant()));
		}
		return printOutPath;
	}
	
	/**
	 * Finds a report file for an InspectionTypeGroup.  
	 * 		
	 * @param 	type			An InspectionType
	 * @return					A File object for the resolved report
	 */
	public static File getPrintOutFile(PrintOut printOut) {
		return absolutize(getReportPath(printOut));
	}
	
	public static File getPreviewImage(PrintOut printOut) {
		return absolutize(getPrintOutPreveiwPath(printOut));
	}
	
	public static File getPreviewThumb(PrintOut printOut) {
		return absolutize(getPrintOutPreveiwThumbPath(printOut));
	}

	/**
	 * Finds a summary report for a Tenant.  Defaults to the all tenant report directory if the Tenant specific report does not exist.
	 * @see 	#getSummaryReportPath(Tenant)
	 * @param	getAppRoot()	A base directory to resolve the file from
	 * @param 	tenant			A Tenant
	 * @return					A File object for the resolved report
	 */
	public static File getSummaryReportFile(Tenant tenant) {
		File tenantReport = absolutize(mergePaths(REPORT_PATH_BASE, getTenantPathPart(tenant), SUMMARY_REPORT_FILE_NAME));
		return (tenantReport.exists()) ? tenantReport : getAllTenantReportFile(SUMMARY_REPORT_FILE_NAME);
	}
	
	private static String getInspectionPath(Inspection inspection) {
		String dateCreatedPath = (new SimpleDateFormat(CREATED_DATE_PATH_FORMAT)).format(inspection.getCreated());
		return mergePaths(dateCreatedPath, inspection.getId().toString());
	}
	
	private static String getProjectPath(Project project) {
		String dateCreatedPath = (new SimpleDateFormat(CREATED_DATE_PATH_FORMAT)).format(project.getCreated());
		return mergePaths(dateCreatedPath, project.getId().toString());
	}
	
	private static String getJobAttachmentBasePath(Tenant tenant) {
		return mergePaths(PROJECT_ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}
	
	private static String getProductPath(Asset product) {
		String dateCreatedPath = (new SimpleDateFormat(CREATED_DATE_PATH_FORMAT)).format(product.getCreated());
		return mergePaths(dateCreatedPath, product.getId().toString());
	}
	
	private static String getSubInspectionPath(SubInspection subInspection) {
		return subInspection.getId().toString();
	}
	
	private static String getProductTypePath(Long productTypeId) {
		return productTypeId.toString();
	}
	
	private static String getProductTypePath(AssetType assetType) {
		return getProductTypePath(assetType.getId());
	}
	
	private static String getAttachmentPath(Inspection inspection) {
		return mergePaths(getInspectionPath(inspection));
	}
	
	private static String getNotePath(FileAttachment note) {
		return note.getId().toString();
	}
	
	private static String getProductAttachmentPath(AssetAttachment pAttachment) {
		return pAttachment.getId().toString();
	}

	public static File getInspectionAttachmentFile(Inspection inspection, SubInspection subInspection, FileAttachment attachment) {
		return new File(getAttachmentFile(inspection, subInspection), attachment.getFileName());
	}
	
	public static File getInspectionAttachmentFile(Inspection inspection, FileAttachment attachment) {
		return new File(getAttachmentFile(inspection), attachment.getFileName());
	}
	
	public static File getAttachmentFile(Inspection inspection, SubInspection subInspection ) {
		return absolutize(mergePaths(getInspectionAttachmentBasePath(inspection.getTenant()), getAttachmentPath(inspection), getSubInspectionPath(subInspection)));
	}
	
	public static File getAttachmentFile(Inspection inspection) {
		return absolutize(mergePaths(getInspectionAttachmentBasePath(inspection.getTenant()), getAttachmentPath(inspection)));
	}
	
	public static File getInspectionAttachmentBaseFile(Tenant tenant) {
		return absolutize(getInspectionAttachmentBasePath(tenant));
	}
	
	private static String getInspectionAttachmentBasePath(Tenant tenant) {
		return mergePaths(ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getAttachmentFile(Project project, FileAttachment note) {
		return absolutize(mergePaths(getJobAttachmentBasePath(project.getTenant()), getProjectPath(project), getNotePath(note)));
	}
	
	public static File getJobAttachmentFileBaseFile(Tenant tenant) {
		return absolutize(getJobAttachmentBasePath(tenant));
	}
	
	public static File getProductAttachmentDir(AssetAttachment attachment) {
		return absolutize(mergePaths(getProductAttachmentBasePath(attachment.getTenant()), getProductPath(attachment.getAsset()), getProductAttachmentPath(attachment)));
	}
	
	public static File getProductAttachmentFile(AssetAttachment attachment) {
		return new File(getProductAttachmentDir(attachment), attachment.getFileName());
	}
	
	public static File getProductAttachmentBaseFile(Tenant tenant) {
		return absolutize(getProductAttachmentBasePath(tenant));
	}
	
	private static String getProductAttachmentBasePath(Tenant tenant) {
		return mergePaths(PRODUCT_ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getChartImageFile(Inspection inspection) {
		return absolutize(mergePaths(getInspectionChartImageBasePath(inspection.getTenant()), getInspectionPath(inspection), CHART_FILE_NAME));
	}
	
	public static File getInspectionChartImageBaseFile(Tenant tenant) {
		return absolutize(getInspectionChartImageBasePath(tenant));
	}
	
	private static String getInspectionChartImageBasePath(Tenant tenant) {
		return mergePaths(CHART_IMAGE_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getProofTestFile(Inspection inspection) {
		return absolutize(mergePaths(getInspectionProoftestBasePath(inspection.getTenant()), getInspectionPath(inspection), PROOF_TEST_FILE_NAME));
	}
	
	public static File getInspectionProoftestBaseFile(Tenant tenant) {
		return absolutize(getInspectionProoftestBasePath(tenant));
	}
	
	private static String getInspectionProoftestBasePath(Tenant tenant) {
		return mergePaths(PROOF_TEST_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getButtonImageFile(State state) {
		return absolutize(mergePaths(BUTTON_IMAGE_PATH_BASE, state.getButtonName()+ BUTTON_IMAGE_EXT));
	}
	
	public static File getCommonImageFile(String fileName) {
		return absolutize(mergePaths(COMMON_IMAGE_PATH_BASE, fileName));
	}
	
	public static File getN4LogoImageFile() {
		return absolutize(N4_LOGO_IMAGE);
	}
	
	public static File getCommonTemplatePath() {
		return absolutize(COMMON_TEMPLATE_BASE);
	}
	
	public static File getCommonConfigPath() {
		return absolutize(COMMON_CONFIG_BASE);
	}
	
	public static File getProductTypeAttachmentFile(FileAttachment attachment, Long productTypeId) {
		return absolutize(mergePaths(getProductTypeAttachmentBasePath(attachment.getTenant()), getProductTypePath(productTypeId), attachment.getFileName()));
	}
	
	public static File getProductTypeAttachmentFile(AssetType assetType) {
		return absolutize(mergePaths(getProductTypeAttachmentBasePath(assetType.getTenant()), getProductTypePath(assetType)));
	}
	
	private static String getProductTypeAttachmentBasePath(Tenant tenant) {
		return mergePaths(PRODUCT_TYPE_ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getProductTypeAttachmentBaseFile(Tenant tenant) {
		return absolutize(getProductTypeAttachmentBasePath(tenant));
	}
	
	public static File getProductTypeImageFile(AssetType assetType) {
		return absolutize(mergePaths(getProductTypeImageBasePath(assetType.getTenant()), getProductTypePath(assetType)));
	}
	
	public static File getProductTypeImageBaseFile(Tenant tenant) {
		return absolutize(getProductTypeImageBasePath(tenant));
	}
	
	public static String getProductTypeImageBasePath(Tenant tenant) {
		return mergePaths(PRODUCT_TYPE_IMAGE_PATH_BASE, getTenantPathPart(tenant));
	}

	/** @return The Tenant specific configuration directory for a tenant */
	public static File getConfDir(Tenant tenant) {
		return absolutize(mergePaths(CONF_PATH_BASE, getTenantPathPart(tenant)));
	}
	
	/** @return The Tenant specific configuration properties file for a Tenant and Class */
	public static File getPropertiesFile(Tenant tenant, Class<?> clazz) {
		return parentize(getConfDir(tenant), mergePaths(clazz.getPackage().getName(), clazz.getSimpleName() + PROPERTIES_FILE_EXT));
	}
	
	/** @return The Tenant specific configuration properties file for a Tenant and Package */
	public static File getPropertiesFile(Tenant tenant, Package pack) {
		return parentize(getConfDir(tenant), mergePaths(pack.getName(), PACKAGE_PROPERTIES_FILE));
	}
	
	/** @return The Tenant main logo */
	public static File getTenantLogo(Tenant tenant) {
		return absolutize(mergePaths(TENANT_IMAGE_PATH_BASE, getTenantPathPart(tenant), LOGO_IMAGE_FILE_NAME));
	}
	
	/** @return The certificate logo for an Organization defaulting up the parent chain*/
	public static File getCertificateLogo(InternalOrg organization) {
		return getCertificateLogo(organization, true);
	}
	
	/** @return The certificate logo for an Organization */
	public static File getCertificateLogo(InternalOrg organization, boolean defaultToParent) {
		File logo = absolutize(mergePaths(TENANT_IMAGE_PATH_BASE, getOrganizationPathPart(organization), CERTIFICATE_LOGO_IMAGE_FILE_NAME));
		
		// if we're dealing with a SecondaryOrg and the file does not exist, fall back to the tenant
		if (!logo.exists() && organization.isSecondary() && defaultToParent) {
			logo = absolutize(mergePaths(TENANT_IMAGE_PATH_BASE, getTenantPathPart(organization.getTenant()), CERTIFICATE_LOGO_IMAGE_FILE_NAME));
		}
		
		return logo;
	}
	
	private static String getUserPrivatePath(User user) {
		return mergePaths(getTenantUserBasePath(user.getTenant()), user.getId().toString());
	}

	/** @return The absolute private directory for a user  */
	public static File getUserPrivateDir(User user) {
		return absolutize(getUserPrivatePath(user));
	}
	
	/** @return The path to a file under a users private directory */
	public static File getUserFile(User user, String fileName) {
		return new File(getUserPrivateDir(user), fileName);
	}
	
	/** @return The signature image for a user  */
	public static File getSignatureImage(User user) {
		return getUserFile(user, SIGNATURE_IMAGE_FILE_NAME);
	}
	
	/** @return A file from the common config directory */
	public static File getCommonConfigFile(String fileName) {
		return absolutize(mergePaths(COMMON_CONFIG_BASE, fileName));
	}

	public static File getReleaseNotesPath() {
		
		return absolutize(mergePaths(COMMON_PATH_BASE, "releaseNotes.xml"));
		
	}
	
	public static File getReservedTenantNamesConfigFile() {
		return absolutize(RESERVED_TENANT_NAMES_CONFIG_FILE);
	}
}

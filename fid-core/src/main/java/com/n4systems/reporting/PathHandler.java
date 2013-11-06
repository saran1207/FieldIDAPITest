package com.n4systems.reporting;

import com.n4systems.model.*;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class PathHandler {
	private static final String PRIVATE_PATH_BASE = "private";
	private static final String COMMON_PATH_BASE = "common";
	private static final String TEMP_PATH_BASE = "tmp";
	private static final String COMPILED_REPORT_FILE_EXT = ".jasper";
	private static final String REPORT_FILE_EXT = ".jrxml";
	private static final String PROPERTIES_FILE_EXT = ".properties";
	private static final String PACKAGE_PROPERTIES_FILE = "package.properties";
	private static final String COMPILED_SUMMARY_REPORT_FILE_NAME = "inspection_summary_report" + COMPILED_REPORT_FILE_EXT;
    private static final String COMPILED_SUMMARY_REPORT_FILE_NAME_LOCALIZED = "inspection_summary_report_%s" + COMPILED_REPORT_FILE_EXT;
	private static final String ASSET_REPORT_FILE_NAME = "product" + REPORT_FILE_EXT;
    private static final String ASSET_REPORT_FILE_NAME_LOCALIZED = "product_%s" + REPORT_FILE_EXT;
    private static final String COMPILED_ASSET_REPORT_FILE_NAME = "product" + COMPILED_REPORT_FILE_EXT;
    private static final String COMPILED_ASSET_REPORT_FILE_NAME_LOCALIZED = "product_%s" + COMPILED_REPORT_FILE_EXT;
	private static final String CHART_FILE_NAME = "proof_test_chart.png";
	private static final String PROOF_TEST_FILE_NAME = "proof_test.pt";
	private static final String SIGNATURE_IMAGE_FILE_NAME = "signature.gif";
	private static final String EVENT_PATH_BASE = PRIVATE_PATH_BASE + "/inspections";
	private static final String ASSET_PATH_BASE = PRIVATE_PATH_BASE + "/products";
	private static final String ASSET_TYPE_PATH_BASE = PRIVATE_PATH_BASE + "/productTypes";
	private static final String PROJECT_PATH_BASE = PRIVATE_PATH_BASE + "/projects";
	private static final String CONF_PATH_BASE = PRIVATE_PATH_BASE + "/conf";
	private static final String USERS_PATH_BASE = PRIVATE_PATH_BASE + "/users";
	private static final String ASSET_TYPE_ATTACHMENT_PATH_BASE = ASSET_TYPE_PATH_BASE + "/attachments";
	private static final String ASSET_TYPE_IMAGE_PATH_BASE = ASSET_TYPE_PATH_BASE + "/images";
	private static final String PROJECT_ATTACHMENT_PATH_BASE = PROJECT_PATH_BASE + "/attachments";
	private static final String ASSET_ATTACHMENT_PATH_BASE = ASSET_PATH_BASE + "/attachments";
	private static final String ASSET_IMAGE_PATH_BASE = ASSET_PATH_BASE + "/images";
	private static final String ATTACHMENT_PATH_BASE = EVENT_PATH_BASE + "/attachments";
	private static final String PROOF_TEST_PATH_BASE = EVENT_PATH_BASE + "/prooftests";
	private static final String CHART_IMAGE_PATH_BASE = EVENT_PATH_BASE + "/chartimages";
    private static final String EVENT_SIGNATURE_PATH_BASE = EVENT_PATH_BASE + "/signatures";
	private static final String REPORT_PATH_BASE = PRIVATE_PATH_BASE + "/reports";
	private static final String ALL_TENANT_REPORT_PATH = REPORT_PATH_BASE + "/all_tenants";
	private static final String COMMON_IMAGE_PATH_BASE = COMMON_PATH_BASE + "/images";
	private static final String COMMON_TEMPLATE_BASE = COMMON_PATH_BASE + "/templates";
	private static final String COMMON_CONFIG_BASE = COMMON_PATH_BASE + "/conf";
	private static final String BUTTON_IMAGE_PATH_BASE = COMMON_IMAGE_PATH_BASE + "/buttons";
	private static final String BUTTON_IMAGE_EXT = ".png";
	private static final String N4_LOGO_IMAGE = COMMON_IMAGE_PATH_BASE + "/n4_logo.gif";
	private static final String RESERVED_TENANT_NAMES_CONFIG_FILE = COMMON_CONFIG_BASE + "/reservedTenantNames.txt";
	
	// paths are in the format <tenant id>/<created year>/<created month>/<event id>
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

    private static String getReportFileName(Locale locale) {
        return "product_"+locale.getLanguage() +COMPILED_REPORT_FILE_EXT;
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

	public static File getTempFileInRoot(String tempFileName) {
		return parentize(getTempRoot(), tempFileName);
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

	public static File getAllTenantReportFile(String reportName, Locale locale) {
		return absolutize(mergePaths(ALL_TENANT_REPORT_PATH, reportName));
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
	 * @param 	asset	A Asset
	 * @return	A string path relative to the application root
	 */
	private static String getReportPath(Asset asset) {
		return mergePaths(REPORT_PATH_BASE, getTenantPathPart(asset.getTenant()), ASSET_REPORT_FILE_NAME);
	}

    private static String getReportPath(Asset asset, Locale locale) {
        return mergePaths(REPORT_PATH_BASE, getTenantPathPart(asset.getTenant()), getReportFileName(locale));
    }

	private static String getCompiledReportPath(Asset asset) {
		return mergePaths(REPORT_PATH_BASE, getTenantPathPart(asset.getTenant()), COMPILED_ASSET_REPORT_FILE_NAME);
	}

    private static String localizeReportPath(Tenant tenant, String localizedPathFormat, String defaultPath, Locale locale) {
        String localizedPath;
        if (locale != null) {
            localizedPath = String.format(localizedPathFormat, locale.getLanguage());
        } else {
            localizedPath = defaultPath;
        }
        if (tenant != null) {
            return mergePaths(REPORT_PATH_BASE, getTenantPathPart(tenant), localizedPath);
        } else {
            return mergePaths(REPORT_PATH_BASE, localizedPath);
        }
    }

    private static File getPossiblyLocalizedReport(Tenant tenant, Locale locale, String localizedPathFormat, String defaultPath) {
        File localizedFile = null;
        if (locale != null) {
            localizedFile = absolutize(localizeReportPath(tenant, localizedPathFormat, defaultPath, locale));
        }
        File tenantReport;
        if (localizedFile != null && localizedFile.exists()) {
            tenantReport = localizedFile;
        } else {
            tenantReport = absolutize(localizeReportPath(tenant, localizedPathFormat, defaultPath, null));
        }

        if (tenantReport.exists()) {
            return tenantReport;
        }

        return (tenantReport.exists()) ? tenantReport : absolutize(localizeReportPath(null, localizedPathFormat, defaultPath, locale));
    }

	public static File getReportFile(Asset asset, Locale locale) {
        return getPossiblyLocalizedReport(asset.getTenant(), locale, ASSET_REPORT_FILE_NAME_LOCALIZED, ASSET_REPORT_FILE_NAME);
	}

	public static File getCompiledReportFile(Asset asset, Locale locale) {
        return getPossiblyLocalizedReport(asset.getTenant(), locale, COMPILED_ASSET_REPORT_FILE_NAME_LOCALIZED, COMPILED_ASSET_REPORT_FILE_NAME);
	}

	private static String getReportFileName(PrintOut printOut, Locale locale) {
        String pdfTemplate = printOut.getPdfTemplate();
        if (locale != null) {
            pdfTemplate += "_"+ locale.getLanguage();
        }
        return pdfTemplate + REPORT_FILE_EXT;
	}
	
	private static String getCompiledReportFileName(PrintOut printOut, Locale locale) {
        String pdfTemplate = printOut.getPdfTemplate();
        if (locale != null) {
            pdfTemplate += "_"+ locale.getLanguage();
        }
        return pdfTemplate + COMPILED_REPORT_FILE_EXT;
	}

	private static String getReportPath(PrintOut printOut, Locale locale) {
		return mergePaths(getReportPathBase(printOut), getReportFileName(printOut, locale));
	}
	
	private static String getCompiledReportPath(PrintOut printOut, Locale locale) {
		return mergePaths(getReportPathBase(printOut), getCompiledReportFileName(printOut, locale));
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

	public static File getPrintOutFile(PrintOut printOut, Locale locale) {
        File localizedFile = absolutize(getReportPath(printOut, locale));
        if (localizedFile.exists()) {
            return localizedFile;
        }
        return absolutize(getReportPath(printOut, null));
	}

	public static File getCompiledPrintOutFile(PrintOut printOut, Locale locale) {
        File localizedCompiledFile = absolutize(getCompiledReportPath(printOut, locale));
        if (localizedCompiledFile.exists()) {
            return localizedCompiledFile;
        }
        return absolutize(getCompiledReportPath(printOut, null));
	}
	
	public static File getPreviewImage(PrintOut printOut) {
		return absolutize(getPrintOutPreveiwPath(printOut));
	}
	
	public static File getPreviewThumb(PrintOut printOut) {
		return absolutize(getPrintOutPreveiwThumbPath(printOut));
	}
	
	public static File getCompiledSummaryReportFile(Tenant tenant, Locale locale) {
        return getPossiblyLocalizedReport(tenant, locale, COMPILED_SUMMARY_REPORT_FILE_NAME_LOCALIZED, COMPILED_ASSET_REPORT_FILE_NAME);
	}
	
	private static String getEventPath(Event event) {
		String dateCreatedPath = (new SimpleDateFormat(CREATED_DATE_PATH_FORMAT)).format(event.getCreated());
		return mergePaths(dateCreatedPath, event.getId().toString());
	}
	
	private static String getProjectPath(Project project) {
		String dateCreatedPath = (new SimpleDateFormat(CREATED_DATE_PATH_FORMAT)).format(project.getCreated());
		return mergePaths(dateCreatedPath, project.getId().toString());
	}
	
	private static String getJobAttachmentBasePath(Tenant tenant) {
		return mergePaths(PROJECT_ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}
	
	private static String getAssetPath(Asset asset) {
		String dateCreatedPath = (new SimpleDateFormat(CREATED_DATE_PATH_FORMAT)).format(asset.getCreated());
		return mergePaths(dateCreatedPath, asset.getId().toString());
	}
	
	private static String getSubEventPath(SubEvent subEvent) {
		return subEvent.getId().toString();
	}
	
	private static String getAssetTypePath(Long assetTypeId) {
		return assetTypeId.toString();
	}
	
	private static String getAssetTypePath(AssetType assetType) {
		return getAssetTypePath(assetType.getId());
	}
	
	private static String getAttachmentPath(Event event) {
		return mergePaths(getEventPath(event));
	}
	
	private static String getNotePath(FileAttachment note) {
		return note.getId().toString();
	}
	
	private static String getAssetAttachmentPath(AssetAttachment pAttachment) {
		return pAttachment.getId().toString();
	}

	public static File getEventAttachmentFile(Event event, SubEvent subEvent, FileAttachment attachment) {
		return new File(getAttachmentFile(event, subEvent), attachment.getFileName());
	}
	
	public static File getEventAttachmentFile(Event event, FileAttachment attachment) {
		return new File(getAttachmentFile(event), attachment.getFileName());
	}
	
	public static File getAttachmentFile(Event event, SubEvent subEvent) {
		return absolutize(mergePaths(getEventAttachmentBasePath(event.getTenant()), getAttachmentPath(event), getSubEventPath(subEvent)));
	}
	
	public static File getAttachmentFile(Event event) {
		return absolutize(mergePaths(getEventAttachmentBasePath(event.getTenant()), getAttachmentPath(event)));
	}
	
	public static File getEventAttachmentBaseFile(Tenant tenant) {
		return absolutize(getEventAttachmentBasePath(tenant));
	}
	
	private static String getEventAttachmentBasePath(Tenant tenant) {
		return mergePaths(ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getAttachmentFile(Project project, FileAttachment note) {
		return absolutize(mergePaths(getJobAttachmentBasePath(project.getTenant()), getProjectPath(project), getNotePath(note)));
	}
	
	public static File getAssetAttachmentDir(AssetAttachment attachment) {
		return absolutize(mergePaths(getAssetAttachmentBasePath(attachment.getTenant()), getAssetPath(attachment.getAsset()), getAssetAttachmentPath(attachment)));
	}
	
	public static File getAssetAttachmentFile(AssetAttachment attachment) {
		return new File(getAssetAttachmentDir(attachment), attachment.getFileName());
	}
	
	public static File getAssetAttachmentBaseFile(Tenant tenant) {
		return absolutize(getAssetAttachmentBasePath(tenant));
	}
	
	private static String getAssetAttachmentBasePath(Tenant tenant) {
		return mergePaths(ASSET_ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getAssetImageFile(Asset asset) {
		return absolutize(mergePaths(getAssetImageBasePath(asset.getTenant()), getAssetPath(asset), asset.getImageName()));
	}

	private static String getAssetImageBasePath(Tenant tenant) {
		return mergePaths(ASSET_IMAGE_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getAssetImageDir(Asset asset) {
		return absolutize(mergePaths(getAssetImageBasePath(asset.getTenant()), getAssetPath(asset)));
	}

	public static File getChartImageFile(Event event) {
		return absolutize(mergePaths(getEventChartImageBasePath(event.getTenant()), getEventPath(event), CHART_FILE_NAME));
	}
	
	public static File getEventChartImageBaseFile(Tenant tenant) {
		return absolutize(getEventChartImageBasePath(tenant));
	}
	
	private static String getEventChartImageBasePath(Tenant tenant) {
		return mergePaths(CHART_IMAGE_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getProofTestFile(Event event) {
		return absolutize(mergePaths(getEventProoftestBasePath(event.getTenant()), getEventPath(event), PROOF_TEST_FILE_NAME));
	}
	
	public static File getEventProoftestBaseFile(Tenant tenant) {
		return absolutize(getEventProoftestBasePath(tenant));
	}
	
	private static String getEventProoftestBasePath(Tenant tenant) {
		return mergePaths(PROOF_TEST_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getButtonImageFile(Button button) {
		return absolutize(mergePaths(BUTTON_IMAGE_PATH_BASE, button.getButtonName()+ BUTTON_IMAGE_EXT));
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
	
	public static File getAssetTypeAttachmentFile(FileAttachment attachment, Long assetTypeId) {
		return absolutize(mergePaths(getAssetTypeAttachmentBasePath(attachment.getTenant()), getAssetTypePath(assetTypeId), attachment.getFileName()));
	}
	
	public static File getAssetTypeAttachmentFile(AssetType assetType) {
		return absolutize(mergePaths(getAssetTypeAttachmentBasePath(assetType.getTenant()), getAssetTypePath(assetType)));
	}
	
	private static String getAssetTypeAttachmentBasePath(Tenant tenant) {
		return mergePaths(ASSET_TYPE_ATTACHMENT_PATH_BASE, getTenantPathPart(tenant));
	}

	private static String getEventSignatureBasePath(Tenant tenant) {
		return mergePaths(EVENT_SIGNATURE_PATH_BASE, getTenantPathPart(tenant));
	}
	
	public static File getAssetTypeImageFile(AssetType assetType) {
		return absolutize(mergePaths(getAssetTypeImageBasePath(assetType.getTenant()), getAssetTypePath(assetType)));
	}

    public static File getEventSignatureBaseFile(Tenant tenant) {
        return absolutize(getEventSignatureBasePath(tenant));
    }

    public static File getEventSignatureDirectory(Tenant tenant, Long eventId) {
        return absolutize(mergePaths(getEventSignatureBasePath(tenant), eventId.toString()));
    }
	
	public static String getAssetTypeImageBasePath(Tenant tenant) {
		return mergePaths(ASSET_TYPE_IMAGE_PATH_BASE, getTenantPathPart(tenant));
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

	public static File getReleaseNotesPath() {
		return absolutize(mergePaths(COMMON_PATH_BASE, "releaseNotes.xml"));
	}
	
	public static File getReservedTenantNamesConfigFile() {
		return absolutize(RESERVED_TENANT_NAMES_CONFIG_FILE);
	}
}

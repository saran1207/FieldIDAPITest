package com.n4systems.usage;

import static com.n4systems.reporting.PathHandler.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.n4systems.model.Tenant;
import com.n4systems.util.Directory;

/**
 * Calculates the sizes and number of files in Tenant directories.  Used in disk usage reports and
 * Tenant storage limit calculations.
 */
public class TenantDiskUsageCalculator {
	
	private enum TenantDirType {
		INSPECTION_ATTACH, INSPECTION_CHART, INSPECTION_PROOFTEST, PRODUCT_ATTACH, PRODUCT_TYPE_IMAGE, PRODUCT_TYPE_ATTACH, JOB_NOTE, USER;
	}
	
	/** The TenantDirTypes to use in Tenant disk limit calculations */
	private static final TenantDirType[] LIMITING_DIR_TYPES = {
		TenantDirType.INSPECTION_ATTACH, TenantDirType.PRODUCT_ATTACH, TenantDirType.PRODUCT_TYPE_ATTACH, TenantDirType.JOB_NOTE
	};
	
	private final Tenant tenant;
	private final Map<TenantDirType, Directory> dirs;

	public TenantDiskUsageCalculator(Tenant tenant) {
		this.tenant = tenant;
		
		dirs = new HashMap<TenantDirType, Directory>();
		dirs.put(TenantDirType.INSPECTION_ATTACH, 		new Directory(getInspectionAttachmentBaseFile(tenant)));
		dirs.put(TenantDirType.INSPECTION_CHART, 		new Directory(getInspectionChartImageBaseFile(tenant)));
		dirs.put(TenantDirType.INSPECTION_PROOFTEST,	new Directory(getInspectionProoftestBaseFile(tenant)));
		dirs.put(TenantDirType.PRODUCT_ATTACH,  		new Directory(getProductAttachmentBaseFile(tenant)));
		dirs.put(TenantDirType.PRODUCT_TYPE_IMAGE,  	new Directory(getProductTypeImageBaseFile(tenant)));
		dirs.put(TenantDirType.PRODUCT_TYPE_ATTACH, 	new Directory(getProductTypeAttachmentBaseFile(tenant)));
		dirs.put(TenantDirType.JOB_NOTE,  				new Directory(getJobAttachmentFileBaseFile(tenant)));
		dirs.put(TenantDirType.USER,  					new Directory(getAbsoluteUserBaseFile(tenant)));
	}

	public Tenant getTenant() {
		return tenant;
	}
	
	public Directory getInspectionAttachmentUsage() {
		return dirs.get(TenantDirType.INSPECTION_ATTACH);
	}

	public Directory getInspectionChartImageUsage() {
		return dirs.get(TenantDirType.INSPECTION_CHART);
	}

	public Directory getInspectionProoftestFileUsage() {
		return dirs.get(TenantDirType.INSPECTION_PROOFTEST);
	}

	public Directory getProductAttachmentUsage() {
		return dirs.get(TenantDirType.PRODUCT_ATTACH);
	}

	public Directory getProductTypeImageUsage() {
		return dirs.get(TenantDirType.PRODUCT_TYPE_IMAGE);
	}

	public Directory getProductTypeAttachmentUsage() {
		return dirs.get(TenantDirType.PRODUCT_TYPE_ATTACH);
	}

	public Directory getJobNoteUsage() {
		return dirs.get(TenantDirType.JOB_NOTE);
	}

	public Directory getUserUsage() {
		return dirs.get(TenantDirType.USER);
	}

	/**
	 * Calculates the aggregate size in bytes of the limited tenant dirs. 
	 * @return size in bytes
	 */
	public long totalLimitingSize() {
		return calcTotalSize(LIMITING_DIR_TYPES);
	}
	
	/**
	 * Calculates the total number of files for all Tenant directories.  
	 * @return count of files
	 */
	public long totalFiles() {
		return calcTotalFiles(TenantDirType.values());
	}

	/**
	 * Calculates the total size for all Tenant directories.  
	 * @return count of files
	 */
	public long totalSpaceUsed() {
		return calcTotalSize(TenantDirType.values());
	}

	private long calcTotalFiles(TenantDirType[] types) {
		long total = 0L;
		for (TenantDirType type: types) {
			total += dirs.get(type).getNumberOfFiles();
		}
		return total;
	}
	
	private long calcTotalSize(TenantDirType[] types) {
		long total = 0L;
		for (TenantDirType type: types) {
			total += dirs.get(type).getTotalSize();
		}
		return total;
	}
	
	public String toString() {
		return tenant.getName() + " file usage " + FileUtils.byteCountToDisplaySize(totalSpaceUsed()) + " over " + totalFiles() + " files";
	}

}

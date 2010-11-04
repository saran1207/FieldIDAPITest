package com.n4systems.usage;

import static com.n4systems.reporting.PathHandler.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.limiters.LimitLoader;
import com.n4systems.services.limiters.LimitType;
import com.n4systems.util.Directory;

/**
 * Calculates the sizes and number of files in Tenant directories.  Used in disk usage reports and
 * Tenant storage limit calculations.
 */
public class TenantDiskUsageCalculator implements LimitLoader {
	
	private enum TenantDirType {
		INSPECTION_ATTACH, INSPECTION_CHART, INSPECTION_PROOFTEST, ASSET_ATTACH, ASSET_TYPE_IMAGE, ASSET_TYPE_ATTACH, JOB_NOTE, USER;
	}
	
	/** The TenantDirTypes to use in Tenant disk limit calculations */
	private static final TenantDirType[] LIMITING_DIR_TYPES = {
		TenantDirType.INSPECTION_ATTACH, TenantDirType.ASSET_ATTACH, TenantDirType.ASSET_TYPE_ATTACH, TenantDirType.JOB_NOTE
	};
	
	private Tenant tenant;
	private Map<TenantDirType, Directory> dirs;

	public TenantDiskUsageCalculator() {}
	
	public TenantDiskUsageCalculator(Tenant tenant) {
		setTenant(tenant);
	}
	
	private void initDirMap() {
		dirs = new HashMap<TenantDirType, Directory>();
		dirs.put(TenantDirType.INSPECTION_ATTACH, 		new Directory(getEventAttachmentBaseFile(tenant)));
		dirs.put(TenantDirType.INSPECTION_CHART, 		new Directory(getEventChartImageBaseFile(tenant)));
		dirs.put(TenantDirType.INSPECTION_PROOFTEST,	new Directory(getEventProoftestBaseFile(tenant)));
		dirs.put(TenantDirType.ASSET_ATTACH,  		new Directory(getAssetAttachmentBaseFile(tenant)));
		dirs.put(TenantDirType.ASSET_TYPE_IMAGE,  	new Directory(getAssetTypeImageBaseFile(tenant)));
		dirs.put(TenantDirType.ASSET_TYPE_ATTACH, 	new Directory(getAssetTypeAttachmentBaseFile(tenant)));
		dirs.put(TenantDirType.JOB_NOTE,  				new Directory(getJobAttachmentFileBaseFile(tenant)));
		dirs.put(TenantDirType.USER,  					new Directory(getTenantUserBaseFile(tenant)));
	}
	
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
		initDirMap();
	}

	public Tenant getTenant() {
		return tenant;
	}
	
	public Directory getEventAttachmentUsage() {
		return dirs.get(TenantDirType.INSPECTION_ATTACH);
	}

	public Directory getEventChartImageUsage() {
		return dirs.get(TenantDirType.INSPECTION_CHART);
	}

	public Directory getEventProoftestFileUsage() {
		return dirs.get(TenantDirType.INSPECTION_PROOFTEST);
	}

	public Directory getProductAttachmentUsage() {
		return dirs.get(TenantDirType.ASSET_ATTACH);
	}

	public Directory getProductTypeImageUsage() {
		return dirs.get(TenantDirType.ASSET_TYPE_IMAGE);
	}

	public Directory getProductTypeAttachmentUsage() {
		return dirs.get(TenantDirType.ASSET_TYPE_ATTACH);
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

	public long getLimit(Transaction transaction) {
		// transaction is ignored since this is not a database limit
		return totalLimitingSize();
	}

	public LimitType getType() {
		return LimitType.DISK_SPACE;
	}

}

package com.n4systems.fieldid.actions.asset;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class MultiAddAssetSerialNumberGenAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MultiAddAssetSerialNumberGenAction.class);
	
	public enum SerialNumberGenType { RANGE, AUTO, BATCH, MANUAL }
	
	private final SerialNumberCounter serialNumberCounter;
	
	private Integer quantity = 1;
	private String type = SerialNumberGenType.RANGE.name();
	private String prefix = "";
	private String start = "1";
	private String suffix = "";
	private String ident = "";
	
	private List<AssetIdentifierView> identifiers = new ArrayList<AssetIdentifierView>();

    private AssetType assetType;
    private Long assetTypeId;
	
	public MultiAddAssetSerialNumberGenAction(PersistenceManager persistenceManager, SerialNumberCounter serialNumberCounter) {
		super(persistenceManager);
		this.serialNumberCounter = serialNumberCounter;
	}

	public String doGenerate() {
		try {
			
			switch(SerialNumberGenType.valueOf(type)) {
				case RANGE:
					// Our start number may be zero padded, if so, we will use that to form a number format to left pad zeros
					// otherwise, we use '#' to denote a normal non-padded number
					String numberPadding = (start.startsWith("0")) ? start.replaceAll(".", "0") : "#";
					
					// will look something like 'some_prefix{0,number,0000}some_suffix' which will produce serials like some_prefix0005some_suffix
					MessageFormat format = new MessageFormat(prefix + "{0,number," + numberPadding + "}" + suffix);
					
					Long startNum = Long.valueOf(start);
					for (long i = startNum; i < quantity + startNum; i++) {
						identifiers.add(new AssetIdentifierView(getNextRangeSerial(i, format)));
					}
					break;
				case AUTO:
					for (int i = 0; i < quantity; i++) {
						identifiers.add(new AssetIdentifierView(getNextAutoSerial()));
					}
					break;
				case BATCH:
					for (int i = 0; i < quantity; i++) {
						identifiers.add(new AssetIdentifierView(ident));
					}
					break;
				case MANUAL:
				default:
					for (int i = 0; i < quantity; i++) {
						identifiers.add(new AssetIdentifierView());
					}
					break;
			}
		} catch(Exception e) {
			logger.error("Failed generating serial numbers", e);
			return ERROR;
		}

		return SUCCESS;
	}
	
	private String getNextRangeSerial(Long n, MessageFormat format) {
		// format actually takes an array of objects but we only ever use the first field
		return format.format(new Object[] {n});
	}
	
	private String getNextAutoSerial() {
		return serialNumberCounter.generateSerialNumber(getPrimaryOrg(), assetType);
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}
	
	public List<AssetIdentifierView> getIdentifiers() {
		return identifiers;
	}

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
        if (assetTypeId != null) {
            this.assetType = persistenceManager.find(AssetType.class, assetTypeId, getTenantId());
        }
    }

}

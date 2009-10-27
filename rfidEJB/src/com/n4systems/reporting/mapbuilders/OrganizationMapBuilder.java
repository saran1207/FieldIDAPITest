package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.StreamHelper;

public class OrganizationMapBuilder extends AbstractMapBuilder<InternalOrg> {
	private final MapBuilder<AddressInfo> addressMapBuilder;
	
	public OrganizationMapBuilder(MapBuilder<AddressInfo> addressMapBuilder) {
		super(ReportField.CERTIFICATE_NAME);
		
		this.addressMapBuilder = addressMapBuilder;
	}
	
	public OrganizationMapBuilder() {
		this(new OrganizationAddressMapBuilder());
	}
	
	@Override
	protected void setAllFields(InternalOrg entity, Transaction transaction) {
		setField(ReportField.CERTIFICATE_NAME, 			entity.getCertificateName());
		setField(ReportField.N4_LOGO_IMAGE, 			StreamHelper.openQuietly(PathHandler.getN4LogoImageFile()));
		setField(ReportField.ORGANIZATION_LOGO_IMAGE,	StreamHelper.openQuietly(PathHandler.getCertificateLogo(entity)));
		
		setAllFields(addressMapBuilder, entity.getAddressInfo(), transaction);
	}
	
}

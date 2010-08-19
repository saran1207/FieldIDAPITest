package com.n4systems.handlers.creator.signup;

import static com.n4systems.handlers.creator.signup.model.builder.AccountPlaceHolderBuilder.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.CatalogOnlyConnectionSaver;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.safetynetwork.TypedOrgConnection.ConnectionType;



public class LinkTenantHandlerImpTest extends TestUsesTransactionBase {


	@Before
	public void setUp() {
		mockTransaction();
	}

	@Test public void should_create_org_connection_between_new_tenant_and_refering_tenant() {
		OrgConnectionSaver mockConnSaver = createMock(OrgConnectionSaver.class);
		mockConnSaver.save(same(mockTransaction), (OrgConnection)anyObject());
		replay(mockConnSaver);
		
		Capture<TypedOrgConnection> catalogOnlyConnection = new Capture<TypedOrgConnection>();
		CatalogOnlyConnectionSaver mockCatalogOnlySaver = createMock(CatalogOnlyConnectionSaver.class);
		mockCatalogOnlySaver.save(same(mockTransaction), capture(catalogOnlyConnection));
		replay(mockCatalogOnlySaver);
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		
		
		LinkTenantHandler sut = new LinkTenantHandlerImp(mockConnSaver, mockCatalogOnlySaver);
		sut.setAccountPlaceHolder(accountPlaceHolder);
		sut.setReferrerOrg(aPrimaryOrg().build());
		sut.link(mockTransaction);
		
		verify(mockConnSaver);
		verify(mockCatalogOnlySaver);
		
		assertEquals(accountPlaceHolder.getPrimaryOrg(), catalogOnlyConnection.getValue().getOwner());
		assertEquals(ConnectionType.CATALOG_ONLY, catalogOnlyConnection.getValue().getConnectionType());

	}
	
	@Test
	public void create_org_connection_uses_referrer_as_vendor_and_signup_as_customer() {
		
		final PrimaryOrg referrerOrg = (PrimaryOrg)OrgBuilder.aPrimaryOrg().build();
		final AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();		
		
		Capture<OrgConnection> orgConnection = new Capture<OrgConnection>();
		OrgConnectionSaver mockConnSaver = createMock(OrgConnectionSaver.class);
		mockConnSaver.save(same(mockTransaction), capture(orgConnection));
		replay(mockConnSaver);
		
		
		CatalogOnlyConnectionSaver mockCatalogOnlySaver = createMock(CatalogOnlyConnectionSaver.class);
		mockCatalogOnlySaver.save(same(mockTransaction), (TypedOrgConnection)anyObject());
		replay(mockCatalogOnlySaver);
		
		
		
		LinkTenantHandler sut = new LinkTenantHandlerImp(mockConnSaver, mockCatalogOnlySaver);
			sut.setAccountPlaceHolder(accountPlaceHolder);
			sut.setReferrerOrg(referrerOrg);
		
		sut.link(mockTransaction);
		
		verify(mockConnSaver);
		verify(mockCatalogOnlySaver);
		
		assertSame(accountPlaceHolder.getPrimaryOrg(), orgConnection.getValue().getCustomer());
		assertSame(referrerOrg, orgConnection.getValue().getVendor());
	}
}

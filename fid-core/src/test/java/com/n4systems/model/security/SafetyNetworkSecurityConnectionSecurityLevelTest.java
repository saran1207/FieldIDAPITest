package com.n4systems.model.security;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.test.helpers.FluentArrayList;


@RunWith(Parameterized.class)
public class SafetyNetworkSecurityConnectionSecurityLevelTest {

	
	
	@Parameters
	public static Collection<Object[]> data() {
		PrimaryOrg manufacturer = aPrimaryOrg().build();
		PrimaryOrg distributor = aPrimaryOrg().build();
		PrimaryOrg rentalShop = aPrimaryOrg().build();
		PrimaryOrg equipmentRenter = aPrimaryOrg().build();
		PrimaryOrg nonConnectedOrg = aPrimaryOrg().build();
		
		
		
		// manufacturer ----> distributor ----> rentalShop ----> equipmentRener
		FluentArrayList<OrgConnection> connections = new FluentArrayList<OrgConnection>(new OrgConnection(manufacturer, distributor), new OrgConnection(distributor, rentalShop), new OrgConnection(rentalShop, equipmentRenter));
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		data.add(new Object[] {connections, SecurityLevel.LOCAL, manufacturer, manufacturer });
		data.add(new Object[] {connections, SecurityLevel.DIRECT, manufacturer, distributor });
		data.add(new Object[] {connections, SecurityLevel.ONE_AWAY, manufacturer, rentalShop });
		data.add(new Object[] {connections, SecurityLevel.MANY_AWAY, manufacturer, equipmentRenter });
		data.add(new Object[] {connections, SecurityLevel.MANY_AWAY, manufacturer, nonConnectedOrg });
		
		
		data.add(new Object[] {connections, SecurityLevel.LOCAL, distributor, distributor });
		data.add(new Object[] {connections, SecurityLevel.DIRECT, distributor, rentalShop });
		data.add(new Object[] {connections, SecurityLevel.ONE_AWAY, distributor, equipmentRenter });
		data.add(new Object[] {connections, SecurityLevel.MANY_AWAY, distributor, nonConnectedOrg });
		
		data.add(new Object[] {connections, SecurityLevel.LOCAL, rentalShop, rentalShop });
		data.add(new Object[] {connections, SecurityLevel.DIRECT, equipmentRenter, rentalShop });
		data.add(new Object[] {connections, SecurityLevel.MANY_AWAY, rentalShop, nonConnectedOrg });
		
		
		data.add(new Object[] {connections, SecurityLevel.LOCAL, equipmentRenter, equipmentRenter });
		data.add(new Object[] {connections, SecurityLevel.MANY_AWAY, equipmentRenter, nonConnectedOrg });
		
		data.add(new Object[] {connections, SecurityLevel.LOCAL, nonConnectedOrg, nonConnectedOrg });
		
		return data;
	}
	
	
	
	private SafetyNetworkSecurityLevelProvider sut = new ThreadSafeSafetyNetworkSecurityLevelProvider();
	private final BaseOrg org2;
	private final SecurityLevel securityLevel;
	private final List<OrgConnection> connections;
	private final BaseOrg org1;
	
	public SafetyNetworkSecurityConnectionSecurityLevelTest(List<OrgConnection> connections, SecurityLevel securityLevel, BaseOrg org1, BaseOrg org2) {
		this.connections = connections;
		this.securityLevel = securityLevel;
		this.org1 = org1;
		this.org2 = org2;
		
	}
	
	
	@Before
	public void initConnections() {
		for (OrgConnection connection : connections) {
			sut.connect(connection);
		}
	}
	
	
	
	
	@Test
	public void should_be_the_correct_security_distance() {
		assertThat(sut.getConnectionSecurityLevel(org1, org2), is(securityLevel));
		assertThat(sut.getConnectionSecurityLevel(org2, org1), is(securityLevel));
	}
	
	
	
	
	
	
	
}

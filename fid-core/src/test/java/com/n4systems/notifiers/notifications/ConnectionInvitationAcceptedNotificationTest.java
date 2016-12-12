package com.n4systems.notifiers.notifications;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ConnectionInvitationAcceptedNotificationTest {

	
	@Test
	public void should_you_get_a_subject_with_the_company_name_at_the_begining() throws Exception {
		ConnectionInvitationAcceptedNotification sut = new ConnectionInvitationAcceptedNotification();
		
		sut.setAcceptingCompanyName("Halo");
		assertEquals("Halo has accepted your invitation on Field ID", sut.subject());
	}
}

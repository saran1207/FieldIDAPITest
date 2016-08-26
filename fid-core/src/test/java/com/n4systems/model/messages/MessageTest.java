package com.n4systems.model.messages;

import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MessageTest {

	
	@Test
	public void should_set_the_senders_name_as_the_name_of_the_sender_org() throws Exception {
		// fixture setup
		PrimaryOrg sender = PrimaryOrgBuilder.aPrimaryOrg().withName("sender").build();
		
		Message sut = new Message();
		
		// exercise
		sut.setSender(sender);
		
		// verify
		assertEquals(sender.getName(), sut.getSender().getName());
	}
	
	@Test
	public void should_set_the_recievers_name_as_the_name_of_the_receiver() throws Exception {
		// fixture setup
		PrimaryOrg receiver = PrimaryOrgBuilder.aPrimaryOrg().withName("receiver").build();
		Message sut = new Message();
		
		// exercise
		sut.setRecipient(receiver);
		
		// verify
		assertEquals(receiver.getName(), sut.getRecipient().getName());
	}
	
	@Test
	public void should_set_the_owner_to_match_the_receiver() throws Exception {
		// fixture setup
		PrimaryOrg receiver = PrimaryOrgBuilder.aPrimaryOrg().withName("receiver").build();
		Message sut = new Message();
		
		// exercise
		sut.setRecipient(receiver);
		
		// verify
		assertEquals(receiver, sut.getOwner());
	}
	
	
	@Test
	public void should_set_the_tenat_to_match_the_receivers_tenant() throws Exception {
		// fixture setup
		PrimaryOrg receiver = PrimaryOrgBuilder.aPrimaryOrg().withName("receiver").build();
		Message sut = new Message();
		
		// exercise
		sut.setRecipient(receiver);
		
		// verify
		assertEquals(receiver.getTenant(), sut.getTenant());
	}
	
	
	
	
}

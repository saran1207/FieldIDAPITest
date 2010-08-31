package com.n4systems.model.messages;

import static org.junit.Assert.*;


import org.junit.Test;

import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.InternalOrg;


public class MessageTest {

	
	@Test
	public void should_set_the_senders_name_as_the_name_of_the_sender_org() throws Exception {
		// fixture setup
		InternalOrg sender = PrimaryOrgBuilder.aPrimaryOrg().withName("sender").build();
		
		Message sut = new Message();
		
		// exercise
		sut.setSender(sender);
		
		// verify
		assertEquals(sender.getName(), sut.getSender());
	}
	
	@Test
	public void should_set_the_recievers_name_as_the_name_of_the_receiver() throws Exception {
		// fixture setup
		InternalOrg receiver = PrimaryOrgBuilder.aPrimaryOrg().withName("receiver").build();
		Message sut = new Message();
		
		// exercise
		sut.setReceiver(receiver);
		
		// verify
		assertEquals(receiver.getName(), sut.getReceiver());
	}
	
	@Test
	public void should_set_the_owner_to_match_the_receiver() throws Exception {
		// fixture setup
		InternalOrg receiver = PrimaryOrgBuilder.aPrimaryOrg().withName("receiver").build();
		Message sut = new Message();
		
		// exercise
		sut.setReceiver(receiver);
		
		// verify
		assertEquals(receiver, sut.getOwner());
	}
	
	
	@Test
	public void should_set_the_tenat_to_match_the_receivers_tenant() throws Exception {
		// fixture setup
		InternalOrg receiver = PrimaryOrgBuilder.aPrimaryOrg().withName("receiver").build();
		Message sut = new Message();
		
		// exercise
		sut.setReceiver(receiver);
		
		// verify
		assertEquals(receiver.getTenant(), sut.getTenant());
	}
	
	
	
	
}

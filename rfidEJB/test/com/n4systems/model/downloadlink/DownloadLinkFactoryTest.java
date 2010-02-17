package com.n4systems.model.downloadlink;

import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.builders.UserBuilder;

public class DownloadLinkFactoryTest {

	@Test
	public void test_create_download_link() {
		DownloadLinkFactory dlf = new DownloadLinkFactory();
		
		UserBean user = UserBuilder.anEmployee().build();
		
		DownloadLink link = dlf.createDownloadLink(user, "myname", ContentType.CSV);
		
		assertEquals(DownloadState.REQUESTED, link.getState());
		assertEquals("myname", link.getName());
		assertEquals(ContentType.CSV, link.getContentType());
		assertEquals(user.getTenant(), link.getTenant());
		assertEquals(user, link.getUser());
	}
}

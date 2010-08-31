package com.n4systems.model.downloadlink;

import static org.junit.Assert.*;

import org.junit.Test;


import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;

public class DownloadLinkFactoryTest {

	@Test
	public void test_create_download_link() {
		DownloadLinkFactory dlf = new DownloadLinkFactory();
		
		User user = UserBuilder.anEmployee().build();
		
		DownloadLink link = dlf.createDownloadLink(user, "myname", ContentType.CSV);
		
		assertEquals(DownloadState.REQUESTED, link.getState());
		assertEquals("myname", link.getName());
		assertEquals(ContentType.CSV, link.getContentType());
		assertEquals(user.getTenant(), link.getTenant());
		assertEquals(user, link.getUser());
	}
}

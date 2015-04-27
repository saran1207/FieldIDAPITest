package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.test.helpers.TempFile;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DownloadLinkActionTest {
	private final ContentType contextType = ContentType.EXCEL;
	
	private DownloadLink link = new DownloadLink() {
		private static final long serialVersionUID = 1L;

		private TempFile downloadFile = new TempFile(true);

		@Override
		public File getFile(boolean createParents) {
			return downloadFile;
		}

		@Override
		public String prepareFileName() {
			return "filename";
		}
	};
	
	private DownloadLinkAction action;
	
	@Before
	public void setupDownloadLink() {
		link.setContentType(contextType);
		link.setName("download_link");

		DownloadLinkSaver downloadLinkSaver = createMock(DownloadLinkSaver.class);
		expect(downloadLinkSaver.update(link)).andReturn(link);

		action = new DownloadLinkAction(null) {
			private static final long serialVersionUID = 1L;
			@Override
			protected DownloadLink loadDownloadLink() {
				return link;
			}
		};
		
		action.setFileId(100L);
		action.setDownloadLinkSaver(downloadLinkSaver );
	}
	
	@After
	public void closeInputStream() {
		InputStream stream = action.getFileStream();
		
		if (stream != null) {
			try { stream.close(); } catch(IOException e) {}
		}
	}

	//These tests had to be disabled, because they're running against code that now uses ServiceLocator, which means
	//we can no longer effectively unit test...
	
//	@Test
	public void download_params_set_after_do_download() {
		String result = action.doDownload();
		
		assertEquals(AbstractAction.SUCCESS, result);
		assertNotNull(action.getFileStream());
		assertEquals(String.valueOf(link.getFile().length()), action.getFileSize());
		assertEquals(contextType.getMimeType(), action.getContentType());
		assertEquals(link.prepareFileName(), action.getFileName());	
	}
	
//	@Test
	public void download_returns_error_on_null_fileid() {
		action.setFileId(null);
		assertEquals(AbstractAction.ERROR, action.doDownload());
	}
}

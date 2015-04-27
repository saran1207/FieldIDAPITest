package com.n4systems.taskscheduling.task;

import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.mail.MailManager;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.persistence.savers.Saver;
import org.junit.Test;

import java.io.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class AbstractExportTaskTest {

	private class AbstractExportTaskImpl extends AbstractExportTask {
		public AbstractExportTaskImpl(DownloadLink downloadLink, String downloadUrl, String templateName, Saver<DownloadLink> linkSaver, MailManager mailManager, Exporter exporter) {
			super(downloadLink, downloadUrl, templateName, linkSaver, mailManager, exporter);
		}
	}
	
	@Test
	public void create_map_writer_is_excel() throws IOException {
		AbstractExportTaskImpl task = new AbstractExportTaskImpl(null, null, null, null, null, null) {
			protected OutputStream getFileStream(File downloadFile) throws FileNotFoundException {
				return new ByteArrayOutputStream();
			}
		};
		
		MapWriter writer = task.createMapWriter(new ByteArrayOutputStream(), UserBuilder.anEmployee().build());
		
		assertNotNull(writer);
		assertTrue(writer instanceof ExcelMapWriter);
	}
	
	@Test
	public void date_format_comes_from_users_primary_org() throws IOException {
		AbstractExportTaskImpl task = new AbstractExportTaskImpl(null, null, null, null, null, null);
		
		String expectedFormat = "yyyy-MM-dd";
		
		User user = UserBuilder.anEmployee().build();
		user.getOwner().getPrimaryOrg().setDateFormat(expectedFormat);
		
		String dateFormat = task.getDateFormat(user);
		
		assertEquals(expectedFormat, dateFormat);
	}

	//Due to changes in the way that we process AbstractExportTask classes and a dependence on ServiceLocator actually
	//locating services, we can no longer unit test this particular functionality.
//	@Test
	public void generate_file_opens_map_writer_and_calls_export() throws Exception {
		Exporter exporter = createMock(Exporter.class);
		final MapWriter mapWriter = createMock(MapWriter.class);
		
		AbstractExportTaskImpl task = new AbstractExportTaskImpl(null, null, null, null, null, exporter) {
			protected MapWriter createMapWriter(File downloadFile, User user) throws IOException {
				return mapWriter;
			}
		};
		
		exporter.export(mapWriter);
		mapWriter.close();
		
		replay(mapWriter);
		replay(exporter);
		
		task.generateFile(null, null, null);
		
		verify(mapWriter);
		verify(exporter);
	}

}

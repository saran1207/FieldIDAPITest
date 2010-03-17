package com.n4systems.taskscheduling.task;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.MailManager;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.persistence.savers.Saver;

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
		
		MapWriter writer = task.createMapWriter(null, UserBuilder.anEmployee().build());
		
		assertNotNull(writer);
		assertTrue(writer instanceof ExcelMapWriter);
	}
	
	@Test
	public void date_format_comes_from_users_primary_org() throws IOException {
		AbstractExportTaskImpl task = new AbstractExportTaskImpl(null, null, null, null, null, null);
		
		String expectedFormat = "yyyy-MM-dd";
		
		UserBean user = UserBuilder.anEmployee().build();
		user.getOwner().getPrimaryOrg().setDateFormat(expectedFormat);
		
		String dateFormat = task.getDateFormat(user);
		
		assertEquals(expectedFormat, dateFormat);
	}
	
	@Test
	public void generate_file_opens_map_writer_and_calls_export() throws Exception {
		Exporter exporter = createMock(Exporter.class);
		final MapWriter mapWriter = createMock(MapWriter.class);
		
		AbstractExportTaskImpl task = new AbstractExportTaskImpl(null, null, null, null, null, exporter) {
			protected MapWriter createMapWriter(File downloadFile, UserBean user) throws IOException {
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

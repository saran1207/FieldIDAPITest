package com.n4systems.taskscheduling.task;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.CsvMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.exporting.io.MapWriterFactory;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.test.helpers.TempFile;

public class CustomerExportTaskTest {

	@Test
	public void test_generate_file() throws Exception {
		Exporter exporter = createMock(Exporter.class);
		MapWriterFactory writerFactory = createMock(MapWriterFactory.class);
		
		DownloadLink link = new DownloadLink();
		link.setContentType(ContentType.EXCEL);
		
		MapWriter writer = new CsvMapWriter(new ByteArrayOutputStream());
		
		CustomerExportTask task = new CustomerExportTask(link, null, null, null, exporter, writerFactory);
		
		expect(writerFactory.create((OutputStream)anyObject(), (ContentType)anyObject())).andReturn(writer);
		
		exporter.export(writer);
		
		replay(writerFactory);
		replay(exporter);
		
		task.generateFile(new TempFile(), null, null);
		
		verify(writerFactory);
		verify(exporter);
	}

}

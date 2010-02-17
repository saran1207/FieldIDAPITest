package com.n4systems.exporting.io;

import java.io.IOException;
import java.io.OutputStream;

import com.n4systems.model.downloadlink.ContentType;

public class MapWriterFactory {
	
	public MapWriterFactory() {}
	
	public MapWriter create(OutputStream out, ContentType type) throws IOException {
		switch (type) {
			case EXCEL:
				return new ExcelMapWriter(out);
			case CSV:
				return new CsvMapWriter(out);
			default:
				throw new IllegalArgumentException("Invalid export content type [" + type.name() + "]");
		}
	}
	
}

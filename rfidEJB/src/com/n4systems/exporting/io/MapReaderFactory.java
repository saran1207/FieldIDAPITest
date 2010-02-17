package com.n4systems.exporting.io;

import java.io.IOException;
import java.io.InputStream;

import com.n4systems.util.ArrayUtils;

public class MapReaderFactory {
	private static final String[] CSV_CONTENT_TYPES = {
		"text/csv"
	};	
	
	private static final String[] EXCEL_CONTENT_TYPES = {
		"application/vnd.ms-excel", 
		"application/msexcel", "application/x-msexcel", 
		"application/x-ms-excel", "application/x-excel", 
		"application/x-dos_ms_excel", "application/xls"
	};
	
	public MapReaderFactory() {}
	
	public boolean isSupportedContentType(String contentType) {
		return isCsv(contentType) || isExcel(contentType);
	}

	public boolean isCsv(String contentType) {
		return ArrayUtils.contains(contentType, CSV_CONTENT_TYPES);
	}

	public boolean isExcel(String contentType) {
		return ArrayUtils.contains(contentType, EXCEL_CONTENT_TYPES);
	}

	public MapReader createMapReader(InputStream in, String contentType) throws IOException {
		MapReader reader;

		if (isCsv(contentType)) {
			reader = new CsvMapReader(in);
		} else if (isExcel(contentType)){
			reader = new ExcelMapReader(in);
		} else {
			throw new IllegalArgumentException("Unsupported content-type [" + contentType + "]");
		}
		
		return reader;
	}
}

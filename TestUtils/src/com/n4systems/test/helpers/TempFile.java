package com.n4systems.test.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class TempFile extends File {
	private static final long serialVersionUID = 1L;

	public TempFile(boolean withRandomData) {
		this("test_" + UUID.randomUUID().toString().substring(25), true, withRandomData, true);
	}
	
	public TempFile(String fileName, boolean create, boolean withRandomData, boolean deleteOnExit) {
		super(System.getProperty("java.io.tmpdir"), fileName);
		
		if (deleteOnExit) {
			deleteOnExit();
		}
		
		if (create) {
			try {
				createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("Could not create TempFile", e);
			}
		}
		
		if (withRandomData) {
			writeRandomData();
		}
	}
	
	private void writeRandomData() {
		FileWriter writer = null;
		try {
			writer = new FileWriter(this);
			
			// for dummy data we'll use 25 random uuid's
			for (int i = 0; i < 25; i++) {
				writer.write(UUID.randomUUID().toString());
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Could not write random data to TempFile", e);
		} finally {
			if (writer != null) { try { writer.close(); } catch (IOException e) {} }
		}
	}
}

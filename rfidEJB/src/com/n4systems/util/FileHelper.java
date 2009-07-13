package com.n4systems.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class FileHelper {
	
	public static File getTempDir(File baseDir) throws IOException {
		File reportDir = new File(baseDir, UUID.randomUUID().toString());
		
		if(!reportDir.mkdirs()) {
			throw new IOException("Unable to create directory path: " + reportDir.getPath());
		}
		
		return reportDir;
	}
	
	public static File getNonConflictingFile(File directory, String fileName, String suffix) throws IOException {
		int fileNum = 1;
		File file = new File(directory, fileName + suffix);
		
		// if the file exists, we'll need to append numbers until we find one that doesn't
		if(file.exists()) {
			// find a file that doesn't exist
			while((file = new File(directory, fileName + "-" + fileNum + suffix)).exists()) {
				fileNum++;
			}
		}
		
		return file;
	}
	
	// XXX - make me recursive
	public static File zipDirectory(File directory, String fileName) throws FileNotFoundException, IOException {
		OutputStream out = null;
		ZipOutputStream zipOut = null;
		
		FileInputStream in = null;
		byte[] buffer = new byte[1024 * 8];
		int readBytes;

		File zipFile = getNonConflictingFile(directory.getParentFile(), fileName, ".zip");
		
		try {
			out = new FileOutputStream(zipFile);
			zipOut = new ZipOutputStream(out);
			
			//walk files in the directory and write them to the zip stream
			//note that this is not recursive in any way ...
			for(File file: directory.listFiles()) {
				in = new FileInputStream(file);
				
				// each file is a new zip entry ...
				ZipEntry zipFileEntry = new ZipEntry(file.getName());
				zipOut.putNextEntry(zipFileEntry);
				
				while((readBytes = in.read(buffer)) != -1) {
					zipOut.write(buffer, 0, readBytes); 
				}
				
				in.close();
			}
		} finally {
			// close any streams that haven't been closed .... P.S. I love IOUtils
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(zipOut);
			IOUtils.closeQuietly(out);
		}
		
		return zipFile;
	}
}

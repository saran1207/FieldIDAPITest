package com.n4systems.reporting;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.io.IOUtils;

import com.n4systems.exceptions.ReportException;

public class CertificatePrinter {
	
	
	
	private  JRPdfExporter getPdfExporter() {
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

		return exporter;
	}
	
	public  void printToPDF(List<JasperPrint> printList, OutputStream outputStream) throws ReportException {
		JRPdfExporter exporter = getPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, printList);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new ReportException("Failed to print report", e);
		}
	}

	public  byte[] printToPDF(List<JasperPrint> printList) throws ReportException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		printToPDF(printList, byteBuffer);
		return byteBuffer.toByteArray();
	}

	public  void printToPDF(JasperPrint jasperPrint, OutputStream outputStream) throws ReportException {
		JRPdfExporter exporter = getPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new ReportException("Failed to print report", e);
		}
	}
	
	public  void printToPDF(JasperPrint jasperPrint, File file) throws ReportException {
		OutputStream fileOut = null;
		try {
			fileOut = new BufferedOutputStream(new FileOutputStream(file));
			printToPDF(jasperPrint, fileOut);
		} catch(IOException e) {
			throw new ReportException("Failed to print report", e);
		} finally {
			IOUtils.closeQuietly(fileOut);
		}
	}

	public byte[] printToPDF(JasperPrint jasperPrint) throws ReportException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		printToPDF(jasperPrint, byteBuffer);
		return byteBuffer.toByteArray();
	}
	
}

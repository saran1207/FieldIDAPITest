package com.n4systems.reporting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Inspection;
import com.n4systems.model.LineItem;
import com.n4systems.model.PrintOut;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.SubInspection;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.mapbuilders.BaseInspectionMapBuilder;
import com.n4systems.reporting.mapbuilders.LineItemMapBuilder;
import com.n4systems.reporting.mapbuilders.MapBuilder;
import com.n4systems.reporting.mapbuilders.ProductTypeMapBuilder;
import com.n4systems.reporting.mapbuilders.ProofTestMapBuilder;
import com.n4systems.util.ReportMap;

public class InspectionCertificateGenerator {
	private final MapBuilder<ProductType> productTypeMapBuilder; 
	private final MapBuilder<LineItem> orderMapBuilder;
	private final MapBuilder<ProofTestInfo> proofTestMapBuilder;
	private final MapBuilder<Inspection> baseInspectionMapBuilder;
	private final DateTimeDefiner dateDefiner;
	
	public InspectionCertificateGenerator(DateTimeDefiner dateDefiner, MapBuilder<Inspection> baseInspectionMapBuilder, MapBuilder<ProductType> productTypeMapBuilder, MapBuilder<ProofTestInfo> proofTestMapBuilder, MapBuilder<LineItem> orderMapBuilder) {
		this.dateDefiner = dateDefiner;
		this.baseInspectionMapBuilder = baseInspectionMapBuilder;
		this.productTypeMapBuilder = productTypeMapBuilder;
		this.proofTestMapBuilder = proofTestMapBuilder;
		this.orderMapBuilder = orderMapBuilder;
	}
	
	public InspectionCertificateGenerator(DateTimeDefiner dateDefiner) {
		this(dateDefiner, new BaseInspectionMapBuilder(dateDefiner), new ProductTypeMapBuilder(), new ProofTestMapBuilder(), new LineItemMapBuilder());
	}
	
	public JasperPrint generate(InspectionReportType type, Inspection inspection, Transaction transaction) throws NonPrintableEventType, ReportException {
		JasperPrint jPrint = null;
		
		if (!inspection.isPrintableForReportType(type)) {
			throw new NonPrintableEventType(String.format("Inspection [%s] was not printable or did not have a PrintOut for InspectionReportType [%s]", inspection.getId(), type.getDisplayName()));
		}
		
		PrintOut printOutToPrint = inspection.getType().getGroup().getPrintOutForReportType(type);
		
		if (printOutToPrint.isWithSubInspections()) {
			jPrint = generateFull(inspection, transaction, printOutToPrint);
		} else {
			jPrint = generate(inspection, transaction, printOutToPrint);
		}
	
		return jPrint;
	}
	
	private ReportMap<Object> createMainReportMap(Inspection inspection, File jasperFile, Transaction transaction) {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		System.out.println(jasperFile.getParent() + "/");
		baseInspectionMapBuilder.addParams(reportMap, inspection, transaction);
		
		return reportMap;
	}

	private JasperPrint generateFull(Inspection inspection, Transaction transaction, PrintOut printOut) throws NonPrintableEventType, ReportException {
		File jasperFile = resolveJasperFile(inspection, printOut);

		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = loadJasperReport(jasperFile);
			
			ReportMap<Object> reportMap = createMainReportMap(inspection, jasperFile, transaction);
			
			ReportMap<Object> inspectionReportMap = new InspectionReportMapProducer(inspection, dateDefiner).produceMap();
			reportMap.put("mainInspection", inspectionReportMap);
			reportMap.put("product", inspectionReportMap.get("product"));
			
			List<ReportMap<Object>> inspectionResultMaps = new ArrayList<ReportMap<Object>>();
			inspectionResultMaps.add(inspectionReportMap);
			
			for (SubInspection subInspection : inspection.getSubInspections()) {
				inspectionResultMaps.add(new InspectionReportMapProducer(subInspection, dateDefiner).produceMap());
			}

			JRDataSource jrDataSource = new JRMapCollectionDataSource(inspectionResultMaps);
			
			reportMap.put("allInspections", inspectionResultMaps);
			
			
			
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);
		
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}

	private JasperPrint generate(Inspection inspection, Transaction transaction, PrintOut printOut) throws NonPrintableEventType, ReportException {
		File jasperFile = resolveJasperFile(inspection, printOut);

		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = loadJasperReport(jasperFile);
			
			ReportMap<Object> reportMap = createMainReportMap(inspection, jasperFile, transaction);
	
			reportMap.put("chartPath", PathHandler.getChartImageFile(inspection).getAbsolutePath());
	
			proofTestMapBuilder.addParams(reportMap, inspection.getProofTestInfo(), transaction);
			productTypeMapBuilder.addParams(reportMap, inspection.getProduct().getType(), transaction);
			orderMapBuilder.addParams(reportMap, inspection.getProduct().getShopOrder(), transaction);
	
			reportMap.putAll(new ProductReportMapProducer(inspection.getProduct(), dateDefiner).produceMap());
			
			ReportMap<Object> inspectionMap = new InspectionReportMapProducer(inspection, dateDefiner).produceMap();
			reportMap.putAll(inspectionMap);
			
			
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, (JRDataSource)inspectionMap.get("results"));
		
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}
	
	private JasperReport loadJasperReport(File jasperFile) throws JRException {
		return (JasperReport)JRLoader.loadObject(jasperFile);
	}

	private File resolveJasperFile(Inspection inspection, PrintOut printOut) throws NonPrintableEventType, ReportException {
		File jasperFile = PathHandler.getPrintOutFile(printOut);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException(String.format("Report file [%s] missing for tenant [%s] and Inspection Type Group [%s]", jasperFile.getAbsolutePath(), inspection.getTenant().getName(), inspection.getType().getGroup().getName()));
		}
		
		return jasperFile;
	}

}

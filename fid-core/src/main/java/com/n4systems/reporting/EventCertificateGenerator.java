package com.n4systems.reporting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.LineItem;
import com.n4systems.model.PrintOut;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.SubEvent;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.mapbuilders.BaseEventMapBuilder;
import com.n4systems.reporting.mapbuilders.LineItemMapBuilder;
import com.n4systems.reporting.mapbuilders.MapBuilder;
import com.n4systems.reporting.mapbuilders.AssetTypeMapBuilder;
import com.n4systems.reporting.mapbuilders.ProofTestMapBuilder;
import com.n4systems.util.ReportMap;

public class EventCertificateGenerator {
	private final MapBuilder<AssetType> productTypeMapBuilder;
	private final MapBuilder<LineItem> orderMapBuilder;
	private final MapBuilder<ProofTestInfo> proofTestMapBuilder;
	private final MapBuilder<Event> baseEventMapBuilder;
	private final DateTimeDefiner dateDefiner;
	
	public EventCertificateGenerator(DateTimeDefiner dateDefiner, MapBuilder<Event> baseEventMapBuilder, MapBuilder<AssetType> productTypeMapBuilder, MapBuilder<ProofTestInfo> proofTestMapBuilder, MapBuilder<LineItem> orderMapBuilder) {
		this.dateDefiner = dateDefiner;
		this.baseEventMapBuilder = baseEventMapBuilder;
		this.productTypeMapBuilder = productTypeMapBuilder;
		this.proofTestMapBuilder = proofTestMapBuilder;
		this.orderMapBuilder = orderMapBuilder;
	}
	
	public EventCertificateGenerator(DateTimeDefiner dateDefiner) {
		this(dateDefiner, new BaseEventMapBuilder(dateDefiner), new AssetTypeMapBuilder(), new ProofTestMapBuilder(), new LineItemMapBuilder());
	}
	
	public JasperPrint generate(EventReportType type, Event event, Transaction transaction) throws NonPrintableEventType, ReportException {
		JasperPrint jPrint = null;
		
		guard(type, event);
		
		PrintOut printOutToPrint = event.getType().getGroup().getPrintOutForReportType(type);
		
		if (printOutToPrint.isWithSubEvents()) {
			jPrint = generateFull(event, transaction, printOutToPrint);
		} else {
			jPrint = generate(event, transaction, printOutToPrint);
		}
	
		return jPrint;
	}

	private void guard(EventReportType type, Event event) throws NonPrintableEventType {
		if (!event.isPrintableForReportType(type)) {
			throw new NonPrintableEventType(String.format("Event [%s] was not printable or did not have a PrintOut for EventReportType [%s]", event.getId(), type.getDisplayName()));
		}
	}
	
	private ReportMap<Object> createMainReportMap(Event event, File jasperFile, Transaction transaction) {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		reportMap.put("SUBREPORT_DIR", jasperFile.getParent() + "/");
		
		baseEventMapBuilder.addParams(reportMap, event, transaction);
		
		return reportMap;
	}

	private JasperPrint generateFull(Event event, Transaction transaction, PrintOut printOut) throws NonPrintableEventType, ReportException {
		File jasperFile = resolveJasperFile(event, printOut);

		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = loadJasperReport(jasperFile);
			
			ReportMap<Object> reportMap = createMainReportMap(event, jasperFile, transaction);
			
			ReportMap<Object> eventReportMan = new EventReportMapProducer(event, dateDefiner).produceMap();
			reportMap.put("mainInspection", eventReportMan);
			reportMap.put("product", eventReportMan.get("product"));
			
			List<ReportMap<Object>> eventResultMaps = new ArrayList<ReportMap<Object>>();
			eventResultMaps.add(eventReportMan);
			
			for (SubEvent subEvent : event.getSubEvents()) {
				eventResultMaps.add(new SubEventReportMapProducer(subEvent, event, dateDefiner).produceMap());
			}

			JRDataSource jrDataSource = new JRMapCollectionDataSource(eventResultMaps);
			
			reportMap.put("allInspections", eventResultMaps);
			
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, jrDataSource);
		
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		
		return jasperPrint;
	}

	private JasperPrint generate(Event event, Transaction transaction, PrintOut printOut) throws NonPrintableEventType, ReportException {
		File jasperFile = resolveJasperFile(event, printOut);

		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = loadJasperReport(jasperFile);
			
			ReportMap<Object> reportMap = createMainReportMap(event, jasperFile, transaction);
	
			reportMap.put("chartPath", PathHandler.getChartImageFile(event).getAbsolutePath());
	
			proofTestMapBuilder.addParams(reportMap, event.getProofTestInfo(), transaction);
			productTypeMapBuilder.addParams(reportMap, event.getAsset().getType(), transaction);
			orderMapBuilder.addParams(reportMap, event.getAsset().getShopOrder(), transaction);
	
			reportMap.putAll(new AssetReportMapProducer(event.getAsset(), dateDefiner).produceMap());
			
			ReportMap<Object> eventMap = new EventReportMapProducer(event, dateDefiner).produceMap();
			reportMap.putAll(eventMap);
			
			
			jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, (JRDataSource)eventMap.get("results"));
		
		} catch (Exception e) {
			throw new ReportException("Failed to generate report", e);
		}

		return jasperPrint;
	}
	
	private JasperReport loadJasperReport(File jasperFile) throws JRException {
		return (JasperReport)JRLoader.loadObject(jasperFile);
	}

	private File resolveJasperFile(Event event, PrintOut printOut) throws NonPrintableEventType, ReportException {
		File jasperFile = PathHandler.getPrintOutFile(printOut);

		// check to see if the report exists
		if (!jasperFile.canRead()) {
			throw new ReportException(String.format("Report file [%s] missing for tenant [%s] and Event Type Group [%s]", jasperFile.getAbsolutePath(), event.getTenant().getName(), event.getType().getGroup().getName()));
		}
		
		return jasperFile;
	}

}

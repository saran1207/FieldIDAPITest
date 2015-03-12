package com.n4systems.reporting;

import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is just to test the report map producer and the report templates to make sure they've been built correctly...
 *
 * Created by Jordan Heath on 14-11-05.
 */
public class ReportTester implements Runnable {

    public static void main(String[] args) {
        ReportTester myself = new ReportTester();

        myself.run();
    }

    @Override
    public void run() {
        ProcedureDefinition procDef = new ProcedureDefinition();
        procDef.setProcedureCode("Procedure Code");
        procDef.setEquipmentDescription("Equipment Description");
        procDef.setEquipmentNumber("Equipment Number");
        procDef.setEquipmentLocation("Equipment Location");
        procDef.setBuilding("Building");
        procDef.setWarnings("Warnings");


        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPosition("The Boss");

        procDef.setModifiedBy(user);
        procDef.setApprovedBy(user);
        procDef.setDevelopedBy(user);
        procDef.setModified(new Date(System.currentTimeMillis()));
        procDef.setCreated(new Date(System.currentTimeMillis()));
        procDef.setRevisionNumber(Long.parseLong("1"));
        procDef.setPublishedState(PublishedState.PUBLISHED);

//        procDef.setIsolationPoints(Lists.newArrayList());

        List<IsolationPoint> isolationPointList = Arrays.stream(IsolationPointSourceType.values()).map(this::makeFakeIsolationPointOfType).collect(Collectors.toList());

        procDef.setIsolationPoints(isolationPointList);

        HashMap<String, Object> reportMap = new HashMap<>();

//        File subreportFile = new File("/Users/jheath/Downloads/long-form/procedure-isolation-points-long.jasper");
//        File subreportFile = new File("/Users/jheath/Downloads/procedure-updated/procedure-isolation-points-long.jasper");
        File subreportFile = new File("/Users/jheath/Downloads/short-form-2/procedure-isolation-points-long.jasper");
//        File imageSubreportFile = new File("/Users/jheath/Downloads/short-form-1/isolation-points-images-short.jasper");

        FileInputStream inStream = null;
        FileInputStream imageSubreportStream = null;

        try {
            inStream = new FileInputStream(subreportFile);
//            imageSubreportStream = new FileInputStream(imageSubreportFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        reportMap.put("isolationPointSubreport", inStream);
//        reportMap.put("imageSubreport", imageSubreportStream);

        reportMap.putAll(new LotoPrintoutReportMapProducer("Report Title",
                            procDef,
                            new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault()),
                            null,
                            null).produceMap());


        try {
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("/Users/jheath/Downloads/procedure-shortform/procedure.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("/Users/jheath/Downloads/short-form-2/procedure.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, new JREmptyDataSource());
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

            File file = new File("/Users/jheath/Downloads/test-short-form-2.pdf");

            FileOutputStream output = new FileOutputStream(file);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();
            output.close();

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }

    private IsolationPoint makeFakeIsolationPointOfType(IsolationPointSourceType type) {
        IsolationPoint isolationPoint = new IsolationPoint();
        isolationPoint.setCheck("Check");

        IsolationDeviceDescription isolationDeviceDescription = new IsolationDeviceDescription();
        isolationDeviceDescription.setFreeformDescription("Isolation Device");

        isolationPoint.setDeviceDefinition(isolationDeviceDescription);
        isolationPoint.setSourceType(type);
        isolationPoint.setSourceText("Source Text");
        isolationPoint.setMethod("Method");
        isolationPoint.setIdentifier("Identifier");
        isolationPoint.setLocation("Location");
        isolationPoint.setFwdIdx(1);

        return isolationPoint;
    }

}

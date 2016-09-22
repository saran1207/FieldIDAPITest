package com.fieldid.service;

import com.fieldid.model.CountByTenant;
import com.fieldid.service.asset.AssetUsageAnalyzerService;
import com.fieldid.service.event.EventUsageAnalyzerService;
import com.fieldid.service.procedure.ProcedureUsageAnalyzerService;
import com.fieldid.service.procedure.definition.ProcedureDefinitionUsageAnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
@Service
public class AnalyzerService {
    private static final Logger logger = Logger.getLogger(AnalyzerService.class);

    private static final String outputFormat = "| %-40s | %10d |\n";
    private static final String outputHeaderFormat = "| %-40s | %10s |\n";

    private AssetUsageAnalyzerService assetService;
    private EventUsageAnalyzerService eventService;
    private ProcedureUsageAnalyzerService procedureService;
    private ProcedureDefinitionUsageAnalyzerService procedureDefinitionService;



    @Autowired
    public AnalyzerService(AssetUsageAnalyzerService assetService,
                           EventUsageAnalyzerService eventService,
                           ProcedureUsageAnalyzerService procedureService,
                           ProcedureDefinitionUsageAnalyzerService procedureDefinitionService) {
        this.assetService = assetService;
        this.eventService = eventService;
        this.procedureService = procedureService;
        this.procedureDefinitionService = procedureDefinitionService;
    }

    public void analyze() {
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("/tmp/analysis-output.txt"))) {
            analyzeAssets(writer);
            analyzeEvents(writer);
            analyzeProcedureDefinitions(writer);
            analyzeProcedures(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void analyzeProcedures(BufferedWriter writer) throws IOException {
        logger.info("Procedures Analysis Start");
        writer.write("Procedures Analysis Start\n");

        logger.info("Since the beginning of time...");
        writer.write("Since the beginning of time...\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedures"));
        printResults(procedureService.getTotalProcedureCountByTenant(), writer);

        logger.info("In the last month...");
        writer.write("In the last month...\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedures"));
        printResults(procedureService.getPastMonthProcedureCountByTenant(), writer);

        logger.info("Each month for the last year...");
        writer.write("Each month for the last year...\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedures"));
        for (int i = 0; i < 13; i++) {
            printResults(procedureService.getMonthlyProcedureCountByTenant(i), writer);
        }

        logger.info("Procedures Analysis End");
        writer.write("Procedures Analysis End\n");
    }

    private void analyzeProcedureDefinitions(BufferedWriter writer) throws IOException {
        logger.info("Procedure Definitions Analysis Start");
        writer.write("Procedure Definitions Analysis Start\n");

        logger.info("Since the beginning of time...");
        writer.write("Since the beginning of time...\n");
        printResults(procedureDefinitionService.getTotalProcedureDefinitionCountByTenant(), writer);

        logger.info("In the last month...");
        writer.write("In the last month...\n");
        printResults(procedureDefinitionService.getPastMonthProcedureDefinitionCountByTenant(), writer);

        logger.info("Procedure Definitions Analysis End");
        writer.write("Procedure Definitions Analysis End\n");
    }

    private void analyzeEvents(BufferedWriter writer) {
        logger.info("Event Analysis Start");
        logger.info("Event Analysis Start");

        logger.info("Since the beginning of time...");
        logger.info("Since the beginning of time...");
        printResults(eventService.getTotalEventCountByTenant(), writer);

        logger.info("In the last month...");
        logger.info("In the last month...");
        printResults(eventService.getPastMonthEventCountByTenant(), writer);

        logger.info("In the past 12 months...");
        logger.info("In the past 12 months...");
        for (int i = 0; i < 13; i++) {
            eventService.getTotalEventCountByTenantByMonth(i);
        }

        logger.info("Event Analysis End");
        logger.info("Event Analysis End");
    }

    private void analyzeAssets(BufferedWriter writer) {
        logger.info("Asset Analysis Start");

        logger.info("Since the beginning of time...");
        printResults(assetService.getTotalAssetCountByTenant(), writer);

        logger.info("In the last month...");
        printResults(assetService.getPastMonthAssetCountByTenant(), writer);

        logger.info("Asset Analysis End");
    }

    private void printResults(List<CountByTenant> countOfSomeSort, BufferedWriter writer) {
        logger.info("There are " + countOfSomeSort.size() + " results!");

        countOfSomeSort.forEach(
                countByTenant -> {
                    try {
                        writer.write(String.format(outputFormat, countByTenant.getName(), countByTenant.getCount()));
                    } catch (IOException e) {
                        logger.error("There was a problem while writing to the output file...", e);
                    }
                }
        );

        logger.info("-----------------------");
    }
}

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
import java.time.LocalDate;
import java.util.List;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
@Service
public class AnalyzerService {
    private static final Logger logger = Logger.getLogger(AnalyzerService.class);

    private static final String outputFormat = "| %-40s | %40d |\n";
    private static final String outputHeaderFormat = "| %-40s | %40s |\n";

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
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedures"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(procedureService.getTotalProcedureCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        logger.info("In the last month...");
        writer.write("In the last month...\n");
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedures"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(procedureService.getPastMonthProcedureCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        LocalDate localDate = LocalDate.now();

        logger.info("Each month for the last year...");
        writer.write("Each month for the last year...\n");
        for (int i = 0; i < 13; i++) {
            writer.write("For the month of " + localDate.minusMonths(i).getMonth().name() + ":\n");
            writer.write("--------------------------------------------------------------------------------------\n");
            writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedures"));
            writer.write("--------------------------------------------------------------------------------------\n");
            printResults(procedureService.getMonthlyProcedureCountByTenant(i), writer);
            writer.write("--------------------------------------------------------------------------------------\n");
            writer.write("\n\n\n");
        }

        logger.info("Procedures Analysis End");
        writer.write("Procedures Analysis End\n");
    }

    private void analyzeProcedureDefinitions(BufferedWriter writer) throws IOException {
        logger.info("Procedure Definitions Analysis Start");
        writer.write("Procedure Definitions Analysis Start\n");

        logger.info("Since the beginning of time...");
        writer.write("Since the beginning of time...\n");
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedure Definitions"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(procedureDefinitionService.getTotalProcedureDefinitionCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        logger.info("In the last month...");
        writer.write("In the last month...\n");
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Procedure Definitions"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(procedureDefinitionService.getPastMonthProcedureDefinitionCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        logger.info("Procedure Definitions Analysis End");
        writer.write("Procedure Definitions Analysis End\n");
    }

    private void analyzeEvents(BufferedWriter writer) throws IOException {
        logger.info("Event Analysis Start");
        writer.write("Event Analysis Start\n");

        logger.info("Since the beginning of time...");
        writer.write("Since the beginning of time...\n");
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Events"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(eventService.getTotalEventCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        logger.info("In the last month...");
        writer.write("In the last month...\n");
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Events"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(eventService.getPastMonthEventCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        LocalDate localDate = LocalDate.now();

        logger.info("In the past 12 months...");
        writer.write("In the past 12 months...\n");
        for (int i = 0; i < 13; i++) {
            writer.write("For the month of " + localDate.minusMonths(i).getMonth().name() + ":\n");
            writer.write("--------------------------------------------------------------------------------------\n");
            writer.write(String.format(outputHeaderFormat, "Tenant Name", "Events"));
            writer.write("--------------------------------------------------------------------------------------\n");
            printResults(eventService.getTotalEventCountByTenantByMonth(i), writer);
            writer.write("--------------------------------------------------------------------------------------\n");
            writer.write("\n\n\n");
        }

        logger.info("Event Analysis End");
        writer.write("Event Analysis End\n");
    }

    private void analyzeAssets(BufferedWriter writer) throws IOException {
        logger.info("Asset Analysis Start");
        writer.write("Asset Analysis Start\n");

        logger.info("Since the beginning of time...");
        writer.write("Since the beginning of time...\n");
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Assets"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(assetService.getTotalAssetCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        logger.info("In the last month...");
        writer.write("In the last month...\n");
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write(String.format(outputHeaderFormat, "Tenant Name", "Assets"));
        writer.write("--------------------------------------------------------------------------------------\n");
        printResults(assetService.getPastMonthAssetCountByTenant(), writer);
        writer.write("--------------------------------------------------------------------------------------\n");
        writer.write("\n\n\n");

        logger.info("Asset Analysis End");
        writer.write("Asset Analysis End\n");
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

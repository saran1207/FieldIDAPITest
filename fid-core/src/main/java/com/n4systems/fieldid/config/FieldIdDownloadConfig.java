package com.n4systems.fieldid.config;

import com.n4systems.fieldid.service.asset.AssetExcelExportService;
import com.n4systems.fieldid.service.event.EventExcelExportService;
import com.n4systems.fieldid.service.event.EventPrintService;
import com.n4systems.fieldid.service.event.EventSummaryJasperGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldIdDownloadConfig {
    @Bean
    public EventPrintService eventPrintService() {
        return new EventPrintService();
    }

    @Bean
    public EventSummaryJasperGenerator eventSummaryJasperGenerator() {
        return new EventSummaryJasperGenerator();
    }

    @Bean
    public EventExcelExportService eventExcelExportService() {
        return new EventExcelExportService();
    }

    @Bean
    public AssetExcelExportService assetExcelExportService() {
        return new AssetExcelExportService();
    }
}

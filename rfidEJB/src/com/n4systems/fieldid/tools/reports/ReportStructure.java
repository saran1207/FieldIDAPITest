package com.n4systems.fieldid.tools.reports;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Use {@link ColumnMapping} and {@link ColumnMappingFactory}
 */
@Deprecated
public class ReportStructure extends TableStructure {

	private String reportType;
	private ReportStructure( String reportType ) {
		super();
		this.reportType = reportType;
	}
	
	public static ReportStructure getReportType( String reportType, boolean usingSerialNumber ) {
		if (reportType == null || reportType.equalsIgnoreCase("inspectionSummary")) {
			return createInspectionSummaryReport( usingSerialNumber );
		} else if (reportType.equalsIgnoreCase("inspectionSummaryCG")) {
			return createInspectionSummaryCGReport( usingSerialNumber );
		} else if (reportType.equalsIgnoreCase("inspectionSummaryQuick")) {
			return createInspectionSummaryQuickReport( usingSerialNumber );
		} else if (reportType.equalsIgnoreCase("inspectionSummaryCharge")) {
			return createInspectionSummaryChargeReport( usingSerialNumber );
		} else if (reportType.equalsIgnoreCase("nisInspectionSummary")) {
			return createNISInspectionReport( usingSerialNumber );
		} else if (reportType.equalsIgnoreCase("nisInspectionSummary2")) {
			return createNISInspectionReport2( usingSerialNumber );
		} else if (reportType.equalsIgnoreCase("hercInspectionSummary")) {
			return createHercInspectionSummaryReport( usingSerialNumber );
		}
		
		
		return null;
		
	}
	
	private static ReportStructure createInspectionSummaryQuickReport( boolean usingSerialNumber ) {
		ReportStructure rstruct = new ReportStructure( "inspectionSummaryQuick" );
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.InspectionType );
		columns.add( getSerialNumber( usingSerialNumber ) );
		columns.add( ColumnDefinition.Description );
		columns.add( ColumnDefinition.InspectionDate );
		columns.add( ColumnDefinition.Comments );
		columns.add( ColumnDefinition.Empty );
		rstruct.setColumns( columns );
	          
		return rstruct;
	}
	
	

	private static ReportStructure createInspectionSummaryChargeReport( boolean usingSerialNumber ) {
		ReportStructure rstruct = new ReportStructure( "inspectionSummaryCharge" );
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.InspectionType );
		columns.add( ColumnDefinition.Division );
		columns.add( ColumnDefinition.Location );
		columns.add( getSerialNumber( usingSerialNumber ) );
		columns.add( ColumnDefinition.ProductType );
		columns.add( ColumnDefinition.Description );
		columns.add( ColumnDefinition.Charge );
		columns.add( ColumnDefinition.Result );
		columns.add( ColumnDefinition.Comments );
		columns.add( ColumnDefinition.Empty );
		rstruct.setColumns( columns );
        
		return rstruct;
	}

	

	private static ReportStructure createInspectionSummaryCGReport( boolean usingSerialNumber ) {
		ReportStructure rstruct = new ReportStructure( "inspectionSummaryCG" );
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.Division );
		columns.add( getSerialNumber( usingSerialNumber ) );
		columns.add( ColumnDefinition.ProductType );
		columns.add( ColumnDefinition.PartNumber );
		columns.add( ColumnDefinition.Location );
		columns.add( ColumnDefinition.Description );
		columns.add( ColumnDefinition.Result );
		columns.add( ColumnDefinition.Comments );
		columns.add( ColumnDefinition.Empty );
		
		rstruct.setColumns( columns );
          
		return rstruct;
	}

	private static ReportStructure createInspectionSummaryReport( boolean usingSerialNumber ) {
		ReportStructure rstruct = new ReportStructure( "inspectionSummary" );
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.InspectionType );
		columns.add( ColumnDefinition.Division );
		columns.add( getSerialNumber( usingSerialNumber ) );
		columns.add( ColumnDefinition.ProductType );
		columns.add( ColumnDefinition.Description );
		columns.add( ColumnDefinition.Result );
		columns.add( ColumnDefinition.Empty );     
		
		rstruct.setColumns( columns );
          
		return rstruct;
	}

	

	private static ReportStructure createHercInspectionSummaryReport( boolean usingSerialNumber ) {
		ReportStructure rstruct = new ReportStructure( "inspectionSummary" );
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.InspectionType );
		columns.add( ColumnDefinition.Division );
		columns.add( getSerialNumber( usingSerialNumber ) );
		columns.add( ColumnDefinition.ProductType );
		columns.add( ColumnDefinition.Description );
		columns.add( ColumnDefinition.InspectionDate );
		columns.add( ColumnDefinition.Result );
		columns.add( ColumnDefinition.Empty );     
		
		rstruct.setColumns( columns );
          
		return rstruct;
	}
	
	private static ReportStructure createNISInspectionReport( boolean usingSerialNumber ) {
		ReportStructure rstruct = new ReportStructure( "nisInspectionSummary" );
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.Division );
		columns.add( ColumnDefinition.Location );
		columns.add( ColumnDefinition.ProductType );
		columns.add( ColumnDefinition.ProductStatus );
		columns.add( ColumnDefinition.Description );
		columns.add( getSerialNumber(usingSerialNumber) );
		columns.add( ColumnDefinition.InspectionType );
		columns.add( ColumnDefinition.Result );
		columns.add( ColumnDefinition.Comments );
		columns.add( ColumnDefinition.Empty );
		
		rstruct.setColumns( columns );
		
		return rstruct;
	}
	
	private static ReportStructure createNISInspectionReport2( boolean usingSerialNumber ) {
		ReportStructure rstruct = new ReportStructure( "nisInspectionSummary" );
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add( ColumnDefinition.Division );
		columns.add( ColumnDefinition.Location );
		columns.add( ColumnDefinition.ProductType );
		columns.add( ColumnDefinition.ProductStatus );
		columns.add( ColumnDefinition.Description );
		columns.add( getSerialNumber(usingSerialNumber) );
		columns.add( ColumnDefinition.InspectionDate );
		columns.add( ColumnDefinition.Result );
		columns.add( ColumnDefinition.Comments );
		columns.add( ColumnDefinition.Empty );
		
		rstruct.setColumns( columns );
		
		return rstruct;
	}
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
}

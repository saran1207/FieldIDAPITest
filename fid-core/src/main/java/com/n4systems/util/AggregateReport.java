package com.n4systems.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateReport {

	private List<AggregateReportRecord> inspectionTypeGroupsByProductTypes;
	
	private Map<String,List<AggregateReportRecord>> countsByProductType;
	
	private Map<String,List<AggregateReportRecord>> countsByInspectionTypeGroup;
	
	private List<AggregateReportRecord> distinctProductsByProductType;

	private Long totalInspections;
	
	private Long totalProducts;
	
	private List<String> productTypes;
	
	private List<String> inspectionTypeGroups;
	
	
	public List<AggregateReportRecord> getInspectionTypeGroupsByProductTypes() {
		return inspectionTypeGroupsByProductTypes;
	}

	public void setInspectionTypeGroupsByProductTypes(
			List<AggregateReportRecord> inspectionTypeGroupsByProductTypes ) {
		this.inspectionTypeGroupsByProductTypes = inspectionTypeGroupsByProductTypes;
		
		countsByProductType = new HashMap<String, List<AggregateReportRecord>>();
		countsByInspectionTypeGroup = new HashMap<String, List<AggregateReportRecord>>();
		for( AggregateReportRecord record : inspectionTypeGroupsByProductTypes ) {
			List<AggregateReportRecord> records = countsByProductType.get( record.getProductTypeName() );
			if( records == null ) {
				records = new ArrayList<AggregateReportRecord>();
			}
			records.add( record );
			countsByProductType.put( record.getProductTypeName(), records );
			
			
			records = countsByInspectionTypeGroup.get( record.getInspectionTypeGroupName() );
			if( records == null ) {
				records = new ArrayList<AggregateReportRecord>();
			}
			records.add( record );
			countsByInspectionTypeGroup.put( record.getInspectionTypeGroupName(), records );
		}
		productTypes = null;
		inspectionTypeGroups = null;
	}

	public List<AggregateReportRecord> getDistinctProductsByProductType() {
		return distinctProductsByProductType;
	}

	public void setDistinctProductsByProductType(
			List<AggregateReportRecord> distinctProductsByProductType ) {
		this.distinctProductsByProductType = distinctProductsByProductType;
	}

	
	public Long getDisinctProductsForProductType( String productTypeName ) {
		for( AggregateReportRecord record : distinctProductsByProductType ) {
			if( record.getProductTypeName().equals( productTypeName ) ) {
				return record.getCount();
			}
		}
		return null;
	}
	
	
	public Long getInspectionsForProductType( String productTypeName ) {
		Long count = 0L;
		for( AggregateReportRecord record : countsByProductType.get( productTypeName ) ) {
			count += record.getCount();
		}
		return count;
	}
	
	public Long getInspectionsForInspectionTypeGroup( String inspectionTypeGroupName ) {
		Long count = 0L;
		for( AggregateReportRecord record : countsByInspectionTypeGroup.get( inspectionTypeGroupName ) ) {
			count += record.getCount();
		}
		return count;
	}
	
	

	public Long getTotalInspections() {
		if( totalInspections == null ) {
			totalInspections = 0L;
			for( AggregateReportRecord record : inspectionTypeGroupsByProductTypes ) {
				totalInspections += record.getCount();
			}
		}
		return totalInspections;
	}

	public Long getTotalProducts() {
		if( totalProducts == null ) {
			totalProducts = 0L;
			for( AggregateReportRecord record : distinctProductsByProductType ) {
				totalProducts += record.getCount();
			}
		}
		return totalProducts;
	}

	public Map<String, List<AggregateReportRecord>> getCountsByProductType() {
		return countsByProductType;
	}

	public Map<String, List<AggregateReportRecord>> getCountsByInspectionTypeGroup() {
		return countsByInspectionTypeGroup;
	}

	public List<String> getProductTypes() {
		if( productTypes == null ) {
			productTypes = new ArrayList<String>( countsByProductType.keySet() );
			Collections.sort( productTypes );
		}
		return productTypes;
	}

	public List<String> getInspectionTypeGroups() {
		if( inspectionTypeGroups == null ) {
			inspectionTypeGroups = new ArrayList<String>( countsByInspectionTypeGroup.keySet() );
			Collections.sort( inspectionTypeGroups );
		}
		return inspectionTypeGroups;
	}
	
	
	
}

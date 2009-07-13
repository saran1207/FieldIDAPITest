package com.n4systems.fieldid.tools.reports;

public enum ColumnDefinition {
	Productname				(	"label.productname",			"productName"								), 
	EndUserName				(	"label.eusername",				"endUserName"								), 
	InspectionDate			(	"label.inspdate",				"dateOfInspection"							), 
	ProductStatus			(	"label.productstatus",			"productStatusName"							),
	InspectionType			(	"label.inspectiontype",			"readableEventType"							), 
	Division				(	"label.division",				"divisionName"								), 
	SerialNumber			(	"label.serialnumber",			"serialNumber"								), 
	ReelID					(	"label.reelid",					"reelId"									),
	RfidNumber				(	"label.rfidnumber",				"rfidNumber"								),
	Result					(	"label.result",					"inspectionResult"							), 
	Location				(	"label.location",				"location"									), 
	ProductType				(	"label.producttype",			"name"										), 
	Charge					(	"label.charge",					"chargeString"								), 
	Comments				(	"label.comments",				"comments"									),
	ProductLastInspectionDate(	"label.lastinspectiondate",		"lastInspectionDate"						),
	ScheduleLastInspectionDate(	"label.lastinspectiondate",		"lastInspectionDate",		false,	false	),
	NextInspectionDate		(	"label.nextinspectiondate",		"nextInspectionDate"						),
	OrderNumber				(	"label.onumber",				"ordernumber"								), 
	CustomerReferenceNumber	(	"label.yourreferencenumber",	"customerRefNumber"							),
	purchaseOrder			(	"label.purchaseorder",			"purchaseOrder"								),
	DateCreated				(	"label.datecreated",			"dateCreated"								),
	DateIdentified          (   "label.identified",             "identified"                                ),
	AssignedUser			(	"label.assignedto",				"assignedUser"								),
	Description				(	"label.desc",					"productSerialDescription",	false			),
	PartNumber				(	"label.partnumber",				"partNumber",				false			),
	DaysPastDue				( 	"label.daysPastDue",			"daysPastDue",				false		 	),
	EditScheduleLink		( 	"", 							"", 						false,	false 	),
	InspectionsLink			(	"",								"", 						false, 	false	),
	Empty					(	"", 							"",							false, 	false	);
	
	private String label;	
	private String nameField;
	private boolean sortable;
	private boolean printable;
	
	ColumnDefinition( String label, String nameField ) {
		this( label, nameField, true, true );
	}
	
	ColumnDefinition( String label, String nameField, boolean sortable ) {
		this( label, nameField, sortable, true);
	}
	
	ColumnDefinition( String label, String nameField, boolean sortable, boolean printable ) {
		this.label = label;
		this.nameField = nameField;
		this.sortable = sortable;
		this.printable = printable;
	}

	public String getLabel() {
		return label;
	}

	public boolean isSortable() {
		return sortable;
	}

	public String getNameField() {
		return nameField;
	}
	
	public boolean isPrintable() {
		return printable;
	}
}      


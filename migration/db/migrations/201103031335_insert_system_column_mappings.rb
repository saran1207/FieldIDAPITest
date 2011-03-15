require 'column_mapping_group'
require 'column_mapping'
require 'system_column_mapping'

class InsertSystemColumnMappings < ActiveRecord::Migration
  DATE_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler"
  EVENT_SERIAL_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.EventSerialNumberHandler"
  EVENT_RFID_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.EventRfidNumberHandler"
  ASSIGNED_TO_UPDATE_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.AssignedToUpdateHandler"
  ASSIGNED_TO_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.AssignedToUpdateHandler"
  ENUM_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.EnumHandler"
  SCHED_ASSET_LINK_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.EventScheduleAssetLinkHandler"
  LAST_EVT_DATE_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.LastEventDateHandler"
  ASSET_LINK_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.AssetLinkHandler"
  NETWORK_LAST_EVENT_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.NetworkLastEventDateHandler"
  PUBLISHED_ASSET_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.PublishedAssetHandler"
  NEXT_SCHEDULED_DATE_HANDLER = "com.n4systems.fieldid.viewhelpers.handlers.NextScheduledEventDateHandler"

  def self.up
    grp_event_event_information = create_group({:report_type=>"EVENT", :label=>"label.event_information", :order => 0, :group_key=> "event_details"})
    grp_event_identifiers = create_group({:report_type=>"EVENT", :label=>"label.identifiers", :order => 1, :group_key=> "identifiers"})
    grp_event_ownership = create_group({:report_type=>"EVENT", :label=>"label.ownership", :order => 2, :group_key=> "ownership"})
    grp_event_order_details = create_group({:report_type=>"EVENT", :label=>"label.orderdetails", :order => 3, :group_key=> "order_details"})
    grp_event_asset_details = create_group({:report_type=>"EVENT", :label=>"label.asset_details", :order => 4, :group_key=> "asset_details"})
    grp_event_prooftest_information = create_group({:report_type=>"EVENT", :label=>"label.prooftestinformation", :order => 5, :group_key=> "proof_test_details"})
    grp_event_date_identified = create_group({:report_type=>"EVENT", :label=>"label.identifieddate", :order => 10, :group_key=> "date_identified"})

    grp_schedule_schedule_information = create_group({:report_type=>"SCHEDULE", :label=>"label.scheduleinformation", :order => 0, :group_key=> "schedule_details"})
    grp_schedule_event_information = create_group({:report_type=>"SCHEDULE", :label=>"label.event_information", :order => 1, :group_key=> "event_details"})
    grp_schedule_identifiers = create_group({:report_type=>"SCHEDULE", :label=>"label.identifiers", :order => 2, :group_key=> "identifiers"})
    grp_schedule_ownership = create_group({:report_type=>"SCHEDULE", :label=>"label.ownership", :order => 3, :group_key=> "ownership"})
    grp_schedule_order_details = create_group({:report_type=>"SCHEDULE", :label=>"label.orderdetails", :order => 4, :group_key=> "order_details"})
    grp_schedule_asset_details = create_group({:report_type=>"SCHEDULE", :label=>"label.asset_details", :order => 5, :group_key=> "asset_details"})

    grp_asset_identifiers = create_group({:report_type=>"ASSET", :label=>"label.identifiers", :order => 0, :group_key=> "identifiers"})
    grp_asset_ownership = create_group({:report_type=>"ASSET", :label=>"label.ownership", :order => 1, :group_key=> "ownership"})
    grp_asset_order_details = create_group({:report_type=>"ASSET", :label=>"label.orderdetails", :order => 2, :group_key=> "order_details"})
    grp_asset_asset_details = create_group({:report_type=>"ASSET", :label=>"label.asset_details", :order => 3, :group_key=> "asset_details"})
    grp_asset_date_identified = create_group({:report_type=>"ASSET", :label=>"label.identifieddate", :order => 4, :group_key=> "date_identified"})

    grp_sched_job_event_information = create_group({:report_type=>"EVENT_TO_JOB", :label=>"label.eventinformation", :order => 1, :group_key=> "event_details"})
    grp_sched_job_asset_information = create_group({:report_type=>"EVENT_TO_JOB", :label=>"label.assetinformation", :order => 2, :group_key=> "asset_details"})


    defs = []
    defs << { :label => "label.date_performed", :path_expression => "date", :sortable => true, :output_handler => DATE_HANDLER, :default_order => 11, :group => grp_event_event_information, :name=>"event_search_date_performed"}
    defs << { :label => "label.eventtype", :path_expression => "type.name", :sortable => true, :default_order => 12, :group => grp_event_event_information, :name=>"event_search_eventtype" }
    defs << { :label => "label.result", :path_expression => "status.displayName", :sortable => true, :default_order => 13, :group => grp_event_event_information, :name=>"event_search_eventresult" }
    defs << { :label => "label.eventbook", :path_expression => "book.name", :sortable => true, :default_order => 14, :group => grp_event_event_information, :name=>"event_search_eventbook" }
    defs << { :label => "label.performed_by", :path_expression => "performedBy.displayName", :sortable => false, :default_order => 15, :group => grp_event_event_information, :name=>"event_search_performed_by" }
    defs << { :label => "label.comments", :path_expression => "comments", :sortable => true, :default_order => 16, :group => grp_event_event_information, :name=>"event_search_comments" }
    defs << { :label => "label.serialnumber", :path_expression => "asset", :sort_expression=> "asset.serialNumber", :sortable => true, :output_handler => EVENT_SERIAL_HANDLER, :default_order => 17, :group => grp_event_identifiers, :name=>"event_search_serialnumber" }
    defs << { :label => "label.rfidnumber", :path_expression => "asset", :sort_expression=> "asset.rfidNumber", :sortable => true, :output_handler => EVENT_RFID_HANDLER, :default_order => 18, :group => grp_event_identifiers, :name=>"event_search_rfidnumber" }
    defs << { :label => "label.referencenumber", :path_expression => "asset.customerRefNumber", :sortable => true, :default_order => 19, :group => grp_event_identifiers, :name=>"event_search_referencenumber" }
    defs << { :label => "label.assignedto", :path_expression => "assignedTo", :sortable => false, :output_handler => ASSIGNED_TO_UPDATE_HANDLER, :default_order => 20, :group => grp_event_ownership, :name=>"event_search_assignedto", :required_extended_feature => "AssignedTo" }
    defs << { :label => "label.eusername", :path_expression => "owner.customerOrg.name", :sortable => true, :default_order => 21, :group => grp_event_ownership, :name=>"event_search_customer" }
    defs << { :label => "label.division", :path_expression => "owner.divisionOrg.name", :sortable => true, :default_order => 22, :group => grp_event_ownership, :name=>"event_search_division" }
    defs << { :label => "label.location", :path_expression => "advancedLocation.fullName", :sort_expression => "advancedLocation.freeformLocation", :sortable => true, :default_order => 23, :group => grp_event_ownership, :name=>"event_search_location" }
    defs << { :label => "label.organization", :path_expression => "owner.internalOrg.name", :sort_expression => "owner.secondaryOrg.name", :sortable => true, :default_order => 24, :group => grp_event_ownership, :name=>"event_search_organization" }
    defs << { :label => "label.ordernumber", :path_expression => "asset.shopOrder.order.orderNumber", :sortable => true, :default_order => 25, :group => grp_event_order_details, :name=>"event_search_order_number" }
    defs << { :label => "label.orderdescription", :path_expression => "asset.shopOrder.description", :sortable => true, :default_order => 26, :group => grp_event_order_details, :name=>"event_search_order_description" }
    defs << { :label => "label.purchaseorder", :path_expression => "asset.purchaseOrder", :sortable => true, :default_order => 27, :group => grp_event_order_details, :name=>"event_search_purchaseorder" }
    defs << { :label => "label.asset_type_group", :path_expression => "asset.type.group.name", :sortable => true, :default_order => 28, :group => grp_event_asset_details, :name=>"event_search_assettypegroup" }
    defs << { :label => "label.assettype", :path_expression => "asset.type.name", :sortable => true, :default_order => 29, :group => grp_event_asset_details, :name=>"event_search_assettype" }
    defs << { :label => "label.assetstatus", :path_expression => "assetStatus.name", :sortable => true, :default_order => 30, :group => grp_event_asset_details, :name=>"event_search_assetstatus" }
    defs << { :label => "label.identifiedby", :path_expression => "asset.identifiedBy.displayName", :sortable => false, :default_order => 31, :group => grp_event_asset_details, :name=>"event_search_identifiedby" }
    defs << { :label => "label.description", :path_expression => "asset.description", :sortable => false, :default_order => 32, :group => grp_event_asset_details, :name=>"event_search_description" }
    defs << { :label => "label.peakload", :path_expression => "proofTestInfo.peakLoad", :sortable => true, :default_order => 34, :group => grp_event_prooftest_information, :name=>"event_search_peakload", :required_extended_feature => "ProofTestIntegration" }
    defs << { :label => "label.testduration", :path_expression => "proofTestInfo.duration", :sortable => true, :default_order => 35, :group => grp_event_prooftest_information, :name=>"event_search_testduration", :required_extended_feature => "ProofTestIntegration" }
    defs << { :label => "label.peakloadduration", :path_expression => "proofTestInfo.peakLoadDuration", :sortable => true, :default_order => 36, :group => grp_event_prooftest_information, :name=>"event_search_peakloadduration", :required_extended_feature => "ProofTestIntegration" }
    defs << { :label => "label.identifieddate", :path_expression => "asset.identified", :sortable => true, :default_order => 33, :group => grp_event_date_identified, :name=>"event_search_identified" }

    defs << { :label => "label.scheduleddate", :path_expression => "nextDate", :sortable => true, :default_order => 48, :group => grp_schedule_schedule_information, :name => "event_schedule_nextdate" }
    defs << { :label => "label.status", :path_expression => "status", :sortable => true, :output_handler => ENUM_HANDLER, :default_order => 51, :group => grp_schedule_schedule_information, :name => "event_schedule_status" }
    defs << { :label => "label.daysPastDue", :path_expression => "daysPastDue", :sortable => false, :default_order => 55, :group => grp_schedule_schedule_information, :name => "event_schedule_dayspastdue" }
    defs << { :label => "label.serialnumber", :path_expression => "asset.serialNumber", :sortable => true, :output_handler => SCHED_ASSET_LINK_HANDLER, :default_order => 20, :group => grp_schedule_identifiers, :name => "event_schedule_serialnumber" }
    defs << { :label => "label.rfidnumber", :path_expression => "asset.rfidNumber", :sortable => true, :output_handler => SCHED_ASSET_LINK_HANDLER, :default_order => 25, :group => grp_schedule_identifiers, :name => "event_schedule_rfidnumber" }
    defs << { :label => "label.referencenumber", :path_expression => "asset.customerRefNumber", :sortable => true, :default_order => 35, :group => grp_schedule_identifiers, :name => "event_schedule_referencenumber" }
    defs << { :label => "label.assignedto", :path_expression => "asset.assignedUser.displayName", :sortable => false, :output_handler => ASSIGNED_TO_HANDLER, :default_order => 89, :group => grp_schedule_ownership, :name => "event_schedule_assignedto", :required_extended_feature => "AssignedTo" }
    defs << { :label => "label.customername", :path_expression => "owner.customerOrg.name", :sortable => true, :default_order => 44, :group => grp_schedule_ownership, :name => "event_schedule_customer" }
    defs << { :label => "label.division", :path_expression => "owner.divisionOrg.name", :sortable => true, :default_order => 46, :group => grp_schedule_ownership, :name => "event_schedule_division" }
    defs << { :label => "label.location", :path_expression => "advancedLocation.fullName", :sort_expression=>"advancedLocation.freeformLocation", :sortable => true, :default_order => 46, :group => grp_schedule_ownership, :name => "event_schedule_location" }
    defs << { :label => "label.organization", :path_expression => "owner.internalOrg.name", :sort_expression=>"owner.secondaryOrg.name", :sortable => true, :default_order => 49, :group => grp_schedule_ownership, :name => "event_schedule_organization" }
    defs << { :label => "label.eventtype", :path_expression => "eventType.name", :sortable => true, :default_order => 40, :group => grp_schedule_event_information, :name => "event_schedule_eventtype" }
    defs << { :label => "label.lasteventdate", :path_expression => "id", :sortable => false, :output_handler => LAST_EVT_DATE_HANDLER, :default_order => 42, :group => grp_schedule_event_information, :name => "event_schedule_lastdate" }
    defs << { :label => "label.ordernumber", :path_expression => "asset.shopOrder.order.orderNumber", :sortable => true, :default_order => 100, :group => grp_schedule_order_details, :name => "event_schedule_order_number" }
    defs << { :label => "label.orderdescription", :path_expression => "asset.shopOrder.description", :sortable => true, :default_order => 10, :group => grp_schedule_order_details, :name => "event_schedule_order_description" }
    defs << { :label => "label.purchaseorder", :path_expression => "asset.purchaseOrder", :sortable => true, :default_order => 120, :group => grp_schedule_order_details, :name => "event_schedule_purchaseorder" }
    defs << { :label => "label.asset_type_group", :path_expression => "asset.type.group.name", :sortable => true, :default_order => 36, :group => grp_schedule_asset_details, :name => "event_schedule_assettypegroup" }
    defs << { :label => "label.assettype", :path_expression => "asset.type.name", :sortable => true, :default_order => 37, :group => grp_schedule_asset_details, :name => "event_schedule_assettype" }
    defs << { :label => "label.assetstatus", :path_expression => "asset.assetStatus.name", :sortable => true, :default_order => 70, :group => grp_schedule_asset_details, :name => "event_schedule_assetstatus" }
    defs << { :label => "label.identifiedby", :path_expression => "asset.identifiedBy.displayName", :sortable => false, :default_order => 92, :group => grp_schedule_asset_details, :name => "event_schedule_identifiedby" }
    defs << { :label => "label.description", :path_expression => "asset.description", :sortable => false, :default_order => 95, :group => grp_schedule_asset_details, :name => "event_schedule_description" }

    defs << { :label => "label.serialnumber", :path_expression => "serialNumber", :sortable => true, :output_handler => ASSET_LINK_HANDLER, :default_order => 1, :group => grp_asset_identifiers, :name => "asset_search_serialnumber" }
    defs << { :label => "label.rfidnumber", :path_expression => "rfidNumber", :sortable => true, :output_handler => ASSET_LINK_HANDLER, :default_order => 2, :group => grp_asset_identifiers, :name => "asset_search_rfidnumber" }
    defs << { :label => "label.referencenumber", :path_expression => "customerRefNumber", :sortable => true, :default_order => 3, :group => grp_asset_identifiers, :name => "asset_search_referencenumber" }
    defs << { :label => "label.assignedto", :path_expression => "assignedUser", :sortable => false, :output_handler => ASSIGNED_TO_HANDLER, :default_order => 4, :group => grp_asset_ownership, :name => "asset_search_assignedto", :required_extended_feature => "AssignedTo" }
    defs << { :label => "label.eusername", :path_expression => "owner.customerOrg.name", :sortable => true, :default_order => 5, :group => grp_asset_ownership, :name => "asset_search_customer" }
    defs << { :label => "label.division", :path_expression => "owner.divisionOrg.name", :sortable => true, :default_order => 6, :group => grp_asset_ownership, :name => "asset_search_division" }
    defs << { :label => "label.location", :path_expression => "advancedLocation.fullName", :sort_expression=> "advancedLocation.freeformLocation", :sortable => true, :default_order => 7, :group => grp_asset_ownership, :name => "asset_search_location" }
    defs << { :label => "label.organization", :path_expression => "owner.internalOrg.name", :sort_expression=> "owner.secondaryOrg.name", :sortable => true, :default_order => 8, :group => grp_asset_ownership, :name => "asset_search_organization" }
    defs << { :label => "label.orderdescription", :path_expression => "shopOrder.description", :sortable => true, :default_order => 9, :group => grp_asset_order_details, :name => "asset_search_order_description" }
    defs << { :label => "label.ordernumber", :path_expression => "shopOrder.order.orderNumber", :sortable => true, :default_order => 10, :group => grp_asset_order_details, :name => "asset_search_order_number" }
    defs << { :label => "label.purchaseorder", :path_expression => "purchaseOrder", :sortable => true, :default_order => 11, :group => grp_asset_order_details, :name => "asset_search_purchaseorder" }
    defs << { :label => "label.asset_type_group", :path_expression => "type.group.name", :sortable => true, :default_order => 12, :group => grp_asset_asset_details, :name => "asset_search_assettypegroup" }
    defs << { :label => "label.assettype", :path_expression => "type.name", :sortable => true, :default_order => 13, :group => grp_asset_asset_details, :name => "asset_search_assettype" }
    defs << { :label => "label.assetstatus", :path_expression => "assetStatus.name", :sortable => true, :default_order => 14, :group => grp_asset_asset_details, :name => "asset_search_assetstatus" }
    defs << { :label => "label.lasteventdate", :path_expression => "lastEventDate", :sortable => true, :output_handler => DATE_HANDLER, :default_order => 15, :group => grp_asset_asset_details, :name => "asset_search_lasteventdate" }
    defs << { :label => "label.networklasteventdate", :path_expression => "networkId", :sortable => false, :output_handler => NETWORK_LAST_EVENT_HANDLER, :default_order => 16, :group => grp_asset_asset_details, :name => "asset_search_network_lasteventdate" }
    defs << { :label => "label.identifiedby", :path_expression => "identifiedBy.displayName", :sortable => false, :default_order => 17, :group => grp_asset_asset_details, :name => "asset_search_identifiedby" }
    defs << { :label => "label.modifiedby", :path_expression => "modifiedBy.displayName", :sortable => false, :default_order => 18, :group => grp_asset_asset_details, :name => "asset_search_modifiedby" }
    defs << { :label => "label.comments", :path_expression => "comments", :sortable => true, :default_order => 19, :group => grp_asset_asset_details, :name => "asset_search_comments" }
    defs << { :label => "label.description", :path_expression => "description", :sortable => false, :default_order => 20, :group => grp_asset_asset_details, :name => "asset_search_description" }
    defs << { :label => "label.safetynetwork", :path_expression => "published", :sortable => true, :output_handler => PUBLISHED_ASSET_HANDLER, :default_order => 21, :group => grp_asset_asset_details, :name => "asset_search_published" }
    defs << { :label => "label.nextscheduleddate", :path_expression => "id", :sortable => false, :output_handler => NEXT_SCHEDULED_DATE_HANDLER, :default_order => 22, :group => grp_asset_asset_details, :name => "asset_next_scheduled_date" }
    defs << { :label => "label.identifieddate", :path_expression => "identified", :sortable => true, :default_order => 23, :group => grp_asset_date_identified, :name => "asset_search_identified" }

    defs << { :label => "label.serialnumber", :path_expression => "asset.serialNumber", :sortable => true, :output_handler => SCHED_ASSET_LINK_HANDLER, :default_order => 20, :group => grp_sched_job_asset_information, :name=>"schedule_to_job_serialnumber" }
    defs << { :label => "label.customername", :path_expression => "owner.customerOrg.name", :sortable => true, :default_order => 30, :group => grp_sched_job_event_information, :name=>"schedule_to_job_customername" }
    defs << { :label => "label.location", :path_expression => "advancedLocation.fullName", :sortable => true, :default_order => 33, :group => grp_sched_job_event_information, :name=>"schedule_to_job_location" }
    defs << { :label => "label.assettype", :path_expression => "asset.type.name", :sortable => true, :default_order => 37, :group => grp_sched_job_asset_information, :name=>"schedule_to_job_assettype" }
    defs << { :label => "label.eventtype", :path_expression => "eventType.name", :sortable => true, :default_order => 40, :group => grp_sched_job_event_information, :name=>"schedule_to_job_event_type" }
    defs << { :label => "label.status", :path_expression => "status", :sortable => true, :output_handler => ENUM_HANDLER, :default_order => 44, :group => grp_sched_job_event_information, :name=>"schedule_to_job_status" }

    defs.each do |d|
      create_column_mapping(d).save
    end

  end

  def self.down
    SystemColumnMapping.delete_all
    ColumnMapping.delete_all
  end

  def self.create_column_mapping(opts)
    mapping = SystemColumnMapping.new

    column_mapping = ColumnMapping.new
    column_mapping.created = Time.now
    column_mapping.modified = Time.now
    column_mapping.label = opts[:label]
    column_mapping.path_expression = opts[:path_expression]
    column_mapping.sort_expression = opts[:sort_expression]
    column_mapping.sortable = opts[:sortable]
    column_mapping.output_handler = opts[:output_handler]
    column_mapping.default_order = 1000+opts[:default_order]
    column_mapping.column_mapping_group = opts[:group]
    column_mapping.name = opts[:name]
    column_mapping.required_extended_feature = opts[:required_extended_feature]

    mapping.column_mapping = column_mapping
    return mapping
  end

  def self.create_group(opts)
    group = ColumnMappingGroup.new
    group.created = Time.now
    group.modified = Time.now
    group.label = opts[:label]
    group.ordervalue = opts[:order]
    group.group_key = opts[:group_key]
    group.report_type = opts[:report_type]
    #group.custom = opts[:custom] || false
    group.save
    return group
  end

end

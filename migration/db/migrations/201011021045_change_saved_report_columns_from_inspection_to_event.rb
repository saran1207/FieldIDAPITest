require "saved_report_column"

class ChangeSavedReportColumnsFromInspectionToEvent < ActiveRecord::Migration

  COLUMNS = %w{search_serialnumber search_inspectiontype search_customer search_inspectionresult search_assettype search_assetstatus search_unirope_charge search_order_description search_date_performed search_rfidnumber search_inspectionbook search_performed_by search_division search_organization search_referencenumber search_comments search_identified search_identifiedby search_description search_location search_order_number search_peakload search_testduration search_peakloadduration search_purchaseorder search_infooption_diam_inches search_infooption_average_break_strength_lbs search_size search_commonname search_length search_infooption_manufacturer search_infooption_type search_infooption_width search_infooption_length search_infooption_model search_infooption_capacity search_infooption_make_/_model search_infooption_drive_type search_partnumber search_infooption_feet_of_lift search_infooption_cpr_number search_infooption_working_load_limit search_itemid search_infooption_ply search_infooption_batch_number search_infooption_wire_finish search_infooption_lift search_infooption_adjuster_type search_infooption_size search_infooption_reach search_infooption_color-capacity search_infooption_asset_description search_assettypegroup search_infooption_grip_range search_infooption_diameter search_infooption_angle search_infooption_number_of_legs search_infooption_6_year_due search_infooption_hydrotest_due search_infooption_location/unit search_infooption_make search_infooption_wu-n2_hydrotest_due search_infooption_wu-n2_serial_number search_infooption_model_number search_infooption_serial_number}

  def self.up
    COLUMNS.each {|column| rename_report_column(column, :inspection, :event) }
    SavedReportColumn.update_all({ :element => "event_search_eventtype" },  { :element => "event_search_inspectiontype" })
    SavedReportColumn.update_all({ :element => "event_search_eventresult" },  { :element => "event_search_inspectionresult" })
    SavedReportColumn.update_all({ :element => "event_search_eventbook" },  { :element => "event_search_inspectionbook" })
  end

  def self.down
    SavedReportColumn.update_all({ :element => "event_search_inspectiontype" },  { :element => "event_search_eventtype" })
    SavedReportColumn.update_all({ :element => "event_search_inspectionresult" },  { :element => "event_search_eventresult" })
    SavedReportColumn.update_all({ :element => "event_search_inspectionbook" },  { :element => "event_search_eventbook" })
    COLUMNS.each {|column| rename_report_column(column, :event, :inspection) }
  end

  def self.rename_report_column(column, old_prefix, new_prefix)
    SavedReportColumn.update_all({ :element => "#{new_prefix}_#{column}" },  { :element => "#{old_prefix}_#{column}" })
  end

end
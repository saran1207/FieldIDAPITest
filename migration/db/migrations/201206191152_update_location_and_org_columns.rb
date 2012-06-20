require 'column_mapping'

class UpdateLocationAndOrgColumns < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all(
        { :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.completedordue.ReportingLocationHandler", :sort_expression => nil, :path_expression=>"", :join_expression=> nil },
          { :name => "event_search_location" } )

    ColumnMapping.update_all(
        { :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.completedordue.ReportingCustomerHandler", :sort_expression => nil, :path_expression=>"", :join_expression=> nil },
          { :name => "event_search_customer" } )

    ColumnMapping.update_all(
        { :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.completedordue.ReportingOrganizationHandler", :sort_expression => nil, :path_expression=>"", :join_expression=> nil },
          { :name => "event_search_organization" } )

    ColumnMapping.update_all(
        { :output_handler => "com.n4systems.fieldid.viewhelpers.handlers.completedordue.ReportingDivisionHandler", :sort_expression => nil, :path_expression=>"", :join_expression=> nil },
          { :name => "event_search_division" } )
  end

  def self.down
    ColumnMapping.update_all(
        { :output_handler => nil, :sort_expression => "advancedLocation.predefinedLocation.id,advancedLocation.freeformLocation", :path_expression=>"advancedLocation.fullName", :join_expression=> nil },
          { :name => "event_search_location" } )

    ColumnMapping.update_all(
        { :output_handler => nil, :sort_expression => nil, :path_expression=>"event.owner.customerOrg.name", :join_expression=> "event.owner.customerOrg" },
          { :name => "event_search_customer" } )

    ColumnMapping.update_all(
        { :output_handler => nil, :sort_expression => "event.owner.secondaryOrg.name", :path_expression=>"event.owner.secondaryOrg.name", :join_expression=>"event.owner.secondaryOrg" },
          { :name => "event_search_organization" } )

    ColumnMapping.update_all(
        { :output_handler => nil, :sort_expression => nil, :path_expression=>"event.owner.divisionOrg.name", :join_expression=>"event.owner.divisionOrg" },
          { :name => "event_search_division" } )
  end

end
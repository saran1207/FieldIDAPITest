require 'column_mapping_group'
require 'column_mapping'
require 'system_column_mapping'

class AddNonIntergrationOrderNumberToColumnMappings < ActiveRecord::Migration
  def self.up
  
  	columns = ColumnMapping.find_by_sql("select * from column_mappings where name like '%order_number' or path_expression like '%shopOrder.description'")
  	
  	columns.each do |col|
  	  col.required_extended_feature = "Integration"
  	  col.save
  	end
  
    add_column(:column_mappings, :excluded_by_extended_feature, :string)
    ColumnMapping.reset_column_information
    ColumnMapping.update_all("excluded_by_extended_feature = null");
 
  	grp_asset_order_details = ColumnMappingGroup.find(:first, :conditions => {:group_key => 'order_details', :report_type=> 'ASSET'})
  	grp_event_order_details = ColumnMappingGroup.find(:first, :conditions => {:group_key => 'order_details', :report_type=> 'EVENT'})
  	grp_schedule_order_details = ColumnMappingGroup.find(:first, :conditions => {:group_key => 'order_details', :report_type=> 'SCHEDULE'})
  	
  	defs = []
    defs << { :label => "label.ordernumber", :path_expression => "nonIntergrationOrderNumber", :sortable => true, :default_order => 10100, :group => grp_asset_order_details, :name => "asset_search_non_intergration_order_number", :excluded_by_extended_feature => "Integration" }
    defs << { :label => "label.ordernumber", :path_expression => "asset.nonIntergrationOrderNumber", :sortable => true, :default_order => 10250, :group => grp_event_order_details, :name=>"event_search_non_intergration_order_number", :excluded_by_extended_feature => "Integration" }
  	defs << { :label => "label.ordernumber", :path_expression => "asset.nonIntergrationOrderNumber", :sortable => true, :default_order => 11000, :group => grp_schedule_order_details, :name => "event_schedule_non_intergration_order_number", :excluded_by_extended_feature => "Integration" }
  	
  	defs.each do |d|
      create_column_mapping(d).save
    end
  
  end
  
  def self.down
  
  	mappings = ColumnMapping.find(:all, :conditions => { :excluded_by_extended_feature => "Integration" })
  	
  	mappings.each do |mapping|
  		sys_column = SystemColumnMapping.find(:first, :conditions => {:column_id => mapping.id})
  		SystemColumnMapping.delete(sys_column.id)
  		ColumnMapping.delete(mapping.id)
  	end
  	
  	remove_column(:column_mappings, :excluded_by_extended_feature)  	
  	
  	columns = ColumnMapping.find(:all, :conditions => { :required_extended_feature => "Integration" })
  	
  	columns.each do |col|
  		col.required_extended_feature = nil
  		col.save
  	end
  
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
    column_mapping.default_order = opts[:default_order]
    column_mapping.column_mapping_group = opts[:group]
    column_mapping.name = opts[:name]
    column_mapping.required_extended_feature = opts[:required_extended_feature]
	column_mapping.excluded_by_extended_feature = opts[:excluded_by_extended_feature]
	
    mapping.column_mapping = column_mapping
    return mapping
  end

end
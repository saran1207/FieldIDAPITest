require 'column_mapping'
require 'system_column_mapping'

class AddOrderDetailsToColumnMappings < ActiveRecord::Migration
  def self.up
  
  	po_columns = ColumnMapping.find_by_sql("select * from column_mappings where name like '%_purchaseorder'")
  	
  	po_columns.each do |col|
  	  col.required_extended_feature = "OrderDetails"
  	  col.save
  	end
  	
  	nio_columns = ColumnMapping.find_by_sql("select * from column_mappings where name like '%_non_intergration_order_number'")
  	
  	nio_columns.each do |col|
  	  col.required_extended_feature = "OrderDetails"
  	  col.save
  	end
  	
  end
  
  def self.down
    
  	po_columns = ColumnMapping.find_by_sql("select * from column_mappings where name like '%_purchaseorder'")
  	
  	po_columns.each do |col|
  	  col.required_extended_feature = nil
  	  col.save
  	end
  	
  	nio_columns = ColumnMapping.find_by_sql("select * from column_mappings where name like '%_non_intergration_order_number'")
  	
  	nio_columns.each do |col|
  	  col.required_extended_feature = nil
  	  col.save
  	end
  	
  end
end
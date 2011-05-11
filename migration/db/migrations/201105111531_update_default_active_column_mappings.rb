require 'active_column_mapping'
require 'column_mapping'
require 'column_layout'

class UpdateDefaultActiveColumnMappings < ActiveRecord::Migration

  def self.up  	
    execute "update active_column_mappings set mapping_id = 1  where id= 1"
    execute "update active_column_mappings set mapping_id = 7  where id= 2"
    execute "update active_column_mappings set mapping_id = 11 where id= 3"
    execute "update active_column_mappings set mapping_id = 13 where id= 5"
    execute "update active_column_mappings set mapping_id = 19 where id= 6"
    execute "update active_column_mappings set mapping_id = 20 where id= 7"
    
    events_layout = ColumnLayout.find(1)

	create_default_column(events_layout, "event_search_eventtype", 7)
	create_default_column(events_layout, "event_search_performed_by", 8)
	create_default_column(events_layout, "event_search_eventresult", 9)

    execute "update active_column_mappings set mapping_id = 48 where id= 8"
    execute "update active_column_mappings set mapping_id = 60 where id= 9"
    execute "update active_column_mappings set mapping_id = 61 where id= 10"
    execute "update active_column_mappings set mapping_id = 52 where id= 11"
    execute "update active_column_mappings set mapping_id = 54 where id= 12"
    execute "update active_column_mappings set mapping_id = 70 where id= 13"
    execute "update active_column_mappings set mapping_id = 62 where id= 14"
    execute "update active_column_mappings set mapping_id = 69 where id= 15"  
    
    execute "update active_column_mappings set mapping_id = 44 where id= 20"
    execute "update active_column_mappings set mapping_id = 34 where id= 21"
    execute "update active_column_mappings set mapping_id = 36 where id= 22"
    execute "update active_column_mappings set mapping_id = 38 where id= 23"
    execute "update active_column_mappings set mapping_id = 39 where id= 24"  
  end

  def self.down
    execute "update active_column_mappings set mapping_id = 2  where id= 1"
    execute "update active_column_mappings set mapping_id = 3  where id= 2"
    execute "update active_column_mappings set mapping_id = 7  where id= 3"
    execute "update active_column_mappings set mapping_id = 11 where id= 5"
    execute "update active_column_mappings set mapping_id = 19 where id= 6"
    execute "update active_column_mappings set mapping_id = 20 where id= 7"
  
    execute "update active_column_mappings set mapping_id = 48 where id= 8"
    execute "update active_column_mappings set mapping_id = 50 where id= 9"
    execute "update active_column_mappings set mapping_id = 52 where id= 10"
    execute "update active_column_mappings set mapping_id = 54 where id= 11"
    execute "update active_column_mappings set mapping_id = 60 where id= 12"
    execute "update active_column_mappings set mapping_id = 61 where id= 13"
    execute "update active_column_mappings set mapping_id = 62 where id= 14"
    execute "update active_column_mappings set mapping_id = 70 where id= 15"      

    execute "update active_column_mappings set mapping_id = 32 where id= 20"
    execute "update active_column_mappings set mapping_id = 34 where id= 21"
    execute "update active_column_mappings set mapping_id = 38 where id= 22"
    execute "update active_column_mappings set mapping_id = 39 where id= 23"
    execute "update active_column_mappings set mapping_id = 44 where id= 24"  
  end
  
  def self.create_default_column(layout, name, order)
  	column = find_column(name)
  
    active_mapping = ActiveColumnMapping.new
    active_mapping.created = Time.now
    active_mapping.modified = Time.now
    active_mapping.column_mapping = column
    active_mapping.ordervalue = order
    active_mapping.column_layout = layout
    active_mapping.save
  end  

  def self.find_column(name)
    ColumnMapping.find(:first, :conditions => {:name => name})
  end


end
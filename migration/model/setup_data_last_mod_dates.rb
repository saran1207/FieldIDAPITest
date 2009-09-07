require 'organization'
class SetupDataLastModDates < ActiveRecord::Base
  set_table_name :setupdatalastmoddates
  set_primary_key :tenant_id
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',       :class_name => 'Tenant'
end

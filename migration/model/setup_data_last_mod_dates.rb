require 'organization'
class SetupDataLastModDates < ActiveRecord::Base
  set_table_name :setupdatalastmoddates
  set_primary_key :r_tenant
  
  belongs_to  :tenant,      :foreign_key => 'r_tenant',       :class_name => 'Organization'
end
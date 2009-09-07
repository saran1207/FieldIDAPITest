require 'organization'

class ProductCodeMapping < ActiveRecord::Base
  set_table_name :productcodemapping
  set_primary_key :uniqueid
  
  belongs_to  :tenant,             :foreign_key => 'tenant_id',              :class_name => 'Tenant'
  
  def displayString
    "#{productcode} (#{id.to_s})"
  end
end

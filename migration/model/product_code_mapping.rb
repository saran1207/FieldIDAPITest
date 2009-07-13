require 'organization'

class ProductCodeMapping < ActiveRecord::Base
  set_table_name :productcodemapping
  set_primary_key :uniqueid
  
  belongs_to  :tenant,             :foreign_key => 'r_tenant',              :class_name => 'Organization'
  
  def displayString
    "#{productcode} (#{id.to_s})"
  end
end
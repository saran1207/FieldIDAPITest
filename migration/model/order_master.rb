require "tenant"

class OrderMaster < ActiveRecord::Base
  set_table_name :ordermaster
  set_primary_key :uniqueid
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',     :class_name => 'Tenant'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby',   :class_name => 'User'
  
  def isUsingItemName
    if itemName.size > 0
      return true
    else
      return false
    end
  end
  
  def itemName  
    item_name = ""
    
    if !itemgroup.nil? && itemgroup.strip.size > 0
      item_name = itemgroup.strip + " "
    end
    
    if !itemnumber.nil?
      item_name += itemnumber
    end
    
    return item_name.strip
  end
  
  def displayString
    "#{ordernumber} (#{uniqueid.to_s})"
  end
  
end

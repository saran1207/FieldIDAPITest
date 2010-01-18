require "tenant"
require "base_org"

class PrimaryOrg < ActiveRecord::Base
  set_table_name :org_primary
  set_primary_key :org_id
  
  belongs_to  :baseOrg, :foreign_key => 'org_id',     :class_name => 'BaseOrg'
  
  def parent
  	return nil
  end
  
  def isPrimary?
  	return true
  end
  
  def isSecondary?
  	return false
  end
  
  def isCustomer?
  	return false
  end
  
  def isDivision?
  	return false
  end
  
  def orgPath
	return "P:#{baseOrg.name}"
  end
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end
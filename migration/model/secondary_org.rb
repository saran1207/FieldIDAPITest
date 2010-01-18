require "base_org"
require "primary_org"

class SecondaryOrg < ActiveRecord::Base
  set_table_name :org_secondary
  set_primary_key :org_id
  
  belongs_to  :baseOrg,     :foreign_key => 'org_id',         :class_name => 'BaseOrg'
  belongs_to  :primaryOrg,  :foreign_key => 'primaryorg_id',  :class_name => 'PrimaryOrg'
  
  def parent
  	return primaryOrg
  end
  
  def isPrimary?
  	return false
  end
  
  def isSecondary?
  	return true
  end
  
  def isCustomer?
  	return false
  end
  
  def isDivision?
  	return false
  end
  
  def orgPath
	return "#{parent.orgPath} -> S:#{baseOrg.name}"
  end
  
  def displayString
    "#{baseOrg.name} (#{id.to_s})"
  end
end
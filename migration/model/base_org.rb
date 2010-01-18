require "tenant"
require "user"
require "addressinfo"

class BaseOrg < ActiveRecord::Base
  set_table_name :org_base

  named_scope :select_id,  :select => "id"
  named_scope :primaries_only,  :conditions => "secondary_id IS NULL AND customer_id IS NULL AND division_id IS NULL"
  named_scope :secondaries_only,:conditions => "secondary_id = id"
    
  named_scope :customers_only, lambda { |secondary_id| 
  	if (secondary_id.nil?)
  		{:conditions => "secondary_id IS NULL AND customer_id = id"}
  	else
  		{:conditions => ["secondary_id = ? AND customer_id = id", secondary_id]}
	end
  }
  
  named_scope :divisions_only, lambda { |customer_id| 
  	{:conditions => ["customer_id = ? AND division_id = id", customer_id]}
  }
  
  named_scope :for_tenant, lambda { |tenant_id| 
  	{:conditions => {:tenant_id => tenant_id}}
  }
  
  named_scope :named, lambda { |name|
  	{:conditions => {:name => name}}
  }
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',      :class_name => 'Tenant'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby',     :class_name => 'User'
  belongs_to  :addressInfo, :foreign_key => 'addressinfo_id', :class_name => 'Addressinfo'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end
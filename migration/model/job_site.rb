require "tenant"
require "user"
require "addressinfo"

class JobSite < ActiveRecord::Base
  set_table_name :jobsites
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',      :class_name => 'Tenant'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby',     :class_name => 'User'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
  
  def customerId
    return name.downcase.gsub(/\W/, '')
  end
end
require 'tenant'
require 'user'

class Configuration < ActiveRecord::Base
  set_table_name :configurations
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',   :class_name => 'Tenant'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby', :class_name => 'User'

  def displayString
    tenantString="global"
    if !tenantId.nil?
      tenantString = tenantId.to_s
    end
    
    "#{key} (" + tenantString + ") = #{value}"
  end
end
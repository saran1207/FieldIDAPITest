require "user"
require "tenant"
class FileAttachment < ActiveRecord::Base
	set_table_name :fileattachments
  
  belongs_to  :tenant,        :foreign_key => 'tenant_id',           :class_name => 'Tenant'
  belongs_to  :modifiedBy,    :foreign_key => 'modifiedby',         :class_name => 'User'

  
  def displayString
    "#{fileName} (#{id.to_s})"
  end
  
end

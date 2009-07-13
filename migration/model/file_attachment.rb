require "organization"
require "user"

class FileAttachment < ActiveRecord::Base
	set_table_name :fileattachments
  
  belongs_to  :tenant,        :foreign_key => 'r_tenant',           :class_name => 'Organization'
  belongs_to  :modifiedBy,    :foreign_key => 'modifiedby',         :class_name => 'User'

  
  def displayString
    "#{fileName} (#{id.to_s})"
  end
  
end
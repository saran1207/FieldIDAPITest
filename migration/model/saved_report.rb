require "user"
require "tenant"
require "saved_report_criteria"

class SavedReport < ActiveRecord::Base
  set_table_name :savedreports
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',        :class_name => 'Tenant'
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby',       :class_name => 'User'
  belongs_to  :user,        :foreign_key => 'user_id',          :class_name => 'User'
  has_many    :criteria,    :foreign_key => 'savedreports_id',  :class_name => 'SavedReportCriteria'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
  
end
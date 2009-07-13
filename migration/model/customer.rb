require "organization"
require "user"
require "addressinfo"

class Customer < ActiveRecord::Base
  set_table_name :customers
  
  belongs_to  :tenant,      :foreign_key => 'r_tenant',       :class_name => 'Organization'
  belongs_to  :modifiedby,  :foreign_key => 'modifiedby',     :class_name => 'User'
  belongs_to  :addressinfo, :foreign_key => 'addressinfo_id', :class_name => 'Addressinfo'
  has_many    :divisions,   :foreign_key => 'r_enduser',      :class_name => 'Division'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end
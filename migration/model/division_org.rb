require "base_org"
require "customer_org"

class DivisionOrg < ActiveRecord::Base
  set_table_name :org_division
  set_primary_key :org_id
  
  belongs_to  :baseOrg, :foreign_key => 'org_id',     :class_name => 'BaseOrg'
  belongs_to  :parent,  :foreign_key => 'parent_id',  :class_name => 'CustomerOrg'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end
require "base_org"
require "customer_org"
require "secondary_org"

class DivisionOrg < ActiveRecord::Base
  set_table_name :org_division
  set_primary_key :org_id
  
  belongs_to  :baseOrg, :foreign_key => 'org_id',     :class_name => 'BaseOrg'
  belongs_to  :parent,  :foreign_key => 'parent_id',  :class_name => 'BaseOrg'
  
  before_save :ensure_base_org_populated_properly
  
  def displayString
    "#{name} (#{id.to_s})"
  end
  
  protected
  def ensure_base_org_populated_properly
    baseOrg.secondary_id = parent.secondary_id
    baseOrg.customer_id = parent_id   
    baseOrg.division_id = org_id

    baseOrg.save
  end
  
end
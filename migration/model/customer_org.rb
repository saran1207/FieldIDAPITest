require "base_org"
require "secondary_org"
require "division_org"

class CustomerOrg < ActiveRecord::Base
  set_table_name :org_customer
  set_primary_key :org_id
  
  belongs_to  :baseOrg, :foreign_key => 'org_id',     :class_name => 'BaseOrg'
  belongs_to  :parent,  :foreign_key => 'parent_id',  :class_name => 'BaseOrg'
  has_many    :divisions, :foreign_key => 'parent_id', :class_name => 'DivisionOrg'
  
  before_save :ensure_base_org_populated_properly 
  after_save  :ensure_divisions_adjust_ownership
  
  def displayString
    "#{name} (#{id.to_s})"
  end
  
  protected
  def ensure_base_org_populated_properly
    baseOrg.secondary_id = parent.secondary_id
    baseOrg.customer_id = org_id   
    baseOrg.division_id = nil
    
    baseOrg.save
  end
  
  def ensure_divisions_adjust_ownership
    divisions.each do |d|
      d.save
    end
  end
end
require "customer"
require "division"
require "tenant"
require "base_org"
require "primary_org"
require "secondary_org"
require "customer_org"
require "division_org"

class MigrateOrgCustomerDivisions < ActiveRecord::Migration
	def self.up
    
    add_column(:org_customer, :legacy_id, :integer)
    add_column(:org_division, :legacy_id, :integer)
    
    now = Time.now
    
    Customer.find(:all).each do |customer|
      customerBase = BaseOrg.new
      customerBase.created = date_or_now(customer.created)
      customerBase.modified = date_or_now(customer.modified)
      customerBase.modifiedBy = customer.modifiedby
      customerBase.tenant = customer.tenant
      customerBase.name = customer.name
      customerBase.addressInfo = customer.addressinfo
      customerBase.save
      customerBase.customer_id = customerBase.id
      customerBase.save
      
      customerOrg = CustomerOrg.new
      customerOrg.legacy_id = customer.id
      customerOrg.baseOrg = customerBase
      customerOrg.parent = customer.tenant.primaryOrg
      customerOrg.code = customer.customerid
      customerOrg.contactname = customer.contactname
      customerOrg.contactemail = customer.contactemail
      customerOrg.save
      
      customer.divisions.each do |division|
        divisionBase = BaseOrg.new
        divisionBase.created = date_or_now(division.created)
        divisionBase.modified = date_or_now(division.modified)
        divisionBase.modifiedBy = division.modifiedBy
        divisionBase.tenant = division.tenant
        divisionBase.name = division.name
        divisionBase.addressInfo = division.addressinfo
        divisionBase.save
        divisionBase.customer_id = customerBase.id
        divisionBase.division_id = divisionBase.id
        divisionBase.save
        
        divisionOrg = DivisionOrg.new
        divisionOrg.legacy_id = division.id
        divisionOrg.baseOrg = divisionBase
        divisionOrg.parent = customerBase
        divisionOrg.code = division.divisionid
        divisionOrg.contactname = division.contactname
        divisionOrg.contactemail = division.contactemail
        divisionOrg.save
      end
    end
    
  end
	
	def self.down
  end

  def self.date_or_now(date)
    return date.nil? ? Time.now : date 
  end
end
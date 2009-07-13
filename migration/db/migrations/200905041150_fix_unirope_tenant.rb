require "organization"
require "inspectionmaster"
require "product"
require "user"
class FixUniropeTenant < ActiveRecord::Migration
  def self.up
    unirope_tenant = Organization.find(:first, :conditions => {:displayname => "Unirope Ltd. Montreal"})
    unirope_organization = Organization.find(:first, :conditions => {:displayname => "Unirope Ltd."})
    
    
    puts unirope_tenant.name
    puts unirope_organization.name
    
    users_for_montreal = User.find_by_sql(["select uniqueid from users where r_organization = ?", unirope_tenant.id])
    products_for_montreal = Product.find_by_sql(["select id from products where organization_id = ?", unirope_tenant.id])
    inspections_for_montreal = Inspection.find_by_sql(["select inspection_id from inspectionsmaster where organization_id = ?", unirope_tenant.id])
    
    users_for_main_location = User.find_by_sql(["select uniqueid from users where r_organization = ?", unirope_organization.id])
    products_for_main_location = Product.find_by_sql(["select id from products where organization_id = ?", unirope_organization.id])
    inspections_for_main_location = Inspection.find_by_sql(["select inspection_id from inspectionsmaster where organization_id = ?", unirope_organization.id])
    
    
    User.update_all("r_organization = " + unirope_organization.id.to_s, ["uniqueid IN(?) ", users_for_montreal])
    Product.update_all("organization_id = " + unirope_organization.id.to_s, ["id IN(?) ", products_for_montreal])
    Inspection.update_all("organization_id = " + unirope_organization.id.to_s, ["inspection_id IN(?) ", inspections_for_montreal])
    
    User.update_all("r_organization = " + unirope_tenant.id.to_s, ["uniqueid IN(?) ", users_for_main_location])
    Product.update_all("organization_id = " + unirope_tenant.id.to_s, ["id IN(?) ", products_for_main_location])
    Inspection.update_all("organization_id = " + unirope_tenant.id.to_s, ["inspection_id IN(?) ", inspections_for_main_location])
    
    
    rename_unirope_organizations(unirope_tenant, unirope_organization)
  end
  
   
  def self.down
  end

  private
    def self.rename_unirope_organizations(unirope_tenant, unirope_organization)
      montreal_address = unirope_tenant.r_addressinfo
      ontario_address = unirope_organization.r_addressinfo
      
      unirope_tenant.displayname = "Unirope Ltd."
      unirope_tenant.r_addressinfo = ontario_address
      
      unirope_organization.displayname = "Unirope Ltd. Montreal"
      unirope_organization.r_addressinfo = montreal_address
      
      
      unirope_organization.save
      unirope_tenant.save
      
    end
end
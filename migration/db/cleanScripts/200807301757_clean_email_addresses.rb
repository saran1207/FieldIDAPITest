require "product_type"
class CleanEmailAddresses < ActiveRecord::Migration 
  def self.up
    
    ProductType.transaction do 
      execute "update users set emailaddress = 'dev@n4systems.com'"
      execute "update org_division set contactemail = 'dev@n4systems.com'"
      execute "update org_customer set contactemail = 'dev@n4systems.com'"
      execute "update org_primary set externalusername = concat(externalusername, '_')"
    end

  end
  
  def self.down
    
  end

end
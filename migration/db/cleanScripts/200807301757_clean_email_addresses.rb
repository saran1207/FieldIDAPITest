require "product_type"
class CleanEmailAddresses < ActiveRecord::Migration 
  def self.up
    
    ProductType.transaction do 
      execute "update customers set contactemail = 'dev@n4systems.com'"
      execute "update users set emailaddress = 'dev@n4systems.com'"
      execute "update organization set adminemail = 'dev@n4systems.com'"
    end

  end
  
  def self.down
    
  end

end
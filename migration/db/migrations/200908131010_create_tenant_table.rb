require "tenant"

class CreateTenantTable < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
      create_table :tenants do |t|
        t.string :name, :null => false
      end
    end
    
  end
  
  def self.down
    
  end
  
end
require "tenant"

class RenameKeyColumns < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
      
      rename_column(:tagoptions, :key, :optionkey)
      rename_column(:findproductoption, :key, :optionkey)
      rename_column(:configurations, :key, :name)
      
    end

  end
  
  def self.down
  end
  
end
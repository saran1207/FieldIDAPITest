class SetContactNameNullWhereBlank < ActiveRecord::Migration
  def self.up
	execute "UPDATE divisions set contactname = null where contactname = ' '"
  end
  
  def self.down 
    
  end
end
class DropLegacyTablesAndColumns < ActiveRecord::Migration
  def self.up

    remove_column(:lineitems, :legacy_ordermaster)

    drop_table(:ordermaster)
    
  end
  
  def self.down
  end
end
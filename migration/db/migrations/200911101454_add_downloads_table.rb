class AddDownloadsTable < ActiveRecord::Migration
  def self.up
    create_table :downloads do |t| 
      create_entity_with_tenant_fields_on(t)
      t.string :name, :null => false
      t.string :contenttype, :limit => 32, :null => false
      t.string :state, :limit => 32, :null => false
      t.integer :user_id, :null => false
    end
    
    create_foreign_keys_for_entity_with_tenant(:downloads)
    add_foreign_key(:downloads, :users,  :source_column => :user_id, :foreign_column => :uniqueid, :name => "fk_downloads_owner")

  end
  
  def self.down
    drop_table(:downloads)
  end
end
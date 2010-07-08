class AddPredefinedLocationLevels < ActiveRecord::Migration
  def self.up
    create_table :predefined_location_levels do |t|
      create_entity_with_tenant_fields_on(t)
    end
    create_foreign_keys_for_entity_with_tenant(:predefined_location_levels)
    add_index(:predefined_location_levels, :tenant_id, :unique => true)
    
    create_table :predefined_location_levels_levels, :id => false, :primary_key => [:predefined_location_levels_id, :orderidx] do |t|
      t.string :name, :null => false
      t.integer :predefined_location_levels_id, :null => false
      t.integer :orderidx, :null => false
    end
    
    add_foreign_key(:predefined_location_levels_levels, :predefined_location_levels, :source_column => :predefined_location_levels_id, :foreign_column => :id)
  end
  
  def self.down
    drop_table :predefined_location_levels_levels
    drop_table :predefined_location_levels
  end
end
class AddPredefinedLocation < ActiveRecord::Migration 
  def self.up
    create_table :predefinedlocations do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :name, :null => false
      t.integer :parent_id
    end
    add_foreign_key(:predefinedlocations, :predefinedlocations,  :source_column => :parent_id, :foreign_column => :id)
  end
  
  def self.down
    drop_table :predefinedlocations
  end
end
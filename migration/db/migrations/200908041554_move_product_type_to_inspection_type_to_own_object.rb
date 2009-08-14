class MoveProductTypeToInspectionTypeToOwnObject < ActiveRecord::Migration
  def self.up
    create_table :associatedinspectiontypes do |t|
      create_abstract_entity_fields_on(t)
      t.integer :inspectiontype_id, :null => false
      t.integer :producttype_id, :null => false
    end
    add_foreign_key(:associatedinspectiontypes, :inspectiontypes,  :source_column => :inspectiontype_id, :foreign_key => :id)
    add_foreign_key(:associatedinspectiontypes, :producttypes,  :source_column => :producttype_id, :foreign_key => :id)
    create_foreign_keys_for_abstract_entity(:associatedinspectiontypes)
    add_index(:associatedinspectiontypes,[:producttype_id, :inspectiontype_id], :unique => true, :name => "idx_associatedinspectiontypes_unique")
  end
  
  def self.down
    drop_table :associatedinspectiontypes
  end
end
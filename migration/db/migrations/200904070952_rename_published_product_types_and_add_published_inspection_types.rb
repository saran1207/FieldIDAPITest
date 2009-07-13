class RenamePublishedProductTypesAndAddPublishedInspectionTypes < ActiveRecord::Migration
  def self.up
    create_table :catalogs_inspectiontypes, :id => false do |t|
      t.integer :catalogs_id, :null => false
      t.integer :publishedinspectiontypes_id, :null => false
    end
    add_index(:catalogs_inspectiontypes, [:catalogs_id, :publishedinspectiontypes_id], :unique => true)
    add_index(:catalogs_inspectiontypes, :publishedinspectiontypes_id, :unique => true)
    foreign_key(:catalogs_inspectiontypes, :catalogs_id, :catalogs, :id)
    foreign_key(:catalogs_inspectiontypes, :publishedinspectiontypes_id, :inspectiontypes, :id)

    rename_column(:catalogs_producttypes, :publishedtypes_id, :publishedproducttypes_id)
        
  end
  
  def self.down
    rename_column(:catalogs_producttypes, :publishedproducttypes_id, :publishedtypes_id)
    drop_table :catalogs_inspectiontypes
  end
end
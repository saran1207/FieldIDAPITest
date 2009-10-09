require "inspection_type"
require "associated_inspection_type"
class MoveTenantFromInspectionTypeToAit < ActiveRecord::Migration
  def self.up
    AssociatedInspectionType.find_in_batches() do |batch|
      batch.each do | ait |
        ait.r_tenant = ait.inspection_type.r_tenant
        ait.save
      end
    end
    add_foreign_key(:associatedinspectiontypes, :organization, :source_column => :r_tenant, :foreign_column => :id)
    change_column(:associatedinspectiontypes, :r_tenant, :integer, :null => false)
    AssociatedInspectionType.reset_column_information
  end
  
  def self.down
    drop_foreign_key(:associatedinspectiontypes, :organization, :source_column => :r_tenant, :foreign_column => :id)
    change_column(:associatedinspectiontypes, :r_tenant, :integer, :null => true)
  end
end
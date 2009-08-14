require "associated_inspection_type"
class AddTenantToAssociatedInspectionType < ActiveRecord::Migration
  def self.up
    add_column(:associatedinspectiontypes, :r_tenant, :integer)
    AssociatedInspectionType.reset_column_information
  end
  
  def self.down
    remove_column(:associatedinspectiontypes, :r_tenant)
    AssociatedInspectionType.reset_column_information
  end
end
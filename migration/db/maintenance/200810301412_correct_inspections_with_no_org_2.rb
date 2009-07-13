require "inspection"
require "user"
class CorrectInspectionsWithNoOrg2 < ActiveRecord::Migration
  def self.up
    Inspection.transaction do
      inspections = Inspection.find( :all, :conditions => " organization_id is null " )
      for inspection in inspections do
        inspection.organization = inspection.inspector.organization
        inspection.save
      end
    end
  end
  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end
require "inspection_schedule"
class AddLocationToInspectionSchedule < ActiveRecord::Migration
  def self.up
    add_column(:inspectionschedules, :location, :string)
    InspectionSchedule.reset_column_information
      
    execute 'update inspectionschedules set "location" = (select "location" from products where id = inspectionschedules.product_id) where status != \'COMPLETE\''
    execute 'update inspectionschedules set "location" = (select "location" from inspectionsmaster where inspection_id = inspectionschedules.inspection_inspection_id) where status = \'COMPLETE\''
  end
  
  def self.down
    remove_column(:inspectionschedules, :location)
  end
end
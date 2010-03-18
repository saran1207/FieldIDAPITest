require "inspectionmaster"
class AddNotNullToInspectionResult < ActiveRecord::Migration
  def self.up
    Inspection.transaction do 
      Inspection.find(:all, :conditions => {:status => nil}).each do |inspection|
        inspection.status = 'N/A'
        inspection.save
      end
      
      change_column(:inspectionsmaster, :status, :string, :null => false)
    end
  end
  
  def self.down
    change_column(:inspectionsmaster, :status, :string, :null => true)
  end
end
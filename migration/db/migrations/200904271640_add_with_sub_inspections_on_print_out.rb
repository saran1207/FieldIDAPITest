require "print_out"
class AddWithSubInspectionsOnPrintOut < ActiveRecord::Migration
  def self.up
    add_column(:printouts, :withsubinspections ,:boolean)
    PrintOut.update_all(:withsubinspections => false)
    change_column(:printouts, :withsubinspections, :boolean, :null => false)
  end
  
  def self.down
    remove_column(:printouts, :withsubinspections)
  end
end 
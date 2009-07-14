require "organization"
require "inspection_completor"
require "state_set"

class FillOutStandardsInspectionsToBeComplete < ActiveRecord::Migration
  def self.up
    standard = Organization.find(:first, :conditions => { :name => "standard" })
    cm_button_group = StateSet.find(:first, :conditions => { :name => "CM Default", :r_tenant => standard.id })
    skip_state = nil
    cm_button_group.states.each do |state|
      skip_state = state unless state.displaytext != "Skip"
    end
    
    
    inspection_completor = InspectionCompletor.new(standard, skip_state, cm_button_group)
    inspection_completor.process
  
  end
  
  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end
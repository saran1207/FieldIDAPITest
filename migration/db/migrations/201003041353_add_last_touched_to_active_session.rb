require "active_session"
class AddLastTouchedToActiveSession < ActiveRecord::Migration
  def self.up
    ActiveSession.delete_all
    add_column(:activesessions, :lasttouched, :datetime, :null=> false)
    ActiveSession.reset_column_information
  end
  
  def self.down
    remove_column(:activesessions, :lasttouched)
  end
end
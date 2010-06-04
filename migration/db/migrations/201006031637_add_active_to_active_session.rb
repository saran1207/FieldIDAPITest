require "active_session"

class AddActiveToActiveSession < ActiveRecord::Migration
  def self.up
     add_column(:activesessions, :active, :boolean, :null=> false)
     ActiveSession.reset_column_information
     ActiveSession.update_all("active = FALSE");

  end

  def self.down
    remove_column(:activesessions, :active)
  end
end
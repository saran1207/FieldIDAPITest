require "active_session"

class AddDateCreatedToActiveSessions < ActiveRecord::Migration
  def self.up
     add_column(:activesessions, :datecreated, :datetime, :null=> false)
     ActiveSession.reset_column_information
     ActiveSession.update_all("datecreated = '2010-06-01 00:00:00'");

  end

  def self.down
    remove_column(:activesessions, :datecreated)
  end
end
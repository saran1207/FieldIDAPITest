require "active_session"

class SetActiveSessionCreateTimeToLastTouchedTime < ActiveRecord::Migration
  def self.up
     
     ActiveSession.reset_column_information
     ActiveSession.update_all("datecreated = lasttouched");

  end

  def self.down
  end
end
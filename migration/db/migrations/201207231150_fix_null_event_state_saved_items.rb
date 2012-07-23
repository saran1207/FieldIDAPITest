require 'saved_report'

class FixNullEventStateSavedItems < ActiveRecord::Migration

  def self.up
    execute("update saved_reports set eventState = 'ALL' where eventState is null")
  end

  def self.down
    execute("update saved_reports set eventState = null where eventState = 'ALL'")
  end

end
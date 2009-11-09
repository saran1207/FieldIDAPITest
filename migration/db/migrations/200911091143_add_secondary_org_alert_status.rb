require "alert_status"
class AddSecondaryOrgAlertStatus < ActiveRecord::Migration
  def self.up
    add_column(:alertstatus, :secondaryorgs, :int)
    AlertStatus.reset_column_information
    AlertStatus.update_all("secondaryorgs = 0")
    change_column(:alertstatus, :secondaryorgs, :int, :null => false)
  end
end
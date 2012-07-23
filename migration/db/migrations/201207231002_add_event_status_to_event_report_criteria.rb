class AddEventStatusToEventReportCriteria < ActiveRecord::Migration

  def self.up
    add_column(:saved_reports, :eventStatus, :integer, :null=>true)
  end

  def self.down
    remove_column(:saved_reports, :eventStatus)
  end

end
class AddPersistenceOfIncompleteReportCriteriaToSavedItems < ActiveRecord::Migration

  def self.up
    add_column(:saved_reports, :dueDateRange, :string)
    add_column(:saved_reports, :dueFromDate, :datetime)
    add_column(:saved_reports, :dueToDate, :datetime)

    add_column(:saved_reports, :eventStatus, :string, :length => 30)
    add_column(:saved_reports, :includeDueDateRange, :string, :length => 30)
  end

  def self.down
    remove_column(:saved_reports, :dueDateRange)
    remove_column(:saved_reports, :dueFromDate)
    remove_column(:saved_reports, :dueToDate)

    remove_column(:saved_reports, :eventStatus)
    remove_column(:saved_reports, :includeDueDateRange)
  end
  
end
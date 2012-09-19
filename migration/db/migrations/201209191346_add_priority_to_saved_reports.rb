class AddPriorityToSavedReports < ActiveRecord::Migration

  def self.up
    execute("alter table saved_reports add column priority bigint")
  end

  def self.down
    remove_column(:saved_reports, :priority)
  end

end
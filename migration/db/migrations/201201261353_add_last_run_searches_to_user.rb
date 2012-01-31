class AddLastRunSearchesToUser < ActiveRecord::Migration

  def self.up
    add_column(:users, :displayLastRunSearches, :boolean)
    add_column(:users, :lastRunSearchId, :integer)
    add_column(:users, :lastRunReportId, :integer)

    execute "update users set displayLastRunSearches = true"
  end

  def self.down
    remove_column(:users, :displayLastRunSearches)
    remove_column(:users, :lastRunSearchId)
    remove_column(:users, :lastRunReportId)
  end

end
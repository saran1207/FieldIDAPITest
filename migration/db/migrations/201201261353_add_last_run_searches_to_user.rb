class AddLastRunSearchesToUser < ActiveRecord::Migration

  def self.up
    add_column(:users, :displayLastRunSearches, :boolean)
    add_column(:users, :lastRunSearchId, :integer)
    add_column(:users, :lastRunReportId, :integer)

    execute "update users set displayLastRunSearches = true"
  end

  def self.down
    drop_column(:users, :displayLastRunSearches)
    drop_column(:users, :lastRunSearchId)
    drop_column(:users, :lastRunReportId)
  end

end
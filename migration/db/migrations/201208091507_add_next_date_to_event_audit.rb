class AddNextDateToEventAudit < ActiveRecord::Migration
  def self.up
    add_column(:eventaudit, :nextDate, :timestamp, :null=>true)
  end

  def self.down
    remove_column(:eventaudit, :nextDate)
  end
end
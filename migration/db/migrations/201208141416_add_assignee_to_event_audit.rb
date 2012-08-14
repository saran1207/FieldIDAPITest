class AddAssigneeToEventAudit < ActiveRecord::Migration
  def self.up
    add_column(:eventaudit, :assignee, :string, :null=>true)
  end

  def self.down
    remove_column(:eventaudit, :assignee)
  end
end
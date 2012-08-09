class AddAssigneeToEvents < ActiveRecord::Migration

  def self.up
    add_column(:masterevents, :assignee_id, :integer)

    add_foreign_key(:masterevents, :users, :source_column => :assignee_id, :foreign_column => :id)
  end

  def self.down
    remove_column(:masterevents, :assignee_id)
  end

end
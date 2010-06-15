class AddAssignedToToEvent < ActiveRecord::Migration
  def self.up
    add_column(:inspectionsmaster, :assigneduser_id, :integer)
    add_column(:inspectionsmaster, :assignmentapplyed, :boolean)
    add_foreign_key(:inspectionsmaster, :users,  :source_column => :assigneduser_id, :foreign_column => :id, :name => "fk_assigned_user")
  end
  
  
  def self.down
    drop_foreign_key(:inspectionsmaster, :users,  :name => "fk_assigned_user")
    remove_column(:inspectionsmaster, :assigneduser_id)
    remove_column(:inspectionsmaster, :assignmentapplyed)
  end
end
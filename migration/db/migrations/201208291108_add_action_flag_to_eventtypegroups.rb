class AddActionFlagToEventtypegroups < ActiveRecord::Migration
  def self.up
    add_column(:eventtypegroups, :action, :boolean, :null=>false)
  end

  def self.down
    remove_column(:eventtypegroups, :action)
  end
end
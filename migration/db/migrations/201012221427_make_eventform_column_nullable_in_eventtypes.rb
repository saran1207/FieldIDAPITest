class MakeEventformColumnNullableInEventtypes < ActiveRecord::Migration

  def self.up
    change_column(:eventtypes, :eventform_id, :integer, :null => true)
  end

  def self.down
    change_column(:eventtypes, :eventform_id, :integer, :null => false)
  end

end
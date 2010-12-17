class AddEventFormToEvent < ActiveRecord::Migration

  def self.up
    add_column(:events, :eventform_id, :integer)

    execute("update events set eventform_id = type_id")

    change_column(:events, :eventform_id, :integer, :null => false)
    add_foreign_key(:events, :eventforms, :source_column => :eventform_id, :foreign_column => :id)
  end

  def self.down
    remove_column(:events, :eventform_id)
  end

end
class AddDescriptionToSavedItems < ActiveRecord::Migration
  
 def self.up
    add_column(:saved_items, :description, :string, :limit => 1000)
  end

  def self.down
    remove_column(:saved_items, :description);
  end

end
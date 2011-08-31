class ChangeObservationTextTo2048 < ActiveRecord::Migration

  def self.up
   change_column :observations, :text, :string, :limit => 2048
  end

  def self.down
     change_column :observations, :text, :string, :limit => 1000
  end

end
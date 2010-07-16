require "inspection_schedule"

class CreatePredefinedLocationSearchIds < ActiveRecord::Migration
  def self.up
    create_table :predefinedlocations_searchids, :id => false, :primary_key => [:predefinedlocation_id, :search_id] do |t|
      t.integer :predefinedlocation_id,	:null => false
      t.integer :search_id,				:null => false
    end
    execute("ALTER TABLE predefinedlocations_searchids ADD PRIMARY KEY (predefinedlocation_id, search_id)")
    add_foreign_key(:predefinedlocations_searchids, :predefinedlocations, :source_column => :predefinedlocation_id, :foreign_column => :id)
    add_index(:predefinedlocations_searchids, :search_id)
  end

  def self.down
    drop_table :predefinedlocations_searchids
  end
end
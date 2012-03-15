class CreateUniqueIndexOnScheduleId < ActiveRecord::Migration

  def self.up
    add_index "masterevents", ["schedule_id"], :name => "schedule_id_unique", :unique => true
  end

  def self.down
    remove_index "masterevents", :name => "schedule_id_unique"
  end

end
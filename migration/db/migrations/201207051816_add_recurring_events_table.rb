
class AddRecurringEventsTable < ActiveRecord::Migration

  def self.up

      create_table "recurring_asset_type_events", :primary_key => "id", :force => true do |t|
        t.integer   "tenant_id",        :limit => 20
        t.datetime  "created",          :null => false
        t.datetime  "modified",         :null => false
        t.integer   "modifiedby",       :limit => 20
        t.integer   "createdby",        :limit => 20
        t.integer   :event_type_id,     :null => false
        t.integer   :asset_type_id,     :null => true
        t.integer   :owner_id,          :limit => 20, :null => true
        t.string    :recurrence,        :null => false
      end

  end

  def self.down
      drop_table :recurring_asset_type_events
  end

end
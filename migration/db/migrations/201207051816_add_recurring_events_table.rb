
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

      add_foreign_key(:recurring_events, :assets, :source_column => :asset_id, :foreign_column => :id, :name => "fk_recurringevents_asset")
      add_foreign_key(:recurring_events, :eventtypes, :source_column => :event_type_id, :foreign_column => :id, :name => "fk_recurringevents_event_type")
      add_foreign_key(:recurring_events, :org_base, :source_column => :owner_id, :foreign_column => :id, :name => "fk_recurringevents_owner")

  end

  def self.down
      drop_table :recurring_asset_type_events
  end

end
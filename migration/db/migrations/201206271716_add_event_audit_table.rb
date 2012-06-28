
class AddEventAuditTable < ActiveRecord::Migration

  def self.up

      create_table "eventaudit", :primary_key => "id", :force => true do |t|
        t.timestamp     :modified,          :null => false
        t.integer       :modifiedby,        :null => false
        t.string        :tenant_id,         :null => false
        t.string        :owner,             :null => true
        t.string        :location,          :null => true
        t.string        :performed_by,      :null => true
        t.timestamp     :performed,         :null => true
        t.string        :event_book,        :null => true
        t.string        :result,            :null => true
        t.string        :comments,          :null => true
        t.string        :asset_status,      :null => true
        t.string        :event_status,      :null => true
        t.string        :printable,         :null => true
        t.string        :assigned_to,       :null => true
      end

     create_table "eventaudit_event", :primary_key => "id", :force => true do |t|
        t.integer :event_id, :null => false
        t.integer :eventaudit_id, :null => false
      end

      add_foreign_key(:eventaudit_event, :masterevents, :source_column => :event_id, :foreign_column => :event_id, :name => "fk_eventaudit_asset")
      add_foreign_key(:eventaudit_event, :eventaudit, :source_column => :audit_id, :foreign_column => :id, :name => "fk_eventaudit_audit")
  end

  def self.down
      drop_table :eventaudit
      drop_table :eventaudit_event
  end

end
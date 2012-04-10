class AddSendSavedItemSchedulesTables < ActiveRecord::Migration

  def self.up
    create_table "send_saved_item_schedules", :primary_key => "id" do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer "user_id", :null => false
      t.integer "saved_item_id", :null => false
      t.integer "hour_to_send", :null => false
      t.string  "frequency", :null => false
      t.string  "message", :limit => 1024
      t.boolean "send_to_owner", :null => false
    end

    create_table "send_saved_item_schedules_emails" do |t|
      t.integer "send_saved_item_schedule_id", :null =>false
      t.string  "email", :null => false
      t.integer "orderIdx", :null => false
    end

    add_foreign_key(:send_saved_item_schedules, :users, :source_column => :user_id, :foreign_column => :id)
    add_foreign_key(:send_saved_item_schedules, :saved_items, :source_column => :saved_item_id, :foreign_column => :id)
    add_foreign_key(:send_saved_item_schedules_emails, :send_saved_item_schedules, :source_column => :send_saved_item_schedule_id, :foreign_column => :id)
  end

  def self.down
    drop_table :send_saved_item_schedules_emails
    drop_table :send_saved_item_schedules
  end

end
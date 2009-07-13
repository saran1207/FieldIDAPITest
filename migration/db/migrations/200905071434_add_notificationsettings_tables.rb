class AddNotificationsettingsTables < ActiveRecord::Migration
  
  def self.up

    create_table "notificationsettings" do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer :end_length
      t.string  :end_unit
      t.string  :name,          :null => false
      t.integer :notifyDay,     :null => false
      t.boolean :notifyWeekly,  :null => false
      t.integer :start_length
      t.string  :start_unit
      t.integer :customer_id
      t.integer :division_id
    end
    
    create_foreign_keys_for_entity_with_tenant(:notificationsettings)
    
    foreign_key(:notificationsettings, :customer_id, :customers, :id)
    foreign_key(:notificationsettings, :division_id, :divisions, :id)

    create_table "notificationsettings_addresses", :id => false do |t|
      t.integer :notificationsettings_id, :null => false
      t.string :addr, :null => false
      t.integer :orderidx, :null => false
     end

     execute("ALTER TABLE notificationsettings_addresses ADD PRIMARY KEY (notificationsettings_id, orderidx)")
     
  end
  
  def self.down
    
    drop_table :notificationsettings_addresses
    drop_table :notificationsettings
    
  end
  
end
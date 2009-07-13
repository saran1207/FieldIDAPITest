class RecreateNotificationsettingsTables < ActiveRecord::Migration
  
  def self.up
    
    drop_table :notificationsettings_addresses
    drop_table :notificationsettings

    create_table "notificationsettings" do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :frequency, :null => false
      t.string :name, :null => false
      t.string :periodend, :null => false
      t.string :periodstart, :null => false
      t.boolean :usingjobsite, :null => false
      t.integer :user_id, :null => false
    end
    
    create_foreign_keys_for_entity_with_tenant(:notificationsettings)
    foreign_key(:notificationsettings, :user_id, :users, :uniqueid)
    
    create_table "notificationsettings_addresses", :id => false do |t|
      t.integer :notificationsettings_id, :null => false
      t.string :addr, :null => false
      t.integer :orderidx, :null => false
    end
    
    execute("ALTER TABLE notificationsettings_addresses ADD PRIMARY KEY (notificationsettings_id, orderidx)")
    foreign_key(:notificationsettings_addresses, :notificationsettings_id, :notificationsettings, :id)
    
    create_table "notificationsettings_owner" do |t|
      t.integer :notificationsettings_id, :null => false
      t.integer :customer_id
      t.integer :division_id
      t.integer :jobsite_id
    end
    
    foreign_key(:notificationsettings_owner, :notificationsettings_id, :notificationsettings, :id)
    foreign_key(:notificationsettings_owner, :jobsite_id, :jobsites, :id)
    foreign_key(:notificationsettings_owner, :division_id, :divisions, :id)
    foreign_key(:notificationsettings_owner, :customer_id, :customers, :id)
    
    execute("CREATE UNIQUE INDEX unique_notificationsettings_owner_customer_division ON notificationsettings_owner (notificationsettings_id, customer_id, division_id)")
    execute("CREATE UNIQUE INDEX unique_notificationsettings_owner_jobsite ON notificationsettings_owner (notificationsettings_id, jobsite_id)")
    
    create_table "notificationsettings_inspectiontypes", :id => false do |t|
      t.integer :notificationsettings_id, :null => false
      t.integer :inspectiontype_id, :null => false
      t.integer :orderidx, :null => false
    end
    
    execute("ALTER TABLE notificationsettings_inspectiontypes ADD PRIMARY KEY (notificationsettings_id, orderidx)")
    foreign_key(:notificationsettings_inspectiontypes, :notificationsettings_id, :notificationsettings, :id)
    
    create_table "notificationsettings_producttypes", :id => false do |t|
      t.integer :notificationsettings_id, :null => false
      t.integer :producttype_id, :null => false
      t.integer :orderidx, :null => false
    end
    
    execute("ALTER TABLE notificationsettings_producttypes ADD PRIMARY KEY (notificationsettings_id, orderidx)")
    foreign_key(:notificationsettings_producttypes, :notificationsettings_id, :notificationsettings, :id)
     
  end
  
  def self.down
    drop_table :notificationsettings_producttypes
    drop_table :notificationsettings_inspectiontypes
    drop_table :notificationsettings_owner
    drop_table :notificationsettings_addresses
    drop_table :notificationsettings
  end
  
end
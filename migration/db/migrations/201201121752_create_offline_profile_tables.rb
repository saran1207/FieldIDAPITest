class CreateOfflineProfileTables < ActiveRecord::Migration

	def self.up
	  create_table :offline_profiles, :primary_key => "id" do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer :user_id, :null => false
    end

    create_foreign_keys_for_entity_with_tenant(:offline_profiles)
    add_foreign_key(:offline_profiles, :users, :source_column => :createdby, :foreign_column => :id, :name => "fk_offline_profiles_createdby")
    add_foreign_key(:offline_profiles, :users, :source_column => :user_id, :foreign_column => :id, :name => "fk_offline_profiles_user")
    
    create_table :offline_profiles_assets, :id => false, :primary_key => ["offline_profiles_id", "assets"] do |t|
      t.integer :offline_profiles_id, :null => false
      t.string :assets, :null => false
    end
    add_foreign_key(:offline_profiles_assets, :offline_profiles, :source_column => :offline_profiles_id, :foreign_column => :id)
    
    create_table :offline_profiles_orgs, :id => false, :primary_key => ["offline_profiles_id", "organizations"] do |t|
      t.integer :offline_profiles_id, :null => false
      t.integer :organizations, :null => false
    end
    add_foreign_key(:offline_profiles_orgs, :offline_profiles, :source_column => :offline_profiles_id, :foreign_column => :id)
    
	end
	
	def self.down
	  drop_table :offline_profiles_orgs
	  drop_table :offline_profiles_assets
	  drop_table :offline_profiles
	end

end
class MakeSavedItemsTables < ActiveRecord::Migration

  def self.up
    create_table :saved_items do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :type, :limit =>2, :null => false
      t.string :name, :limit =>255, :null => false
      t.string :sharedbyname, :limit =>255
      t.integer :user_id, :null => false
      t.integer :orderIdx
      t.integer :report_id
    end

    create_table :users_saved_items, :id => false do |t|
      t.integer :user_id, :null => false
      t.integer :item_id, :null => false
      t.integer :orderIdx
    end

    add_foreign_key(:users_saved_items, :users, :source_column => :user_id, :foreign_column => :id)
    add_foreign_key(:users_saved_items, :saved_items, :source_column => :item_id, :foreign_column => :id)
    add_foreign_key(:saved_items, :saved_reports, :source_column => :report_id, :foreign_column => :id)
  end

  def self.down
    drop_table :users_saved_items
    drop_table :saved_items
  end


end
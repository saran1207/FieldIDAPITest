class AddSeenItTables < ActiveRecord::Migration
  def self.up
    create_table :seenitstorageitem do |t|
      create_abstract_entity_fields_on(t)
      t.integer :userid , :null => true
    end
    create_foreign_keys_for_abstract_entity(:seenitstorageitem)
    add_index(:seenitstorageitem, :userid, :unique => true)
    create_table :seenitstorageitem_itemsseen, :id => false do |t|
      t.integer :seenitstorageitem_id, :null => true
      t.string :element
    end
    
    add_foreign_key(:seenitstorageitem_itemsseen, :seenitstorageitem, :source_column => :seenitstorageitem_id, :foreign_column => :id)
    execute("ALTER TABLE seenitstorageitem_itemsseen ADD PRIMARY KEY (seenitstorageitem_id, element)")
    
  end
  
  def self.down
    remove_foreign_key(:seenitstorageitem_itemsseen, :seenitstorageitem, :source_column => :seenitstorageitem_id, :foreign_column => :id)
    drop_table :seenitstorageitem_itemsseen
    remove_index(:seenitstorageitem, :userid)
    drop_table :seenitstorageitem 
    
  end
end

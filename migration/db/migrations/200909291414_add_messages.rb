class AddMessages < ActiveRecord::Migration
  def self.up
    create_table :messages do |t|
      create_entity_with_owner_fields_on(t)
      t.string :sender, :null => false
      t.string :receiver, :null => false
      t.string :subject, :limit => 1000, :null => false
      t.text :body, :null => false
      t.boolean :unread, :null => false
    end
    
    create_foreign_keys_for_entity_with_owner(:messages)
    
  end
  
  def self.down
    drop_table(:messages)
  end
end
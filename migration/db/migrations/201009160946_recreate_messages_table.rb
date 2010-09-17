class RecreateMessagesTable < ActiveRecord::Migration
  def self.up
  
  	execute "ALTER TABLE messagecommands DROP FOREIGN KEY fk_messagecommands_users"
  	execute "ALTER TABLE messages DROP FOREIGN KEY fk_messages_messagecommands"
    drop_table :messagecommands_paramaters
    drop_table :messagecommands
    drop_table :messages
    
    create_table :messages do |t|
      create_entity_with_owner_fields_on(t)
      t.integer   :createdBy
      t.integer   :sender_id
      t.integer   :recipient_id
      t.column    :subject, 'varchar(1000)',  :null => false
      t.text      :body,                      :null => false
      t.boolean   :unread,                    :null => false
      t.boolean   :vendorConnection,          :null => false
      t.boolean   :processed,                 :null => false
    end  

	create_foreign_keys_for_entity_with_owner(:messages)
    add_foreign_key(:messages, :users,       :source_column => :createdBy,    :foreign_column => :id,     :name => "createdBy")
    add_foreign_key(:messages, :org_primary, :source_column => :sender_id,    :foreign_column => :org_id, :name => "sender_id")
    add_foreign_key(:messages, :org_primary, :source_column => :recipient_id, :foreign_column => :org_id, :name => "recipient_id")
  end
    
  def self.down
    drop_table :messages
  end
end
      
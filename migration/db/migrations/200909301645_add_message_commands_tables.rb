class AddMessageCommandsTables < ActiveRecord::Migration
  def self.up
    create_table :messagecommands do |t| 
      create_abstract_entity_fields_on(t)
      t.string :commandtype, :limit => 50, :null => false
    end
    
    create_foreign_keys_for_abstract_entity(:messagecommands)

    create_table :messagecommands_paramaters, :id => false do |t|
      t.integer :messagecommands_id, :null => false
      t.string :element
      t.string :mapkey
    end
    
    add_foreign_key(:messagecommands_paramaters, :messagecommands, :source_column => :messagecommands_id, :foreign_column => :id)
    execute("ALTER TABLE messagecommands_paramaters ADD PRIMARY KEY (messagecommands_id, mapkey)")
    
  end
  
  def self.down
    drop_table(:messagecommands_paramaters)
    drop_table(:messagecommands)
  end
end
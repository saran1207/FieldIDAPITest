class AddTasksTables < ActiveRecord::Migration
  
  def self.up

    create_table "tasks", :id => false do |t|
      t.string :id, :null => false
      t.timestamp :created, :null => false
      t.timestamp :modified, :null => false
      t.string :classname, :null => false
      t.string :cronexpression, :null => false
      t.string :taskgroup
     end
     
     execute("ALTER TABLE tasks ADD PRIMARY KEY (id)")
     
     create_table "tasks_property", :id => false do |t|
      t.string :tasks_id, :null => false
      t.string :element
      t.string :mapkey
     end
     
     execute("ALTER TABLE tasks_property ADD PRIMARY KEY (tasks_id, mapkey)")
     
     foreign_key(:tasks_property, :tasks_id, :tasks, :id )     
     
  end
  
  def self.down
    
    drop_table :tasks_property
    drop_table :tasks
    
  end
  
end
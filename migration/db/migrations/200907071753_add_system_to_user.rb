class AddSystemToUser < ActiveRecord::Migration
  def self.up
    add_column(:users, :system, :boolean)
    
    execute("update users set system = false where userid <> 'n4systems'")
    execute("update users set system = true where userid = 'n4systems'")
    execute("alter table users alter column system SET NOT NULL;")
    
  end
  
  def self.down
    remove_column(:users, :system)
  end
end
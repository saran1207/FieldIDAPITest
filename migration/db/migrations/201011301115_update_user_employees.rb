require "user"

class UpdateUserEmployees < ActiveRecord::Migration
  def self.up   
	execute "update users set employee = true where owner_id not in (select customer_id from org_base where customer_id is not null)"
  end
  
  def self.down
    User.update_all(:employee => false)
  end
end
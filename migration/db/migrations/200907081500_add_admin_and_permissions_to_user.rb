require "user"

INTEGER_MAX = (2**31) - 1

class AddAdminAndPermissionsToUser < ActiveRecord::Migration
  def self.up
    add_column(:users, :admin, :boolean)
    add_column(:users, :permissions, :integer)
    
    execute("update users set admin = false, permissions = 0")
    execute("alter table users alter column system SET NOT NULL;")
    execute("alter table users alter column permissions SET NOT NULL;")
    
    adminUsers = Array[ 310381 ]
    
    users = User.find(:all, :order => "r_tenant")
    
    users.each do |user|
      if adminUsers.include?(user.uniqueid)
        user.admin = true;
        user.permissions = INTEGER_MAX
      else
        if user.system
          user.permissions = INTEGER_MAX
        else
          user.permissionActions.each do |action|
            user.permissions |= action.bitMask
          end
        end
      end
      
      puts "USER: " + user.userid + " (" + user.uniqueid.to_s + "), Tenant: " + user.tenant.name + ", Perm: " + user.permissions.to_s
      user.save()
    end
    
    drop_table(:permissions)
    drop_table(:permissionaction)
    
  end
  
  def self.down
  end
end
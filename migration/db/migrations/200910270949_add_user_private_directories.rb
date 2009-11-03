require "user"

class AddUserPrivateDirectories < ActiveRecord::Migration
  def self.up
    
    User.find_each do |user|
    
      tenant_dir = "/var/fieldid/private/users/#{user.tenant.name}"
      user_dir = tenant_dir + "/#{user.uniqueid}/"
      
      Dir.mkdir(tenant_dir) unless File.exists? tenant_dir 
      Dir.mkdir(user_dir) unless File.exists? user_dir  
    
    end
  end
end
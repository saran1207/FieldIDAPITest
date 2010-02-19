require "tenant"
require "base_org"
require "permission_action"
require "permissions"

class User < ActiveRecord::Base
  set_table_name :users
  set_primary_key :uniqueid

  belongs_to  :tenant,              :foreign_key => 'tenant_id',       :class_name => 'Tenant'
  
  belongs_to :owner,                :foreign_key => 'owner_id',         :class_name => 'BaseOrg'
  
  

  def id
    uniqueid
  end

  def self.findByDisplayName(tenant, displayName)
    if displayName.nil?
      return nil
    end
    
    findName = displayName.downcase
    findName.gsub!(' ', '')
    
    user = User.find(:first, :conditions => ["tenant_id = :tenantId and (lower(trim(userid)) = :name or lower(trim(lastname)) || ',' || lower(trim(firstname)) = :name or lower(trim(firstname)) || lower(trim(lastname)) = :name)", {:tenantId => tenant.id, :name => findName}])
    
    if !user.nil?
      puts "Found User: " + user.displayString
    else
      puts "Could not locate User with display name: '" + displayName + "' for tenant: " + tenant.displayString
    end
    
    return user
  end

  def displayString
    "#{userid} (#{uniqueid.to_s}) - Tenant[#{tenant.name}]"
  end
end

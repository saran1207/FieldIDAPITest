require "user"
class SetupAdminUsersForAllAccounts < ActiveRecord::Migration
  def self.up
    mark_admin_user('sesposito', 15511504) # dresser
    
    mark_admin_user('tmeador', 15511549) # manchester sling company
     
    mark_admin_user('bpurcell', 15511545) # Marcal rope and rigging
    
    mark_admin_user('adam', 15511515) # bhp
    
    create_cmco_admin_user
  end
  
  
  def self.create_cmco_admin_user
    cmco_admin_user = User.new
    cmco_admin_user.userid = 'swingfield'
    cmco_admin_user.firstname = 'Stacie'
    cmco_admin_user.lastname = 'Wingfield'
    cmco_admin_user.emailaddress = 'Stacie.Wingfield@cmworks.com'
    cmco_admin_user.tenant_id = 15511516
    cmco_admin_user.owner_id = 15511516
    cmco_admin_user.permissions = 2147483647
    cmco_admin_user.admin = true
    cmco_admin_user.active = true
    cmco_admin_user.deleted = false
    cmco_admin_user.system = false
    cmco_admin_user.save
  end
  
  def self.mark_admin_user(username, tenant_id)
    dresser_admin_user = User.find(:first, {:conditions => {:userid => username, :tenant_id => tenant_id}})
    dresser_admin_user.admin = true
    dresser_admin_user.save
  end
  
end


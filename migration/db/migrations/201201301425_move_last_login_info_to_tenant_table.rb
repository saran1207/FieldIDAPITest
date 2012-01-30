require 'active_session'

class MoveLastLoginInfoToTenantTable < ActiveRecord::Migration

  def self.up
    add_column(:tenants, :last_login_user, :integer)
    add_column(:tenants, :last_login_time, :datetime)

    add_foreign_key(:tenants, :users, :source_column => :last_login_user, :foreign_column => :id)

    ActiveSession.find(:all).each do |session|
      tenant = session.user.tenant
      if session.user.usertype != "SYSTEM" and ( tenant.last_login_time.nil? or session.lasttouched > tenant.last_login_time )
        tenant.last_login_user = session.user.id
        tenant.last_login_time = session.lasttouched
        tenant.save
      end
    end

  end

  def self.down
    execute "alter table tenants drop foreign key fk_tenants_users"
    remove_column(:tenants, :last_login_user)
    remove_column(:tenants, :last_login_time)
  end

end
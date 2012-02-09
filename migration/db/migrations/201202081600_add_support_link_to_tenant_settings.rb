require 'active_session'

class AddSupportLinkToTenantSettings < ActiveRecord::Migration

  def self.up
    add_column(:tenant_settings, :supportUrl, :string)
  end

  def self.down
    remove_column(:tenant_settings, :supportUrl)
  end

end
require 'tenant'

class AddDisabledFlagToTenant < ActiveRecord::Migration

  def self.up
    add_column(:tenants, :disabled,:boolean, :default => false)
  end

  def self.down
    remove_colum(:tenants, :disabled)
  end

end
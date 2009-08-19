require "tenant"
require "organization"

class AddMoreOrgFields < ActiveRecord::Migration
  
  def self.up
    Tenant.transaction do
      
      add_column(:org_secondary, :certificatename, :string)
      add_column(:org_secondary, :defaulttimezone, :string)
      
      add_column(:org_primary, :certificatename, :string)
      add_column(:org_primary, :defaulttimezone, :string)
      add_column(:org_primary, :dateformat, :string)
      
    end

  end
  
  def self.down
  end
  
end
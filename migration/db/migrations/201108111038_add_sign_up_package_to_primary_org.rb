require 'primary_org'


class AddSignUpPackageToPrimaryOrg < ActiveRecord::Migration

  def self.up
    add_column :org_primary, :signuppackage, :string, :limit => 255, :null => false
    PrimaryOrg.reset_column_information
    PrimaryOrg.update_all(:signuppackage => 'Legacy')
  end

  def self.down
    remove_column :org_primary, :signuppackage
  end

end
class AddAutoAcceptToPrimaryOrg < ActiveRecord::Migration
  def self.up
  	add_column(:org_primary, :autoaccept, :boolean)
  	execute("UPDATE org_primary set autoaccept = false")
  	execute("ALTER TABLE org_primary MODIFY autoaccept tinyint(1) NOT NULL")
  end
  
  def self.down
  	remove_column(:org_primary, :autoaccept)
  end
end
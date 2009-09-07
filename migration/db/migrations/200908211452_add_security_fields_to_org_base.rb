class AddSecurityFieldsToOrgBase < ActiveRecord::Migration
	def self.up
    
    add_column(:org_base, :secondary_id, :integer)
    add_column(:org_base, :customer_id, :integer)
    add_column(:org_base, :division_id, :integer)
    
    add_index(:org_base, :secondary_id)
    add_index(:org_base, :customer_id)
    add_index(:org_base, :division_id)

  end
	
	def self.down
	end
end
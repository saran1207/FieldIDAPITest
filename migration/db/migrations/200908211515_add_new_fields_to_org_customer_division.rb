class AddNewFieldsToOrgCustomerDivision < ActiveRecord::Migration
	def self.up
    add_column(:org_customer, :code, :string) 
    add_column(:org_customer, :contactemail, :string)
    add_column(:org_customer, :contactname, :string)

    add_column(:org_division, :code, :string) 
    add_column(:org_division, :contactemail, :string)
    add_column(:org_division, :contactname, :string)
    
    CustomerOrg.reset_column_information
    DivisionOrg.reset_column_information
  end
	
	def self.down
	end
end
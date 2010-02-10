require "java"
require "base_org"

class GenerateExternalIds < ActiveRecord::Migration
  def self.up
    BaseOrg.transaction do
		BaseOrg.find_each do |org|
			org.external_id = java.util.UUID.randomUUID().toString()
			org.save
		end

  		change_column(:org_base, :external_id, :string, {:limit => 36, :null => false})
  	end
    BaseOrg.reset_column_information
  end
  
  def self.down
  end
  
end
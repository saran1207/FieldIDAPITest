class ChangeNetsuiteIdToExternalIdInContractPricings < ActiveRecord::Migration

	def self.up
		remove_column :contractpricings, :netsuiterecordid
		add_column :contractpricings, :externalid, :integer
	end
	
	def self.down
		remove_column :contractpricings, :externalid
		add_column :contractpricings, :netsuiterecordid, :integer
	end
	
end
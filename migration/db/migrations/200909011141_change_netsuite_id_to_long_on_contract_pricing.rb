class ChangeNetsuiteIdToLongOnContractPricing < ActiveRecord::Migration
	
	def self.up
		remove_column :contractpricings, :netsuiterecordid
		add_column :contractpricings, :netsuiterecordid, :integer
	end
	
	def self.down
		remove_column :contractpricings, :netsuiterecordid
		add_column :contractpricings, :netsuiterecordid, :string
	end
end
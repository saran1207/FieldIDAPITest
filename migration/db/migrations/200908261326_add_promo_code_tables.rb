class AddPromoCodeTables < ActiveRecord::Migration
	def self.up
		create_table "promocodes" do |t|
			create_abstract_entity_fields_on(t)
			t.string :code, :null => false
		end		
		
		create_table "promocode_extendedfeatures" do |t|
			t.integer :promocode_id, :null => false
			t.string :feature, :null => false
		end
		
		add_index(:promocodes, :code, :unique => true)
		
		add_foreign_key(:promocode_extendedfeatures, :promocodes,  :source_column => :promocode_id, :foreign_key => :id)		
	end
	
	def self.down
		drop_foreign_key(:promocode_extendedfeatures, :promocodes)
		
		remove_index(:promocodes, :code)
		
		drop_table :promocode_extendedfeatures
		drop_table :promocodes		
	end
end
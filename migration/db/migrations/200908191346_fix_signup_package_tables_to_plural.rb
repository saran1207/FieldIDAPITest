class FixSignupPackageTablesToPlural < ActiveRecord::Migration
	def self.up
		drop_table :signuppackage
		drop_table :signuppackage_extendedfeatures
		drop_table :contractpricing
	
		create_table "signuppackages" do |t|
			create_abstract_entity_fields_on(t)
			t.string  :syncid
			t.string  :name
			t.integer :diskspace_limit
			t.integer :user_limit	
			t.integer :asset_limit		
		end
		
		create_table "signuppackages_extendedfeatures", :id => false do |t|
			t.integer  :signuppackage_id
			t.string   :element
		end
		
    	add_foreign_key(:signuppackages_extendedfeatures, :signuppackages,  :source_column => :signuppackage_id, :foreign_key => :id)
		
		create_table "contractpricings" do |t|
			create_abstract_entity_fields_on(t)
			t.string  :netsuiterecordid
			t.string  :contractlength
			t.string  :price
			t.integer :signuppackage_id
		end
		
    	add_foreign_key(:contractpricings, :signuppackages,  :source_column => :signuppackage_id, :foreign_key => :id)
	end
	
	def self.down
		drop_table :signuppackages
		drop_table :signuppackages_extendedfeatures
		drop_table :contractpricings
	
		create_table "signuppackage" do |t|
			create_abstract_entity_fields_on(t)
			t.string  :syncid
			t.string  :name
			t.integer :diskspace_limit
			t.integer :user_limit		
			t.integer :asset_limit	
		end
		
		create_table "signuppackage_extendedfeatures", :id => false do |t|
			t.integer  :signuppackage_id
			t.string   :element
		end
		
    	add_foreign_key(:signuppackage_extendedfeatures, :signuppackage,  :source_column => :signuppackage_id, :foreign_key => :id)
		
		create_table "contractpricing" do |t|
			create_abstract_entity_fields_on(t)
			t.string  :netsuiterecordid
			t.string  :contractlength
			t.string  :price
			t.integer :signuppackage_id
		end
		
    	add_foreign_key(:contractpricing, :signuppackage,  :source_column => :signuppackage_id, :foreign_key => :id)
	end
end
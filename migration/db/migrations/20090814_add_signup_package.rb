class AddSignupPackage < ActiveRecord::Migration
	def self.up
		create_table "signuppackage" do |t|
			create_abstract_entity_fields_on(t)
			t.string  :syncid
			t.string  :name
			t.integer :diskspace_limit
			t.integer :user_limit			
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
	
	def self.down
		drop_table :signuppackage
		drop_table :signuppackage_extendedfeatures
		drop_table :contractpricing
	end
end

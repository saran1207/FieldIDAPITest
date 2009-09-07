class RemoveSignupPackageTable < ActiveRecord::Migration
	def self.up
#		execute("delete from signuppackages")
#		execute("delete from contractpricings;")

#    	drop_foreign_key(:contractpricings, :signuppackages)
#		add_column :contractpricings, :syncid, :string
#		remove_column :contractpricings, :signuppackage_id

    drop_foreign_key(:contractpricings, :signuppackages,  :source_column => :signuppackage_id, :foreign_key => :id)
    drop_table :signuppackages_extendedfeatures
		
        drop_table :signuppackages		
	end
	
	def self.down
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

		add_column :contractpricings, :signuppackage_id, :integer
		remove_column :contractpricings, :syncid
		
    	add_foreign_key(:contractpricings, :signuppackages,  :source_column => :signuppackage_id, :foreign_key => :id)
		
	end
end

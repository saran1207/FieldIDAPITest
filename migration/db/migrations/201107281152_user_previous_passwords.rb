class UserPreviousPasswords < ActiveRecord::Migration

  def self.up  
  	create_table :user_previous_pw, :primary_key => "id" do |t| 
  		t.string :password, :null => false
  		t.integer :userId, :null => false
  	end
    add_foreign_key(:user_previous_pw, :users, :source_column => :userId, :foreign_column => :id)  	
  end
  
  def self.down    
  	drop_table :user_previous_pw
  end
  
end
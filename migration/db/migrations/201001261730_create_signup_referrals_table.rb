class CreateSignupReferralsTable < ActiveRecord::Migration
  def self.up
  	
	create_table :signupreferrals do |t|
		t.timestamp :signupdate,		:null => false
    	t.integer :referral_tenant_id,	:null => false
    	t.integer :referred_tenant_id,	:null => false
    	t.integer :referral_user_id,	:null => false
    end

	add_index(:signupreferrals, :referred_tenant_id, :unique => true)
	add_foreign_key(:signupreferrals, :tenants, :source_column => :referral_tenant_id,	:foreign_column => :id, 		:name => "signupreferrals_referral_tenant")
	add_foreign_key(:signupreferrals, :tenants, :source_column => :referred_tenant_id,	:foreign_column => :id, 		:name => "signupreferrals_referred_tenant")
	add_foreign_key(:signupreferrals, :users, 	:source_column => :referral_user_id, 	:foreign_column => :uniqueid, 	:name => "signupreferrals_referral_user")
  end
  
  def self.down
   	drop_table(:signupreferrals)
  end
end
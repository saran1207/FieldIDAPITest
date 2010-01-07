require "promo_code"
class AddSecondaryOrgsToPromocode < ActiveRecord::Migration
  def self.up
    add_column(:promocodes, :secondary_org_limit, :integer)
    PromoCode.reset_column_information
    
    PromoCode.update_all(:secondary_org_limit => 0)
    
    change_column(:promocodes, :secondary_org_limit, :integer, :null => false)
  end
end
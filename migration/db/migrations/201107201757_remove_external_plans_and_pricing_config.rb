class RemoveExternalPlansAndPricingConfig < ActiveRecord::Migration

  def self.up
  	execute "delete from configurations where identifier = 'EXTERNAL_PLANS_AND_PRICING_ENABLED'"
  end

  def self.down
  end

end

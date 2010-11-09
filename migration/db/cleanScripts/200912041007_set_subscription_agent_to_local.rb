require "configuration"
class SetSubscriptionAgentToLocal < ActiveRecord::Migration
  def self.up
    Configuration.delete_all(:identifier => 'SUBSCRIPTION_AGENT')
    Configuration.delete_all(:identifier => 'EXTERNAL_PLANS_AND_PRICING_ENABLED')
    Configuration.create(:identifier => 'SUBSCRIPTION_AGENT', :value => 'com.n4systems.subscription.local.LocalSubscriptionAgent', :created => Time.now, :modified => Time.now)
    Configuration.create(:identifier => 'EXTERNAL_PLANS_AND_PRICING_ENABLED', :value => 'false', :created => Time.now, :modified => Time.now)
  end
end
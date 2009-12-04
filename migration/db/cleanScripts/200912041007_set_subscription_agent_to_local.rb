require "configuration"
class SetSubscriptionAgentToLocal < ActiveRecord::Migration
  def self.up
    Configuration.delete_all(:identifier => 'SUBSCRIPTION_AGENT')
    Configuration.create(:identifier => 'SUBSCRIPTION_AGENT', :value => 'com.n4systems.subscription.local.LocalSubscriptionAgent', :created => Time.now, :modified => Time.now)
  end
end
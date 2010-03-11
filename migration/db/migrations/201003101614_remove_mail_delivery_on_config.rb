require "configuration"
class RemoveMailDeliveryOnConfig < ActiveRecord::Migration
  def self.up
    Configuration.delete_all(:identifier => 'MAIL_DELIVERY_ON')
  end
end
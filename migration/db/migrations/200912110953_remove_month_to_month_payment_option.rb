require "contract_pricing"
class RemoveMonthToMonthPaymentOption < ActiveRecord::Migration
  def self.up
    ContractPricing.delete_all(:paymentoption => "MONTH_TO_MONTH")
  end
  
end
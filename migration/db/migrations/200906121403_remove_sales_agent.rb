require "order"
class RemoveSalesAgent < ActiveRecord::Migration
  def self.up
    remove_column :orders, :salesagent
    Order.reset_column_information
    execute "DELETE from ordermapping where orderkey = 'ORDER_SALES_AGENT'"
  end
  
  def self.down
    add_column :orders, :salesagent, :string
    Order.reset_column_information
  end
end 
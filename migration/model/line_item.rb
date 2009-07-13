require "user"
require "order"

class LineItem < ActiveRecord::Base
  set_table_name :lineitems
  
  belongs_to  :modifiedBy,  :foreign_key => 'modifiedby', :class_name => 'User'
  belongs_to  :order,       :foreign_key => 'order_id',   :class_name => 'Order'
  
  def self.createFromOrderMaster(order, orderMaster)
    
    quantity = 0
    if !orderMaster.quantity.nil?
      quantity = orderMaster.quantity
    end
    
    productCode = ""
    if orderMaster.isUsingItemName
      productCode = orderMaster.itemName
    else
      productCode = orderMaster.productcode
    end
    
    # nothing really we can do about these ...
    if productCode.nil?
      return nil
    end
    
    poNumber = nil
    if !orderMaster.ponumber.nil? && orderMaster.ponumber.strip.size > 0
      poNumber = orderMaster.ponumber.strip
    end
    
    lineId = nil
    if !orderMaster.externallineitemuniqueid.nil? && orderMaster.externallineitemuniqueid.strip.size > 0
      lineId = orderMaster.externallineitemuniqueid.strip
    end
    
    desc = nil
    if !orderMaster.productname.nil? && orderMaster.productname.strip.size > 0 && orderMaster.productname.size < 512
      desc = orderMaster.productname.strip
    end
    index = order.lineItems.size
    
    puts "Creating LineItem: Order [" + order.ordernumber + "] ProductCode [" + productCode + "] Quantity [" + quantity.to_s + "] index [" + index.to_s + "]"
    
    lineItem = LineItem.create(
              :r_tenant     => orderMaster.tenant.id,
              :created      => order.created, 
              :modified     => order.modified,
              :order        => order,
              :legacy_ordermaster => orderMaster.uniqueid,
              :quantity     => quantity,
              :productcode  => productCode,
              :ponumber     => poNumber,
              :lineid       => lineId,
              :description  => desc,
              :index        => index
            )
    
    order.lineItems << lineItem  
    
    lineItem
  end
  
  def displayString
    "#{productIdent} (#{id.to_s})"
  end
  
end
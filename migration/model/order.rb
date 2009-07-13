require "organization"
require "user"
require "customer"
require "division"
require "line_item"

class Order < ActiveRecord::Base
  set_table_name :orders
  
  belongs_to  :tenant,          :foreign_key => 'r_tenant',           :class_name => 'Organization'
  belongs_to  :modifiedBy,      :foreign_key => 'modifiedby',         :class_name => 'User'
  belongs_to  :customer,        :foreign_key => 'customer_id',        :class_name => 'Customer'
  belongs_to  :division,        :foreign_key => 'division_id',        :class_name => 'Division'
  has_many    :lineItems,       :foreign_key => 'order_id',           :class_name => 'LineItem'
  
  def self.resolveOrCreate(orderMaster)
    
    # There are aprox 77000 unirope orders with an ordernumber of '0' which should not be migrated 
    if orderMaster.tenant.id == 2 && orderMaster.ordernumber.strip == "0"
      return nil
    end
  
    order = Order.find(:first, :conditions => ["r_tenant = :tenantId and ordernumber = :ordernumber", {:tenantId => orderMaster.tenant.id, :ordernumber => orderMaster.ordernumber}])
    
    if order.nil?
      order = createFromOrderMaster(orderMaster)
    else
      puts "Found Order: ordernumber [" + order.ordernumber.to_s + "] tenant [" +  order.tenant.displayString + "] "
    end
    
    order
  end
  
  def self.createFromOrderMaster(orderMaster)
    puts "Creating Order: ordernumber [" + orderMaster.ordernumber.to_s + "] tenant [" +  orderMaster.tenant.displayString + "] "
    
    now = Time.now
    
    created = now
    modified = now
    
    if !orderMaster.datecreated.nil?
      created = orderMaster.datecreated
    end
  
    if !orderMaster.datemodified.nil?
      modified = orderMaster.datemodified
    end
    
    order = Order.create(
              :r_tenant     => orderMaster.tenant.id,
              :created      => created, 
              :modified     => modified,
              :ordertype    => resolveLegacyType(orderMaster.ordertype),
              :ordernumber  => orderMaster.ordernumber.strip,
              :orderdate    => orderMaster.orderdate,
              :customer     => orderMaster.customer,
              :division     => orderMaster.division
            )

    order
  end
  
  def self.resolveLegacyType(legacyType)
    case legacyType
      when "shoporder":     return "SHOP"
      when "customerorder": return "CUSTOMER"
      else raise "NonExistentOrderType [" + legacyType + "]"
    end
  end
  
  def displayString
    "#{ordernumber} (#{id.to_s})"
  end
  
end
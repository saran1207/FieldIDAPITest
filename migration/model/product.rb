require 'tenant'
require 'product_type'
require "info_option"
require "productserial_infooption"
require "sub_product"
require "base_org"
require "product_status"

#require "rubygems"
#require 'active_record'
class Product < ActiveRecord::Base
  set_table_name :products
  
belongs_to  :tenant,        :foreign_key => 'tenant_id',                :class_name => 'Tenant'
  belongs_to  :productinfo,   :foreign_key => 'type_id',                :class_name => 'ProductType'
  has_many    :infoOptionsFK, :foreign_key => 'r_productserial',        :class_name => 'ProductserialInfooption'
  has_many    :infoOptions,                                             :class_name => 'InfoOption',				:through => :infoOptionsFK
  belongs_to  :identifiedBy,  :foreign_key => 'identifiedby_id',        :class_name => 'User'
  belongs_to  :assignedUser,  :foreign_key => 'assigneduser_id',        :class_name => 'User'
  has_many    :subProducts,	  :foreign_key => 'masterproduct_id',       :class_name => 'SubProduct'
  belongs_to  :owner,         :foreign_key => 'owner_id',               :class_name => 'BaseOrg'
  belongs_to  :product_status,   :foreign_key => 'productstatus_unqiueid',     :class_name => 'ProductStatus'
  
  def findInfoOptionByInfoField(infoField)
    
    option = nil
    for infoOption in infoOptions
      if infoOption.infoField.uniqueid == infoField.uniqueid
        option = infoOption
        break
      end
    end
    
    return option
  end
  
  def getNextInfoOptionWeight
    # start at -1 since 0 is a valid weight
    weight = -1
    
    for infoOption in infoOptions
      if infoOption.weight > weight
        weight = infoOption.weight
      end
    end
    
    return (weight + 1)
  end
  
  def displayString
    "#{serialnumber} (#{id.to_s}): " + productinfo.displayString
  end
end

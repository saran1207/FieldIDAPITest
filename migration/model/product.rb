require 'organization'
require 'product_type'
require "customer"
require "division"
require "info_option"
require "productserial_infooption"

class Product < ActiveRecord::Base
  set_table_name :products
  
  belongs_to  :tenant,        :foreign_key => 'r_tenant',               :class_name => 'Organization'
  belongs_to  :organization,  :foreign_key => 'r_organization',         :class_name => 'Organization'
  belongs_to  :productinfo,   :foreign_key => 'type_id',                :class_name => 'ProductType'
  belongs_to  :owner,         :foreign_key => 'owner_id',               :class_name => 'Customer'
  belongs_to  :division,      :foreign_key => 'division_id',            :class_name => 'Division'
  has_many    :infoOptionsFK, :foreign_key => 'r_productserial',        :class_name => 'ProductserialInfooption'
  has_many    :infoOptions,                                             :class_name => 'InfoOption',              :through => :infoOptionsFK
  belongs_to  :identifiedBy,  :foreign_key => 'identifiedby_uniqueid',  :class_name => 'User'
  belongs_to  :assignedUser,  :foreign_key => 'assigneduser_id',        :class_name => 'User'
  
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
    "#{serialnumber} (#{id.to_s}) - " + productinfo.displayString
  end
end
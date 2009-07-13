require "product"
require "info_option"

class ProductserialInfooption < ActiveRecord::Base
  set_table_name :productserial_infooption
  set_primary_key :uniqueid
  
  belongs_to  :productSerial, :foreign_key => 'r_productserial',  :class_name => 'ProductSerial'
  belongs_to  :infoOption,    :foreign_key => 'r_infooption',     :class_name => 'InfoOption'
  
end
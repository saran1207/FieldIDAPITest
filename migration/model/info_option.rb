require "info_field"

class InfoOption < ActiveRecord::Base
  set_table_name :infooption
  set_primary_key :uniqueid
  
  belongs_to  :infoField,         :foreign_key => 'r_infofield',      :class_name => 'InfoField'
  has_one     :productSerialFK,   :foreign_key => 'r_infooption',     :class_name => 'ProductserialInfooption'
  has_one     :productSerial,                                         :class_name => 'ProductSerial',           :through => :productSerialFK
  
  def displayString
    "#{name} (#{uniqueid})"
  end
end
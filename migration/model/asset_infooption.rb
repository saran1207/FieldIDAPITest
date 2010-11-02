require "asset"
require "info_option"

class AssetInfooption < ActiveRecord::Base
  set_table_name :asset_infooption
  set_primary_key :uniqueid

  belongs_to  :productSerial, :foreign_key => 'r_productserial',  :class_name => 'ProductSerial'
  belongs_to  :infoOption,    :foreign_key => 'r_infooption',     :class_name => 'InfoOption'

end
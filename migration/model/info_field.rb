require "product_type"
require "info_option"

class InfoField < ActiveRecord::Base
  set_table_name :infofield
  set_primary_key :uniqueid
  
  belongs_to  :productType, :foreign_key => 'r_productinfo',  :class_name => 'ProductType'
  has_many    :infoOptions, :foreign_key => 'r_infofield',    :class_name => 'InfoOption',  :order => :weight
  
  def getStaticInfoOptions
    staticOpts = Array.new
    
    for opt in infoOptions
      if opt.staticdata?
        staticOpts << opt
      end
    end
    
    return staticOpts
  end
  
  def displayString
    "#{name} (#{uniqueid})"
  end  
end
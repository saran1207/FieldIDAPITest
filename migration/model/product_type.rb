require 'organization'
require 'product_type_inspection_type'
require 'product_type_attachment'
require "product_type_file_attachment"
require 'info_field'

class ProductType < ActiveRecord::Base
  set_table_name :producttypes
  
  belongs_to  :tenant,             :foreign_key => 'r_tenant',              :class_name => 'Organization'
  belongs_to  :modifiedBy,         :foreign_key => 'modifiedby',            :class_name => 'User'
  has_many    :inspectionTypeFKs,  :foreign_key => 'producttype_id',        :class_name => 'ProductTypeInspectionType'
  has_many    :inspectionTypes,                                             :class_name => 'InspectionType',            :through => :inspectionTypeFKs
  has_many    :infoFields,         :foreign_key => 'r_productinfo',         :class_name => 'InfoField',                 :order => :weight
  has_many    :attachments,        :foreign_key => 'producttypes_id',       :class_name => 'ProductTypeAttachment'
  has_many    :fileAttachments,    :foreign_key => 'producttypes_id',       :class_name => 'ProductTypeFileAttachments'
  
  
  def findInfoFieldByName(name)
    field = nil
    
    for infoField in infoFields
      # try and match the infofield by name
      if infoField.name.downcase == name.downcase
        field = infoField
        break
      end
    end
    
    return field
  end
  
  def getFilePath
    puts uniqueid.to_s
    
    return tenant.name + "/" + uniqueid.to_s
  end
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end
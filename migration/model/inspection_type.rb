require "tenant"
require "inspection_type_group"
require "inspection_type_supported_proof_test"
require "inspection_type_info_field_name"
require "inspection_type_criteria_section"
require "product_type_inspection_type"
require "criteria_section"

class InspectionType < ActiveRecord::Base
  set_table_name :inspectiontypes
  
  belongs_to  :tenant,                :foreign_key => 'tenant_id',           :class_name => 'Tenant'
  belongs_to  :group,                 :foreign_key => 'group_id',           :class_name => 'InspectionTypeGroup'
  has_many    :supportedProofTests,   :foreign_key => 'inspectiontypes_id', :class_name => 'InspectionTypeSupportedProofTest'
  has_many    :infoFieldNames,        :foreign_key => 'inspectiontypes_id', :class_name => 'InspectionTypeInfoFieldName'
  has_many    :sectionsFKs,           :foreign_key => 'inspectiontypes_id', :class_name => 'InspectionTypeCriteriaSection',   :order => :orderidx
  has_many    :sections,                                                    :class_name => 'CriteriaSection',                 :through => :sectionsFKs
  has_many    :productTypeFKs,        :foreign_key => 'inspectiontypes_id', :class_name => 'ProductTypeInspectionType'
  has_many    :productTypes,                                                :class_name => 'ProductType',                     :through => :productTypeFKs

  def getFirstSupportedProofTest
  	if !supportedProofTests.nil? && supportedProofTests.size > 0
  		return supportedProofTests[0].element
  	else
  		return nil;
  	end
  end
  
  def displayString
    "#{name} (#{id.to_s})"
  end
  
end

require "inspection_type_criteria_section"
require "inspection_type"
require "criteria_section_criteria"
require "criteria"
require "tenant"
class CriteriaSection < ActiveRecord::Base
  set_table_name :criteriasections
  
  belongs_to  :tenant,              :foreign_key => 'tenant_id',             :class_name => 'Tenant'
  has_one     :inspectionTypeFK,    :foreign_key => 'sections_id',          :class_name => 'InspectionTypeCriteriaSection'
  has_one     :inspectionType,                                              :class_name => 'InspectionType',                :through => :inspectionTypeFK
  has_many    :criteriaFKs,         :foreign_key => 'criteriasections_id',  :class_name => 'CriteriaSectionCriteria',       :order => :orderidx
  has_many    :criterias,                                                   :class_name => 'Criteria',                      :through => :criteriaFKs
  
  def displayString
    "#{title} (#{id.to_s})"
  end
end

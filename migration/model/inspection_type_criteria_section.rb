require 'composite_primary_keys'
require 'inspection_type'
require 'criteria_section'
class InspectionTypeCriteriaSection < ActiveRecord::Base
  set_table_name    :inspectiontypes_criteriasections
  set_primary_keys  :inspectiontypes_id, :sections_id
  
  belongs_to :inspectionType,  :foreign_key => 'inspectiontypes_id', :class_name => 'InspectionType'
  belongs_to :section,         :foreign_key => 'sections_id',        :class_name => 'CriteriaSection'
   
  before_save :pre_save
  
  def pre_save
    # we need to update the order index before we save.
    # the only way I've found to do this was to run a count
    self.orderidx = inspectionType.sectionsFKs.count
  end

end
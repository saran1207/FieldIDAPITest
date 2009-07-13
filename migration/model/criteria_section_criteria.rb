require 'composite_primary_keys'
require "criteria_section"
require "criteria"
class CriteriaSectionCriteria < ActiveRecord::Base
  set_table_name    :criteriasections_criteria
  set_primary_keys  :criteriasections_id, :criteria_id
    
  belongs_to :criteriaSection,  :foreign_key => 'criteriasections_id',  :class_name => 'CriteriaSection'
  belongs_to :criteria,         :foreign_key => 'criteria_id',          :class_name => 'Criteria'
   
  before_save :pre_save
  
  def pre_save
    # we need to update the order index before we save.
    # the only way I've found to do this was to run a count
    self.orderidx = criteriaSection.criteriaFKs.count
  end
end
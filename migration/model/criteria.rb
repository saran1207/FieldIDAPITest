require "criteria_section_criteria"
require "criteria_section"
require "state_set"
class Criteria < ActiveRecord::Base
  set_table_name :criteria
  
  belongs_to  :tenant,            :foreign_key => 'tenant_id',     :class_name => 'Tenant'
  belongs_to  :states,            :foreign_key => 'states_id',    :class_name => 'StateSet'
  has_one     :criteriaSectionFK, :foreign_key => 'sections_id',  :class_name => 'CriteriaSectionCriteria'
  has_one     :criteriaSection,                                   :class_name => 'CriteriaSection',         :through => :criteriaSectionFK
  
  def createLegacyButtonStateMapping
    for state in self.states.states
      state.createLegacyButtonStateMapping(self)
    end
  end
  
  def displayString
    "#{displaytext} (#{id.to_s})"
  end
end

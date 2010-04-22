require 'criteria_result'
require 'observation'
class CriteriaResultDeficiency < ActiveRecord::Base
  set_table_name :criteriaresults_deficiencies

  belongs_to  :criteriaresults,	:foreign_key => 'criteriaresults_id',	:class_name => 'CriteriaResult'
  belongs_to :observation, :foreign_key => 'deficiencies_id', :class_name=> 'Observation'

end
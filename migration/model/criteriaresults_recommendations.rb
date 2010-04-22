require 'criteria_result'
require 'criteria_result'
class CriteriaResultRecommendation < ActiveRecord::Base
  set_table_name :criteriaresults_recommendations

  belongs_to  :criteriaresults,	:foreign_key => 'criteriaresults_id',	:class_name => 'CriteriaResult'
  belongs_to :observation,  :foreign_key=> 'recommendations_id', :class_name=> 'Observation'
end
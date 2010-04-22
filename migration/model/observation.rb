#require 'active_record'
require 'criteriaresults_deficiencies'
require 'criteriaresults_recommendations'
class Observation < ActiveRecord::Base
  set_table_name :observations
  set_inheritance_column nil

  has_one  :criteriaresults_recommendations,		:class_name => 'CriteriaResultRecommendation'
  has_one  :criteriaresults_deficiencies,  :class_name => 'CriteriaResultDeficiency'
end
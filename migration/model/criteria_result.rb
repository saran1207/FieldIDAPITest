require "user"
require "state"
require "criteria"
require "abstract_inspection"
require "tenant"

class CriteriaResult < ActiveRecord::Base
  set_table_name :criteriaresults

  belongs_to  :tenant,        :foreign_key => 'tenant_id',         :class_name => 'Tenant'
  belongs_to  :modifiedBy,    :foreign_key => 'modifiedBy',       :class_name => 'User'
  belongs_to  :state,         :foreign_key => 'state_id',         :class_name => 'State'
  belongs_to  :criteria,      :foreign_key => 'criteria_id',      :class_name => 'Criteria'
  belongs_to  :inspection,    :foreign_key => 'inspection_id',    :class_name => 'AbstractInspection'
  
end

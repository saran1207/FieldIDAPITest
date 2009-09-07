require 'composite_primary_keys'
require "organization"
require "criteria"
require "state"
class LegacyButtonStateMapping < ActiveRecord::Base
  set_table_name :legacybuttonstatemappings
  set_primary_keys :buttonstateid, :tenant_id
  
  belongs_to  :buttonState, :foreign_key => 'buttonstateid',  :class_name => 'ButtonState'
  belongs_to  :tenant,      :foreign_key => 'tenant_id',       :class_name => 'Tenant'
  belongs_to  :criteria,    :foreign_key => 'criteria_id',    :class_name => 'Criteria'
  belongs_to  :state,       :foreign_key => 'state_id',       :class_name => 'State'
  
  def displayString
    "#{:buttonState.uniqueid.to_s} (" + tenant.displayString + ", " + criteria.displayString + ", " + state.displayString + ")"
  end
end

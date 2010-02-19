require "tenant"
require "state_set_state"
require "state_set"

class State < ActiveRecord::Base
  set_table_name :states
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',   :class_name => 'Tenant'
  has_one     :stateSetFK,  :foreign_key => 'states_id',  :class_name => 'StateSetState'
  has_one     :stateSet,                                  :class_name => 'StateSet',      :through => :stateSetFK
  
  def matches(buttonState)
    # for a buttonState to match, both the value/displayText and status must be the same
    return buttonState.value == self.displaytext && buttonState.getStatus == self.status
  end

  def displayString
    "#{displaytext} (#{id.to_s})"
  end
end

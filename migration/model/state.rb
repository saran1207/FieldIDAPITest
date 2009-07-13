require "organization"
require "state_set_state"
require "state_set"
require "legacy_button_state_mapping"
class State < ActiveRecord::Base
  set_table_name :states
  
  belongs_to  :tenant,      :foreign_key => 'r_tenant',   :class_name => 'Organization'
  has_one     :stateSetFK,  :foreign_key => 'states_id',  :class_name => 'StateSetState'
  has_one     :stateSet,                                  :class_name => 'StateSet',      :through => :stateSetFK
  
  def createLegacyButtonStateMapping(criteria, buttonState)

    # we need to check first that there is not already a mapping for this criteria and buttonState (this case is rare but it does exist)
    lbsmCount = LegacyButtonStateMapping.count(:conditions => ["r_tenant = :tenantId and buttonstateid = :buttonstateid", {:tenantId => self.tenant.id, :buttonstateid => buttonState.uniqueid}])
    
    if lbsmCount == 0
      puts "Creating LegacyButtonStateMapping for ButtonState [" + buttonState.displayString + "], Criteria [" + criteria.displayString + "] State [" + self.displayString + "]"
      LegacyButtonStateMapping.create(:tenant => self.tenant, :criteria => criteria, :state => self, :buttonState => buttonState)
    else
      puts "WARNING: LegacyButtonStateMapping already exists for tenant [" + tenant.displayString + "] and ButtonState [" + buttonState.displayString + "]"
    end
  end
  
  def matches(buttonState)
    # for a buttonState to match, both the value/displayText and status must be the same
    return buttonState.value == self.displaytext && buttonState.getStatus == self.status
  end

  def displayString
    "#{displaytext} (#{id.to_s})"
  end
end
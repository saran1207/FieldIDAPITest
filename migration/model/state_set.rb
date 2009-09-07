require "organization"
require "criteria"
require "state_set_state"
require "state"
class StateSet < ActiveRecord::Base
  set_table_name :statesets
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',     :class_name => 'Tenant'
  has_many    :criteria,    :foreign_key => 'states_id',    :class_name => 'Criteria'
  has_many    :stateFKs,    :foreign_key => 'statesets_id', :class_name => 'StateSetState',  :order => :orderidx
  has_many    :states,                                      :class_name => 'State',          :through => :stateFKs
  
  def self.findOrCreate(tenant, criteria, button)
    # Note 1: ok this one is kinda strange ... we need to resolve or create a state set based on our button's buttonstates
    # Since we will be normalizing data from the buttonstate table, we need a way to group the objects
    # The best way to do this, (that I can think of) is to construct the name of the state set, and lookup
    # to see if this name already exists.  The state set's name will be constructed from the 'value's of the 
    # buttonstates, in the order of their 'buttonorder'.  Eg A Button having 3 ButtonStates with 'values' in the 
    # order 'N/A', 'Pass', 'Fail', will product a StateSet name of 'N/A,Pass,Fail'.  In this way, we have encoded
    # both value and order into our new name.

    # Note 2: This method does not take into account the imagepath or inspectionstatus when resolving.  This could
    # create a situation where 2 or more button states produce the same state set name but have different statuses.
    # Eg/ ButtonStates: N/A (NA), Yes (PASS), No (FAIL) and  N/A (NA), Yes (FAIL), No (PASS) will both produce the name
    # "N/A,Yes,No", however the state mappings for them are different.  In this case, state sets will always resolve to
    # the first one created, regardless of their status mappings.  This is bad as it would create backwards mapped buttons.  
    # I have checked the current button state orderings in the DB and at this time (Sep 11, 2008) this case does not exist.
    # To absolutely, ensure this does not happen, we construct a concatenated string of a Button's ButtonState statuses and compare it
    # to a string as constructed from the loaded StateSet's States, if the two do not match, we throw an exception .... read the 
    # code, it'll make more sense :) 

    # now, without further a-doo, on with the show ...

    # we need to start by constructing our StateSet name (to see if it can be resolved)
    stateSetName = createStateSetNameFromButton(button)
    
    # try and load a state set by tenant and name
    stateSet = StateSet.find(:first, :conditions => ["tenant_id = :tenantId and name = :stateSetName", {:tenantId => tenant.id, :stateSetName => stateSetName}])
  
    if stateSet.nil?
      # we didn't find a set for this name, let's create one
      puts "Creating New StateSet for: [" +  tenant.displayString + "] named [" + stateSetName + "]"
      
      now = Time.now
      stateSet = StateSet.create :tenant => tenant, :name => stateSetName, :created => now, :modified => now
      
      # now we need to create our States
      for buttonState in button.buttonStates do
        displayText = buttonState.value
        status = buttonState.getStatus
        buttonname = buttonState.getButtonName
        
        puts "Creating State for: text [" +  displayText + "] status [" + status + "] buttonname [" + buttonname + "]"
        state = State.new :tenant => tenant, :created => now, :modified => now, :displaytext => displayText, :status => status, :buttonname => buttonname
        stateSet.states << state
        
      end
      
      # save the set now that we've added our states
      stateSet.save
      
    else
      # we have found a state set with a matching name, now lets make sure it's state statuses come in the same order (See Note 2 above)
      if createStateSetHashFromButton(button)  != stateSet.getStateStatusHash
        # the hashes didn't match, this could be handled by creating 
        raise "StatusHashes do not match!! StateSet: [" + stateSet.displayString + "] from button [" + button.displayString + "]" 
      end
      
      # the state status hashes match, so our resolved state set is valid
      puts "Found StateSet: " + stateSet.name
    end
    
    # before we can return our state set, we need to create LegacyButtonStateMapping's from our button states
    #  we could have done this above when we created the StateSet but then we wouldn't have mappings for StateSet's that have been resolved
    for buttonState in button.buttonStates do
      # let's find which one of our states matches this ButtonState
      for state in stateSet.states do
        if state.matches(buttonState)
          state.createLegacyButtonStateMapping(criteria, buttonState)
          break
        end
      end
    end
    
    return stateSet
  end
  
  # Constructs a State Set name from a Button's ButtonState values's (comma seperated and in order) 
  def self.createStateSetNameFromButton(button)
    stateSetName = ""
    firstState = true
    for buttonState in button.buttonStates do
      if firstState
        firstState = false
      else
        stateSetName += ","
      end
      stateSetName += buttonState.value
    end
    
    return stateSetName
  end
  
  # Constructs a string of a Button's ButtonState State.Status's (concatenated and in order) (See Note 2 above)
  def self.createStateSetHashFromButton(button)
    stateStatusString = ""
    for buttonState in button.buttonStates do
      stateStatusString += buttonState.getStatus
    end
    
    return stateStatusString
  end
  
  # Constructs a string of a each state's status (concatenated and in order) (See Note 2 above)
  def getStateStatusHash
    stateStatusString = ""
    for state in self.states do
      stateStatusString += state.status
    end
    
    return stateStatusString
  end
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end

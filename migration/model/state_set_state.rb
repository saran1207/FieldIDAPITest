require 'composite_primary_keys'
require "state_set"
require "state"
class StateSetState < ActiveRecord::Base
  set_table_name :statesets_states
  set_primary_keys :statesets_id, :states_id
  
  belongs_to :stateSet,  :foreign_key => 'statesets_id',  :class_name => 'StateSet'
  belongs_to :state,     :foreign_key => 'states_id',     :class_name => 'State'
   
  before_save :pre_save
  
  def pre_save
    # we need to update the order index before we save.
    # the only way I've found to do this was to run a count
    self.orderidx = stateSet.stateFKs.count
  end
end
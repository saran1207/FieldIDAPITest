require 'master_event'
require 'event_type'

class EventSchedule < ActiveRecord::Base
    set_table_name :eventschedules

    belongs_to  :event,      :foreign_key => 'event_event_id',      :class_name => 'MasterEvent'
    has_one :event_type,     :foreign_key => 'eventtype_id', :class_name => "EventType"
end
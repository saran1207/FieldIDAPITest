require 'master_event'

class EventSchedule < ActiveRecord::Base
    set_table_name :eventschedules

    belongs_to  :event,      :foreign_key => 'event_event_id',      :class_name => 'MasterEvent'
end
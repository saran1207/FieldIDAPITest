require 'event'
require 'event_schedule'

class MasterEvent < ActiveRecord::Base

  set_primary_key :event_id
  set_table_name :masterevents

  belongs_to  :event,      :foreign_key => 'event_id',      :class_name => 'Event'
  has_one :schedule, :foreign_key => "event_event_id", :class_name => "EventSchedule"

end
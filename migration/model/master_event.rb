require 'event'
require 'event_schedule'
require 'event_group'

class MasterEvent < ActiveRecord::Base

  set_primary_key :event_id
  set_table_name :masterevents

  belongs_to  :event,      :foreign_key => 'event_id',      :class_name => 'Event'
  belongs_to  :event_group,      :foreign_key => 'group_id',      :class_name => 'EventGroup'
  has_one :schedule, :foreign_key => "event_event_id", :class_name => "EventSchedule"

end
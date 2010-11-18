require 'task'

class ScheduleEventsTaskHourly < ActiveRecord::Migration

  def self.up
    Task.update_all({ :cronexpression => "5 * * * *" },  { :id => "EventScheduleNotifications" })
  end

  def self.down
    Task.update_all({ :cronexpression => "0 7 * * *" },  { :id => "EventScheduleNotifications" })
  end

end
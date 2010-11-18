require 'task'

class ChangeTaskFromInspectionToEvent < ActiveRecord::Migration

  def self.up
    Task.update_all({ :id => "EventScheduleNotifications" },  { :id => "InspectionScheduleNotifications" })
    Task.update_all({ :classname => "com.n4systems.taskscheduling.task.EventScheduleNotificationTask" },  { :classname => "com.n4systems.taskscheduling.task.InspectionScheduleNotificationTask" })
  end

  def self.down
    Task.update_all({ :id => "InspectionScheduleNotifications" },  { :id => "EventScheduleNotifications" })
    Task.update_all({ :classname => "com.n4systems.taskscheduling.task.InspectionScheduleNotificationTask" },  { :classname => "com.n4systems.taskscheduling.task.EventScheduleNotificationTask" })
  end

end
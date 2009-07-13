require 'task'

class CreateScheduledTasks < ActiveRecord::Migration
  
  def self.up
    now = Time.now
    
    execute("INSERT INTO tasks (id, created, modified, classname, cronexpression, taskgroup, enabled) values ('notifications', '" + now.to_s + "', '" + now.to_s + "', 'com.n4systems.taskscheduling.task.InspectionScheduleNotificationTask', '0 7 * * *', 'default', true)")
    execute("INSERT INTO tasks (id, created, modified, classname, cronexpression, taskgroup, enabled) values ('serialnumbercounter', '" + now.to_s + "', '" + now.to_s + "', 'com.n4systems.taskscheduling.task.SerialNumberCounterTask', '0 1 * * *', 'default', true)")
     
  end
  
  def self.down
  end
  
end
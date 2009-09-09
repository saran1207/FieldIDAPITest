class FixTaskTable < ActiveRecord::Migration
  def self.up
    remove_column(:tasks, :taskentityid)
    execute("ALTER TABLE tasks MODIFY id VARCHAR(255) NOT NULL")
    
    execute("UPDATE tasks set id = 'InspectionScheduleNotifications' where classname = 'com.n4systems.taskscheduling.task.InspectionScheduleNotificationTask'")
    execute("UPDATE tasks set id = 'SerialNumberCounter' where classname = 'com.n4systems.taskscheduling.task.SerialNumberCounterTask'")
    execute("UPDATE tasks set id = 'DiskUsage' where classname = 'com.n4systems.taskscheduling.task.DiskUsageTask'")
    execute("UPDATE tasks set id = 'LimitUpdate' where classname = 'com.n4systems.taskscheduling.task.TenantLimitUpdaterTask'")
    execute("UPDATE tasks set id = 'SignUpPackageSync' where classname = 'com.n4systems.taskscheduling.task.SignUpPackageSyncTask'")
  end
  
  def self.down
  end
end
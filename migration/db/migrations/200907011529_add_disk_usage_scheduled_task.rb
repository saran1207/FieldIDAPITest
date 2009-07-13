class AddDiskUsageScheduledTask < ActiveRecord::Migration
  def self.up
    now = Time.now
    execute("INSERT INTO tasks (id, created, modified, classname, cronexpression, taskgroup, enabled) values ('diskUsage', '" + now.to_s + "', '" + now.to_s + "', 'com.n4systems.taskscheduling.task.DiskUsageTask', '0 4 * * 6', 'default', true)")      
  end
  
  def self.down
    execute("DELETE tasks where id = 'diskUsage'");
  end
end
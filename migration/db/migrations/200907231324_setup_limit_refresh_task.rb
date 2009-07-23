class SetupLimitRefreshTask < ActiveRecord::Migration
  def self.up
    now = Time.now
    execute("INSERT INTO tasks (id, created, modified, classname, cronexpression, taskgroup, enabled) values ('limitUpdate', '" + now.to_s + "', '" + now.to_s + "', 'com.n4systems.taskscheduling.task.TenantLimitUpdaterTask', '*/2 * * * *', 'default', true)")
  end
  
  def self.down
    execute("DELETE tasks where id = 'limitUpdate'")
  end
end
class AddCacheLogTask < ActiveRecord::Migration
  def self.up
  	execute("insert into tasks (id, created, modified, classname, cronexpression, taskgroup, enabled) values ('CacheLogger', now(), now(), 'com.n4systems.taskscheduling.task.CacheLoggerTask', '*/5 * * * *', 'default', true);")
  end
end
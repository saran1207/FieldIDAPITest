class AddDownloadCleanupTask < ActiveRecord::Migration
  def self.up
  	execute("insert into tasks (id, created, modified, classname, cronexpression, taskgroup, enabled) values ('DownloadCleanup', now(), now(), 'com.n4systems.taskscheduling.task.DownloadCleanupTask', '30 1 * * *', 'default', true);")
  end
end
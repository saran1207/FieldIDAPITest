require 'task'

class ScheduleSendSavedItemsTask < ActiveRecord::Migration

  def self.up
    task = Task.new
    task.id = "SendSavedItems"
    task.created = Time.now
    task.modified = Time.now
    task.classname = "com.n4systems.taskscheduling.task.SendSavedItemsTask"
    task.cronexpression = "0 * * * *"
    task.taskgroup = "default"
    task.enabled = true

    task.save
  end

  def self.down
    Task.delete_all(:id=>"SendSavedItems")
  end

end
require 'task'

class AddRecurringSchedulesTask < ActiveRecord::Migration

  def self.up
    task = Task.new
    task.id = "RecurringSchedules"
    task.created = Time.now
    task.modified = Time.now
    task.classname = "com.n4systems.taskscheduling.task.RecurringSchedulesTask"
    task.cronexpression = "0 0 * * *"
    task.taskgroup = "default"
    task.enabled = true

    task.save
  end

  def self.down
    Task.delete_all(:id=>"RecurringSchedules")
  end

end
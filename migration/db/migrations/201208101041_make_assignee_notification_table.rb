class MakeAssigneeNotificationTable < ActiveRecord::Migration

  def self.up
    execute("create table assignee_notifications (id bigint(21) not null auto_increment, primary key (id), event_id bigint(21) not null)")

    execute("alter table assignee_notifications add foreign key event_id_index (event_id) references masterevents (event_id)")

    execute("insert into tasks (id,created,modified,classname,cronexpression,taskgroup,enabled) values ('AssigneeNotification',now(),now(),'com.n4systems.taskscheduling.task.NotifyAssigneesTask','*/5 * * * *','default', 1)")
  end

  def self.down
    drop_table :assignee_notifications
    execute('delete from tasks where id = "AssigneeNotification"')
  end


end
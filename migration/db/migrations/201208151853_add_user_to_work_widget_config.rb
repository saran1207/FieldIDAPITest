class AddUserToWorkWidgetConfig < ActiveRecord::Migration

    def self.up
        add_column(:widget_configurations_work, :user_id, :integer)
        execute("alter table widget_configurations_work modify user_id bigint null;")
        execute("alter table widget_configurations_work add foreign key fk_work_widget_user(user_id) references users(id) on update no action on delete no action")
    end

    def self.down
        remove_column :widget_configurations_work, :user_id
    end

end
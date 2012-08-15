class AddWorkWidgetConfig < ActiveRecord::Migration

  def self.up

     create_table "widget_configurations_work", :primary_key => "id" do |t|
       t.integer :org_id
       t.integer :asset_type_id
       t.integer :event_type_id
     end

    execute("alter table widget_configurations_work modify id bigint not null;")
    execute("alter table widget_configurations_work modify org_id bigint null;")
    execute("alter table widget_configurations_work modify asset_type_id bigint null;")
    execute("alter table widget_configurations_work modify event_type_id bigint null;")

    execute("alter table widget_configurations_work add foreign key fk_work_widget_config (id) references widget_configurations(id) on update no action on delete no action")
    execute("alter table widget_configurations_work add foreign key fk_work_widget_org(org_id) references org_base(id) on update no action on delete no action")
    execute("alter table widget_configurations_work add foreign key fk_work_widget_asset_type(asset_type_id) references assettypes(id) on update no action on delete no action")
    execute("alter table widget_configurations_work add foreign key fk_work_widget_event_type(event_type_id) references eventtypes(id) on update no action on delete no action")

    end

   def self.down
     drop_table :widget_configurations_work
   end

end
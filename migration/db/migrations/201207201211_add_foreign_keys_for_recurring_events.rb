
class AddForeignKeysForRecurringEvents < ActiveRecord::Migration

  def self.up
        execute("alter table recurring_asset_type_events modify column asset_type_id bigint(21) not null")
        execute("alter table recurring_asset_type_events modify column event_type_id bigint(21) not null")
        execute("alter table recurring_asset_type_events modify column owner_id bigint(21) null")
        execute("alter table recurring_asset_type_events modify column tenant_id bigint(21) not null")
        execute("alter table recurring_asset_type_events modify column id bigint(21) not null auto_increment")
        execute("alter table recurring_asset_type_events modify column modifiedby bigint(21) null")
        execute("alter table recurring_asset_type_events modify column createdby bigint(21) null")

        add_column(:recurring_asset_type_events, :state, :string, :null => false)

        execute("alter table masterevents add constraint fk_events_recurring_events foreign key (recurring_event_id) references recurring_asset_type_events(id) on update no action on delete no action")

        execute("alter table recurring_asset_type_events add constraint fk_recurringevents_tenant foreign key (tenant_id) references tenants(id) on update no action on delete no action")
        execute("alter table recurring_asset_type_events add constraint fk_recurringevents_eventtype foreign key (event_type_id) references eventtypes(id) on update no action on delete no action")
        execute("alter table recurring_asset_type_events add constraint fk_recurringevents_assettype foreign key (asset_type_id) references assettypes(id) on update no action on delete no action")
        execute("alter table recurring_asset_type_events add constraint fk_recurringevents_owner foreign key (owner_id) references org_base(id) on update no action on delete no action")
  end

  def self.down

        execute("alter table recurring_asset_type_events drop foreign key fk_recurringevents_tenant")
        execute("alter table recurring_asset_type_events drop foreign key fk_recurringevents_eventtype")
        execute("alter table recurring_asset_type_events drop foreign key fk_recurringevents_assettype")
        execute("alter table recurring_asset_type_events drop foreign key fk_recurringevents_owner")

        execute("alter table masterevents drop foreign key fk_events_recurring_events")
        remove_column(:recurring_asset_type_events, :state)
  end

end
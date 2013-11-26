alter table widget_configurations_actions add column thing_action_id bigint default null;
alter table widget_configurations_actions add constraint fk_widget_configurations_actions_on_thing_action_type_id foreign key (thing_action_id) references thing_events(id);


alter table widget_configurations_work add column thing_event_type_id bigint default null;
alter table widget_configurations_work add constraint fk_widget_configurations_work_on_thing_event_type_id foreign key (thing_event_type_id) references thing_events(id);
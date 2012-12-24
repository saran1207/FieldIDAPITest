rename table states to buttons;
rename table statesets to button_groups;
rename table statesets_states to button_groups_buttons;

alter table button_groups_buttons drop foreign key fk_statesets_states_states;
alter table button_groups_buttons drop foreign key fk_statesets_states_statesets;

alter table button_groups_buttons change statesets_id button_group_id bigint;
alter table button_groups_buttons change states_id button_id bigint;

alter table button_groups_buttons add foreign key fk_buttons (button_id) references buttons(id);
alter table button_groups_buttons add foreign key fk_buttons_groups (button_group_id) references button_groups(id);


alter table oneclick_criteria drop foreign key fk_oneclick_criteria_statesets;

alter table oneclick_criteria change states_id button_group_id bigint;

alter table button_groups_buttons add foreign key fk_button_group (button_group_id) references button_groups(id);
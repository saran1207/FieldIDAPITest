alter table procedures add column workflow_state varchar(255) not null;
update procedures set workflow_state="OPEN";
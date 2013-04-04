alter table isolation_points add column created datetime not null;
alter table isolation_points add column modified datetime not null;

alter table isolation_points add column createdby datetime default null;
alter table isolation_points add column modifiedby datetime default null;

alter table isolation_points add constraint fk_isolation_points_created_user foreign key (createdby) references users (id);
alter table isolation_points add constraint fk_isolation_points_modified_user foreign key (modifiedby) references users (id);

update isolation_points set created=now(),modified=now();
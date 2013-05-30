rename table serialnumbercounter to identifier_counters;

alter table identifier_counters change column uniqueid id bigint;

alter table identifier_counters add column createdby bigint;
alter table identifier_counters add column modifiedby bigint;

alter table identifier_counters add column created datetime;
alter table identifier_counters add column modified datetime;
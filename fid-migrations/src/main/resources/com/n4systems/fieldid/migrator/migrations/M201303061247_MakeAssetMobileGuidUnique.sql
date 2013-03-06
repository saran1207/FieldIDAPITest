create temporary table temp_duplicate_guid_table select min(id) as id, mobileguid as mobileguid from assets group by mobileguid having count(*) > 1;

update assets a, temp_duplicate_guid_table d set a.mobileguid = uuid() where a.id <> d.id and a.mobileguid = d.mobileguid;

drop temporary table temp_duplicate_guid_table;

alter table assets add constraint mobile_guid_unique unique key (mobileguid);
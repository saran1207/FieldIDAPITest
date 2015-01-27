update eventforms ef
join observationcount_groups eg on ef.tenant_id=eg.tenant_id
set ef.observationcount_group_id=eg.id;


alter table eventforms
add CONSTRAINT `eventform_to_observationcount_group` FOREIGN KEY(`observationcount_group_id`) REFERENCES `observationcount_groups`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


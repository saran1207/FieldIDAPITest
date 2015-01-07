alter table preconfigured_energy_sources
add `created` datetime NOT NULL,
add `modified` datetime NOT NULL,
add `modifiedby` bigint(21) DEFAULT NULL,
add `createdby` bigint(21) DEFAULT NULL;

update preconfigured_energy_sources set created=now(), modified=now();
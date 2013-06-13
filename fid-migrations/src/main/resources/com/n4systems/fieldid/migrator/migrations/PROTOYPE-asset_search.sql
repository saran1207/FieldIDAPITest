#this was the state of the asset_attribute_view.
#'CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `asset_attribute_view` AS select ((`a`.`id` * 10000000) + `opt`.`uniqueid`) AS `id`,`a`.`id` AS `assetId`,`a`.`identifier` AS `displayName`,`field`.`name` AS `attribute`,`opt`.`name` AS `textValue`,`field`.`fieldtype` AS `type`,cast(`opt`.`name` as signed) AS `number_hint`,(case when ((`field`.`fieldtype` = 'datefield') and (cast(`opt`.`name` as decimal(10,4)) > 0)) then from_unixtime((cast(`opt`.`name` as unsigned) / 1000)) else NULL end) AS `datefield` from (((`assets` `a` join `infofield` `field`) join `infooption` `opt`) join `asset_infooption` `aio`) where ((`aio`.`r_productserial` = `a`.`id`) and (`aio`.`r_infooption` = `opt`.`uniqueid`) and (`opt`.`r_infofield` = `field`.`uniqueid`) and (`a`.`tenant_id` = (select `tenants`.`id` from `tenants` where (`tenants`.`name` = 'n4'))) and (`a`.`state` <> 'ARCHIVED'))'

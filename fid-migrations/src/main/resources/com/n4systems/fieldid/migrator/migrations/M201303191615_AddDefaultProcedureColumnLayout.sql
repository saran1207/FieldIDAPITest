set @completedDateColumn = (select id from column_mappings where name = "procedure_search_completed_date");

insert into column_layouts values(null, now(), now(), null, null, null, @completedDateColumn, "DESC", "PROCEDURE");

set @layout = (select max(id) from column_layouts);

insert into active_column_mappings values(null,now(),now(),null,null,null, (select id from column_mappings where name="procedure_search_completed_date") ,10,  @layout);
insert into active_column_mappings values(null,now(),now(),null,null,null, (select id from column_mappings where name="procedure_search_identifier") ,20,  @layout);
insert into active_column_mappings values(null,now(),now(),null,null,null, (select id from column_mappings where name="procedure_search_performedby") ,30,  @layout);

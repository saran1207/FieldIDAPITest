create temporary table temp_duplicate_image_table select min(id) as id, criteriaresult_id, file_name from criteriaresult_images group by criteriaresult_id,file_name having count(*) > 1;

update criteriaresult_images c, temp_duplicate_image_table d set c.file_name="__REMOVE_THIS_DUPLICATE__" where c.criteriaresult_id=d.criteriaresult_id and c.file_name = d.file_name and c.id <> d.id;
delete from criteriaresult_images where file_name = "__REMOVE_THIS_DUPLICATE__";

drop temporary table temp_duplicate_image_table;
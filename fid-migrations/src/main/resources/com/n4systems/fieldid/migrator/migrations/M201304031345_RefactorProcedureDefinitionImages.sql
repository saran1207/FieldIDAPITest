alter table isolation_points add column image_annotation_id bigint;
alter table isolation_points add constraint fk_iso_point_image_annotation foreign key (image_annotation_id) references image_annotation(id);

alter table procedure_definition_images drop column file_name;

alter table editable_images drop column original_filename;




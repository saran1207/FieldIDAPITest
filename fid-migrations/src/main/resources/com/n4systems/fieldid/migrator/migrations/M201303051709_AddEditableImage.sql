create table editable_images (
  id bigint(20) not null auto_increment,
  filename varchar(255) not null,
  original_filename varchar(255) not null,
  primary key (id)
);

create table image_annotation (
  id bigint(20) not null auto_increment,
  type_id bigint(20) not null,
  text varchar(200),
  image_id bigint(20) not null,
  x float,
  y float,
  primary key (id),
  constraint fk_img_annotation_img foreign key (image_id) references editable_images(id)
);

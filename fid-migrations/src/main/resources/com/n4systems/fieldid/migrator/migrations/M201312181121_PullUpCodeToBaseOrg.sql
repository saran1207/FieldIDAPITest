alter table org_base add column code varchar(255);
update org_base org, org_division dv set org.code = dv.code where org.id = dv.org_id;
update org_base org, org_customer cust set org.code = cust.code where org.id = cust.org_id;

alter table org_base add column report_image_id bigint(20) default null;
alter table org_base add constraint fk_image_id_s3_attachments foreign key (image_id) references s3_attachments(id);
alter table org_base add constraint fk_report_image_id_s3_attachments foreign key (report_image_id) references s3_attachments(id);


CREATE TABLE IF NOT EXISTS procedure_notifications (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  procedure_id bigint(20) NOT NULL,
  primary key(id),
  constraint fk_procedure_notifications_procedure foreign key (procedure_id) references procedures(id)
);
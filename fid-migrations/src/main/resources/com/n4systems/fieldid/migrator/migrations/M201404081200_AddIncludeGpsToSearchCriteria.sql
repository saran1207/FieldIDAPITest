ALTER TABLE saved_reports ADD COLUMN hasGps TINYINT(1);

alter table saved_reports add column south_bounds decimal(15,10);
alter table saved_reports add column west_bounds decimal(15,10);
alter table saved_reports add column north_bounds decimal(15,10);
alter table saved_reports add column east_bounds decimal(15,10);
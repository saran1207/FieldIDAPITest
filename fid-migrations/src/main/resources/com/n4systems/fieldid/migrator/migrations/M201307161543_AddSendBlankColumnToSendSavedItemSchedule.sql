alter table send_saved_item_schedules add send_blank_report TINYINT(1) NOT NULL default 0;

UPDATE send_saved_item_schedules SET send_blank_report = 0;
alter table eventforms
add observationcount_pass_calculation_type varchar(255) NOT NULL,
add observationcount_pass_comparator varchar(255) NOT NULL,
add observationcount_pass_value1 decimal(15,10) NOT NULL,
add observationcount_pass_value2 decimal(15,10) DEFAULT NULL,
add observationcount_fail_calculation_type varchar(255) NOT NULL,
add observationcount_fail_comparator varchar(255) NOT NULL,
add observationcount_fail_value1 decimal(15,10) NOT NULL,
add observationcount_fail_value2 decimal(15,10) DEFAULT NULL;

update eventforms
set observationcount_pass_calculation_type='SUM',
observationcount_fail_calculation_type='SUM',
observationcount_fail_comparator = 'BETWEEN',
observationcount_pass_comparator = 'BETWEEN';

alter table eventforms
add observationcount_group_id bigint(20) NOT NULL;
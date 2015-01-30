alter table eventforms
add observationcount_pass bigint(20) DEFAULT NULL;

alter table eventforms
add observationcount_fail bigint(20) DEFAULT NULL;

alter table eventforms
add CONSTRAINT `eventform_to_observationcount_fail` FOREIGN KEY(`observationcount_fail`) REFERENCES `observationcount`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

alter table eventforms
add CONSTRAINT `eventform_to_observationcount_pass` FOREIGN KEY(`observationcount_pass`) REFERENCES `observationcount`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
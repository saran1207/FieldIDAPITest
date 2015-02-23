alter table observationcount
drop column value;

CREATE TABLE observationcount_result (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  observationcount_id bigint(20) NOT NULL,
  value bigint(20),
  PRIMARY KEY (id),
  KEY fk_observationcountresult_to_observationcount (observationcount_id),
  CONSTRAINT fk_observationcountresult_to_observationcount FOREIGN KEY (observationcount_id) REFERENCES observationcount (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

drop table observationcount_criteriaresults;

CREATE TABLE observationcount_criteriaresults (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

CREATE TABLE observationcount_criteriaresult_observationcountsresults (
  id bigint(21) NOT NULL AUTO_INCREMENT,
  observationcountcriteriaresult_id bigint(20) NOT NULL,
  observationcountresult_id bigint(20) NOT NULL,
  orderIdx bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_observationcountcriteriaresult (observationcountcriteriaresult_id),
  KEY fk_observationcountresult (observationcountresult_id),
  CONSTRAINT fk_observationcountcriteriaresult FOREIGN KEY (observationcountcriteriaresult_id) REFERENCES observationcount_criteriaresults (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_observationcountresult FOREIGN KEY (observationcountresult_id) REFERENCES observationcount_result (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
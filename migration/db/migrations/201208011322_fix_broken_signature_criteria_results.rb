class FixBrokenSignatureCriteriaResults < ActiveRecord::Migration

  def self.up
    # Need to "unsign" signature criteria results of subevents since their data was not saving correctly.
    execute("update subevents se, events e, criteriaresults cr, signature_criteriaresults scr set scr.signed = 0 where date(e.created) >= '2012-08-01' and se.event_id = e.id and cr.event_id = e.id and cr.id = scr.id;")
  end

  def self.down
  end

end
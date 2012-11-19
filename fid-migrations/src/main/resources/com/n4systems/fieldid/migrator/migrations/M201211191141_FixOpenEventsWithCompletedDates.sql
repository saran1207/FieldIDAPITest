create temporary table completed_open_events_that_have_results select me.event_id as id from masterevents me, criteriaresults res where event_state  = "OPEN" and completeddate is not null and res.event_id = me.event_id group by me.event_id having count(*)>0;

update masterevents me set completeddate = null,performedby_id = null,result="VOID" where completeddate is not null and event_state="OPEN" and me.event_id not in (select id from completed_open_events_that_have_results);

drop temporary table completed_open_events_that_have_results;
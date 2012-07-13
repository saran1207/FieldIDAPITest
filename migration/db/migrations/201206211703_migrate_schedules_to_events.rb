class MigrateSchedulesToEvents < ActiveRecord::Migration

  def self.up
    
    execute("INSERT INTO events (tenant_id, created, modified, modifiedby, type_id, asset_id, mobileguid, editable, createdby) 
    SELECT es.tenant_id, es.created, es.modified, es.modifiedby, es.eventtype_id, es.asset_id, es.mobileguid, true, es.createdby 
    FROM eventschedules es 
    LEFT OUTER JOIN masterevents me ON es.id=me.schedule_id 
    WHERE es.status <> 'COMPLETED' AND es.state ='ACTIVE' AND me.event_id IS NULL;")

    execute("INSERT INTO eventgroups (id, created, modified) VALUES (-1, now(), now())") 

    execute("INSERT INTO masterevents (event_id, location, printable, group_id, state, owner_id, predefinedlocation_id, schedule_id, nextDate, event_state, project_id) 
    SELECT e.id, es.location, false, -1, 'ACTIVE', es.owner_id, es.predefinedlocation_id, es.id, es.nextDate, 'OPEN', es.project_id
    FROM eventschedules es, events e
    WHERE es.mobileguid = e.mobileguid;")
    
    execute("INSERT INTO eventgroups (created, modified, modifiedby, mobileguid, createdby)
    SELECT e.created, e.modified, e.modifiedby, e.mobileguid, e.createdby
    FROM events e, masterevents me
    WHERE e.id = me.event_id AND me.group_id = -1;")
    
    execute("UPDATE masterevents me, eventgroups eg, events e SET me.group_id = eg.id WHERE me.group_id = -1 AND me.event_id = e.id AND e.mobileguid = eg.mobileguid;")
    
    execute("DELETE FROM eventgroups WHERE id = -1;")

  end

  def self.down
  end

end
  
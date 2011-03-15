require 'active_column_mapping'
require 'column_mapping'
require 'column_layout'


class AddDefaultColumnLayouts < ActiveRecord::Migration

  def self.up
    default_events_layout = create_column_layout("EVENT", "event_search_date_performed", "DESC")

    create_default_column(default_events_layout, "event_search_eventtype", 1)
    create_default_column(default_events_layout, "event_search_eventresult", 2)
    create_default_column(default_events_layout, "event_search_serialnumber", 3)
    create_default_column(default_events_layout, "event_search_customer", 4)
    create_default_column(default_events_layout, "event_search_customer", 5)
    create_default_column(default_events_layout, "event_search_assettype", 6)
    create_default_column(default_events_layout, "event_search_assetstatus", 7)

    default_assets_layout = create_column_layout("ASSET", "asset_search_identified", "DESC")

    create_default_column(default_assets_layout, "asset_search_serialnumber", 1)
    create_default_column(default_assets_layout, "asset_search_referencenumber", 2)
    create_default_column(default_assets_layout, "asset_search_customer", 3)
    create_default_column(default_assets_layout, "asset_search_location", 4)
    create_default_column(default_assets_layout, "asset_search_assettype", 5)
    create_default_column(default_assets_layout, "asset_search_assetstatus", 6)
    create_default_column(default_assets_layout, "asset_search_lasteventdate", 7)
    create_default_column(default_assets_layout, "asset_search_identified", 8)

    default_sched_layout = create_column_layout("SCHEDULE", "event_schedule_nextdate", "ASC")

    create_default_column(default_sched_layout, "event_schedule_nextdate", 1)
    create_default_column(default_sched_layout, "event_schedule_status", 2)
    create_default_column(default_sched_layout, "event_schedule_dayspastdue", 3)
    create_default_column(default_sched_layout, "event_schedule_serialnumber", 4)
    create_default_column(default_sched_layout, "event_schedule_referencenumber", 5)
    create_default_column(default_sched_layout, "event_schedule_customer", 6)
    create_default_column(default_sched_layout, "event_schedule_eventtype", 7)
    create_default_column(default_sched_layout, "event_schedule_lastdate", 8)
    create_default_column(default_sched_layout, "event_schedule_assettype", 9)

    default_event_job_layout = create_column_layout("EVENT_TO_JOB", "event_schedule_nextdate", "ASC")

    create_default_column(default_event_job_layout, "schedule_to_job_serialnumber", 1)
    create_default_column(default_event_job_layout, "schedule_to_job_customername", 2)
    create_default_column(default_event_job_layout, "schedule_to_job_location", 3)
    create_default_column(default_event_job_layout, "schedule_to_job_assettype", 4)
    create_default_column(default_event_job_layout, "schedule_to_job_event_type", 5)
    create_default_column(default_event_job_layout, "schedule_to_job_status", 6)
  end

  def self.down
    
  end

  def self.create_column_layout(type, sort_column, sort_direction)
    layout = create_entity(ColumnLayout)
    layout.created = Time.now
    layout.modified = Time.now
    layout.report_type = type
    layout.sort_column = find_column(sort_column)
    layout.sort_direction = sort_direction
    layout.save
    return layout
  end

  def self.create_default_column(layout, name, order)
    column = find_column(name)
    active_mapping = create_entity(ActiveColumnMapping)
    active_mapping.column_mapping = column
    active_mapping.ordervalue = order
    active_mapping.column_layout = layout
    active_mapping.save
  end

  def self.find_column(name)
    ColumnMapping.find(:first, :conditions => {:name => name})
  end

  def self.create_entity(clazz)
    entity = clazz.new
    entity.created = Time.now
    entity.modified = Time.now
    return entity
  end

end
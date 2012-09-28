delete from dashboard_columns_widget_definitions where widget_definition_id in (select id from widget_definitions where widget_type = 'COMMON_LINKS');

delete from widget_definitions where widget_type = 'COMMON_LINKS';
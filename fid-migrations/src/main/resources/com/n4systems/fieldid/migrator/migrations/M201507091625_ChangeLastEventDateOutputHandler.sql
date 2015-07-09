UPDATE column_mappings
SET output_handler = 'com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler'
WHERE name = 'asset_search_lasteventdate';
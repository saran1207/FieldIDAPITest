class ChangeDateIdentifiedColumnGroup < ActiveRecord::Migration

  def self.up
    execute( "update column_mappings set group_id = (select id from column_mapping_groups where group_key = 'identifiers' and report_type = 'EVENT') where group_id = (select id from column_mapping_groups where group_key = 'date_identified' and report_type='EVENT')")
	execute( "update column_mappings set group_id = (select id from column_mapping_groups where group_key = 'identifiers' and report_type = 'ASSET') where group_id = (select id from column_mapping_groups where group_key = 'date_identified' and report_type='ASSET')")
  end

  def self.down
      execute( "update column_mappings set group_id = (select id from column_mapping_groups where group_key = 'date_identified' and report_type = 'EVENT') where group_id = (select id from column_mapping_groups where group_key = 'identifiers' and report_type='EVENT') and label='label.identifieddate'")
	  execute( "update column_mappings set group_id = (select id from column_mapping_groups where group_key = 'date_identified' and report_type = 'ASSET') where group_id = (select id from column_mapping_groups where group_key = 'identifiers' and report_type='ASSET') and label='label.identifieddate'")
  end

end
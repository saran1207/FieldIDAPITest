class ChangeProductIdColumnsToAssetId < ActiveRecord::Migration

  def self.up
    change_column(:inspections, :product_id, :index_inspections_on_product_id, :fk_inspections_products, :asset_id, :index_inspections_on_asset_id, :fk_inspections_assets, :asset_id, :assets, :id)
    change_column(:inspections, :productstatus_id, :fk_inspections_productstatus, :fk_inspections_productstatus, :assetstatus_id, :index_inspections_on_assetstatus_id, :fk_inspections_assetstatus, :assetstatus_id, :assetstatus, :uniqueid)
    change_column(:assets, :productstatus_uniqueid, :index_products_on_productstatus_uniqueid, :fk_products_productstatus, :assetstatus_id, :index_assets_on_assetstatus_id, :fk_assets_assetstatus, :assetstatus_id, :assetstatus, :uniqueid)
    change_column(:projects_assets, :products_id, :index_projects_products_on_products_id, :fk_projects_products_products, :asset_id, :index_projects_assets_on_asset_id, :fk_projects_products_assets, :asset_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:assettypeschedules, :producttype_id, :index_producttypeschedules_on_producttype_id, :fk_producttypeschedules_producttypes, :assettype_id, :index_projects_assets_on_asset_id, :fk_assettypeschedules_assettypes, :assettype_id, :assettypes, :id, "BIGINT(20) NOT NULL")
    change_column(:assettypes_fileattachments, :producttypes_id, :index_producttypes_fileattachments_on_producttypes_id, :fk_producttypes_fileattachments_producttypes, :assettype_id, :index_assettypes_fileattachments_on_assettypes_id, :fk_assettypes_fileattachments_assettypes, :assettype_id, :assettypes, :id, "BIGINT(20) NOT NULL")
    change_column(:assettypes_assettypes, :producttypes_id, :fk_producttypes_producttypes_master_type, :fk_producttypes_producttypes_master_type, :assettype_id, :index_assettypes_assettypes_master_type, :fk_assettypes_assettypes_master_type, :assettype_id, :assettypes, :id, "BIGINT(20) NOT NULL")
    change_column(:subassets, :product_id, :fk_subproducts_subproduct, :fk_subproducts_subproduct, :asset_id, :index_subassets_subasset, :fk_subassets_subasset, :asset_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:subassets, :masterproduct_id, :index_subproducts_on_masterproduct_id, :fk_subproducts_masterproduct, :masterasset_id, :index_subassets_masterasset, :fk_subassets_masterasset, :masterasset_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:assetattachments, :product_id, :index_productattachments_on_product_id, :fk_productattachments_products, :asset_id, :index_assetattachments_on_asset_id, :fk_assetattachments_assets, :asset_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:inspectionschedules, :product_id, :index_inspectionschedules_on_product_id, :fk_inspectionschedules_products, :asset_id, :index_inspectionschedules_on_asset_id, :fk_inspectionschedules_assets, :asset_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:lineitems, :productcode, :index_lineitems_on_productcode, nil, :assetcode, :index_lineitems_on_assetcode, nil, :assetcode, :assets, :id, "varchar(255) COLLATE utf8_unicode_ci NOT NULL")
    change_column(:catalogs_assettypes, :publishedproducttypes_id, :index_catalogs_producttypes_on_publishedproducttypes_id, :fk_catalogs_producttypes_producttypes, :publishedassettypes_id, :index_catalogs_assettypes_on_publishedassettypes_id, :fk_catalogs_assettypes_assettypes, :publishedassettypes_id, :assettypes, :id, "BIGINT(20) NOT NULL")

    execute "alter table notificationsettings_assettypes change column producttype_id assettype_id bigint(20) not null"
    execute "alter table setupdatalastmoddates change column producttypes assettypes datetime not null"
  end

  def self.down
    change_column(:inspections, :asset_id, :index_inspections_on_product_id, :fk_inspections_assets, :product_id, :fk_inspections_products, :fk_inspections_products, :product_id, :assets, :id)
    change_column(:inspections, :assetstatus_id, :index_inspections_on_assetstatus_id, :fk_inspections_assetstatus, :productstatus_id, :fk_inspections_productstatus, :fk_inspections_productstatus, :productstatus_id, :assetstatus, :uniqueid)
    change_column(:assets, :assetstatus_id, :index_assets_on_assetstatus_id, :fk_assets_assetstatus, :productstatus_uniqueid, :index_products_on_productstatus_uniqueid, :fk_products_productstatus, :productstatus_uniqueid, :assetstatus, :uniqueid)
    change_column(:projects_assets, :asset_id, :index_projects_assets_on_asset_id, :fk_projects_products_assets, :products_id, :index_projects_products_on_products_id, :fk_projects_products_products, :products_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:assettypeschedules, :assettype_id, :index_projects_assets_on_asset_id, :fk_assettypeschedules_assettypes, :producttype_id, :index_producttypeschedules_on_producttype_id, :fk_producttypeschedules_producttypes, :producttype_id, :assettypes, :id, "BIGINT(20) NOT NULL")
    change_column(:assettypes_fileattachments, :assettype_id, :index_assettypes_fileattachments_on_assettypes_id, :fk_assettypes_fileattachments_assettypes, :producttypes_id, :index_producttypes_fileattachments_on_producttypes_id, :fk_producttypes_fileattachments_producttypes, :producttypes_id, :assettypes, :id, "BIGINT(20) NOT NULL")
    change_column(:assettypes_assettypes, :assettype_id, :index_assettypes_assettypes_master_type, :fk_assettypes_assettypes_master_type, :producttypes_id, :fk_producttypes_producttypes_master_type, :fk_producttypes_producttypes_master_type, :producttypes_id, :assettypes, :id, "BIGINT(20) NOT NULL")
    change_column(:subassets, :asset_id, :index_subassets_subasset, :fk_subassets_subasset, :product_id, :fk_subproducts_subproduct, :fk_subproducts_subproduct, :product_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:subassets, :masterasset_id, :index_subassets_masterasset, :fk_subassets_masterasset, :masterasset_id, :index_subproducts_on_masterproduct_id, :fk_subproducts_masterproduct, :masterasset_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:assetattachments, :asset_id, :index_assetattachments_on_asset_id, :fk_assetattachments_assets, :product_id, :index_productattachments_on_product_id, :fk_productattachments_products, :product_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:inspectionschedules, :asset_id, :index_inspectionschedules_on_asset_id, :fk_inspectionschedules_assets, :product_id, :index_inspectionschedules_on_product_id, :fk_inspectionschedules_products, :product_id, :assets, :id, "BIGINT(20) NOT NULL")
    change_column(:lineitems, :assetcode, :index_lineitems_on_assetcode, nil, :productcode, :index_lineitems_on_productcode, nil, :productcode, :assets, :id, "varchar(255) COLLATE utf8_unicode_ci NOT NULL")
    change_column(:catalogs_assettypes, :publishedassettypes_id, :index_catalogs_assettypes_on_publishedassettypes_id, :fk_catalogs_assettypes_assettypes, :publishedproducttypes_id, :index_catalogs_producttypes_on_publishedproducttypes_id, :fk_catalogs_producttypes_producttypes, :publishedproducttypes_id, :assettypes, :id, "BIGINT(20) NOT NULL")

    execute "alter table notificationsettings_assettypes change column assettype_id producttype_id bigint(20) not null"
    execute "alter table setupdatalastmoddates change column assettypes producttypes datetime not null"
  end

  def self.change_column(table, old_column_name, old_index_name, old_fk_name, new_column_name, new_index_name, new_fk_name, fk_column, fk_target_table, fk_target_column, column_def = "BIGINT(20)")
    execute "alter table #{table} drop foreign key #{old_fk_name}" unless old_fk_name.nil?
    execute "alter table #{table} drop key #{old_index_name}"
    execute "alter table #{table} change column #{old_column_name} #{new_column_name} #{column_def}"
    add_index table, [new_column_name], :name => new_index_name
    execute "alter table #{table} add foreign key #{new_fk_name} (#{fk_column}) references #{fk_target_table} (#{fk_target_column}) on delete no action on update no action" unless new_fk_name.nil?
  end

end
class ChangeTablesFromProductToAsset < ActiveRecord::Migration

  def self.up
    rename_table(:products, :assets)
    rename_table(:productstatus, :assetstatus)
    rename_table(:productattachments, :assetattachments)
    rename_table(:productcodemapping, :assetcodemapping)
    rename_table(:productcodemapping_infooption, :assetcodemapping_infooption)
    rename_table(:productserial_infooption, :asset_infooption)
    rename_table(:productserialextension, :assetextension)
    rename_table(:productserialextensionvalue, :assetextensionvalue)
    rename_table(:producttypegroups, :assettypegroups)
    rename_table(:producttypes, :assettypes)
    rename_table(:producttypes_fileattachments, :assettypes_fileattachments)
    rename_table(:producttypes_producttypes, :assettypes_assettypes)
    rename_table(:producttypeschedules, :assettypeschedules)

    rename_table(:addproducthistory, :addassethistory)
    rename_table(:addproducthistory_infooption, :addassethistory_infooption)

    rename_table(:catalogs_producttypes, :catalogs_assettypes)

    rename_table(:findproductoption, :findassetoption)
    rename_table(:findproductoption_manufacture, :findassetoption_manufacture)

    rename_table(:notificationsettings_producttypes, :notificationsettings_assettypes)

    rename_table(:projects_products, :projects_assets)

    rename_table(:subproducts, :subassets)
  end

  def self.down
    rename_table(:assets, :products)
    rename_table(:assetstatus, :productstatus)
    rename_table(:assetattachments, :assetattachments)
    rename_table(:assetcodemapping, :productcodemapping)
    rename_table(:assetcodemapping_infooption, :productcodemapping_infooption)
    rename_table(:asset_infooption, :productserial_infooption)
    rename_table(:asset_extension, :productserial_extension)
    rename_table(:asset_extensionvalue, :productserial_extensionvalue)
    rename_table(:assettypegroups, :producttypegroups)
    rename_table(:assettypes, :producttypes)
    rename_table(:assettypes_fileattachments, :producttypes_fileattachments)
    rename_table(:assettypes_assettypes, :producttypes_producttypes)
    rename_table(:assettypeschedules, :producttypeschedules)

    rename_table(:addassethistory, :addproducthistory)
    rename_table(:addassethistory_infooption, :addproducthistory_infooption)

    rename_table(:catalogs_assettypes, :catalogs_producttypes)

    rename_table(:findassetoption, :findproductoption)
    rename_table(:findassetoption_manufacture, :findproductoption_manufacture)

    rename_table(:notificationsettings_assettypes, :notificationsettings_producttypes)

    rename_table(:projects_assets, :projects_products)

    rename_table(:subassetss, :subproducts)
  end

end
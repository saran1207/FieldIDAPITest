var asset = new Object();

asset.id = ${asset.id};
asset.identifier = "${asset.identifier?default("")?js_string}";
asset.rfidNumber = "${asset.rfidNumber?default("")?js_string}";
asset.owner = "${asset.owner.internalOrg.displayName?default("")?js_string}";
asset.type = "${asset.type.displayName?default("")?js_string}";
asset.referenceNumber = "${asset.customerRefNumber?default('')?js_string}";

updateLinkedAssetInfo(asset);
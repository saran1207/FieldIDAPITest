var product = new Object();

product.id = ${asset.id};
product.serialNumber = "${asset.serialNumber?default("")?js_string}";
product.rfidNumber = "${asset.rfidNumber?default("")?js_string}";
product.owner = "${asset.owner.internalOrg.displayName?default("")?js_string}";
product.type = "${asset.type.displayName?default("")?js_string}";
product.referenceNumber = "${asset.customerRefNumber?default('')?js_string}";

updateLinkedProductInfo(product);
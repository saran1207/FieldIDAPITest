var product = new Object();

product.id = ${product.id};
product.serialNumber = "${product.serialNumber?default("")?js_string}";
product.rfidNumber = "${product.rfidNumber?default("")?js_string}";
product.owner = "${product.owner.internalOrg.displayName?default("")?js_string}";
product.type = "${product.type.displayName?default("")?js_string}";
product.referenceNumber = "${product.customerRefNumber?default('')?js_string}";
updateLinkedProductInfo(product);
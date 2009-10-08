var product = new Object();

product.id = ${product.id};
product.serialNumber = "${(product.serialNumber?js_string)?default("")}";
product.rfidNumber = "${(product.rfidNumber?js_string)?default("")}";
product.owner = "${(product.owner.internalOrg.displayName?js_string)?default("")}";
product.type = "${(product.type.displayName?js_string)?default("")}";

updateLinkedProductInfo(product);
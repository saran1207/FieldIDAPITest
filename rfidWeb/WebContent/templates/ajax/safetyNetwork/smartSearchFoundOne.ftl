var product = new Object();

product.id = ${product.id};
product.serialNumber = "${(product.serialNumber)?default("")}";
product.rfidNumber = "${(product.rfidNumber)?default("")}";
product.owner = "${(product.owner.displayName)?default("")}";
product.type = "${(product.type.displayName)?default("")}";

updateLinkedProductInfo(product);
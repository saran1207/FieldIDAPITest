-- MySQL dump 10.13  Distrib 5.1.43, for apple-darwin10.2.0 (i386)
--
-- Host: localhost    Database: fieldid_minimal_security
-- ------------------------------------------------------
-- Server version	5.1.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activesessions`
--

DROP TABLE IF EXISTS `activesessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activesessions` (
  `user_id` bigint(21) NOT NULL AUTO_INCREMENT,
  `sessionid` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `lasttouched` datetime NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `index_activesessions_on_user_id` (`user_id`),
  CONSTRAINT `fk_activesessions_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15512622 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activesessions`
--

LOCK TABLES `activesessions` WRITE;
/*!40000 ALTER TABLE `activesessions` DISABLE KEYS */;
INSERT INTO `activesessions` VALUES (15512595,'EBE559EDE9B3B63ECD8AD5CE69691A66','2010-04-14 15:00:57'),(15512596,'85F653E307C7BD107B545BCE7DBB4D19','2010-04-14 16:20:21'),(15512597,'4C3FEE3C6418CD94B57223BF13993C3D','2010-04-14 18:30:37'),(15512598,'7C7F549D38309C8711D9B7F908EE638B.node1','2010-04-05 20:15:05'),(15512599,'8209F0E09CBB9707A6CC84B7A0A87E96.node1','2010-04-01 20:13:46'),(15512600,'9029BD33D2B65FA508169B03148AE553','2010-04-13 18:45:25'),(15512601,'50881AD8EF404DC91CF9B0A6ABBE8CEC.node1','2010-04-01 21:08:50'),(15512602,'0FF16DD9E53222F8ACABB901E996EEA7.node1','2010-04-06 21:24:14'),(15512603,'6DF7963B5B84BB81C561F0AA0235D8FB.node1','2010-04-01 20:44:06'),(15512604,'317B41570B12B0FDD1D2FADA34344994.node1','2010-04-01 20:57:43'),(15512605,'33CAA2D159F6A748FC4EC95E97AADBB3.node1','2010-04-01 20:52:13'),(15512606,'AD94679473F89B0302B525AD7018C006.node1','2010-04-01 21:05:55'),(15512607,'A0AD715B42085A0C5EE6D6E1B4AF1E96.node1','2010-04-01 20:46:48'),(15512608,'1234C206995D3DC45E9EC19E3F852307.node1','2010-04-01 21:00:40'),(15512609,'FFDCF799A1C4ADEB4D78DD377FBFED87.node1','2010-04-01 20:38:40'),(15512610,'6B70C7B5B6FF3A136BACDED46C145919.node1','2010-04-01 21:03:21'),(15512611,'165F42BAA3D1BB36A22EC0CF2A32EACD.node1','2010-04-06 21:21:38'),(15512612,'5681765F9A52F56862D1EC47595B7126.node1','2010-04-01 20:36:01'),(15512613,'82B231DC0DE8E1FFC8C89C4B4F22A19F.node1','2010-04-06 21:19:18'),(15512614,'B0F0D9F586BBD253BDB9CC3682D43415.node1','2010-04-01 20:49:29'),(15512615,'9C30626C60376B3C4D9D9135349FEF03.node1','2010-04-06 21:16:51'),(15512616,'DBEE868FCE2BC4579FBF08AA2DF30427.node1','2010-04-06 21:24:26'),(15512617,'CBF3C28EA754CDF94447DD4F4E2DCC29.node1','2010-04-01 20:41:20'),(15512618,'A456F833E0F6C3696349AD3154DEB08C.node1','2010-04-01 20:54:59'),(15512619,'10DC12C7F0D39F1BCD477B02F7C21407.node1','2010-04-01 21:11:24'),(15512620,'32AE13C2E51DFB72E78B4A4A3277B177.node1','2010-04-01 21:13:57'),(15512621,'09BD2B20FF88D8280ADEDCE7E1615613','2010-04-14 18:31:56');
/*!40000 ALTER TABLE `activesessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `addproducthistory`
--

DROP TABLE IF EXISTS `addproducthistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `addproducthistory` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_fieldiduser` bigint(20) NOT NULL,
  `r_producttype` bigint(20) DEFAULT NULL,
  `r_productstatus` bigint(20) DEFAULT NULL,
  `purchaseorder` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `assigneduser_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `addproducthistory_oneuser` (`r_fieldiduser`),
  KEY `index_addproducthistory_on_assigneduser_id` (`assigneduser_id`),
  KEY `index_addproducthistory_on_r_productstatus` (`r_productstatus`),
  KEY `index_addproducthistory_on_r_producttype` (`r_producttype`),
  KEY `fk_addproducthistory_owner` (`owner_id`),
  KEY `fk_addproducthistory_tenants` (`tenant_id`),
  CONSTRAINT `fk_addproducthistory_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_addproducthistory_productstatus` FOREIGN KEY (`r_productstatus`) REFERENCES `productstatus` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_addproducthistory_producttypes` FOREIGN KEY (`r_producttype`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_addproducthistory_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_addproducthistory_users` FOREIGN KEY (`assigneduser_id`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addproducthistory`
--

LOCK TABLES `addproducthistory` WRITE;
/*!40000 ALTER TABLE `addproducthistory` DISABLE KEYS */;
INSERT INTO `addproducthistory` VALUES (1,15512599,6798,486,'','Good Place',NULL,15539068,NULL),(2,15512595,6799,476,'','Vendor store',NULL,15539090,NULL),(3,15512597,6797,NULL,'','',NULL,15539066,NULL);
/*!40000 ALTER TABLE `addproducthistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `addproducthistory_infooption`
--

DROP TABLE IF EXISTS `addproducthistory_infooption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `addproducthistory_infooption` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_addproducthistory` bigint(20) NOT NULL,
  `r_infooption` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_addproducthistory_infooption_on_r_addproducthistory` (`r_addproducthistory`),
  KEY `index_addproducthistory_infooption_on_r_infooption` (`r_infooption`),
  CONSTRAINT `fk_addproducthistory_infooption_addproducthistory` FOREIGN KEY (`r_addproducthistory`) REFERENCES `addproducthistory` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_addproducthistory_infooption_infooption` FOREIGN KEY (`r_infooption`) REFERENCES `infooption` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addproducthistory_infooption`
--

LOCK TABLES `addproducthistory_infooption` WRITE;
/*!40000 ALTER TABLE `addproducthistory_infooption` DISABLE KEYS */;
INSERT INTO `addproducthistory_infooption` VALUES (41,2,27),(42,2,3),(43,2,1),(44,2,28);
/*!40000 ALTER TABLE `addproducthistory_infooption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `addressinfo`
--

DROP TABLE IF EXISTS `addressinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `addressinfo` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `streetaddress` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `country` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `phone2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_addressinfo_on_modifiedby` (`modifiedby`),
  CONSTRAINT `fk_addressinfo_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12460 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addressinfo`
--

LOCK TABLES `addressinfo` WRITE;
/*!40000 ALTER TABLE `addressinfo` DISABLE KEYS */;
INSERT INTO `addressinfo` VALUES (12432,'2010-02-01 15:49:06','2010-02-01 15:49:06','',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(12433,'2010-02-01 16:05:46','2010-02-01 16:05:47','179 john street','Toronto','ON','Canada','M6J 1R2','647-202-2789',NULL,NULL,NULL),(12434,'2010-02-01 16:09:11','2010-02-01 16:09:11','179 john street','Toronto','ON','Canada','M6J 1R2','647-202-2789',NULL,NULL,NULL),(12435,'2010-02-01 16:09:11','2010-02-01 16:09:11','179 john street','Toronto','ON','Canada','M6J 1R2','647-202-2789',NULL,NULL,NULL),(12436,'2010-03-30 20:00:14','2010-03-30 20:23:25','179 john street','Toronto','ON','Canada','M6J 1R2','416-555-6464',NULL,NULL,NULL),(12437,'2010-03-30 20:03:38','2010-03-30 20:03:38','','','','','','','',NULL,NULL),(12438,'2010-03-30 20:03:49','2010-03-30 20:03:49','','','','','','','',NULL,NULL),(12439,'2010-03-30 20:04:33','2010-03-30 20:04:33','','','','','','','',NULL,''),(12440,'2010-03-30 20:04:45','2010-03-30 20:04:45','','','','','','','',NULL,''),(12441,'2010-03-30 20:05:38','2010-03-30 20:05:38','','','','','','','',NULL,''),(12442,'2010-03-30 20:05:52','2010-03-30 20:05:52','','','','','','','',NULL,''),(12443,'2010-03-30 20:06:38','2010-03-30 20:06:38','','','','','','','',NULL,''),(12444,'2010-03-30 20:06:53','2010-03-30 20:06:53','','','','','','','',NULL,''),(12445,'2010-03-30 20:07:35','2010-03-30 20:07:35','','','','','','','',NULL,''),(12446,'2010-03-30 20:07:58','2010-03-30 20:07:58','','','','','','','',NULL,''),(12447,'2010-03-30 20:08:13','2010-03-30 20:08:13','','','','','','','',NULL,''),(12448,'2010-03-30 20:08:43','2010-03-30 20:08:43','','','','','','','',NULL,''),(12449,'2010-03-30 20:08:59','2010-03-30 20:08:59','','','','','','','',NULL,''),(12450,'2010-03-30 20:09:17','2010-03-30 20:09:17','','','','','','','',NULL,''),(12451,'2010-03-30 20:09:40','2010-03-30 20:09:40','','','','','','','',NULL,''),(12452,'2010-03-30 20:15:49','2010-03-30 20:15:49','','','','','','','',NULL,''),(12453,'2010-03-30 20:16:12','2010-03-30 20:16:12','','','','','','','',NULL,''),(12454,'2010-03-30 20:21:48','2010-03-30 20:21:48','','','','','','','',NULL,''),(12455,'2010-03-30 20:22:06','2010-03-30 20:22:06','','','','','','','',NULL,''),(12456,'2010-03-30 20:22:26','2010-03-30 20:22:26','','','','','','','',NULL,''),(12457,'2010-03-31 15:00:55','2010-03-31 15:00:55','','','','','','','',NULL,''),(12458,'2010-04-14 14:34:13','2010-04-14 14:34:13','','','','','','','',NULL,NULL),(12459,'2010-04-14 17:15:01','2010-04-14 17:15:01','','','','','','','',NULL,NULL);
/*!40000 ALTER TABLE `addressinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alertstatus`
--

DROP TABLE IF EXISTS `alertstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alertstatus` (
  `tenant_id` bigint(20) DEFAULT NULL,
  `diskspace` bigint(20) NOT NULL,
  `assets` bigint(20) NOT NULL,
  `secondaryorgs` int(10) NOT NULL,
  UNIQUE KEY `idx_alertstatus_tenantid` (`tenant_id`),
  KEY `index_alertstatus_on_r_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alertstatus`
--

LOCK TABLES `alertstatus` WRITE;
/*!40000 ALTER TABLE `alertstatus` DISABLE KEYS */;
/*!40000 ALTER TABLE `alertstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `associatedinspectiontypes`
--

DROP TABLE IF EXISTS `associatedinspectiontypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `associatedinspectiontypes` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `inspectiontype_id` bigint(20) NOT NULL,
  `producttype_id` bigint(20) NOT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_associatedinspectiontypes_unique` (`producttype_id`,`inspectiontype_id`),
  KEY `fk_associatedinspectiontypes_inspectiontypes` (`inspectiontype_id`),
  KEY `fk_associatedinspectiontypes_users` (`modifiedby`),
  KEY `fk_associatedinspectiontypes_tenants` (`tenant_id`),
  CONSTRAINT `fk_associatedinspectiontypes_inspectiontypes` FOREIGN KEY (`inspectiontype_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_associatedinspectiontypes_producttypes` FOREIGN KEY (`producttype_id`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_associatedinspectiontypes_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_associatedinspectiontypes_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `associatedinspectiontypes`
--

LOCK TABLES `associatedinspectiontypes` WRITE;
/*!40000 ALTER TABLE `associatedinspectiontypes` DISABLE KEYS */;
INSERT INTO `associatedinspectiontypes` VALUES (1,'2010-03-30 21:30:45','2010-03-30 21:30:45',NULL,1,6798,15511597),(2,'2010-03-31 14:55:21','2010-03-31 14:55:21',NULL,2,6799,15511595);
/*!40000 ALTER TABLE `associatedinspectiontypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autoattributecriteria`
--

DROP TABLE IF EXISTS `autoattributecriteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autoattributecriteria` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_producttype` bigint(20) NOT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_autoattributecriteria_on_modifiedby` (`modifiedby`),
  KEY `index_autoattributecriteria_on_r_producttype` (`r_producttype`),
  KEY `index_autoattributecriteria_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_autoattributecriteria_producttypes` FOREIGN KEY (`r_producttype`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_autoattributecriteria_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autoattributecriteria`
--

LOCK TABLES `autoattributecriteria` WRITE;
/*!40000 ALTER TABLE `autoattributecriteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `autoattributecriteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autoattributecriteria_inputinfofield`
--

DROP TABLE IF EXISTS `autoattributecriteria_inputinfofield`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autoattributecriteria_inputinfofield` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_infofield` bigint(20) NOT NULL,
  `r_autoattributecriteria` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_autoattributecriteria_inputinfofield_on_r_autoattributecr` (`r_autoattributecriteria`),
  KEY `index_autoattributecriteria_inputinfofield_on_r_infofield` (`r_infofield`),
  CONSTRAINT `fk_autoattributecriteria_inputinfofield_autoattributecriteria` FOREIGN KEY (`r_autoattributecriteria`) REFERENCES `autoattributecriteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_autoattributecriteria_inputinfofield_infofield` FOREIGN KEY (`r_infofield`) REFERENCES `infofield` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autoattributecriteria_inputinfofield`
--

LOCK TABLES `autoattributecriteria_inputinfofield` WRITE;
/*!40000 ALTER TABLE `autoattributecriteria_inputinfofield` DISABLE KEYS */;
/*!40000 ALTER TABLE `autoattributecriteria_inputinfofield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autoattributecriteria_outputinfofield`
--

DROP TABLE IF EXISTS `autoattributecriteria_outputinfofield`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autoattributecriteria_outputinfofield` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_infofield` bigint(20) NOT NULL,
  `r_autoattributecriteria` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_autoattributecriteria_outputinfofield_on_r_autoattributec` (`r_autoattributecriteria`),
  KEY `index_autoattributecriteria_outputinfofield_on_r_infofield` (`r_infofield`),
  CONSTRAINT `fk_autoattributecriteria_outputinfofield_autoattributecriteria` FOREIGN KEY (`r_autoattributecriteria`) REFERENCES `autoattributecriteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_autoattributecriteria_outputinfofield_infofield` FOREIGN KEY (`r_infofield`) REFERENCES `infofield` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autoattributecriteria_outputinfofield`
--

LOCK TABLES `autoattributecriteria_outputinfofield` WRITE;
/*!40000 ALTER TABLE `autoattributecriteria_outputinfofield` DISABLE KEYS */;
/*!40000 ALTER TABLE `autoattributecriteria_outputinfofield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autoattributedefinition`
--

DROP TABLE IF EXISTS `autoattributedefinition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autoattributedefinition` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_autoattributecriteria` bigint(20) NOT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_autoattributedefinition_on_modifiedby` (`modifiedby`),
  KEY `index_autoattributedefinition_on_r_autoattributecriteria` (`r_autoattributecriteria`),
  KEY `index_autoattributedefinition_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_autoattributedefinition_autoattributecriteria` FOREIGN KEY (`r_autoattributecriteria`) REFERENCES `autoattributecriteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_autoattributedefinition_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autoattributedefinition`
--

LOCK TABLES `autoattributedefinition` WRITE;
/*!40000 ALTER TABLE `autoattributedefinition` DISABLE KEYS */;
/*!40000 ALTER TABLE `autoattributedefinition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autoattributedefinition_inputinfooption`
--

DROP TABLE IF EXISTS `autoattributedefinition_inputinfooption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autoattributedefinition_inputinfooption` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_infooption` bigint(20) NOT NULL,
  `r_autoattributedefinition` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_autoattributedefinition_inputinfooption_on_r_autoattribut` (`r_autoattributedefinition`),
  KEY `index_autoattributedefinition_inputinfooption_on_r_infooption` (`r_infooption`),
  CONSTRAINT `fk_autoattributedefinition_inputinfooption_autoattributedefiniti` FOREIGN KEY (`r_autoattributedefinition`) REFERENCES `autoattributedefinition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_autoattributedefinition_inputinfooption_infooption` FOREIGN KEY (`r_infooption`) REFERENCES `infooption` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autoattributedefinition_inputinfooption`
--

LOCK TABLES `autoattributedefinition_inputinfooption` WRITE;
/*!40000 ALTER TABLE `autoattributedefinition_inputinfooption` DISABLE KEYS */;
/*!40000 ALTER TABLE `autoattributedefinition_inputinfooption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autoattributedefinition_outputinfooption`
--

DROP TABLE IF EXISTS `autoattributedefinition_outputinfooption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autoattributedefinition_outputinfooption` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_infooption` bigint(20) NOT NULL,
  `r_autoattributedefinition` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_autoattributedefinition_outputinfooption_on_r_autoattribu` (`r_autoattributedefinition`),
  KEY `index_autoattributedefinition_outputinfooption_on_r_infooption` (`r_infooption`),
  CONSTRAINT `fk_autoattributedefinition_outputinfooption_autoattributedefinit` FOREIGN KEY (`r_autoattributedefinition`) REFERENCES `autoattributedefinition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_autoattributedefinition_outputinfooption_infooption` FOREIGN KEY (`r_infooption`) REFERENCES `infooption` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autoattributedefinition_outputinfooption`
--

LOCK TABLES `autoattributedefinition_outputinfooption` WRITE;
/*!40000 ALTER TABLE `autoattributedefinition_outputinfooption` DISABLE KEYS */;
/*!40000 ALTER TABLE `autoattributedefinition_outputinfooption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogs`
--

DROP TABLE IF EXISTS `catalogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalogs` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_catalogs_on_r_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogs`
--

LOCK TABLES `catalogs` WRITE;
/*!40000 ALTER TABLE `catalogs` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogs_inspectiontypes`
--

DROP TABLE IF EXISTS `catalogs_inspectiontypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalogs_inspectiontypes` (
  `catalogs_id` bigint(20) NOT NULL,
  `publishedinspectiontypes_id` bigint(20) NOT NULL,
  UNIQUE KEY `index_catalogs_inspectiontypes_on_catalogs_id_and_publishedinsp` (`catalogs_id`,`publishedinspectiontypes_id`),
  UNIQUE KEY `index_catalogs_inspectiontypes_on_publishedinspectiontypes_id` (`publishedinspectiontypes_id`),
  KEY `index_catalogs_inspectiontypes_on_catalogs_id` (`catalogs_id`),
  CONSTRAINT `fk_catalogs_inspectiontypes_catalogs` FOREIGN KEY (`catalogs_id`) REFERENCES `catalogs` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_catalogs_inspectiontypes_inspectiontypes` FOREIGN KEY (`publishedinspectiontypes_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogs_inspectiontypes`
--

LOCK TABLES `catalogs_inspectiontypes` WRITE;
/*!40000 ALTER TABLE `catalogs_inspectiontypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalogs_inspectiontypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogs_producttypes`
--

DROP TABLE IF EXISTS `catalogs_producttypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalogs_producttypes` (
  `catalogs_id` bigint(20) NOT NULL,
  `publishedproducttypes_id` bigint(20) NOT NULL,
  UNIQUE KEY `index_catalogs_producttypes_on_catalogs_id_and_publishedtypes_i` (`catalogs_id`,`publishedproducttypes_id`),
  UNIQUE KEY `index_catalogs_producttypes_on_publishedtypes_id` (`publishedproducttypes_id`),
  KEY `index_catalogs_producttypes_on_catalogs_id` (`catalogs_id`),
  KEY `index_catalogs_producttypes_on_publishedproducttypes_id` (`publishedproducttypes_id`),
  CONSTRAINT `fk_catalogs_producttypes_catalogs` FOREIGN KEY (`catalogs_id`) REFERENCES `catalogs` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_catalogs_producttypes_producttypes` FOREIGN KEY (`publishedproducttypes_id`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogs_producttypes`
--

LOCK TABLES `catalogs_producttypes` WRITE;
/*!40000 ALTER TABLE `catalogs_producttypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalogs_producttypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commenttemplate`
--

DROP TABLE IF EXISTS `commenttemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commenttemplate` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `datecreated` datetime DEFAULT NULL,
  `datemodified` datetime DEFAULT NULL,
  `modifiedby` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `templateid` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contents` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_commenttemplate_on_r_tenant` (`tenant_id`),
  KEY `commenttemplate_idx` (`templateid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commenttemplate`
--

LOCK TABLES `commenttemplate` WRITE;
/*!40000 ALTER TABLE `commenttemplate` DISABLE KEYS */;
/*!40000 ALTER TABLE `commenttemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configurations`
--

DROP TABLE IF EXISTS `configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configurations` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `identifier` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `tenantid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_configurations_on_modifiedby` (`modifiedby`),
  KEY `index_configurations_on_tenantid` (`tenantid`),
  CONSTRAINT `fk_configurations_tenants` FOREIGN KEY (`tenantid`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_configurations_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurations`
--

LOCK TABLES `configurations` WRITE;
/*!40000 ALTER TABLE `configurations` DISABLE KEYS */;
INSERT INTO `configurations` VALUES (15,'2010-03-30 15:39:34','2010-03-30 15:39:34',NULL,'SUBSCRIPTION_AGENT','com.n4systems.subscription.local.LocalSubscriptionAgent',NULL);
/*!40000 ALTER TABLE `configurations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contractpricings`
--

DROP TABLE IF EXISTS `contractpricings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contractpricings` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `signuppackage` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `paymentoption` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `priceperuserpermonth` float DEFAULT NULL,
  `externalid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contractpricings`
--

LOCK TABLES `contractpricings` WRITE;
/*!40000 ALTER TABLE `contractpricings` DISABLE KEYS */;
INSERT INTO `contractpricings` VALUES (16,'2010-02-01 15:18:11','2010-02-01 15:18:11',NULL,'Plus','ONE_YEAR_UP_FRONT',100,143),(17,'2010-02-01 15:18:11','2010-02-02 18:49:51',NULL,'Unlimited','ONE_YEAR_UP_FRONT',225.5,151),(18,'2010-02-01 15:18:11','2010-02-01 15:18:11',NULL,'Basic','ONE_YEAR_UP_FRONT',40,146),(19,'2010-02-01 15:18:11','2010-02-01 15:18:11',NULL,'Free','ONE_YEAR_UP_FRONT',0,134),(20,'2010-02-01 15:18:11','2010-02-02 18:49:51',NULL,'Enterprise','ONE_YEAR_UP_FRONT',175,149),(21,'2010-02-01 15:18:11','2010-02-02 18:49:51',NULL,'Plus','TWO_YEARS_UP_FRONT',100,159),(22,'2010-02-01 15:18:11','2010-02-02 18:49:52',NULL,'Unlimited','TWO_YEARS_UP_FRONT',202.1,160),(23,'2010-02-01 15:18:11','2010-02-02 18:49:50',NULL,'Basic','TWO_YEARS_UP_FRONT',40,157),(24,'2010-02-01 15:18:11','2010-02-01 15:18:11',NULL,'Free','TWO_YEARS_UP_FRONT',0,134),(25,'2010-02-01 15:18:11','2010-02-02 18:49:51',NULL,'Enterprise','TWO_YEARS_UP_FRONT',175,158);
/*!40000 ALTER TABLE `contractpricings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteria`
--

DROP TABLE IF EXISTS `criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteria` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `displaytext` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `states_id` bigint(20) NOT NULL,
  `principal` tinyint(1) NOT NULL DEFAULT '0',
  `retired` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index_criteria_on_modifiedby` (`modifiedby`),
  KEY `index_criteria_on_r_tenant` (`tenant_id`),
  KEY `index_criteria_on_states_id` (`states_id`),
  CONSTRAINT `fk_criteria_statesets` FOREIGN KEY (`states_id`) REFERENCES `statesets` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_criteria_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteria`
--

LOCK TABLES `criteria` WRITE;
/*!40000 ALTER TABLE `criteria` DISABLE KEYS */;
INSERT INTO `criteria` VALUES (1,15511595,'2010-03-31 14:54:59','2010-03-31 14:54:59',NULL,'First question',343,1,0),(2,15511595,'2010-03-31 14:54:59','2010-03-31 14:54:59',NULL,'Second question',343,0,0),(3,15511595,'2010-03-31 14:54:59','2010-03-31 14:54:59',NULL,'Thrid Question',344,0,0);
/*!40000 ALTER TABLE `criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteria_deficiencies`
--

DROP TABLE IF EXISTS `criteria_deficiencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteria_deficiencies` (
  `criteria_id` bigint(20) NOT NULL,
  `text` varchar(511) COLLATE utf8_unicode_ci NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `fk_criteria_deficiencies_criteria` (`criteria_id`),
  CONSTRAINT `fk_criteria_deficiencies_criteria` FOREIGN KEY (`criteria_id`) REFERENCES `criteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteria_deficiencies`
--

LOCK TABLES `criteria_deficiencies` WRITE;
/*!40000 ALTER TABLE `criteria_deficiencies` DISABLE KEYS */;
/*!40000 ALTER TABLE `criteria_deficiencies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteria_recommendations`
--

DROP TABLE IF EXISTS `criteria_recommendations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteria_recommendations` (
  `criteria_id` bigint(20) NOT NULL,
  `text` varchar(511) COLLATE utf8_unicode_ci NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `fk_criteria_recommendations_criteria` (`criteria_id`),
  CONSTRAINT `fk_criteria_recommendations_criteria` FOREIGN KEY (`criteria_id`) REFERENCES `criteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteria_recommendations`
--

LOCK TABLES `criteria_recommendations` WRITE;
/*!40000 ALTER TABLE `criteria_recommendations` DISABLE KEYS */;
/*!40000 ALTER TABLE `criteria_recommendations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteriaresults`
--

DROP TABLE IF EXISTS `criteriaresults`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteriaresults` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `state_id` bigint(20) NOT NULL,
  `criteria_id` bigint(20) NOT NULL,
  `inspection_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_criteriaresults_on_criteria_id` (`criteria_id`),
  KEY `index_criteriaresults_on_inspection_id` (`inspection_id`),
  KEY `index_criteriaresults_on_modifiedby` (`modifiedby`),
  KEY `index_criteriaresults_on_r_tenant` (`tenant_id`),
  KEY `index_criteriaresults_on_state_id` (`state_id`),
  CONSTRAINT `fk_criteriaresults_criteria` FOREIGN KEY (`criteria_id`) REFERENCES `criteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_criteriaresults_inspections` FOREIGN KEY (`inspection_id`) REFERENCES `inspections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_criteriaresults_states` FOREIGN KEY (`state_id`) REFERENCES `states` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_criteriaresults_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteriaresults`
--

LOCK TABLES `criteriaresults` WRITE;
/*!40000 ALTER TABLE `criteriaresults` DISABLE KEYS */;
INSERT INTO `criteriaresults` VALUES (1,15511595,'2010-04-13 18:08:55','2010-04-13 18:08:55',NULL,1094,1,22),(2,15511595,'2010-04-13 18:08:55','2010-04-13 18:08:55',NULL,1094,2,22),(3,15511595,'2010-04-13 18:08:55','2010-04-13 18:08:55',NULL,1096,3,22),(4,15511595,'2010-04-13 21:43:30','2010-04-13 21:43:30',NULL,1094,2,23),(5,15511595,'2010-04-13 21:43:30','2010-04-13 21:43:30',NULL,1096,3,23),(6,15511595,'2010-04-13 21:43:30','2010-04-13 21:43:30',NULL,1094,1,23),(7,15511595,'2010-04-14 14:34:53','2010-04-14 14:34:53',NULL,1094,1,24),(8,15511595,'2010-04-14 14:34:53','2010-04-14 14:34:53',NULL,1096,3,24),(9,15511595,'2010-04-14 14:34:53','2010-04-14 14:34:53',NULL,1094,2,24),(10,15511595,'2010-04-14 14:50:32','2010-04-14 14:50:32',NULL,1094,2,25),(11,15511595,'2010-04-14 14:50:32','2010-04-14 14:50:32',NULL,1096,3,25),(12,15511595,'2010-04-14 14:50:32','2010-04-14 14:50:32',NULL,1094,1,25),(13,15511595,'2010-04-14 14:59:40','2010-04-14 14:59:40',NULL,1094,1,26),(14,15511595,'2010-04-14 14:59:40','2010-04-14 14:59:40',NULL,1094,2,26),(15,15511595,'2010-04-14 14:59:40','2010-04-14 14:59:40',NULL,1096,3,26),(16,15511595,'2010-04-14 15:00:18','2010-04-14 15:00:18',NULL,1094,1,27),(17,15511595,'2010-04-14 15:00:18','2010-04-14 15:00:18',NULL,1096,3,27),(18,15511595,'2010-04-14 15:00:18','2010-04-14 15:00:18',NULL,1094,2,27),(19,15511595,'2010-04-14 15:00:52','2010-04-14 15:00:52',NULL,1096,3,28),(20,15511595,'2010-04-14 15:00:52','2010-04-14 15:00:52',NULL,1094,2,28),(21,15511595,'2010-04-14 15:00:52','2010-04-14 15:00:52',NULL,1094,1,28);
/*!40000 ALTER TABLE `criteriaresults` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteriaresults_deficiencies`
--

DROP TABLE IF EXISTS `criteriaresults_deficiencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteriaresults_deficiencies` (
  `criteriaresults_id` bigint(20) NOT NULL,
  `deficiencies_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `index_criteriaresults_deficiencies_on_criteriaresults_id_and_de` (`criteriaresults_id`,`deficiencies_id`),
  KEY `index_criteriaresults_deficiencies_on_criteriaresults_id` (`criteriaresults_id`),
  KEY `index_criteriaresults_deficiencies_on_deficiencies_id` (`deficiencies_id`),
  CONSTRAINT `fk_criteriaresults_deficiencies_criteriaresults` FOREIGN KEY (`criteriaresults_id`) REFERENCES `criteriaresults` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_criteriaresults_deficiencies_observations` FOREIGN KEY (`deficiencies_id`) REFERENCES `observations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteriaresults_deficiencies`
--

LOCK TABLES `criteriaresults_deficiencies` WRITE;
/*!40000 ALTER TABLE `criteriaresults_deficiencies` DISABLE KEYS */;
/*!40000 ALTER TABLE `criteriaresults_deficiencies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteriaresults_recommendations`
--

DROP TABLE IF EXISTS `criteriaresults_recommendations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteriaresults_recommendations` (
  `criteriaresults_id` bigint(20) NOT NULL,
  `recommendations_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `index_criteriaresults_recommendations_on_criteriaresults_id_and` (`criteriaresults_id`,`recommendations_id`),
  KEY `index_criteriaresults_recommendations_on_criteriaresults_id` (`criteriaresults_id`),
  KEY `index_criteriaresults_recommendations_on_recommendations_id` (`recommendations_id`),
  CONSTRAINT `fk_criteriaresults_recommendations_criteriaresults` FOREIGN KEY (`criteriaresults_id`) REFERENCES `criteriaresults` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_criteriaresults_recommendations_observations` FOREIGN KEY (`recommendations_id`) REFERENCES `observations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteriaresults_recommendations`
--

LOCK TABLES `criteriaresults_recommendations` WRITE;
/*!40000 ALTER TABLE `criteriaresults_recommendations` DISABLE KEYS */;
/*!40000 ALTER TABLE `criteriaresults_recommendations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteriasections`
--

DROP TABLE IF EXISTS `criteriasections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteriasections` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `retired` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index_criteriasections_on_modifiedby` (`modifiedby`),
  KEY `index_criteriasections_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_criteriasections_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteriasections`
--

LOCK TABLES `criteriasections` WRITE;
/*!40000 ALTER TABLE `criteriasections` DISABLE KEYS */;
INSERT INTO `criteriasections` VALUES (1,15511595,'2010-03-31 14:54:59','2010-03-31 14:54:59',NULL,'Section 1',0),(2,15511595,'2010-03-31 14:54:59','2010-03-31 14:54:59',NULL,'Section 2',0);
/*!40000 ALTER TABLE `criteriasections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criteriasections_criteria`
--

DROP TABLE IF EXISTS `criteriasections_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `criteriasections_criteria` (
  `criteriasections_id` bigint(20) NOT NULL,
  `criteria_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  UNIQUE KEY `criteriasections_criteria_criteria_id_key` (`criteria_id`),
  KEY `index_criteriasections_criteria_on_criteria_id` (`criteria_id`),
  KEY `fk_criteriasections_criteria_criteriasections` (`criteriasections_id`),
  CONSTRAINT `fk_criteriasections_criteria_criteria` FOREIGN KEY (`criteria_id`) REFERENCES `criteria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_criteriasections_criteria_criteriasections` FOREIGN KEY (`criteriasections_id`) REFERENCES `criteriasections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criteriasections_criteria`
--

LOCK TABLES `criteriasections_criteria` WRITE;
/*!40000 ALTER TABLE `criteriasections_criteria` DISABLE KEYS */;
INSERT INTO `criteriasections_criteria` VALUES (1,1,0),(1,2,1),(2,3,0);
/*!40000 ALTER TABLE `criteriasections_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `downloads`
--

DROP TABLE IF EXISTS `downloads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `downloads` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `contenttype` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `state` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_downloads_users` (`modifiedby`),
  KEY `fk_downloads_tenants` (`tenant_id`),
  KEY `fk_downloads_owner` (`user_id`),
  CONSTRAINT `fk_downloads_owner` FOREIGN KEY (`user_id`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_downloads_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_downloads_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `downloads`
--

LOCK TABLES `downloads` WRITE;
/*!40000 ALTER TABLE `downloads` DISABLE KEYS */;
/*!40000 ALTER TABLE `downloads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eulaacceptances`
--

DROP TABLE IF EXISTS `eulaacceptances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eulaacceptances` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `acceptor_uniqueid` bigint(20) NOT NULL,
  `eula_id` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_eulaacceptances_on_acceptor_uniqueid` (`acceptor_uniqueid`),
  KEY `index_eulaacceptances_on_eula_id` (`eula_id`),
  KEY `index_eulaacceptances_on_modifiedby` (`modifiedby`),
  KEY `index_eulaacceptances_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_eulaacceptances_eulas` FOREIGN KEY (`eula_id`) REFERENCES `eulas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_eulaacceptances_modfied_user` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_eulaacceptances_users` FOREIGN KEY (`acceptor_uniqueid`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eulaacceptances`
--

LOCK TABLES `eulaacceptances` WRITE;
/*!40000 ALTER TABLE `eulaacceptances` DISABLE KEYS */;
INSERT INTO `eulaacceptances` VALUES (62,'2010-02-01 16:33:22','2010-02-01 16:33:22',NULL,15511596,15512597,1,'2010-02-01 16:33:22'),(63,'2010-03-30 20:35:24','2010-03-30 20:35:24',NULL,15511597,15512599,1,'2010-03-30 20:35:24'),(64,'2010-03-31 14:50:00','2010-03-31 14:50:00',NULL,15511595,15512595,1,'2010-03-31 14:50:00');
/*!40000 ALTER TABLE `eulaacceptances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eulas`
--

DROP TABLE IF EXISTS `eulas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eulas` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `legaltext` text COLLATE utf8_unicode_ci NOT NULL,
  `effectivedate` datetime NOT NULL,
  `version` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eulas`
--

LOCK TABLES `eulas` WRITE;
/*!40000 ALTER TABLE `eulas` DISABLE KEYS */;
INSERT INTO `eulas` VALUES (1,'2009-08-09 12:14:13','2009-08-09 12:14:13',NULL,'N4 SYSTEMS PRODUCT\nLICENSE AGREEMENT\n\nBY CLICKING THE \"I ACCEPT\" BOX, OR DOWNLOADING OR INSTALLING OR USING THE FIELD ID SERVICE AND/OR THE SOFTWARE, AS APPLICABLE, THAT YOU HAVE SELECTED IN THE ORDERING PROCESS, AS DEFINED BELOW, YOU ARE AGREEING ON BEHALF OF THE ENTITY USING THE SERVICE AND/OR THE SOFTWARE, AS APPLICABLE (\"LICENSEE\") THAT COMPANY WILL BE BOUND BY AND IS BECOMING A PARTY TO THIS N4 SYSTEMS PRODUCT LICENSE AGREEMENT (\"AGREEMENT\") AND THAT YOU HAVE THE AUTHORITY TO BIND COMPANY. IF COMPANY DOES NOT AGREE TO ALL OF THE TERMS OF THIS AGREEMENT, DO NOT SELECT THE \"I ACCEPT\" BOX AND DO NOT USE THE SERVICE AND/OR THE SOFTWARE, AS APPLICABLE. COMPANY HAS NOT BECOME A LICENSEE OF, AND IS NOT AUTHORIZED TO USE THE SERVICE AND/OR THE SOFTWARE, AS APPLICABLE UNLESS AND UNTIL IT HAS AGREED TO BE BOUND BY THESE TERMS. THE \"EFFECTIVE DATE\" FOR THIS AGREEMENT SHALL BE THE DAY YOU CHECK THE \"I ACCEPT\" BOX.\n\nWHEREAS N4 SYSTEMS INC. (“N4 Systems”) has developed the N4 Systems Product which is comprised of a unique software architecture together with additional optional software modules which may be licensed from N4 Systems from time to time;\n\nAND WHEREAS N4 Systems has agreed to grant Company (the ”Licensee”) certain rights to use the N4 Systems Product on the terms and conditions herein set forth;\n\nNOW THEREFORE the parties hereto agree as follows:\n\n\n1. DEFINITIONS\nUnless otherwise specified the following terms will have the meanings ascribed to them as follows:\n\n1.1. “N4 Systems Product“ shall mean the computer software and software solutions, known commonly as “Field ID,” used in connection with the inspection, maintenance, service and repair of equipment (known commercially and referred to herein as “Field ID™”), in machine readable object code form, which may be combined or embodied in any medium whatsoever, consisting of a set of logical instructions and information which guide the functioning of a processor, and which shall include Updates and Upgrades provided from time to time by N4 Systems during the Term, and all information, ephemeral aspects, so-called “look & feel”, graphic design, user interface design, know how, systems and processes concerning such computer program, including without limitation such computer program\'s operational and functional specifications set out in documentation provided or made available by N4 Systems with the N4 Systems Product.\n\n1.2. “Business Day“ shall mean Monday to Friday other than statutory holidays in the province of Ontario, Canada.\n\n1.3. “Confidential Information” shall mean confidential and secret information including, but not limited to, trade secrets, processes, methods, ideas, algorithms plans, software source code, technical specifications, engineering data, computer software programs, manufacturing know-how, or other information relating to, incorporated in or forming part of  the N4 Systems Product, the product specifications and Documentation and any other information which would be reasonably considered to be confidential, including the terms of this Agreement.\n\n1.4. “Documentation“ means the human-readable documentation that is delivered with the N4 Systems Product.\n\n1.5. “Effective Date”  means the date referred to on the last page of this Agreement.\n\n1.6. “License Fee” shall mean the amount payable to N4 Systems in consideration of the grant of any license for the N4 Systems Product as agreed upon between the parties.\n\n1.7. “Maintenance and Support Services“  shall have the meaning set out in Section hereof.\n\n1.8. “Maintenance Fee“ shall mean the fees payable by Licensee, as agreed upon from time to time, in connection with the provision by N4 Systems of the Maintenance and Support Services.\n\n1.9. “Term“ shall have the meaning set out in Section  hereof.\n\n1.10. “Update” shall mean a release of the N4 Systems Product which consists of minor corrections, bug fixes and minor enhancements to the previous version released by N4 Systems to Licensee.\n\n1.11. “Upgrade” shall mean a release of the N4 Systems Product which consists of a new version with substantial enhancements, added functionality or new features from the previous version released by N4 Systems to Licensee.\n\n\n2. GRANT AND RESTRICTIONS\n\n2.1. Upon payment of the License Fee, N4 Systems grants Licensee a restricted, non-transferable and non‑exclusive license to use the N4 Systems Product which shall be hosted by N4 Systems and to use the Documentation and N4 Systems Product in object-code format solely for Licensee\'s professional use.  The N4 Systems Product shall not be used outside of Licensee\'s normal course of business.  Licensee may only permit its employees to use the  N4 Systems Product.  Except as expressly provided herein, the Licensee shall not permit third parties to have access to or use the N4 Systems Product.\n\n2.2. Licensee may not download, copy or reproduce  the N4 Systems Product. \n\n2.3. Licensee shall not, and shall not permit any one else to reverse engineer, decompile, disassemble, or otherwise reduce the N4 Systems Product to any human‑readable form.  Licensee shall not modify, adapt, alter, edit, correct, translate, publish, sell, transfer, assign, convey, rent, lease, loan, pledge, sublicense, distribute, export, enhance, or create derivative works based upon the N4 Systems Product, in whole or part, or otherwise grant or transfer rights to the N4 Systems Product or the rights granted herein in any form or by any media (electronic, mechanical, photocopy, recording, or otherwise).  \n\n\n3. LICENSE FEE\n\n3.1. Licensee shall pay the License Fee, payable on the effective date of this Agreement. \n\n3.2. In addition to the License Fee, Licensee shall pay all taxes, however designated, including sales and use taxes, goods and services, duties and tariffs and other governmental charges payable in relation to its license and use of the N4 Systems Product.  \n\n3.3. If the License Fee or any other amount payable to N4 Systems hereunder is not paid when due, interest shall accrue and be payable by the Licensee to N4 Systems at the rate of eighteen (18%) percent per annum, payable monthly.\n\n\n4. TERM OF AGREEMENT\nWith respect to a license for which a License Fee has been paid, this Agreement shall become effective as of the date where Licensee has accepted the terms and conditions of this Agreement and shall remain in effect [in perpetuity] or [for the term mutually agreed upon in writing by the parties] unless earlier terminated in accordance with the terms of this Agreement.  \n\n\n5. Limitation of Warranty\n\n5.1. EXCEPT AS EXPRESSLY STATED IN THIS AGREEMENT, THE N4 SYSTEMS PRODUCT AND DOCUMENTATION ARE PROVIDED ON AN “AS IS“ BASIS AND THERE ARE NO WARRANTIES, REPRESENTATIONS OR CONDITIONS, EXPRESSED OR IMPLIED, WRITTEN OR ORAL, ARISING BY STATUTE, OPERATION OF LAW OR OTHERWISE, REGARDING THE N4 SYSTEMS PRODUCT OR ANY OTHER PRODUCT OR SERVICE PROVIDED HEREUNDER OR IN CONNECTION HEREWITH.  \n\n5.2. N4 SYSTEMS DISCLAIMS ANY IMPLIED WARRANTY OR CONDITION OF MERCHANTABLE QUALITY, MERCHANTABILITY, FUNCTIONALITY, DURABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  \n\n5.3. N4 SYSTEMS DOES NOT WARRANT THAT THE N4 SYSTEMS PRODUCT WILL MEET LICENSEE\'S REQUIREMENTS OR THAT ITS OPERATION WILL BE UNINTERRUPTED,  ERROR FREE OR SECURE BEYOND WHAT IS OTHERWISE WARRANTED IN THE DOCUMENTATION OR THAT ALL CONTENT OR DATA DELIVERED OR PROVIDED UNDER THIS AGREEMENT OR THROUGH USE OF THE N4 SYSTEMS PRODUCT WILL BE APPROPRIATE OR APPLICABLE TO LICENSEE\'S USE, N4 SYSTEMS DISCLAIMS ANY LIABILITY FOR ANY CONSEQUENCES DUE TO USE, MISUSE OR INTERPRETATION OF INFORMATION CONTAINED, OR NOT CONTAINED, IN THE N4 SYSTEMS PRODUCT OR THE DOCUMENTATION. \n\n5.4. N4 SYSTEMS DOES NOT WARRANT THAT THE N4 SYSTEMS PRODUCT WILL OPERATE WITH ANY OTHER COMPUTER PROGRAM NOT SPECIFIED IN THE DOCUMENTATION.\n\n5.5. NO REPRESENTATION OR OTHER AFFIRMATION OF FACT, INCLUDING BUT NOT LIMITED TO STATEMENTS REGARDING PERFORMANCE OF THE N4 SYSTEMS PRODUCT, WHICH IS NOT CONTAINED IN THIS AGREEMENT, SHALL BE DEEMED TO BE A WARRANTY BY N4 SYSTEMS.  \n\n5.5.1. In exercising its rights under this Agreement, Licensee will give and make no warranties or representations on behalf of N4 Systems as to quality, merchantable quality, fitness for a particular use or purpose or any other features of the N4 Systems Product, or other products or services provided by N4 Systems except as described in published documentation relating to the N4 Systems Product provided by N4 Systems, or other products or services provided by N4 Systems made generally commercially available by N4 Systems.\n\n\n6. LICENSEE OBLIGATIONS AND ACKNOWLEDGEMENTS\n\n6.1. Licensee acknowledges that it is responsible to create back-up records of all information entered by Licensee into the N4 Systems Product.\n\n6.2. If the N4 Systems Product fails to perform in accordance with the Documentation, Licensee shall promptly advise N4 Systems of the defect and shall assist N4 Systems in identifying the defect.\n\n6.3. Licensee acknowledges and agrees that all information and data provided, supplied or entered into the N4 Systems Product may be relied upon by third parties.  Licensee represents and warrants that all information and data provided, supplied or entered into by Licensee into the N4 Systems Product shall be true, correct and complete.\n\n\n7. PRIVACY AND USE OF INFORMATION\n\n7.1. Licensee shall at all times comply with all relevant privacy legislation, regulations and any privacy related professional obligations applicable in the use of the N4 Systems Product and, without limiting the generality of the foregoing, the collection, use and disclosure of data forming part of, incorporated or used in connection with the N4 Systems Product. \n\n7.2. Licensee acknowledges and agrees that N4 Systems may gather, collect, utilize, market and sell  information relating to its clients, usage patterns, usage data, utilized or contributed data provided, entered or supplied by the Licensee in connection with this Agreement (“Usage Data”) provided that any such use shall only be made for internal statistical purposes in an aggregate and anonymous format only.  On termination of this Agreement, N4 Systems shall be entitled to retain and use the Usage Data.\n\n\n8. MAINTENANCE AND TECHNICAL SUPPORT\nIf requested, N4 Systems will provide technical support to Licensee (the “Maintenance and Support Services”) set forth in attached Schedule A, in consideration for the payment by Licensee of the Maintenance Fee.  The Maintenance Support Services shall be provided by N4 Systems for a period to be mutually agreed upon in writing from time to time.\n\n\n9. OWNERSHIP AND CONFIDENTIALITY\n\n9.1. Licensee hereby acknowledges and agrees that all right, title and interest in and to the N4 Systems Product, in whole or in part, and including, without limitation, all patent, copyright, trade-marks, trade secret and all other intellectual and industrial property rights in such N4 Systems Product and the structure, sequence and organization of same, and the media on which such material is contained shall belong to N4 Systems , and that Licensee’s sole rights thereto shall be only those rights granted by N4 Systems pursuant to this Agreement. Licensee further agrees and acknowledges that N4 Systems has and reserves the exclusive, world-wide right in perpetuity to protect the N4 Systems Product and all product specifications including its structure, sequence and organization, screens and any part thereof, under any laws for the protection of intellectual and industrial property, including without limitation, trade secrets, trademarks, copyrights, industrial designs and patents.\n\n9.2. The N4 Systems Product, Documentation, product specifications and all documentation and information, including without limitation, so-called “look and feel” aspects, design and presentation, trade secrets, drawings and technical and marketing information which is or has been supplied by N4 Systems to Licensee, acquired or developed by Licensee is hereby deemed to be Confidential Information and shall be held in trust and confidence for, and on behalf of, N4 Systems, by Licensee and its employees, and shall not be disclosed by Licensee or used by Licensee for any purpose other than as strictly permitted under this Agreement without N4 Systems\' prior written consent.\n\n9.3. Licensee shall treat the Confidential Information in strict confidence and shall not disclose, transfer, copy, reproduce, electronically transmit, store or maintain, remanufacture or in any way duplicate all, or any part of, the Confidential Information except in accordance with the terms and conditions of this Agreement.  Licensee shall be directly liable for the acts or omissions of its employees, agents and contractors with respect to such confidentiality obligations.  Licensee agrees to protect the Confidential Information with the same standard of care and procedures which it uses to protect its own trade secrets, proprietary information and other confidential information and, in any case, not less than a reasonable standard of care.\n\n9.4. The obligations of confidentiality imposed by Section  shall not apply, or shall cease to apply, to any Confidential Information: which at the time of disclosure is within the public domain, other than through a breach of this Agreement; which after disclosure becomes readily and lawfully available to the industry or the public, other than through a breach of this Agreement; which Licensee can establish, by documented and competent evidence, was in its possession prior to the date of disclosure of such Confidential Information by N4 Systems;\n\n9.5. In the event that the Licensee is required by law or direction of a regulatory authority to disclose Confidential Information, the Licensee shall provide N4 Systems with prompt written notice of the requirement to permit N4 Systems to seek a protective order or other appropriate remedy to prevent such disclosure.  In the event that such protective order or other remedy is not obtained, the Licensee shall only disclose such portion of the Confidential Information as may be required to be disclosed.\n\n9.6. Licensee undertakes to comply with its obligations according to the legislation in the jurisdictions in which it carries on business.\n\n\n10. INTELLECTUAL  PROPERTY  RIGHTS  AND INDEMNIFICATION\n\n10.1. Licensee agrees to take adequate steps to protect the N4 Systems Product from unauthorized disclosure or use.  Licensee shall continually use its best efforts to protect N4 Systems\'  trade-marks, trade names, patents, copyrights, and other proprietary rights, but shall not initiate legal action against third parties for infringement thereof. Licensee shall promptly notify N4 Systems of any infringement or improper or unauthorized use of which it has actual knowledge.\n\n10.2. The N4 Systems Product, Documentation and the product specifications are copyrighted and title to all copies is retained by N4 Systems.   Licensee will not alter, remove, cover or otherwise obscure any copyright notices, trade‑mark notices and any other intellectual property rights attaching to or displayed on the N4 Systems Product,  Documentation and any other material and documentation made available under this Agreement.  The Licensee will comply with all reasonable directions issued by N4 Systems from time to time regarding the form and placement of any and all relevant proprietary rights notices in or on the N4 Systems Product, or Documentation or any other related media, packaging or material.\n\n10.3. N4 Systems shall indemnify Licensee against all claims made against Licensee alleging that any use of the N4 Systems Product or Documentation constitutes an infringement of copyright, patent, trade-mark or trade secret rights.  N4 Systems shall have exclusive carriage of the defence of any such claim made against Licensee and has the exclusive right to settle the claim.  Licensee shall cooperate fully in the conduct of the defence.  Licensee shall notify N4 Systems forthwith upon any claim being made against Licensee that its use of the N4 Systems Product is alleged to be an infringement of the intellectual property rights of another.\n\n10.4. Licensee shall, at its expense, defend and indemnify N4 Systems in any suit, claim or proceeding brought against N4 Systems arising out of or resulting from the use of the N4 Systems Product by Licensee for purposes for which it was not authorized hereunder and the installation and integration and the use of the N4 Systems Product in combination with software or other equipment or products not supplied by N4 Systems PROVIDED:\n\n10.4.1. Licensee is notified promptly in writing of the nature of such action, threat of action or claim which comes to N4 Systems\' knowledge; and\n\n10.4.2. N4 Systems renders reasonable assistance as required by Licensee.\n\n10.5. Licensee shall consult with N4 Systems with respect to the defense and settlement of any such claim.\n\n\n11. LIABILITY\nEXCEPT AS OTHERWISE EXPRESSLY STATED IN THIS AGREEMENT, N4 Systems SHALL NOT BE LIABLE TO THE LICENSEE OR ANY PARTY MAKING A CLAIM AGAINST N4 Systems THROUGH THE LICENSEE FOR SPECIAL, INCIDENTAL, PUNITIVE, CONSEQUENTIAL OR INDIRECT DAMAGES OR LOSS (INCLUDING DEATH AND PERSONAL INJURY), IRRESPECTIVE OF THEIR CAUSE, NOTWITHSTANDING THAT A PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH LOSS OR DAMAGE, NOR FOR ANY CLAIMS FOR SUCH LOSS OR DAMAGE INSTITUTED AGAINST A PARTY OR ITS CUSTOMERS BY ANY THIRD PARTY.  WITHOUT LIMITING THE GENERALITY OF THE FOREGOING, N4 Systems ASSUMES NO LIABILITY FOR ANY LOSS OF: USE, DATA, OR THE PROVISION OF INCORRECT DATA, INCOME, BUSINESS, PROFIT, ANTICIPATED REVENUE OR ANY OTHER COMMERCIAL OR ECONOMIC LOSS (EVEN IF N4 SYSTEMS HAD BEEN ADVISED OF THE POSSIBILITY OF SUCH LOSS). IN NO EVENT SHALL N4 Systems\'  LIABILITY TO ANY PERSON OR ENTITY, WHETHER IN CONTRACT, TORT (INCLUDING NEGLIGENCE), OR OTHERWISE, EXCEED THE LICENSE FEE PAID BY LICENSEE UNDER AND DURING THE TERM OF THIS AGREEMENT.\n\n\n12. TERMINATION \n\n12.1. If any of the following conditions arise during the Term, N4 Systems shall have the right, at its option, to terminate this Agreement by giving a written notice of such termination to Licensee, whereupon this Agreement shall immediately cease and terminate:\n\n12.1.1. if any voluntary petition in bankruptcy or any petition for similar relief shall be filed by Licensee;\n\n12.1.2. if any involuntary petition in bankruptcy shall be filed against Licensee and shall not have been dismissed or remains uncontested for a period of thirty (30) days from the filing thereof under any federal, provincial or state bankruptcy, insolvency or similar legislation;\n\n12.1.3. if a receiver shall be appointed for Licensee of its property and such receiver shall not have been dismissed or remains uncontested for a period of thirty (30) days from the date of such appointment; \n\n12.1.4. if Licensee shall make an assignment for the benefit of creditors or admit in writing its inability to meet its debts as they mature; or\n\n12.1.5. if Licensee breaches its confidentiality obligations pursuant to Section hereof.\n\n12.2. Subject to the N4 Systems\' right to immediately terminate the Agreement pursuant to Section  hereof, N4 Systems shall have the right to terminate this Agreement if Licensee fails to perform any other substantial obligation herein on its part to be performed, including but not limited to payment, and the failure continues for at least thirty (30) days after the giving of written notice of such failure to Licensee by N4 Systems .\n\n12.3. Effective upon termination hereof, Licensee shall:\n\n12.3.1. immediately make all payments and perform any other obligations of Licensee arising hereunder within thirty (30) days of termination;\n\n12.3.2. immediately cease to access, use and/or permit to use the N4 Systems Product and Documentation in any manner whatsoever;\n\n12.3.3. as directed at the sole option of N4 Systems, immediately destroy or return all copies of the N4 Systems Product and Documentation including all electronically stored copies thereof and back up copies and related materials and any N4 Systems Confidential Information in Licensee\'s possession, with an affidavit of the Licensee or a senior officer of the Licensee attesting to completion of this task.\n\n12.4. Termination hereunder shall be without prejudice to any other right or remedy to which either party may be entitled hereunder in law.\n\n\n13. ASSIGNMENT\nLicensee shall not assign this Agreement or any right hereunder or assign or delegate any obligation hereunder without the express written consent of N4 Systems.\n\n\n14. RELATIONSHIP\nThis Agreement is entered between separate entities and neither is the agent of the other for any purpose whatsoever. Licensee and N4 Systems are independent contractors and neither has any power nor will it represent itself as having any power to in any way bind or obligate the other or to assume or create any expressed or implied obligation or responsibility on behalf of the other or in other\'s name, and neither party shall have authority to represent itself as agent of the other.  This Agreement shall not be construed as constituting Licensee and N4 Systems as partners or to create any other form of legal association which would impose liability upon one party for the act or failure to act of the other.\n\n\n15. ENTIRE AGREEMENT\n\n15.1. This Agreement and its Schedules constitute the entire agreement between the parties with respect to the subject matter set forth and supersedes any and all prior agreements between the parties either oral or written.\n\n15.2. Unless specifically otherwise provided for herein, this Agreement may not be amended, waived or extended, in whole or in part, except by a writing signed by both parties hereto.\n\n\n16. NOTICES\n\n16.1. Any notice regarding terms and conditions of this Agreement, to be given to any of all of the parties hereunder shall be in writing and may be given by delivering the same to the parties to the addresses, e‑mail addresses and facsimile numbers set out at the beginning of this Agreement.\n\n16.2. Any such notice shall be delivered by registered mail (return receipt requested), by messenger or by hand or by facsimile or e-mail provided evidence of transmission if produced at point of origination.  Any such notice shall be deemed to have been given on the date on which it was delivered if delivered by hand, messenger facsimile or e-mail and on the fifth (5th) business day following posting if given by registered mail and on the next business day after facsimile transmission.  In the case of postal disruption, all notices pursuant to this section  are to be delivered by messenger, hand, facsimile or e-mail but not registered mail.\n\n16.3. Any party hereto may change its address for service from time to time by notice given to the other parties hereto in accordance with the foregoing.\n\n\n17. SEVERABILITY\nShould any part of this Agreement be found to be invalid by a court of competent jurisdiction, the remainder of this Agreement shall continue in full force and effect.\n\n\n18. SURVIVAL\nThe obligations set out in Sections 3, 6, 7, 9, 10, 11, 12, 14, 15, 17, 19, 20 and 22 hereof shall survive the termination or expiration of this Agreement.\n\n\n19. WAIVER\nNo waiver by either party of any breach by the other party of any provision of this Agreement shall, unless in writing, be deemed or construed to be a waiver of any succeeding or other breach of such provision or as a waiver of the provision itself.\n\n\n20. CURRENCY\nAll references to currency in this Agreement shall be references to the lawful currency of United States of America unless otherwise agreed upon by the parties. \n\n\n21. APPLICABLE LAW\nThis Agreement shall be governed by and construed in accordance to the laws applicable in the Province of Ontario, Canada, without giving effect to the principles of conflicts of law and excluding that body of law applicable to choice of law and excluding the United Nations Convention for the International Sale of Goods, if applicable.  Any claim or court proceeding brought by N4 Systems in relation with this Agreement may be presented in the Province of Ontario, Canada.  Licensee agrees that the courts of the Province of Ontario, Canada constitute the appropriate forum for any claim or court proceeding in relation with this Agreement and submits to the exclusive jurisdiction of such courts.\n\n\n22. DISPUTE RESOLUTION\nAny dispute, controversy or claim arising out of or in connection with this Agreement, including any question regarding its negotiation, existence, validity, breach or termination, shall be referred to and finally resolved by arbitration in accordance with the provisions of The Arbitration Act, 1991 (Ontario).  The arbitral tribunal shall be composed of one arbitrator.  The place of arbitration shall be the City of Toronto, Province of Ontario, Canada.  The language of the arbitration shall be English.  Each of the parties hereto hereby irrevocably attorns to the exclusive jurisdiction of any such arbitration. Arbitration shall be a condition precedent to any action taken in a court.  The parties attorn to the jurisdiction of the Courts of the Province of Ontario, sitting at Toronto, Ontario.  The parties waive any right they might have to a trial by jury.\n\n\nSCHEDULE  A\nTO LICENSE AGREEMENT\n\n\n1. Maintenance and technical support to be provided by N4 Systems on the following terms:\n\n1.1. N4 Systems agrees to provide technical support to Licensee for the N4 Systems Product in accordance with this Schedule for the Maintenance Fee payable in advance.\n\n1.2. N4 Systems will provide technical support for the N4 Systems Product and any Updates and Upgrades, such that the N4 Systems Product will operate in conformity with the Documentation, in all material respects.\n\n1.3. N4 Systems will provide telephone, fax, web-based, and electronic mail support on technical issues between the hours of 8:00 and 16:00 (Eastern Time) on business days. \n\n1.4. N4 Systems will provide work arounds, error corrections and software fixes for reported problems within a commercially reasonable period of time taking into account the priority level of the reported problem. \n\n1.5. The Licensee will report incidents describe the nature of the incident and provide details of the circumstances of its occurrence.\n\n1.6. From time to time, N4 Systems will make available to the client the following:\n\n1.6.1. fixes for known bugs or errors in the N4 Systems Product;\n\n1.6.2. available work arounds; and/or\n\n1.6.3. resolutions, error corrections or bug fixes.\n\n1.7. Maintenance and technical support does not include:\n\n1.7.1. custom programming services;\n\n1.7.2. training;\n\n1.7.3. hardware and related supplies; \n\n1.7.4. any support services provided at the client site.\n\n1.8. N4 Systems shall not be liable for delay or failure in performance resulting from acts beyond its control, including, but not limited to acts of God, acts of war, riot, fire, flood, or other disaster, acts of government, strike lockout or communication line or power failures.  Performance times shall be extended for a period of time equivalent to the period of delay.','2009-08-09 12:14:13','1.0');
/*!40000 ALTER TABLE `eulas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fileattachments`
--

DROP TABLE IF EXISTS `fileattachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fileattachments` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `filename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comments` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `index_fileattachments_on_modifiedby` (`modifiedby`),
  KEY `index_fileattachments_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_fileattachments_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fileattachments`
--

LOCK TABLES `fileattachments` WRITE;
/*!40000 ALTER TABLE `fileattachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `fileattachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `findproductoption`
--

DROP TABLE IF EXISTS `findproductoption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `findproductoption` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `identifier` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `findproductoption_uniquekey_type` (`identifier`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `findproductoption`
--

LOCK TABLES `findproductoption` WRITE;
/*!40000 ALTER TABLE `findproductoption` DISABLE KEYS */;
INSERT INTO `findproductoption` VALUES (1,'rfid','RFID Number','Find product by its RFID number'),(2,'serial','Serial Number','Find product by its serial number'),(3,'reelid','Reel ID','Find product by its Reel/ID number');
/*!40000 ALTER TABLE `findproductoption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `findproductoption_manufacture`
--

DROP TABLE IF EXISTS `findproductoption_manufacture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `findproductoption_manufacture` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_findproductoption` bigint(20) NOT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `weight` bigint(20) NOT NULL,
  `mobileweight` bigint(20) DEFAULT '0',
  `datemodified` datetime DEFAULT NULL,
  `datecreated` datetime DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `fpm_unique` (`r_findproductoption`,`tenant_id`),
  KEY `fk_findproductoption_manufacture_organization` (`tenant_id`),
  CONSTRAINT `fk_findproductoption_manufacture_findproductoption` FOREIGN KEY (`r_findproductoption`) REFERENCES `findproductoption` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `findproductoption_manufacture`
--

LOCK TABLES `findproductoption_manufacture` WRITE;
/*!40000 ALTER TABLE `findproductoption_manufacture` DISABLE KEYS */;
/*!40000 ALTER TABLE `findproductoption_manufacture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `infofield`
--

DROP TABLE IF EXISTS `infofield`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `infofield` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `r_productinfo` bigint(20) NOT NULL,
  `required` tinyint(1) DEFAULT NULL,
  `weight` bigint(20) DEFAULT NULL,
  `r_unitofmeasure` bigint(20) DEFAULT NULL,
  `usingunitofmeasure` tinyint(1) DEFAULT NULL,
  `fieldtype` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `retired` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_infofield_on_r_productinfo` (`r_productinfo`),
  KEY `index_infofield_on_r_unitofmeasure` (`r_unitofmeasure`),
  CONSTRAINT `fk_infofield_producttypes` FOREIGN KEY (`r_productinfo`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_infofield_unitofmeasures` FOREIGN KEY (`r_unitofmeasure`) REFERENCES `unitofmeasures` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `infofield`
--

LOCK TABLES `infofield` WRITE;
/*!40000 ALTER TABLE `infofield` DISABLE KEYS */;
INSERT INTO `infofield` VALUES (1,'text field',6799,0,0,NULL,0,'textfield',0),(2,'select field',6799,0,1,NULL,0,'selectbox',0),(3,'combo field',6799,0,2,NULL,0,'combobox',0),(4,'Unit of Measure field',6799,0,3,5,1,'textfield',0);
/*!40000 ALTER TABLE `infofield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `infooption`
--

DROP TABLE IF EXISTS `infooption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `infooption` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `r_infofield` bigint(20) NOT NULL,
  `staticdata` tinyint(1) DEFAULT NULL,
  `weight` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_infooption_on_r_infofield` (`r_infofield`),
  CONSTRAINT `fk_infooption_infofield` FOREIGN KEY (`r_infofield`) REFERENCES `infofield` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `infooption`
--

LOCK TABLES `infooption` WRITE;
/*!40000 ALTER TABLE `infooption` DISABLE KEYS */;
INSERT INTO `infooption` VALUES (1,'option 1',2,1,0),(2,'option 2',2,1,1),(3,'static option 2',3,1,1),(4,'static option 1',3,1,0),(5,'Dynamic Text',1,0,0),(6,'10 ft 2 in ',4,0,0),(7,'Dynamic Text',1,0,0),(8,'dynamic combo text',3,0,0),(9,'10 ft 2 in ',4,0,0),(10,'Dynamic Text',1,0,0),(11,'dynamic combo text',3,0,0),(12,'10 ft 2 in ',4,0,0),(13,'Dynamic Text',1,0,0),(14,'10 ft 2 in ',4,0,0),(15,'Dynamic Text',1,0,0),(16,'10 ft 2 in ',4,0,0),(17,'Dynamic Text',1,0,0),(18,'10 ft 2 in ',4,0,0),(19,'Dynamic Text',1,0,0),(20,'10 ft 2 in ',4,0,0),(21,'Dynamic Text',1,0,0),(22,'10 ft 2 in ',4,0,0),(23,'Dynamic Text',1,0,0),(24,'10 ft 2 in ',4,0,0),(25,'Dynamic Text',1,0,0),(26,'10 ft 2 in ',4,0,0),(27,'Dynamic Text',1,0,0),(28,'10 ft 2 in ',4,0,0);
/*!40000 ALTER TABLE `infooption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectionbooks`
--

DROP TABLE IF EXISTS `inspectionbooks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectionbooks` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `legacyid` bigint(20) DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `open` tinyint(1) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_inspectionbooks_on_modifiedby` (`modifiedby`),
  KEY `index_inspectionbooks_on_r_tenant` (`tenant_id`),
  KEY `fk_inspectionbooks_owner` (`owner_id`),
  CONSTRAINT `fk_inspectionbooks_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionbooks_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectionbooks`
--

LOCK TABLES `inspectionbooks` WRITE;
/*!40000 ALTER TABLE `inspectionbooks` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspectionbooks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectiongroups`
--

DROP TABLE IF EXISTS `inspectiongroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectiongroups` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `mobileguid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_inspectiongroups_on_modifiedby` (`modifiedby`),
  KEY `index_inspectiongroups_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_inspectiongroups_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectiongroups`
--

LOCK TABLES `inspectiongroups` WRITE;
/*!40000 ALTER TABLE `inspectiongroups` DISABLE KEYS */;
INSERT INTO `inspectiongroups` VALUES (1,15511597,'2010-03-30 21:31:50','2010-03-30 21:31:50',15512599,NULL),(2,15511597,'2010-03-30 21:32:10','2010-03-30 21:32:10',15512599,NULL),(3,15511597,'2010-03-30 21:32:28','2010-03-30 21:32:28',15512599,NULL),(4,15511597,'2010-03-30 21:32:56','2010-03-30 21:32:56',15512599,NULL),(5,15511597,'2010-03-30 21:33:20','2010-03-30 21:33:20',15512599,NULL),(6,15511597,'2010-03-30 21:33:39','2010-03-30 21:33:39',15512599,NULL),(7,15511597,'2010-03-30 21:34:11','2010-03-30 21:34:11',15512599,NULL),(8,15511597,'2010-03-30 21:34:26','2010-03-30 21:34:26',15512599,NULL),(9,15511597,'2010-03-30 21:34:43','2010-03-30 21:34:43',15512599,NULL),(10,15511597,'2010-03-30 21:34:56','2010-03-30 21:34:56',15512599,NULL),(11,15511597,'2010-03-30 21:35:10','2010-03-30 21:35:10',15512599,NULL),(12,15511597,'2010-03-30 21:35:27','2010-03-30 21:35:27',15512599,NULL),(13,15511597,'2010-03-30 21:35:43','2010-03-30 21:35:43',15512599,NULL),(14,15511597,'2010-03-30 21:35:59','2010-03-30 21:35:59',15512599,NULL),(15,15511597,'2010-03-30 21:36:14','2010-03-30 21:36:14',15512599,NULL),(16,15511597,'2010-03-30 21:36:28','2010-03-30 21:36:28',15512599,NULL),(17,15511597,'2010-03-30 21:36:43','2010-03-30 21:36:43',15512599,NULL),(18,15511597,'2010-03-30 21:36:59','2010-03-30 21:36:59',15512599,NULL),(19,15511597,'2010-03-30 21:37:22','2010-03-30 21:37:22',15512599,NULL),(20,15511597,'2010-03-30 21:37:43','2010-03-30 21:37:43',15512599,NULL),(21,15511597,'2010-03-30 21:39:07','2010-03-30 21:39:07',15512599,NULL),(22,15511595,'2010-04-13 18:08:55','2010-04-13 18:08:55',15512595,NULL),(23,15511595,'2010-04-13 21:43:30','2010-04-13 21:43:30',15512595,NULL),(24,15511595,'2010-04-14 14:34:53','2010-04-14 14:34:53',15512595,NULL),(25,15511595,'2010-04-14 14:50:32','2010-04-14 14:50:32',15512595,NULL),(26,15511595,'2010-04-14 14:59:40','2010-04-14 14:59:40',15512595,NULL),(27,15511595,'2010-04-14 15:00:18','2010-04-14 15:00:18',15512595,NULL),(28,15511595,'2010-04-14 15:00:52','2010-04-14 15:00:52',15512595,NULL);
/*!40000 ALTER TABLE `inspectiongroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspections`
--

DROP TABLE IF EXISTS `inspections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspections` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `comments` varchar(2500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `formversion` bigint(20) NOT NULL,
  `productstatus_id` bigint(20) DEFAULT NULL,
  `mobileguid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_inspections_on_modifiedby` (`modifiedby`),
  KEY `index_inspections_on_product_id` (`product_id`),
  KEY `index_inspections_on_r_tenant` (`tenant_id`),
  KEY `index_inspections_on_type_id` (`type_id`),
  KEY `fk_inspections_productstatus` (`productstatus_id`),
  CONSTRAINT `fk_inspections_inspectiontypes` FOREIGN KEY (`type_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspections_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspections_productstatus` FOREIGN KEY (`productstatus_id`) REFERENCES `productstatus` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspections_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspections`
--

LOCK TABLES `inspections` WRITE;
/*!40000 ALTER TABLE `inspections` DISABLE KEYS */;
INSERT INTO `inspections` VALUES (1,15511597,'2010-03-30 21:31:50','2010-03-30 21:31:50',15512599,'',1,21,1,486,NULL),(2,15511597,'2010-03-30 21:32:10','2010-03-30 21:32:10',15512599,'',1,20,1,486,NULL),(3,15511597,'2010-03-30 21:32:28','2010-03-30 21:32:28',15512599,'',1,19,1,486,NULL),(4,15511597,'2010-03-30 21:32:57','2010-03-30 21:32:57',15512599,'',1,18,1,486,NULL),(5,15511597,'2010-03-30 21:33:20','2010-03-30 21:33:20',15512599,'',1,17,1,486,NULL),(6,15511597,'2010-03-30 21:33:39','2010-03-30 21:33:39',15512599,'',1,16,1,486,NULL),(7,15511597,'2010-03-30 21:34:11','2010-03-30 21:34:11',15512599,'',1,15,1,486,NULL),(8,15511597,'2010-03-30 21:34:26','2010-03-30 21:34:26',15512599,'',1,14,1,486,NULL),(9,15511597,'2010-03-30 21:34:43','2010-03-30 21:34:43',15512599,'',1,13,1,486,NULL),(10,15511597,'2010-03-30 21:34:56','2010-03-30 21:34:56',15512599,'',1,12,1,486,NULL),(11,15511597,'2010-03-30 21:35:10','2010-03-30 21:35:10',15512599,'',1,11,1,486,NULL),(12,15511597,'2010-03-30 21:35:27','2010-03-30 21:35:27',15512599,'',1,10,1,486,NULL),(13,15511597,'2010-03-30 21:35:43','2010-03-30 21:35:43',15512599,'',1,9,1,486,NULL),(14,15511597,'2010-03-30 21:35:59','2010-03-30 21:35:59',15512599,'',1,8,1,486,NULL),(15,15511597,'2010-03-30 21:36:14','2010-03-30 21:36:14',15512599,'',1,7,1,486,NULL),(16,15511597,'2010-03-30 21:36:28','2010-03-30 21:36:28',15512599,'',1,6,1,486,NULL),(17,15511597,'2010-03-30 21:36:43','2010-03-30 21:36:43',15512599,'',1,5,1,486,NULL),(18,15511597,'2010-03-30 21:36:59','2010-03-30 21:36:59',15512599,'',1,4,1,486,NULL),(19,15511597,'2010-03-30 21:37:22','2010-03-30 21:37:22',15512599,'',1,3,1,486,NULL),(20,15511597,'2010-03-30 21:37:43','2010-03-30 21:37:43',15512599,'',1,1,1,486,NULL),(21,15511597,'2010-03-30 21:39:07','2010-03-30 21:39:07',15512599,'',1,2,1,486,NULL),(22,15511595,'2010-04-13 18:08:55','2010-04-13 18:08:55',15512595,'',2,22,2,476,NULL),(23,15511595,'2010-04-13 21:43:30','2010-04-13 21:43:30',15512595,'',2,24,2,476,NULL),(24,15511595,'2010-04-14 14:34:53','2010-04-14 14:34:53',15512595,'',2,28,2,476,NULL),(25,15511595,'2010-04-14 14:50:32','2010-04-14 14:50:32',15512595,'',2,29,2,476,NULL),(26,15511595,'2010-04-14 14:59:40','2010-04-14 14:59:40',15512595,'',2,30,2,476,NULL),(27,15511595,'2010-04-14 15:00:18','2010-04-14 15:00:18',15512595,'',2,31,2,476,NULL),(28,15511595,'2010-04-14 15:00:52','2010-04-14 15:00:52',15512595,'',2,32,2,476,NULL);
/*!40000 ALTER TABLE `inspections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspections_fileattachments`
--

DROP TABLE IF EXISTS `inspections_fileattachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspections_fileattachments` (
  `inspections_id` bigint(20) NOT NULL,
  `attachments_id` bigint(20) NOT NULL,
  UNIQUE KEY `inspections_fileattachments_attachments_id_key` (`attachments_id`),
  KEY `index_inspections_fileattachments_on_attachments_id` (`attachments_id`),
  KEY `index_inspections_fileattachments_on_inspections_id` (`inspections_id`),
  CONSTRAINT `fk_inspections_fileattachments_fileattachments` FOREIGN KEY (`attachments_id`) REFERENCES `fileattachments` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspections_fileattachments_inspections` FOREIGN KEY (`inspections_id`) REFERENCES `inspections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspections_fileattachments`
--

LOCK TABLES `inspections_fileattachments` WRITE;
/*!40000 ALTER TABLE `inspections_fileattachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspections_fileattachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspections_infooptionmap`
--

DROP TABLE IF EXISTS `inspections_infooptionmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspections_infooptionmap` (
  `inspections_id` bigint(20) NOT NULL,
  `element` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mapkey` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  KEY `fk_inspections_infooptionmap_inspections` (`inspections_id`),
  CONSTRAINT `fk_inspections_infooptionmap_inspections` FOREIGN KEY (`inspections_id`) REFERENCES `inspections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspections_infooptionmap`
--

LOCK TABLES `inspections_infooptionmap` WRITE;
/*!40000 ALTER TABLE `inspections_infooptionmap` DISABLE KEYS */;
INSERT INTO `inspections_infooptionmap` VALUES (22,'','inspection attribute 1'),(23,'','inspection attribute 1'),(24,'','inspection attribute 1'),(25,'','inspection attribute 1'),(26,'','inspection attribute 1'),(27,'','inspection attribute 1'),(28,'','inspection attribute 1');
/*!40000 ALTER TABLE `inspections_infooptionmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectionschedules`
--

DROP TABLE IF EXISTS `inspectionschedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectionschedules` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  `nextdate` datetime NOT NULL,
  `inspectiontype_id` bigint(20) DEFAULT NULL,
  `completeddate` datetime DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `inspection_inspection_id` bigint(20) DEFAULT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `mobileguid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_inspectionschedules_on_inspection_inspection_id` (`inspection_inspection_id`),
  KEY `index_inspectionschedules_on_inspectiontype_id` (`inspectiontype_id`),
  KEY `index_inspectionschedules_on_modifiedby` (`modifiedby`),
  KEY `index_inspectionschedules_on_product_id` (`product_id`),
  KEY `index_inspectionschedules_on_project_id` (`project_id`),
  KEY `index_inspectionschedules_on_r_tenant` (`tenant_id`),
  KEY `fk_inspectionschedules_owner` (`owner_id`),
  CONSTRAINT `fk_inspectionschedules_inspectiontypes` FOREIGN KEY (`inspectiontype_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionschedules_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionschedules_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionschedules_projects` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionschedules_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectionschedules`
--

LOCK TABLES `inspectionschedules` WRITE;
/*!40000 ALTER TABLE `inspectionschedules` DISABLE KEYS */;
INSERT INTO `inspectionschedules` VALUES (1,15511597,'2010-03-30 21:31:50','2010-03-30 21:31:50',NULL,21,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539074,NULL),(2,15511597,'2010-03-30 21:31:51','2010-03-30 21:31:51',NULL,21,'2010-03-30 21:31:00',1,'2010-03-30 21:31:51','COMPLETED',1,'ACTIVE',NULL,'Good Place',15539074,NULL),(3,15511597,'2010-03-30 21:32:10','2010-03-30 21:32:10',NULL,20,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539073,NULL),(4,15511597,'2010-03-30 21:32:10','2010-03-30 21:32:10',NULL,20,'2010-03-30 21:31:00',1,'2010-03-30 21:32:10','COMPLETED',2,'ACTIVE',NULL,'Good Place',15539073,NULL),(5,15511597,'2010-03-30 21:32:28','2010-03-30 21:32:28',NULL,19,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539072,NULL),(6,15511597,'2010-03-30 21:32:28','2010-03-30 21:32:28',NULL,19,'2010-03-30 21:32:00',1,'2010-03-30 21:32:28','COMPLETED',3,'ACTIVE',NULL,'Good Place',15539072,NULL),(7,15511597,'2010-03-30 21:32:57','2010-03-30 21:32:57',NULL,18,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539076,NULL),(8,15511597,'2010-03-30 21:32:57','2010-03-30 21:32:57',NULL,18,'2010-03-30 21:32:00',1,'2010-03-30 21:32:57','COMPLETED',4,'ACTIVE',NULL,'Good Place',15539076,NULL),(9,15511597,'2010-03-30 21:33:20','2010-03-30 21:33:20',NULL,17,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539075,NULL),(10,15511597,'2010-03-30 21:33:20','2010-03-30 21:33:20',NULL,17,'2010-03-30 21:33:00',1,'2010-03-30 21:33:20','COMPLETED',5,'ACTIVE',NULL,'Good Place',15539075,NULL),(11,15511597,'2010-03-30 21:33:39','2010-03-30 21:33:39',NULL,16,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539071,NULL),(12,15511597,'2010-03-30 21:33:39','2010-03-30 21:33:39',NULL,16,'2010-03-30 21:33:00',1,'2010-03-30 21:33:39','COMPLETED',6,'ACTIVE',NULL,'Good Place',15539071,NULL),(13,15511597,'2010-03-30 21:34:11','2010-03-30 21:34:11',NULL,15,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539088,NULL),(14,15511597,'2010-03-30 21:34:11','2010-03-30 21:34:11',NULL,15,'2010-03-30 21:34:00',1,'2010-03-30 21:34:11','COMPLETED',7,'ACTIVE',NULL,'Good Place',15539088,NULL),(15,15511597,'2010-03-30 21:34:26','2010-03-30 21:34:26',NULL,14,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539087,NULL),(16,15511597,'2010-03-30 21:34:26','2010-03-30 21:34:26',NULL,14,'2010-03-30 21:34:00',1,'2010-03-30 21:34:26','COMPLETED',8,'ACTIVE',NULL,'Good Place',15539087,NULL),(17,15511597,'2010-03-30 21:34:43','2010-03-30 21:34:43',NULL,13,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539086,NULL),(18,15511597,'2010-03-30 21:34:43','2010-03-30 21:34:43',NULL,13,'2010-03-30 21:34:00',1,'2010-03-30 21:34:43','COMPLETED',9,'ACTIVE',NULL,'Good Place',15539086,NULL),(19,15511597,'2010-03-30 21:34:56','2010-03-30 21:34:56',NULL,12,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539085,NULL),(20,15511597,'2010-03-30 21:34:56','2010-03-30 21:34:56',NULL,12,'2010-03-30 21:34:00',1,'2010-03-30 21:34:56','COMPLETED',10,'ACTIVE',NULL,'Good Place',15539085,NULL),(21,15511597,'2010-03-30 21:35:10','2010-03-30 21:35:10',NULL,11,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539084,NULL),(22,15511597,'2010-03-30 21:35:10','2010-03-30 21:35:10',NULL,11,'2010-03-30 21:35:00',1,'2010-03-30 21:35:10','COMPLETED',11,'ACTIVE',NULL,'Good Place',15539084,NULL),(23,15511597,'2010-03-30 21:35:27','2010-03-30 21:35:27',NULL,10,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539083,NULL),(24,15511597,'2010-03-30 21:35:27','2010-03-30 21:35:27',NULL,10,'2010-03-30 21:35:00',1,'2010-03-30 21:35:27','COMPLETED',12,'ACTIVE',NULL,'Good Place',15539083,NULL),(25,15511597,'2010-03-30 21:35:43','2010-03-30 21:35:43',NULL,9,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539070,NULL),(26,15511597,'2010-03-30 21:35:43','2010-03-30 21:35:43',NULL,9,'2010-03-30 21:35:00',1,'2010-03-30 21:35:43','COMPLETED',13,'ACTIVE',NULL,'Good Place',15539070,NULL),(27,15511597,'2010-03-30 21:35:59','2010-03-30 21:35:59',NULL,8,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539082,NULL),(28,15511597,'2010-03-30 21:35:59','2010-03-30 21:35:59',NULL,8,'2010-03-30 21:35:00',1,'2010-03-30 21:35:59','COMPLETED',14,'ACTIVE',NULL,'Good Place',15539082,NULL),(29,15511597,'2010-03-30 21:36:14','2010-03-30 21:36:14',NULL,7,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539081,NULL),(30,15511597,'2010-03-30 21:36:14','2010-03-30 21:36:14',NULL,7,'2010-03-30 21:36:00',1,'2010-03-30 21:36:14','COMPLETED',15,'ACTIVE',NULL,'Good Place',15539081,NULL),(31,15511597,'2010-03-30 21:36:28','2010-03-30 21:36:28',NULL,6,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539080,NULL),(32,15511597,'2010-03-30 21:36:28','2010-03-30 21:36:28',NULL,6,'2010-03-30 21:36:00',1,'2010-03-30 21:36:28','COMPLETED',16,'ACTIVE',NULL,'Good Place',15539080,NULL),(33,15511597,'2010-03-30 21:36:43','2010-03-30 21:36:43',NULL,5,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539079,NULL),(34,15511597,'2010-03-30 21:36:43','2010-03-30 21:36:43',NULL,5,'2010-03-30 21:36:00',1,'2010-03-30 21:36:43','COMPLETED',17,'ACTIVE',NULL,'Good Place',15539079,NULL),(35,15511597,'2010-03-30 21:36:59','2010-03-30 21:36:59',NULL,4,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539078,NULL),(36,15511597,'2010-03-30 21:36:59','2010-03-30 21:36:59',NULL,4,'2010-03-30 21:36:00',1,'2010-03-30 21:36:59','COMPLETED',18,'ACTIVE',NULL,'Good Place',15539078,NULL),(37,15511597,'2010-03-30 21:37:23','2010-03-30 21:37:23',NULL,3,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539077,NULL),(38,15511597,'2010-03-30 21:37:23','2010-03-30 21:37:23',NULL,3,'2010-03-30 21:37:00',1,'2010-03-30 21:37:23','COMPLETED',19,'ACTIVE',NULL,'Good Place',15539077,NULL),(39,15511597,'2010-03-30 21:37:43','2010-03-30 21:37:43',NULL,1,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539068,NULL),(40,15511597,'2010-03-30 21:37:43','2010-03-30 21:37:43',NULL,1,'2010-03-30 21:37:00',1,'2010-03-30 21:37:43','COMPLETED',20,'ACTIVE',NULL,'Good Place',15539068,NULL),(41,15511597,'2010-03-30 21:39:07','2010-03-30 21:39:07',NULL,2,'2011-03-30 00:00:00',1,NULL,'SCHEDULED',NULL,'ACTIVE',NULL,'Good Place',15539069,NULL),(42,15511597,'2010-03-30 21:39:07','2010-03-30 21:39:07',NULL,2,'2010-03-30 21:38:00',1,'2010-03-30 21:39:07','COMPLETED',21,'ACTIVE',NULL,'Good Place',15539069,NULL);
/*!40000 ALTER TABLE `inspectionschedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectionsmaster`
--

DROP TABLE IF EXISTS `inspectionsmaster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectionsmaster` (
  `inspection_id` bigint(20) NOT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date` datetime NOT NULL,
  `printable` tinyint(1) NOT NULL,
  `prooftesttype` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `peakload` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `duration` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `inspector_uniqueid` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `peakloadduration` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  KEY `index_inspectionsmaster_on_group_id` (`group_id`),
  KEY `index_inspectionsmaster_on_inspection_id` (`inspection_id`),
  KEY `index_inspectionsmaster_on_inspector_uniqueid` (`inspector_uniqueid`),
  KEY `index_inspectionsmaster_on_state` (`state`),
  KEY `fk_inspectionsmaster_inspectionbooks` (`book_id`),
  KEY `fk_inspectionsmaster_owner` (`owner_id`),
  CONSTRAINT `fk_inspectionsmaster_inspectionbooks` FOREIGN KEY (`book_id`) REFERENCES `inspectionbooks` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionsmaster_inspectiongroups` FOREIGN KEY (`group_id`) REFERENCES `inspectiongroups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionsmaster_inspections` FOREIGN KEY (`inspection_id`) REFERENCES `inspections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionsmaster_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectionsmaster_users` FOREIGN KEY (`inspector_uniqueid`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectionsmaster`
--

LOCK TABLES `inspectionsmaster` WRITE;
/*!40000 ALTER TABLE `inspectionsmaster` DISABLE KEYS */;
INSERT INTO `inspectionsmaster` VALUES (1,'Good Place','2010-03-30 21:31:00',0,NULL,NULL,NULL,'NA',15512599,1,NULL,'ACTIVE',NULL,15539074),(2,'Good Place','2010-03-30 21:31:00',0,NULL,NULL,NULL,'NA',15512599,2,NULL,'ACTIVE',NULL,15539073),(3,'Good Place','2010-03-30 21:32:00',0,NULL,NULL,NULL,'NA',15512599,3,NULL,'ACTIVE',NULL,15539072),(4,'Good Place','2010-03-30 21:32:00',0,NULL,NULL,NULL,'NA',15512599,4,NULL,'ACTIVE',NULL,15539076),(5,'Good Place','2010-03-30 21:33:00',0,NULL,NULL,NULL,'NA',15512599,5,NULL,'ACTIVE',NULL,15539075),(6,'Good Place','2010-03-30 21:33:00',0,NULL,NULL,NULL,'NA',15512599,6,NULL,'ACTIVE',NULL,15539071),(7,'Good Place','2010-03-30 21:34:00',0,NULL,NULL,NULL,'NA',15512599,7,NULL,'ACTIVE',NULL,15539088),(8,'Good Place','2010-03-30 21:34:00',0,NULL,NULL,NULL,'NA',15512599,8,NULL,'ACTIVE',NULL,15539087),(9,'Good Place','2010-03-30 21:34:00',0,NULL,NULL,NULL,'NA',15512599,9,NULL,'ACTIVE',NULL,15539086),(10,'Good Place','2010-03-30 21:34:00',0,NULL,NULL,NULL,'NA',15512599,10,NULL,'ACTIVE',NULL,15539085),(11,'Good Place','2010-03-30 21:35:00',0,NULL,NULL,NULL,'NA',15512599,11,NULL,'ACTIVE',NULL,15539084),(12,'Good Place','2010-03-30 21:35:00',0,NULL,NULL,NULL,'NA',15512599,12,NULL,'ACTIVE',NULL,15539083),(13,'Good Place','2010-03-30 21:35:00',0,NULL,NULL,NULL,'NA',15512599,13,NULL,'ACTIVE',NULL,15539070),(14,'Good Place','2010-03-30 21:35:00',0,NULL,NULL,NULL,'NA',15512599,14,NULL,'ACTIVE',NULL,15539082),(15,'Good Place','2010-03-30 21:36:00',0,NULL,NULL,NULL,'NA',15512599,15,NULL,'ACTIVE',NULL,15539081),(16,'Good Place','2010-03-30 21:36:00',0,NULL,NULL,NULL,'NA',15512599,16,NULL,'ACTIVE',NULL,15539080),(17,'Good Place','2010-03-30 21:36:00',0,NULL,NULL,NULL,'NA',15512599,17,NULL,'ACTIVE',NULL,15539079),(18,'Good Place','2010-03-30 21:36:00',0,NULL,NULL,NULL,'NA',15512599,18,NULL,'ACTIVE',NULL,15539078),(19,'Good Place','2010-03-30 21:37:00',0,NULL,NULL,NULL,'NA',15512599,19,NULL,'ACTIVE',NULL,15539077),(20,'Good Place','2010-03-30 21:37:00',0,NULL,NULL,NULL,'NA',15512599,20,NULL,'ACTIVE',NULL,15539068),(21,'Good Place','2010-03-30 21:38:00',0,NULL,NULL,NULL,'NA',15512599,21,NULL,'ACTIVE',NULL,15539069),(22,'Vendor store','2010-04-13 18:08:00',0,NULL,NULL,NULL,'PASS',15512595,22,NULL,'ACTIVE',NULL,15539065),(23,'Vendor store','2010-04-13 21:43:00',0,NULL,NULL,NULL,'PASS',15512595,23,NULL,'ACTIVE',NULL,15539067),(24,'Vendor store','2010-04-14 14:34:00',0,NULL,NULL,NULL,'PASS',15512595,24,NULL,'ACTIVE',NULL,15539090),(25,'Vendor store','2010-04-14 14:50:00',0,NULL,NULL,NULL,'PASS',15512595,25,NULL,'ACTIVE',NULL,15539090),(26,'Vendor store','2010-04-14 14:59:00',0,NULL,NULL,NULL,'PASS',15512595,26,NULL,'ACTIVE',NULL,15539065),(27,'Vendor store','2010-04-14 15:00:00',0,NULL,NULL,NULL,'PASS',15512595,27,NULL,'ACTIVE',NULL,15539067),(28,'Vendor store','2010-04-14 15:00:00',0,NULL,NULL,NULL,'PASS',15512595,28,NULL,'ACTIVE',NULL,15539090);
/*!40000 ALTER TABLE `inspectionsmaster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectionsmaster_inspectionssub`
--

DROP TABLE IF EXISTS `inspectionsmaster_inspectionssub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectionsmaster_inspectionssub` (
  `inspectionsmaster_inspection_id` bigint(20) NOT NULL,
  `subinspections_inspection_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  UNIQUE KEY `index_inspectionsmaster_inspectionssub_on_subinspections_inspec` (`subinspections_inspection_id`),
  KEY `fk_masterinspections_inspection` (`inspectionsmaster_inspection_id`),
  CONSTRAINT `fk_masterinspections_inspection` FOREIGN KEY (`inspectionsmaster_inspection_id`) REFERENCES `inspections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subinspections_inspection` FOREIGN KEY (`subinspections_inspection_id`) REFERENCES `inspections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectionsmaster_inspectionssub`
--

LOCK TABLES `inspectionsmaster_inspectionssub` WRITE;
/*!40000 ALTER TABLE `inspectionsmaster_inspectionssub` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspectionsmaster_inspectionssub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectionssub`
--

DROP TABLE IF EXISTS `inspectionssub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectionssub` (
  `inspection_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  KEY `index_inspectionssub_on_inspection_id` (`inspection_id`),
  CONSTRAINT `fk_inspectionssub_inspections` FOREIGN KEY (`inspection_id`) REFERENCES `inspections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectionssub`
--

LOCK TABLES `inspectionssub` WRITE;
/*!40000 ALTER TABLE `inspectionssub` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspectionssub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectiontypegroups`
--

DROP TABLE IF EXISTS `inspectiontypegroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectiontypegroups` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `reporttitle` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `printout_id` bigint(20) DEFAULT NULL,
  `observationprintout_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_inspectiontypegroups_on_modifiedby` (`modifiedby`),
  KEY `index_inspectiontypegroups_on_observationprintout_id` (`observationprintout_id`),
  KEY `index_inspectiontypegroups_on_printout_id` (`printout_id`),
  KEY `index_inspectiontypegroups_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_inspectiontypegroups_cert_printouts` FOREIGN KEY (`printout_id`) REFERENCES `printouts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectiontypegroups_observation_printouts` FOREIGN KEY (`observationprintout_id`) REFERENCES `printouts` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectiontypegroups_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectiontypegroups`
--

LOCK TABLES `inspectiontypegroups` WRITE;
/*!40000 ALTER TABLE `inspectiontypegroups` DISABLE KEYS */;
INSERT INTO `inspectiontypegroups` VALUES (267,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'Visual Inspection','Visual Inspection',NULL,NULL),(268,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'Visual Inspection','Visual Inspection',NULL,NULL),(269,15511597,'2010-03-30 20:00:15','2010-03-30 21:29:44',15512599,'repair','repair',NULL,NULL);
/*!40000 ALTER TABLE `inspectiontypegroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectiontypes`
--

DROP TABLE IF EXISTS `inspectiontypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectiontypes` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `printable` tinyint(1) NOT NULL DEFAULT '0',
  `legacyeventid` bigint(20) DEFAULT NULL,
  `retired` tinyint(1) DEFAULT '0',
  `master` tinyint(1) NOT NULL,
  `formversion` bigint(20) NOT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `archivedName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_inspectiontypes_on_group_id` (`group_id`),
  KEY `index_inspectiontypes_on_modifiedby` (`modifiedby`),
  KEY `index_inspectiontypes_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_inspectiontypes_inspectiontypegroups` FOREIGN KEY (`group_id`) REFERENCES `inspectiontypegroups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectiontypes_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectiontypes`
--

LOCK TABLES `inspectiontypes` WRITE;
/*!40000 ALTER TABLE `inspectiontypes` DISABLE KEYS */;
INSERT INTO `inspectiontypes` VALUES (1,15511597,'2010-03-30 21:30:07','2010-03-30 21:30:07',NULL,'Repair',269,NULL,0,NULL,0,0,1,'ACTIVE',NULL),(2,15511595,'2010-03-31 14:53:59','2010-03-31 14:54:59',15512595,'Chain Visual',267,NULL,0,NULL,0,0,2,'ACTIVE',NULL);
/*!40000 ALTER TABLE `inspectiontypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectiontypes_criteriasections`
--

DROP TABLE IF EXISTS `inspectiontypes_criteriasections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectiontypes_criteriasections` (
  `inspectiontypes_id` bigint(20) NOT NULL,
  `sections_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  UNIQUE KEY `inspectiontypes_criteriasections_sections_id_key` (`sections_id`),
  KEY `index_inspectiontypes_criteriasections_on_sections_id` (`sections_id`),
  KEY `fk_inspectiontypes_criteriasections_inspectiontypes` (`inspectiontypes_id`),
  CONSTRAINT `fk_inspectiontypes_criteriasections_criteriasections` FOREIGN KEY (`sections_id`) REFERENCES `criteriasections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inspectiontypes_criteriasections_inspectiontypes` FOREIGN KEY (`inspectiontypes_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectiontypes_criteriasections`
--

LOCK TABLES `inspectiontypes_criteriasections` WRITE;
/*!40000 ALTER TABLE `inspectiontypes_criteriasections` DISABLE KEYS */;
INSERT INTO `inspectiontypes_criteriasections` VALUES (2,1,0),(2,2,1);
/*!40000 ALTER TABLE `inspectiontypes_criteriasections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectiontypes_infofieldnames`
--

DROP TABLE IF EXISTS `inspectiontypes_infofieldnames`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectiontypes_infofieldnames` (
  `inspectiontypes_id` bigint(20) NOT NULL,
  `element` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `fk_inspectiontypes_infofieldnames_inspectiontypes` (`inspectiontypes_id`),
  CONSTRAINT `fk_inspectiontypes_infofieldnames_inspectiontypes` FOREIGN KEY (`inspectiontypes_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectiontypes_infofieldnames`
--

LOCK TABLES `inspectiontypes_infofieldnames` WRITE;
/*!40000 ALTER TABLE `inspectiontypes_infofieldnames` DISABLE KEYS */;
INSERT INTO `inspectiontypes_infofieldnames` VALUES (2,'inspection attribute 1',0);
/*!40000 ALTER TABLE `inspectiontypes_infofieldnames` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspectiontypes_supportedprooftests`
--

DROP TABLE IF EXISTS `inspectiontypes_supportedprooftests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspectiontypes_supportedprooftests` (
  `inspectiontypes_id` bigint(20) NOT NULL,
  `element` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  KEY `index_inspectiontypes_supportedprooftests_on_inspectiontypes_id` (`inspectiontypes_id`),
  CONSTRAINT `fk_inspectiontypes_supportedprooftests_inspectiontypes` FOREIGN KEY (`inspectiontypes_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspectiontypes_supportedprooftests`
--

LOCK TABLES `inspectiontypes_supportedprooftests` WRITE;
/*!40000 ALTER TABLE `inspectiontypes_supportedprooftests` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspectiontypes_supportedprooftests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instructionalvideos`
--

DROP TABLE IF EXISTS `instructionalvideos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instructionalvideos` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instructionalvideos`
--

LOCK TABLES `instructionalvideos` WRITE;
/*!40000 ALTER TABLE `instructionalvideos` DISABLE KEYS */;
/*!40000 ALTER TABLE `instructionalvideos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lineitems`
--

DROP TABLE IF EXISTS `lineitems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lineitems` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) NOT NULL,
  `idx` bigint(20) DEFAULT NULL,
  `description` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity` bigint(20) NOT NULL,
  `lineid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `productcode` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_lineitems_on_index` (`idx`),
  KEY `index_lineitems_on_lineid` (`lineid`),
  KEY `index_lineitems_on_modified` (`modified`),
  KEY `index_lineitems_on_order_id` (`order_id`),
  KEY `index_lineitems_on_productcode` (`productcode`),
  KEY `fk_lineitems_users` (`modifiedby`),
  KEY `fk_lineitems_tenants` (`tenant_id`),
  CONSTRAINT `fk_lineitems_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lineitems_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lineitems_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lineitems`
--

LOCK TABLES `lineitems` WRITE;
/*!40000 ALTER TABLE `lineitems` DISABLE KEYS */;
/*!40000 ALTER TABLE `lineitems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagecommands`
--

DROP TABLE IF EXISTS `messagecommands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messagecommands` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `commandtype` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `processed` tinyint(1) NOT NULL,
  `createdby` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_messagecommands_users` (`modifiedby`),
  CONSTRAINT `fk_messagecommands_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagecommands`
--

LOCK TABLES `messagecommands` WRITE;
/*!40000 ALTER TABLE `messagecommands` DISABLE KEYS */;
/*!40000 ALTER TABLE `messagecommands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagecommands_paramaters`
--

DROP TABLE IF EXISTS `messagecommands_paramaters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messagecommands_paramaters` (
  `messagecommands_id` bigint(20) NOT NULL,
  `element` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mapkey` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`messagecommands_id`,`mapkey`),
  CONSTRAINT `fk_messagecommands_paramaters_messagecommands` FOREIGN KEY (`messagecommands_id`) REFERENCES `messagecommands` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagecommands_paramaters`
--

LOCK TABLES `messagecommands_paramaters` WRITE;
/*!40000 ALTER TABLE `messagecommands_paramaters` DISABLE KEYS */;
/*!40000 ALTER TABLE `messagecommands_paramaters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `sender` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `receiver` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `subject` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `body` text COLLATE utf8_unicode_ci NOT NULL,
  `unread` tinyint(1) NOT NULL,
  `command_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_messages_users` (`modifiedby`),
  KEY `fk_messages_tenants` (`tenant_id`),
  KEY `fk_messages_org_base` (`owner_id`),
  KEY `fk_messages_messagecommands` (`command_id`),
  CONSTRAINT `fk_messages_messagecommands` FOREIGN KEY (`command_id`) REFERENCES `messagecommands` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_messages_org_base` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_messages_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_messages_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificationsettings`
--

DROP TABLE IF EXISTS `notificationsettings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificationsettings` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `frequency` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `periodend` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `periodstart` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `includeoverdue` tinyint(1) NOT NULL,
  `includeupcoming` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_notificationsettings_on_modifiedby` (`modifiedby`),
  KEY `index_notificationsettings_on_r_tenant` (`tenant_id`),
  KEY `index_notificationsettings_on_user_id` (`user_id`),
  KEY `fk_notificationsettings_owner` (`owner_id`),
  CONSTRAINT `fk_notificationsettings_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_notificationsettings_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_notificationsettings_users_owner` FOREIGN KEY (`user_id`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificationsettings`
--

LOCK TABLES `notificationsettings` WRITE;
/*!40000 ALTER TABLE `notificationsettings` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificationsettings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificationsettings_addresses`
--

DROP TABLE IF EXISTS `notificationsettings_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificationsettings_addresses` (
  `notificationsettings_id` bigint(20) NOT NULL,
  `addr` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `index_notificationsettings_addresses_on_notificationsettings_id` (`notificationsettings_id`),
  CONSTRAINT `fk_notificationsettings_addresses_notificationsettings` FOREIGN KEY (`notificationsettings_id`) REFERENCES `notificationsettings` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificationsettings_addresses`
--

LOCK TABLES `notificationsettings_addresses` WRITE;
/*!40000 ALTER TABLE `notificationsettings_addresses` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificationsettings_addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificationsettings_inspectiontypes`
--

DROP TABLE IF EXISTS `notificationsettings_inspectiontypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificationsettings_inspectiontypes` (
  `notificationsettings_id` bigint(20) NOT NULL,
  `inspectiontype_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `index_notificationsettings_inspectiontypes_on_notificationsetti` (`notificationsettings_id`),
  CONSTRAINT `fk_notificationsettings_inspectiontypes_notificationsettings` FOREIGN KEY (`notificationsettings_id`) REFERENCES `notificationsettings` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificationsettings_inspectiontypes`
--

LOCK TABLES `notificationsettings_inspectiontypes` WRITE;
/*!40000 ALTER TABLE `notificationsettings_inspectiontypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificationsettings_inspectiontypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificationsettings_producttypes`
--

DROP TABLE IF EXISTS `notificationsettings_producttypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificationsettings_producttypes` (
  `notificationsettings_id` bigint(20) NOT NULL,
  `producttype_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  KEY `index_notificationsettings_producttypes_on_notificationsettings` (`notificationsettings_id`),
  CONSTRAINT `fk_notificationsettings_producttypes_notificationsettings` FOREIGN KEY (`notificationsettings_id`) REFERENCES `notificationsettings` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificationsettings_producttypes`
--

LOCK TABLES `notificationsettings_producttypes` WRITE;
/*!40000 ALTER TABLE `notificationsettings_producttypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificationsettings_producttypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observations`
--

DROP TABLE IF EXISTS `observations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observations` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `text` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_observations_on_modifiedby` (`modifiedby`),
  KEY `index_observations_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_observations_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observations`
--

LOCK TABLES `observations` WRITE;
/*!40000 ALTER TABLE `observations` DISABLE KEYS */;
/*!40000 ALTER TABLE `observations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordermapping`
--

DROP TABLE IF EXISTS `ordermapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordermapping` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `organizationid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `externalsourceid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `orderkey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sourceorderkey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `ordermapping_uniquekeys_idx` (`externalsourceid`,`orderkey`,`organizationid`),
  KEY `ordermapping_quicklookup` (`externalsourceid`,`organizationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordermapping`
--

LOCK TABLES `ordermapping` WRITE;
/*!40000 ALTER TABLE `ordermapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordermapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `ordernumber` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ordertype` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `orderdate` datetime DEFAULT NULL,
  `description` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ponumber` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_orders_on_modifiedby` (`modifiedby`),
  KEY `index_orders_on_ordernumber` (`ordernumber`),
  KEY `index_orders_on_ordertype` (`ordertype`),
  KEY `index_orders_on_r_tenant` (`tenant_id`),
  KEY `fk_orders_owner` (`owner_id`),
  CONSTRAINT `fk_orders_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_base`
--

DROP TABLE IF EXISTS `org_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_base` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `addressinfo_id` bigint(20) DEFAULT NULL,
  `secondary_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `division_id` bigint(20) DEFAULT NULL,
  `global_id` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_org_base_on_external_id` (`global_id`),
  KEY `fk_org_base_users` (`modifiedby`),
  KEY `fk_org_base_tenants` (`tenant_id`),
  KEY `fk_org_base_addressinfo` (`addressinfo_id`),
  KEY `index_org_base_on_secondary_id` (`secondary_id`),
  KEY `index_org_base_on_customer_id` (`customer_id`),
  KEY `index_org_base_on_division_id` (`division_id`),
  CONSTRAINT `fk_org_base_addressinfo` FOREIGN KEY (`addressinfo_id`) REFERENCES `addressinfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_base_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_base_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15539092 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_base`
--

LOCK TABLES `org_base` WRITE;
/*!40000 ALTER TABLE `org_base` DISABLE KEYS */;
INSERT INTO `org_base` VALUES (15511550,'2009-08-04 22:28:05','2010-02-01 15:49:06',NULL,15511550,'Field ID',12432,NULL,NULL,NULL,'011296e2-19bd-4ea7-890b-61faa52057fe'),(15539065,'2010-02-01 16:05:46','2010-04-14 14:32:44',NULL,15511595,'Test Vendor',12433,NULL,NULL,NULL,'e1173190-e803-4023-894e-862c14de8833'),(15539066,'2010-02-01 16:09:11','2010-04-14 17:15:28',NULL,15511596,'Test Distributor',12434,NULL,NULL,NULL,'a317d5f7-a183-498a-a2e7-189df8ab12d4'),(15539067,'2010-02-01 16:09:11','2010-04-14 17:15:28',NULL,15511595,'Test Distributor',12435,NULL,15539067,NULL,'a8554479-5dae-48fc-861d-6e1631139523'),(15539068,'2010-03-30 20:00:14','2010-03-30 20:23:25',NULL,15511597,'National Company',12436,NULL,NULL,NULL,NULL),(15539069,'2010-03-30 20:03:38','2010-03-30 20:03:38',NULL,15511597,'West Coast',12437,15539069,NULL,NULL,NULL),(15539070,'2010-03-30 20:03:49','2010-03-30 20:03:59',NULL,15511597,'East Coast',12438,15539070,NULL,NULL,NULL),(15539071,'2010-03-30 20:04:33','2010-03-30 20:06:18',NULL,15511597,'Northern States Customer',12439,NULL,15539071,NULL,NULL),(15539072,'2010-03-30 20:04:45','2010-03-30 20:06:05',NULL,15511597,'Southern States Customer',12440,NULL,15539072,NULL,NULL),(15539073,'2010-03-30 20:05:38','2010-03-30 20:05:38',NULL,15511597,'bright SS division',12441,NULL,15539072,15539073,NULL),(15539074,'2010-03-30 20:05:52','2010-03-30 20:05:52',NULL,15511597,'dull SS division',12442,NULL,15539072,15539074,NULL),(15539075,'2010-03-30 20:06:38','2010-03-30 20:06:38',NULL,15511597,'relaxed NS division',12443,NULL,15539071,15539075,NULL),(15539076,'2010-03-30 20:06:53','2010-03-30 20:06:53',NULL,15511597,'uptight NS division',12444,NULL,15539071,15539076,NULL),(15539077,'2010-03-30 20:07:35','2010-03-30 20:07:35',NULL,15511597,'North Western WC Customer',12445,15539069,15539077,NULL,NULL),(15539078,'2010-03-30 20:07:58','2010-03-30 20:07:58',NULL,15511597,'Hot NW Division',12446,15539069,15539077,15539078,NULL),(15539079,'2010-03-30 20:08:13','2010-03-30 20:08:13',NULL,15511597,'cold NW division',12447,15539069,15539077,15539079,NULL),(15539080,'2010-03-30 20:08:43','2010-03-30 20:08:43',NULL,15511597,'South Western WC Customer',12448,15539069,15539080,NULL,NULL),(15539081,'2010-03-30 20:08:59','2010-03-30 20:08:59',NULL,15511597,'Red SW Division',12449,15539069,15539080,15539081,NULL),(15539082,'2010-03-30 20:09:17','2010-03-30 20:09:17',NULL,15511597,'Blue SW division',12450,15539069,15539080,15539082,NULL),(15539083,'2010-03-30 20:09:40','2010-03-30 20:09:40',NULL,15511597,'Upper Eastern  EC Customer',12451,15539070,15539083,NULL,NULL),(15539084,'2010-03-30 20:15:49','2010-03-30 20:15:49',NULL,15511597,'Left UE division',12452,15539070,15539083,15539084,NULL),(15539085,'2010-03-30 20:16:12','2010-03-30 20:16:12',NULL,15511597,'right UE divsion',12453,15539070,15539083,15539085,NULL),(15539086,'2010-03-30 20:21:48','2010-03-30 20:21:48',NULL,15511597,'Lower Eastern EC customer',12454,15539070,15539086,NULL,NULL),(15539087,'2010-03-30 20:22:06','2010-03-30 20:22:06',NULL,15511597,'Front LE division',12455,15539070,15539086,15539087,NULL),(15539088,'2010-03-30 20:22:26','2010-03-30 20:22:26',NULL,15511597,'Back LE Division',12456,15539070,15539086,15539088,NULL),(15539089,'2010-03-31 15:00:55','2010-03-31 15:00:55',NULL,15511595,'Non Linked Customer',12457,NULL,15539089,NULL,'636099b8-656d-4dc8-a0ef-053003b5083e'),(15539090,'2010-04-14 14:34:13','2010-04-14 14:34:13',NULL,15511595,'vendor-secondary',12458,15539090,NULL,NULL,'5412a9e1-ba68-4b10-afa5-88a5370ac2b5'),(15539091,'2010-04-14 17:15:01','2010-04-14 17:15:01',NULL,15511596,'Secondary Distributor',12459,15539091,NULL,NULL,'72d03015-8014-4ad8-bb8b-811d308e25ef');
/*!40000 ALTER TABLE `org_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_connections`
--

DROP TABLE IF EXISTS `org_connections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_connections` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `vendor_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_org_connections_unique` (`vendor_id`,`customer_id`),
  KEY `fk_org_connections_users` (`modifiedby`),
  KEY `fk_org_connection_customer` (`customer_id`),
  CONSTRAINT `fk_org_connections_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_connection_customer` FOREIGN KEY (`customer_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_connection_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_connections`
--

LOCK TABLES `org_connections` WRITE;
/*!40000 ALTER TABLE `org_connections` DISABLE KEYS */;
INSERT INTO `org_connections` VALUES (14,'2010-02-01 16:09:11','2010-02-01 16:09:11',15512597,15539065,15539066);
/*!40000 ALTER TABLE `org_connections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_customer`
--

DROP TABLE IF EXISTS `org_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_customer` (
  `org_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL,
  `code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contactemail` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contactname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `legacy_id` bigint(20) DEFAULT NULL,
  `linked_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`org_id`),
  KEY `fk_org_customer_parent` (`parent_id`),
  KEY `fk_customer_linked_org` (`linked_id`),
  CONSTRAINT `fk_customer_linked_org` FOREIGN KEY (`linked_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_customer_org_base` FOREIGN KEY (`org_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_customer_parent` FOREIGN KEY (`parent_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_customer`
--

LOCK TABLES `org_customer` WRITE;
/*!40000 ALTER TABLE `org_customer` DISABLE KEYS */;
INSERT INTO `org_customer` VALUES (15539067,15539065,NULL,'dev@fieldid.com',NULL,NULL,15539066),(15539071,15539068,'1','','',NULL,NULL),(15539072,15539068,'2','','',NULL,NULL),(15539077,15539069,'c3','','',NULL,NULL),(15539080,15539069,'c4','','',NULL,NULL),(15539083,15539070,'c5','','',NULL,NULL),(15539086,15539070,'c6','','',NULL,NULL),(15539089,15539065,'00001','','',NULL,NULL);
/*!40000 ALTER TABLE `org_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_division`
--

DROP TABLE IF EXISTS `org_division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_division` (
  `org_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL,
  `code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contactemail` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contactname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `legacy_id` bigint(20) DEFAULT NULL,
  `linked_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`org_id`),
  KEY `fk_org_division_parent` (`parent_id`),
  KEY `fk_division_linked_org` (`linked_id`),
  CONSTRAINT `fk_division_linked_org` FOREIGN KEY (`linked_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_division_org_base` FOREIGN KEY (`org_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_division_parent` FOREIGN KEY (`parent_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_division`
--

LOCK TABLES `org_division` WRITE;
/*!40000 ALTER TABLE `org_division` DISABLE KEYS */;
INSERT INTO `org_division` VALUES (15539073,15539072,'d1','','',NULL,NULL),(15539074,15539072,'d2','','',NULL,NULL),(15539075,15539071,'d3','','',NULL,NULL),(15539076,15539071,'d4','','',NULL,NULL),(15539078,15539077,'d5','','',NULL,NULL),(15539079,15539077,'d6','','',NULL,NULL),(15539081,15539080,'d7','','',NULL,NULL),(15539082,15539080,'d8','','',NULL,NULL),(15539084,15539083,'d9','','',NULL,NULL),(15539085,15539083,'d10','','',NULL,NULL),(15539087,15539086,'d11','','',NULL,NULL),(15539088,15539086,'D12','','',NULL,NULL);
/*!40000 ALTER TABLE `org_division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_extendedfeatures`
--

DROP TABLE IF EXISTS `org_extendedfeatures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_extendedfeatures` (
  `org_id` bigint(20) NOT NULL,
  `feature` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  KEY `fk_org_extendedfeatures_org_base` (`org_id`),
  CONSTRAINT `fk_org_extendedfeatures_org_base` FOREIGN KEY (`org_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_extendedfeatures`
--

LOCK TABLES `org_extendedfeatures` WRITE;
/*!40000 ALTER TABLE `org_extendedfeatures` DISABLE KEYS */;
INSERT INTO `org_extendedfeatures` VALUES (15511550,'Branding'),(15539065,'EmailAlerts'),(15539066,'EmailAlerts'),(15539068,'Projects'),(15539068,'MultiLocation'),(15539068,'Branding'),(15539068,'DedicatedProgramManager'),(15539068,'EmailAlerts'),(15539068,'AllowIntegration'),(15539068,'CustomCert'),(15539068,'PartnerCenter');
/*!40000 ALTER TABLE `org_extendedfeatures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_primary`
--

DROP TABLE IF EXISTS `org_primary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_primary` (
  `org_id` bigint(20) NOT NULL,
  `asset_limit` bigint(20) NOT NULL,
  `diskspace_limit` bigint(20) NOT NULL,
  `user_limit` bigint(20) NOT NULL,
  `serialnumberformat` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `usingserialnumber` tinyint(1) NOT NULL,
  `website` varchar(2056) COLLATE utf8_unicode_ci DEFAULT NULL,
  `certificatename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `defaulttimezone` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `dateformat` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `externalid` bigint(20) DEFAULT NULL,
  `org_limit` bigint(20) NOT NULL,
  `externalpassword` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `externalusername` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `autopublish` tinyint(1) DEFAULT NULL,
  `autoaccept` tinyint(1) NOT NULL,
  `plansandpricingavailable` tinyint(1) NOT NULL,
  `defaultvendorcontext` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`org_id`),
  CONSTRAINT `fk_org_primary_org_base` FOREIGN KEY (`org_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_primary`
--

LOCK TABLES `org_primary` WRITE;
/*!40000 ALTER TABLE `org_primary` DISABLE KEYS */;
INSERT INTO `org_primary` VALUES (15511550,-1,262144000,-1,'%y-%g',1,'http://www.fieldid.com','Field ID','United States:New York - New York','MM/dd/yy',NULL,-1,NULL,NULL,0,0,0,NULL),(15539065,250,1048576000,1,'NSA%y-%g',1,NULL,'Test Vendor','Canada:Ontario - Toronto','MM/dd/yy',3751441042252666889,1,'9be34cb57f749e5','dev@n4systems.com_',0,0,0,NULL),(15539066,250,1048576000,2,'NSA%y-%g',1,NULL,'Test Distributor','Canada:Ontario - Toronto','MM/dd/yy',1243471864012991432,2,'4d2475e6523f966','dev@n4systems.com_',0,0,0,15539065),(15539068,-1,1048576000,-1,'NSA%y-%g',1,NULL,'National Company','Canada:Ontario - Toronto','MM/dd/yy',1879964722215024616,-1,'dddf69334f5833d','dev@fieldid.com',0,0,0,NULL);
/*!40000 ALTER TABLE `org_primary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_secondary`
--

DROP TABLE IF EXISTS `org_secondary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_secondary` (
  `org_id` bigint(20) NOT NULL,
  `primaryorg_id` bigint(20) NOT NULL,
  `certificatename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `defaulttimezone` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`org_id`),
  KEY `fk_org_secondary_primaryorg` (`primaryorg_id`),
  CONSTRAINT `fk_org_secondary_org_base` FOREIGN KEY (`org_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_org_secondary_primaryorg` FOREIGN KEY (`primaryorg_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_secondary`
--

LOCK TABLES `org_secondary` WRITE;
/*!40000 ALTER TABLE `org_secondary` DISABLE KEYS */;
INSERT INTO `org_secondary` VALUES (15539069,15539068,'West Coast','Canada:Ontario - Toronto'),(15539070,15539068,'East Coast','Canada:Ontario - Toronto'),(15539090,15539065,'','Canada:Ontario - Toronto'),(15539091,15539066,'','Canada:Ontario - Toronto');
/*!40000 ALTER TABLE `org_secondary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `displayname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `certificatename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `r_addressinfo` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `usingserialnumber` tinyint(1) NOT NULL DEFAULT '1',
  `adminemail` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `serialnumberformat` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `r_tenant` bigint(20) DEFAULT NULL,
  `fidac` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `accountdiscriminator` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dateformat` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `website` varchar(2056) COLLATE utf8_unicode_ci DEFAULT NULL,
  `diskspace_limit` bigint(20) DEFAULT NULL,
  `user_limit` bigint(20) DEFAULT NULL,
  `asset_limit` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_organization_on_snac` (`fidac`),
  UNIQUE KEY `index_organization_on_name` (`name`),
  KEY `index_organization_on_modifiedby` (`modifiedby`),
  KEY `index_organization_on_parent_id` (`parent_id`),
  KEY `index_organization_on_r_addressinfo` (`r_addressinfo`),
  KEY `index_organization_on_r_tenant` (`r_tenant`),
  CONSTRAINT `fk_organization_addressinfo` FOREIGN KEY (`r_addressinfo`) REFERENCES `addressinfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_organization_parent` FOREIGN KEY (`parent_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_organization_tenant` FOREIGN KEY (`r_tenant`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_organization_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_extendedfeatures`
--

DROP TABLE IF EXISTS `organization_extendedfeatures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_extendedfeatures` (
  `organization_id` bigint(20) NOT NULL,
  `element` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  KEY `index_organization_extendedfeatures_on_organization_id` (`organization_id`),
  CONSTRAINT `fk_organization_extendedfeatures_organization` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_extendedfeatures`
--

LOCK TABLES `organization_extendedfeatures` WRITE;
/*!40000 ALTER TABLE `organization_extendedfeatures` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_extendedfeatures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `populatorlog`
--

DROP TABLE IF EXISTS `populatorlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `populatorlog` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `timelogged` datetime DEFAULT NULL,
  `logmessage` text COLLATE utf8_unicode_ci,
  `logstatus` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logtype` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_populatorlog_on_r_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `populatorlog`
--

LOCK TABLES `populatorlog` WRITE;
/*!40000 ALTER TABLE `populatorlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `populatorlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `printouts`
--

DROP TABLE IF EXISTS `printouts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `printouts` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `pdftemplate` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `custom` tinyint(1) DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `withsubinspections` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_printouts_on_custom` (`custom`),
  KEY `index_printouts_on_modifiedby` (`modifiedby`),
  KEY `index_printouts_on_tenant_id` (`tenant_id`),
  KEY `index_printouts_on_type` (`type`),
  CONSTRAINT `fk_printouts_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_printouts_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `printouts`
--

LOCK TABLES `printouts` WRITE;
/*!40000 ALTER TABLE `printouts` DISABLE KEYS */;
INSERT INTO `printouts` VALUES (1,'2009-05-03 18:02:48','2009-08-18 20:39:25','Visual Inspection','Basic Visual Inspection','visual_inspection',0,'CERT',NULL,NULL,0),(2,'2009-05-03 18:02:48','2009-05-03 18:02:48','Repair','Repair','repair',0,'CERT',NULL,NULL,0),(3,'2009-05-03 18:02:48','2009-08-18 12:24:15','Proof Test','Proof Test w/ Graph','proof_test',0,'CERT',NULL,NULL,0),(4,'2009-05-03 18:02:48','2009-11-12 17:37:28','Observation Report','Basic Observation Report','observations',0,'OBSERVATION',NULL,NULL,0),(60,'2009-05-12 16:39:09','2009-08-18 13:06:17','This certificate will show all inspection information as well as observations that were recorded.','1 Column Visual Inspection','inspection_sub',0,'CERT',NULL,NULL,1),(67,'2009-05-21 14:29:48','2009-08-18 13:06:53','','Proof Test','proof_test_nograph',0,'CERT',NULL,NULL,0),(70,'2009-07-02 20:11:59','2009-08-18 12:31:07','','2 Column Visual Inspection','vi-2-column',0,'CERT',NULL,NULL,1),(71,'2009-07-02 20:12:14','2009-11-12 17:37:32','','Basic Observation Report','vi-2-observations',0,'OBSERVATION',NULL,NULL,0),(77,'2009-08-04 20:03:29','2009-08-18 12:54:03','Report that will show all inspection criteria in a 3 column format.','3 Column Visual Inspection','vi-3-column',0,'CERT',NULL,NULL,1),(78,'2009-08-07 13:54:13','2009-08-18 14:08:14','This report will show all observations recorded as well as the product attributes.','Observation Report w/ Attributes','observations_w_attributes',0,'OBSERVATION',NULL,NULL,0),(98,'2009-10-03 18:07:47','2009-10-03 18:09:26','This report will show all visual inspection information with any recorded observations inline with the checklist items.','2 Column Visual Inspection with Observations','vi-2-column-inline-observations',0,'CERT',NULL,NULL,1),(104,'2009-11-17 22:44:14','2009-11-17 22:44:14','','Full Observation Report','full_observations',0,'OBSERVATION',NULL,NULL,1),(108,'2009-12-18 16:27:31','2009-12-18 16:27:31','','Visual Inspection without Criteria','visual_inspection_nocriteria',0,'CERT',NULL,NULL,0);
/*!40000 ALTER TABLE `printouts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productattachments`
--

DROP TABLE IF EXISTS `productattachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productattachments` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  `comment` text COLLATE utf8_unicode_ci,
  `filename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_productattachments_on_product_id` (`product_id`),
  KEY `fk_productattachments_tenants` (`tenant_id`),
  CONSTRAINT `fk_productattachments_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_productattachments_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productattachments`
--

LOCK TABLES `productattachments` WRITE;
/*!40000 ALTER TABLE `productattachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `productattachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productcodemapping`
--

DROP TABLE IF EXISTS `productcodemapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productcodemapping` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `productcode` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `r_productinfo` bigint(20) NOT NULL,
  `customerrefnumber` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_productcodemapping_on_r_productinfo` (`r_productinfo`),
  KEY `fk_productcodemapping_organization` (`tenant_id`),
  CONSTRAINT `fk_productcodemapping_producttypes` FOREIGN KEY (`r_productinfo`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productcodemapping`
--

LOCK TABLES `productcodemapping` WRITE;
/*!40000 ALTER TABLE `productcodemapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `productcodemapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productcodemapping_infooption`
--

DROP TABLE IF EXISTS `productcodemapping_infooption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productcodemapping_infooption` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_infooption` bigint(20) NOT NULL,
  `r_productcodemapping` bigint(20) NOT NULL,
  `r_tenant` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_productcodemapping_infooption_on_r_infooption` (`r_infooption`),
  KEY `index_productcodemapping_infooption_on_r_productcodemapping` (`r_productcodemapping`),
  CONSTRAINT `fk_productcodemapping_infooption_infooption` FOREIGN KEY (`r_infooption`) REFERENCES `infooption` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_productcodemapping_infooption_productcodemapping` FOREIGN KEY (`r_productcodemapping`) REFERENCES `productcodemapping` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productcodemapping_infooption`
--

LOCK TABLES `productcodemapping_infooption` WRITE;
/*!40000 ALTER TABLE `productcodemapping_infooption` DISABLE KEYS */;
/*!40000 ALTER TABLE `productcodemapping_infooption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime DEFAULT NULL,
  `rfidnumber` varchar(46) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `shoporder_id` bigint(20) DEFAULT NULL,
  `serialnumber` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `comments` varchar(2047) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type_id` bigint(20) DEFAULT NULL,
  `productstatus_uniqueid` bigint(20) DEFAULT NULL,
  `mobileguid` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `customerrefnumber` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `identifiedby_uniqueid` bigint(20) DEFAULT NULL,
  `customerorder_id` bigint(20) DEFAULT NULL,
  `lastinspectiondate` datetime DEFAULT NULL,
  `purchaseorder` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `identified` datetime NOT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `assigneduser_id` bigint(20) DEFAULT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `archivedserialnumber` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  `published` bit(1) NOT NULL,
  `linked_id` bigint(20) DEFAULT NULL,
  `countstowardslimit` bit(1) NOT NULL,
  `network_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `productserial_uniqueid_idx` (`id`),
  KEY `index_products_on_assigneduser_id` (`assigneduser_id`),
  KEY `index_products_on_customerorder_id` (`customerorder_id`),
  KEY `index_products_on_identifiedby_uniqueid` (`identifiedby_uniqueid`),
  KEY `index_products_on_mobileguid` (`mobileguid`),
  KEY `index_products_on_modifiedby` (`modifiedby`),
  KEY `index_products_on_productstatus_uniqueid` (`productstatus_uniqueid`),
  KEY `index_productserial_on_purchaseorder` (`purchaseorder`),
  KEY `smart_search_customer_ref_number` (`tenant_id`),
  KEY `smart_search_rfid` (`tenant_id`),
  KEY `smart_search_serial_number` (`tenant_id`),
  KEY `index_products_on_rfidnumber` (`rfidnumber`),
  KEY `index_products_on_serialnumber` (`serialnumber`),
  KEY `index_products_on_shoporder_id` (`shoporder_id`),
  KEY `productserial_rordermaster_idx` (`shoporder_id`),
  KEY `index_products_on_state` (`state`),
  KEY `index_products_on_type_id` (`type_id`),
  KEY `fk_products_owner` (`owner_id`),
  KEY `index_products_on_published` (`published`),
  KEY `fk_linked_product_id` (`linked_id`),
  KEY `index_products_on_countstowardslimit` (`countstowardslimit`),
  KEY `index_products_on_network_id` (`network_id`),
  CONSTRAINT `fk_linked_product_id` FOREIGN KEY (`linked_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_lineitems` FOREIGN KEY (`shoporder_id`) REFERENCES `lineitems` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_modified_by_user` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_orders` FOREIGN KEY (`customerorder_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_productstatus` FOREIGN KEY (`productstatus_uniqueid`) REFERENCES `productstatus` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_producttypes` FOREIGN KEY (`type_id`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_users` FOREIGN KEY (`assigneduser_id`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_proudcts_identifiedby_user` FOREIGN KEY (`identifiedby_uniqueid`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'2010-03-30 21:12:24','2010-03-30 21:37:43','1FFFFFFFFFFFFFFFFFF',15511597,NULL,'N-0001','',6798,486,NULL,'1',15512599,NULL,'2010-03-30 21:37:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539068,'\0',NULL,'',1),(2,'2010-03-30 21:12:24','2010-03-30 21:39:07','2FFFFFFFFFFFFFFFFFF',15511597,NULL,'WC-0002','',6798,486,NULL,'2',15512599,NULL,'2010-03-30 21:38:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539069,'\0',NULL,'',2),(3,'2010-03-30 21:12:24','2010-03-30 21:37:23','3FFFFFFFFFFFFFFFFFF',15511597,NULL,'WC-NW-0003','',6798,486,NULL,'3',15512599,NULL,'2010-03-30 21:37:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539077,'\0',NULL,'',3),(4,'2010-03-30 21:12:24','2010-03-30 21:36:59','4FFFFFFFFFFFFFFFFFF',15511597,NULL,'WC-NW-HOT-0004','',6798,486,NULL,'4',15512599,NULL,'2010-03-30 21:36:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539078,'\0',NULL,'',4),(5,'2010-03-30 21:12:24','2010-03-30 21:36:43','5FFFFFFFFFFFFFFFFFF',15511597,NULL,'WC-NW-COLD-0005','',6798,486,NULL,'5',15512599,NULL,'2010-03-30 21:36:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539079,'\0',NULL,'',5),(6,'2010-03-30 21:12:24','2010-03-30 21:36:28','6FFFFFFFFFFFFFFFFFF',15511597,NULL,'WC-SW-0006','',6798,486,NULL,'6',15512599,NULL,'2010-03-30 21:36:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539080,'\0',NULL,'',6),(7,'2010-03-30 21:12:24','2010-03-30 21:36:14','7FFFFFFFFFFFFFFFFFF',15511597,NULL,'WC-SW-RED-0007','',6798,486,NULL,'7',15512599,NULL,'2010-03-30 21:36:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539081,'\0',NULL,'',7),(8,'2010-03-30 21:12:24','2010-03-30 21:35:59','8FFFFFFFFFFFFFFFFFF',15511597,NULL,'WC-SW-BLUE-0008','',6798,486,NULL,'8',15512599,NULL,'2010-03-30 21:35:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539082,'\0',NULL,'',8),(9,'2010-03-30 21:12:24','2010-03-30 21:35:43','9FFFFFFFFFFFFFFFFFF',15511597,NULL,'EC-0009','',6798,486,NULL,'9',15512599,NULL,'2010-03-30 21:35:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539070,'\0',NULL,'',9),(10,'2010-03-30 21:12:24','2010-03-30 21:35:27','10FFFFFFFFFFFFFFFFF',15511597,NULL,'EC-UE-0010','',6798,486,NULL,'10',15512599,NULL,'2010-03-30 21:35:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539083,'\0',NULL,'',10),(11,'2010-03-30 21:12:24','2010-03-30 21:35:10','11FFFFFFFFFFFFFFFFF',15511597,NULL,'EC-UE-LEFT-0011','',6798,486,NULL,'11',15512599,NULL,'2010-03-30 21:35:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539084,'\0',NULL,'',11),(12,'2010-03-30 21:12:24','2010-03-30 21:34:56','12FFFFFFFFFFFFFFFFF',15511597,NULL,'EC-UE-RIGHT-0012','',6798,486,NULL,'12',15512599,NULL,'2010-03-30 21:34:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539085,'\0',NULL,'',12),(13,'2010-03-30 21:12:24','2010-03-30 21:34:43','13FFFFFFFFFFFFFFFFF',15511597,NULL,'EC-LE-0013','',6798,486,NULL,'13',15512599,NULL,'2010-03-30 21:34:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539086,'\0',NULL,'',13),(14,'2010-03-30 21:12:25','2010-03-30 21:34:26','14FFFFFFFFFFFFFFFFF',15511597,NULL,'EC-LE-FRONT-0014','',6798,486,NULL,'14',15512599,NULL,'2010-03-30 21:34:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539087,'\0',NULL,'',14),(15,'2010-03-30 21:12:25','2010-03-30 21:34:11','15FFFFFFFFFFFFFFFFF',15511597,NULL,'EC-LE-BACK-0015','',6798,486,NULL,'15',15512599,NULL,'2010-03-30 21:34:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539088,'\0',NULL,'',15),(16,'2010-03-30 21:12:25','2010-03-30 21:33:39','16FFFFFFFFFFFFFFFFF',15511597,NULL,'NS-0016','',6798,486,NULL,'16',15512599,NULL,'2010-03-30 21:33:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539071,'\0',NULL,'',16),(17,'2010-03-30 21:12:25','2010-03-30 21:33:20','17FFFFFFFFFFFFFFFFF',15511597,NULL,'NS-RELAX-0017','',6798,486,NULL,'17',15512599,NULL,'2010-03-30 21:33:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539075,'\0',NULL,'',17),(18,'2010-03-30 21:12:25','2010-03-30 21:32:57','18FFFFFFFFFFFFFFFFF',15511597,NULL,'NS-UPTIGHT-0018','',6798,486,NULL,'18',15512599,NULL,'2010-03-30 21:32:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539076,'\0',NULL,'',18),(19,'2010-03-30 21:12:25','2010-03-30 21:32:28','19FFFFFFFFFFFFFFFFF',15511597,NULL,'SS-0019','',6798,486,NULL,'19',15512599,NULL,'2010-03-30 21:32:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539072,'\0',NULL,'',19),(20,'2010-03-30 21:12:25','2010-03-30 21:32:10','20FFFFFFFFFFFFFFFFF',15511597,NULL,'SS-BRIGHT-0020','',6798,486,NULL,'20',15512599,NULL,'2010-03-30 21:31:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539073,'\0',NULL,'',20),(21,'2010-03-30 21:12:25','2010-03-30 21:31:51','21FFFFFFFFFFFFFFFFF',15511597,NULL,'SS-DULL-0021','dull',6798,486,NULL,'21',15512599,NULL,'2010-03-30 21:31:00','','2010-03-30 00:00:00','Good Place',15512599,NULL,'ACTIVE',NULL,15539074,'\0',NULL,'',21),(22,'2010-03-31 14:57:29','2010-04-13 18:08:55','1AAAAAAAAAAAAAAAAA',15511595,NULL,'Vendor-00001-published','Vendor Comment',6799,476,NULL,'',15512595,NULL,'2010-04-13 18:08:00','','2010-03-31 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539065,'',NULL,'',22),(23,'2010-03-31 14:58:57','2010-03-31 14:58:57','2AAAAAAAAAAAAAAAAA',15511595,NULL,'Vendor-00002','',6799,476,NULL,'',15512595,NULL,NULL,'','2010-03-31 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539065,'\0',NULL,'',23),(24,'2010-03-31 14:59:25','2010-04-13 21:43:30',NULL,15511595,NULL,'Vendor-00003-published','',6799,476,NULL,'',15512595,NULL,'2010-04-13 21:43:00','','2010-03-31 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539067,'',NULL,'',24),(25,'2010-03-31 14:59:53','2010-03-31 14:59:53','4AAAAAAAAAAAAAAAAA',15511595,NULL,'Vendor-00004','',6799,476,NULL,'',15512595,NULL,NULL,'','2010-03-31 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539067,'\0',NULL,'',25),(26,'2010-03-31 15:01:37','2010-03-31 15:01:37',NULL,15511595,NULL,'Vendor-00005-published','',6799,476,NULL,'',15512595,NULL,NULL,'','2010-03-31 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539089,'',NULL,'',26),(27,'2010-03-31 15:02:02','2010-03-31 15:02:02','6AAAAAAAAAAAAAAAAA',15511595,NULL,'Vendor-00006','',6799,476,NULL,'',15512595,NULL,NULL,'','2010-03-31 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539089,'\0',NULL,'',27),(28,'2010-04-14 14:34:42','2010-04-14 14:34:53',NULL,15511595,NULL,'Vendor-00007','',6799,476,NULL,'',15512595,NULL,'2010-04-14 14:34:00','','2010-04-14 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539090,'\0',NULL,'',28),(29,'2010-04-14 14:35:18','2010-04-14 14:50:32',NULL,15511595,NULL,'Vendor-00008-published','',6799,476,NULL,'',15512595,NULL,'2010-04-14 14:50:00','','2010-04-14 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539090,'',NULL,'',29),(30,'2010-04-14 14:59:34','2010-04-14 14:59:40',NULL,15511595,NULL,'Vendor-00009-registerd','',6799,476,NULL,'',15512595,NULL,'2010-04-14 14:59:00','','2010-04-14 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539065,'',NULL,'',30),(31,'2010-04-14 15:00:13','2010-04-14 15:00:18',NULL,15511595,NULL,'Vendor-00010-registerd-assigned','',6799,476,NULL,'',15512595,NULL,'2010-04-14 15:00:00','','2010-04-14 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539067,'',NULL,'',31),(32,'2010-04-14 15:00:47','2010-04-14 15:00:52',NULL,15511595,NULL,'Vendor-00011-registerd-secondary','',6799,476,NULL,'',15512595,NULL,'2010-04-14 15:00:00','','2010-04-14 00:00:00','Vendor store',15512595,NULL,'ACTIVE',NULL,15539090,'',NULL,'',32),(33,'2010-04-14 15:03:12','2010-04-14 15:03:12',NULL,15511596,NULL,'Vendor-00009-registerd','',6797,NULL,NULL,'',15512597,NULL,NULL,'','2010-04-14 00:00:00','',15512597,NULL,'ACTIVE',NULL,15539066,'\0',30,'',30),(34,'2010-04-14 15:03:41','2010-04-14 15:03:41',NULL,15511596,NULL,'Vendor-00010-registerd-assigned','',6797,NULL,NULL,'',15512597,NULL,NULL,'','2010-04-14 00:00:00','',15512597,NULL,'ACTIVE',NULL,15539066,'\0',31,'',31),(35,'2010-04-14 16:01:45','2010-04-14 16:01:45',NULL,15511596,NULL,'Vendor-00011-registerd-secondary','',6797,NULL,NULL,'',15512597,NULL,NULL,'','2010-04-14 00:00:00','',15512597,NULL,'ACTIVE',NULL,15539066,'\0',32,'',32);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productserial_infooption`
--

DROP TABLE IF EXISTS `productserial_infooption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productserial_infooption` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_productserial` bigint(20) NOT NULL,
  `r_infooption` bigint(20) NOT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_productserial_infooption_on_r_infooption` (`r_infooption`),
  KEY `index_productserial_infooption_on_r_productserial` (`r_productserial`),
  CONSTRAINT `fk_productserial_infooption_infooption` FOREIGN KEY (`r_infooption`) REFERENCES `infooption` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_productserial_infooption_products` FOREIGN KEY (`r_productserial`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productserial_infooption`
--

LOCK TABLES `productserial_infooption` WRITE;
/*!40000 ALTER TABLE `productserial_infooption` DISABLE KEYS */;
INSERT INTO `productserial_infooption` VALUES (1,22,3),(2,22,6),(3,22,1),(4,22,5),(5,23,9),(6,23,7),(7,23,8),(8,23,1),(9,24,1),(10,24,10),(11,24,12),(12,24,11),(13,25,1),(14,25,13),(15,25,3),(16,25,14),(17,26,1),(18,26,16),(19,26,3),(20,26,15),(21,27,18),(22,27,3),(23,27,17),(24,27,1),(25,28,19),(26,28,20),(27,28,1),(28,28,3),(29,29,1),(30,29,3),(31,29,21),(32,29,22),(33,30,3),(34,30,24),(35,30,1),(36,30,23),(37,31,26),(38,31,3),(39,31,25),(40,31,1),(41,32,27),(42,32,3),(43,32,1),(44,32,28);
/*!40000 ALTER TABLE `productserial_infooption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productserialextension`
--

DROP TABLE IF EXISTS `productserialextension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productserialextension` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `extensionkey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `extensionlabel` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `productserialextension_keypermanufacture` (`extensionkey`,`tenant_id`),
  KEY `index_productserialextension_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_productserialextension_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productserialextension`
--

LOCK TABLES `productserialextension` WRITE;
/*!40000 ALTER TABLE `productserialextension` DISABLE KEYS */;
/*!40000 ALTER TABLE `productserialextension` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productserialextensionvalue`
--

DROP TABLE IF EXISTS `productserialextensionvalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productserialextensionvalue` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_productserial` bigint(20) NOT NULL,
  `r_productserialextension` bigint(20) NOT NULL,
  `extensionvalue` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `productserialextensionvalue_oneperproduct` (`r_productserial`,`r_productserialextension`),
  KEY `index_productserialextensionvalue_on_r_productserial` (`r_productserial`),
  KEY `index_productserialextensionvalue_on_r_productserialextension` (`r_productserialextension`),
  CONSTRAINT `fk_productserialextensionvalue_products` FOREIGN KEY (`r_productserial`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_productserialextensionvalue_productserialextension` FOREIGN KEY (`r_productserialextension`) REFERENCES `productserialextension` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productserialextensionvalue`
--

LOCK TABLES `productserialextensionvalue` WRITE;
/*!40000 ALTER TABLE `productserialextensionvalue` DISABLE KEYS */;
/*!40000 ALTER TABLE `productserialextensionvalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productstatus`
--

DROP TABLE IF EXISTS `productstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productstatus` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `datecreated` datetime DEFAULT NULL,
  `datemodified` datetime DEFAULT NULL,
  `modifiedby` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  KEY `index_productstatus_on_r_tenant` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=491 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productstatus`
--

LOCK TABLES `productstatus` WRITE;
/*!40000 ALTER TABLE `productstatus` DISABLE KEYS */;
INSERT INTO `productstatus` VALUES (476,'In Service',15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL),(477,'Out of Service',15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL),(478,'In for Repair',15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL),(479,'In need of Repair',15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL),(480,'Destroyed',15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL),(481,'In Service',15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL),(482,'Out of Service',15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL),(483,'In for Repair',15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL),(484,'In need of Repair',15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL),(485,'Destroyed',15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL),(486,'In Service',15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL),(487,'Out of Service',15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL),(488,'In for Repair',15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL),(489,'In need of Repair',15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL),(490,'Destroyed',15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL);
/*!40000 ALTER TABLE `productstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producttypegroups`
--

DROP TABLE IF EXISTS `producttypegroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttypegroups` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `name` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_producttypegroups_on_r_tenant_and_name` (`name`,`tenant_id`),
  KEY `fk_producttypegroups_tenants` (`tenant_id`),
  CONSTRAINT `fk_producttypegroups_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producttypegroups`
--

LOCK TABLES `producttypegroups` WRITE;
/*!40000 ALTER TABLE `producttypegroups` DISABLE KEYS */;
/*!40000 ALTER TABLE `producttypegroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producttypes`
--

DROP TABLE IF EXISTS `producttypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttypes` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `cautionurl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `instructions` varchar(2047) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `warnings` varchar(2047) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `hasmanufacturecertificate` tinyint(1) DEFAULT NULL,
  `manufacturecertificatetext` varchar(2001) COLLATE utf8_unicode_ci DEFAULT NULL,
  `descriptiontemplate` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imagename` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `archivedname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `productinfo_itemnumber_key` (`name`,`tenant_id`),
  KEY `index_producttypes_on_group_id` (`group_id`),
  KEY `productinfo_uniqueid_idx` (`id`),
  KEY `index_producttypes_on_modifiedby` (`modifiedby`),
  KEY `index_producttypes_on_r_tenant` (`tenant_id`),
  KEY `index_producttypes_on_state` (`state`),
  CONSTRAINT `fk_producttypes_producttypegroups` FOREIGN KEY (`group_id`) REFERENCES `producttypegroups` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_producttypes_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6800 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producttypes`
--

LOCK TABLES `producttypes` WRITE;
/*!40000 ALTER TABLE `producttypes` DISABLE KEYS */;
INSERT INTO `producttypes` VALUES (6796,NULL,NULL,'*',NULL,'2010-02-01 16:05:46','2010-02-01 16:05:46',15511595,0,NULL,NULL,NULL,NULL,'ACTIVE',NULL,NULL),(6797,NULL,NULL,'*',NULL,'2010-02-01 16:09:11','2010-02-01 16:09:11',15511596,0,NULL,NULL,NULL,NULL,'ACTIVE',NULL,NULL),(6798,NULL,NULL,'*',NULL,'2010-03-30 20:00:14','2010-03-30 21:31:35',15511597,0,NULL,NULL,NULL,NULL,'ACTIVE',NULL,NULL),(6799,'http://www.n4systems.com','Use chain correctly','Chain','Warning of the chain','2010-03-31 14:53:10','2010-03-31 14:53:10',15511595,0,'','',NULL,NULL,'ACTIVE',NULL,NULL);
/*!40000 ALTER TABLE `producttypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producttypes_fileattachments`
--

DROP TABLE IF EXISTS `producttypes_fileattachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttypes_fileattachments` (
  `producttypes_id` bigint(20) NOT NULL,
  `attachments_id` bigint(20) NOT NULL,
  UNIQUE KEY `producttypes_fileattachments_attachments_id_key` (`attachments_id`),
  KEY `index_producttypes_fileattachments_on_attachments_id` (`attachments_id`),
  KEY `index_producttypes_fileattachments_on_producttypes_id` (`producttypes_id`),
  CONSTRAINT `fk_producttypes_fileattachments_fileattachments` FOREIGN KEY (`attachments_id`) REFERENCES `fileattachments` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_producttypes_fileattachments_producttypes` FOREIGN KEY (`producttypes_id`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producttypes_fileattachments`
--

LOCK TABLES `producttypes_fileattachments` WRITE;
/*!40000 ALTER TABLE `producttypes_fileattachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `producttypes_fileattachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producttypes_producttypes`
--

DROP TABLE IF EXISTS `producttypes_producttypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttypes_producttypes` (
  `producttypes_id` bigint(20) NOT NULL,
  `subtypes_id` bigint(20) NOT NULL,
  KEY `index_producttypes_producttypes_on_subtypes_id` (`subtypes_id`),
  KEY `fk_producttypes_producttypes_master_type` (`producttypes_id`),
  CONSTRAINT `fk_producttypes_producttypes_master_type` FOREIGN KEY (`producttypes_id`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_producttypes_producttypes_sub_type` FOREIGN KEY (`subtypes_id`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producttypes_producttypes`
--

LOCK TABLES `producttypes_producttypes` WRITE;
/*!40000 ALTER TABLE `producttypes_producttypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `producttypes_producttypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producttypeschedules`
--

DROP TABLE IF EXISTS `producttypeschedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producttypeschedules` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `frequency` bigint(20) DEFAULT NULL,
  `producttype_id` bigint(20) NOT NULL,
  `autoschedule` tinyint(1) DEFAULT NULL,
  `inspectiontype_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_producttypeschedules_on_inspectiontype_id` (`inspectiontype_id`),
  KEY `index_producttypeschedules_on_modifiedby` (`modifiedby`),
  KEY `index_producttypeschedules_on_producttype_id` (`producttype_id`),
  KEY `index_producttypeschedules_on_r_tenant` (`tenant_id`),
  KEY `fk_producttypeschedules_owner` (`owner_id`),
  CONSTRAINT `fk_producttypeschedules_inspectiontypes` FOREIGN KEY (`inspectiontype_id`) REFERENCES `inspectiontypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_producttypeschedules_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_producttypeschedules_producttypes` FOREIGN KEY (`producttype_id`) REFERENCES `producttypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_producttypeschedules_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producttypeschedules`
--

LOCK TABLES `producttypeschedules` WRITE;
/*!40000 ALTER TABLE `producttypeschedules` DISABLE KEYS */;
INSERT INTO `producttypeschedules` VALUES (1,15511597,'2010-03-30 21:31:35','2010-03-30 21:31:35',NULL,365,6798,1,1,15539068);
/*!40000 ALTER TABLE `producttypeschedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projects` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `projectid` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `started` datetime DEFAULT NULL,
  `estimatedcompletion` datetime DEFAULT NULL,
  `actualcompletion` datetime DEFAULT NULL,
  `duration` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `retired` tinyint(1) DEFAULT NULL,
  `description` varchar(1001) COLLATE utf8_unicode_ci DEFAULT NULL,
  `workperformed` varchar(1001) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ponumber` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `open` tinyint(1) DEFAULT NULL,
  `eventjob` tinyint(1) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_projects_on_r_tenant_and_projectid` (`projectid`,`tenant_id`),
  KEY `index_projects_on_name` (`name`),
  KEY `index_projects_on_r_tenant` (`tenant_id`),
  KEY `index_projects_on_status` (`status`),
  KEY `fk_projects_owner` (`owner_id`),
  CONSTRAINT `fk_projects_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects`
--

LOCK TABLES `projects` WRITE;
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects_fileattachments`
--

DROP TABLE IF EXISTS `projects_fileattachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projects_fileattachments` (
  `projects_id` bigint(20) NOT NULL,
  `notes_id` bigint(20) NOT NULL,
  UNIQUE KEY `projects_fileattachments_notes_id_key` (`notes_id`),
  KEY `index_projects_fileattachments_on_notes_id` (`notes_id`),
  KEY `fk_projects_fileattachments_projects` (`projects_id`),
  CONSTRAINT `fk_projects_fileattachments_fileattachments` FOREIGN KEY (`notes_id`) REFERENCES `fileattachments` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_projects_fileattachments_projects` FOREIGN KEY (`projects_id`) REFERENCES `projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects_fileattachments`
--

LOCK TABLES `projects_fileattachments` WRITE;
/*!40000 ALTER TABLE `projects_fileattachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `projects_fileattachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects_products`
--

DROP TABLE IF EXISTS `projects_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projects_products` (
  `projects_id` bigint(20) NOT NULL,
  `products_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) DEFAULT NULL,
  KEY `index_projects_products_on_projects_id_and_products_id` (`products_id`,`projects_id`),
  KEY `index_projects_products_on_products_id` (`products_id`),
  KEY `index_projects_products_on_projects_id` (`projects_id`),
  CONSTRAINT `fk_projects_products_products` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_projects_products_projects` FOREIGN KEY (`projects_id`) REFERENCES `projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects_products`
--

LOCK TABLES `projects_products` WRITE;
/*!40000 ALTER TABLE `projects_products` DISABLE KEYS */;
/*!40000 ALTER TABLE `projects_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects_users`
--

DROP TABLE IF EXISTS `projects_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projects_users` (
  `projects_id` bigint(20) NOT NULL,
  `resources_uniqueid` bigint(20) NOT NULL,
  UNIQUE KEY `index_projects_users_on_projects_id_and_resources_uniqueid` (`projects_id`,`resources_uniqueid`),
  KEY `index_projects_users_on_projects_id` (`projects_id`),
  KEY `index_projects_users_on_resources_uniqueid` (`resources_uniqueid`),
  CONSTRAINT `fk_projects_users_projects` FOREIGN KEY (`projects_id`) REFERENCES `projects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_projects_users_users` FOREIGN KEY (`resources_uniqueid`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects_users`
--

LOCK TABLES `projects_users` WRITE;
/*!40000 ALTER TABLE `projects_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `projects_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promocode_extendedfeatures`
--

DROP TABLE IF EXISTS `promocode_extendedfeatures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promocode_extendedfeatures` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `promocode_id` bigint(20) NOT NULL,
  `feature` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_promocode_extendedfeatures_promocodes` (`promocode_id`),
  CONSTRAINT `fk_promocode_extendedfeatures_promocodes` FOREIGN KEY (`promocode_id`) REFERENCES `promocodes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promocode_extendedfeatures`
--

LOCK TABLES `promocode_extendedfeatures` WRITE;
/*!40000 ALTER TABLE `promocode_extendedfeatures` DISABLE KEYS */;
/*!40000 ALTER TABLE `promocode_extendedfeatures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promocodes`
--

DROP TABLE IF EXISTS `promocodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promocodes` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `diskspace_limit` bigint(20) NOT NULL,
  `asset_limit` bigint(20) DEFAULT NULL,
  `secondary_org_limit` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_promocodes_on_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promocodes`
--

LOCK TABLES `promocodes` WRITE;
/*!40000 ALTER TABLE `promocodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `promocodes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requesttransactions`
--

DROP TABLE IF EXISTS `requesttransactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requesttransactions` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_requesttransactions_on_name` (`name`),
  KEY `fk_requesttransactions_users` (`modifiedby`),
  KEY `fk_requesttransactions_organization` (`tenant_id`),
  CONSTRAINT `fk_requesttransactions_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requesttransactions`
--

LOCK TABLES `requesttransactions` WRITE;
/*!40000 ALTER TABLE `requesttransactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `requesttransactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `savedreports`
--

DROP TABLE IF EXISTS `savedreports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `savedreports` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `sortcolumn` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `sortdirection` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sharedbyname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_savedreports_on_modifiedby` (`modifiedby`),
  KEY `index_savedreports_on_owner_uniqueid` (`user_id`),
  KEY `index_savedreports_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_savedreports_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_savedreports_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `savedreports`
--

LOCK TABLES `savedreports` WRITE;
/*!40000 ALTER TABLE `savedreports` DISABLE KEYS */;
/*!40000 ALTER TABLE `savedreports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `savedreports_columns`
--

DROP TABLE IF EXISTS `savedreports_columns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `savedreports_columns` (
  `savedreports_id` bigint(20) NOT NULL,
  `element` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `idx` bigint(20) DEFAULT NULL,
  KEY `index_savedreports_columns_on_savedreports_id` (`savedreports_id`),
  CONSTRAINT `fk_savedreports_columns_savedreports` FOREIGN KEY (`savedreports_id`) REFERENCES `savedreports` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `savedreports_columns`
--

LOCK TABLES `savedreports_columns` WRITE;
/*!40000 ALTER TABLE `savedreports_columns` DISABLE KEYS */;
/*!40000 ALTER TABLE `savedreports_columns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `savedreports_criteria`
--

DROP TABLE IF EXISTS `savedreports_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `savedreports_criteria` (
  `savedreports_id` bigint(20) NOT NULL,
  `element` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mapkey` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  KEY `fk_savedreports_criteria_savedreports` (`savedreports_id`),
  CONSTRAINT `fk_savedreports_criteria_savedreports` FOREIGN KEY (`savedreports_id`) REFERENCES `savedreports` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `savedreports_criteria`
--

LOCK TABLES `savedreports_criteria` WRITE;
/*!40000 ALTER TABLE `savedreports_criteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `savedreports_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schema_migrations`
--

DROP TABLE IF EXISTS `schema_migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_migrations` (
  `version` varchar(255) COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `unique_schema_migrations` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schema_migrations`
--

LOCK TABLES `schema_migrations` WRITE;
/*!40000 ALTER TABLE `schema_migrations` DISABLE KEYS */;
INSERT INTO `schema_migrations` VALUES ('200806021150'),('200806041055'),('200806041219'),('200806101208'),('200806101352'),('200806111341'),('200806121127'),('200806131657'),('200806161609'),('200806171339'),('200806171600'),('200806171724'),('200806181026'),('200806181327'),('200806181558'),('200806191152'),('200806191355'),('200806191556'),('200806191955'),('200806201538'),('200806211101'),('200806231235'),('200806231836'),('200806231911'),('200806241736'),('200806251058'),('200806251820'),('200806261045'),('200807091056'),('200807101832'),('200807101841'),('200807101952'),('200807140943'),('200807151743'),('200807171642'),('200807281530'),('200807301757'),('200808011155'),('200808061002'),('200808061139'),('200808081341'),('200808111120'),('200808120943'),('200808121020'),('200808121217'),('200808131154'),('200808151113'),('200808151618'),('200808181021'),('200808181539'),('200808181540'),('200808201137'),('200808291019'),('200808291317'),('200809021139'),('200809021834'),('200809031618'),('200809051121'),('200809081323'),('200809081448'),('200809091030'),('200809091035'),('200809091230'),('200809191450'),('200809191451'),('200809191452'),('200809191453'),('200809191455'),('200809191500'),('200809251156'),('200809251213'),('200809301019'),('200810010909'),('200810071915'),('200810081537'),('200810091020'),('200810091345'),('200810091802'),('200810091944'),('200810131031'),('200810131334'),('200810141005'),('200810141503'),('200810141511'),('200810151417'),('200810211012'),('200810211335'),('200810211534'),('200810221358'),('200810271938'),('200810291044'),('200810291117'),('200810301213'),('200810301412'),('200810301413'),('200810311140'),('200810311141'),('200810311142'),('200810311143'),('200811031750'),('200811051503'),('200811061327'),('200811061355'),('200811071500'),('200811100954'),('200811101606'),('200811121446'),('200811121618'),('200811121646'),('200811141456'),('200811171737'),('200811181141'),('200811181733'),('200811201244'),('200811211319'),('200811211500'),('200812041035'),('200812081158'),('200812081720'),('200812091157'),('200812111751'),('200812221725'),('200812241030'),('200812291752'),('200812301116'),('200812301313'),('200901011557'),('200901021814'),('200901021815'),('200901021816'),('200901021817'),('200901051201'),('200901051439'),('200901051515'),('200901061031'),('200901061635'),('200901061636'),('200901061702'),('200901081403'),('200901121112'),('200901131456'),('200901141621'),('200901141748'),('200901151007'),('200901151656'),('200901191717'),('200901201036'),('200901211052'),('200901211705'),('200901221309'),('200901261049'),('200902091438'),('200902101107'),('200902110601'),('200902121403'),('200902131153'),('200902181158'),('200902191541'),('200902231214'),('200902241318'),('200902251654'),('200902261436'),('200903021152'),('200903021208'),('200903021415'),('200903021501'),('200903031118'),('200903031728'),('200903031737'),('200903041142'),('200903041637'),('200903051400'),('200903061710'),('200903091000'),('200903091214'),('200903091216'),('200903091343'),('200903091414'),('200903091756'),('200903111503'),('200903121335'),('200903121702'),('200903181027'),('200903251611'),('200903261701'),('200903271030'),('200904041525'),('200904061605'),('200904070952'),('200904201305'),('200904201504'),('200904231154'),('200904271640'),('200904291322'),('200905041150'),('200905051338'),('200905071410'),('200905071415'),('200905071421'),('200905071434'),('200905071435'),('200905071436'),('200905081004'),('200905081031'),('200905081158'),('200905111441'),('200905141332'),('200905141421'),('200905141709'),('200905151000'),('200905191354'),('200905191355'),('200905201338'),('200905201726'),('200905251525'),('200905271314'),('200905271414'),('200905281120'),('200906011531'),('200906050929'),('200906081208'),('200906081523'),('200906081749'),('200906091000'),('200906101315'),('200906111546'),('200906121111'),('200906121403'),('200906171230'),('200906221400'),('200906221444'),('200906221451'),('200906251718'),('200907011529'),('200907011612'),('200907021323'),('200907061742'),('200907071753'),('200907081500'),('200907091453'),('200907141334'),('200907161400'),('200907161608'),('200907221327'),('200907221504'),('200907221515'),('200907221700'),('200907231324'),('200907241700'),('200907271130'),('200907271420'),('200907281417'),('200907281507'),('200907301654'),('200907301752'),('200907311224'),('200908031603'),('200908031608'),('200908041438'),('200908041554'),('200908041602'),('200908051438'),('200908101144'),('200908101146'),('200908101147'),('200908101148'),('200908101149'),('200908131010'),('200908131015'),('200908131415'),('200908131712'),('20090814'),('200908141013'),('200908141033'),('200908141704'),('200908171537'),('200908181345'),('200908191346'),('200908191409'),('200908201006'),('200908201007'),('200908210949'),('200908211452'),('200908211459'),('200908211515'),('200908211520'),('200908251659'),('200908261326'),('200908311155'),('200909011141'),('200909011200'),('200909011300'),('200909011305'),('200909020951'),('200909031324'),('200909031406'),('200909031452'),('200909031456'),('200909031558'),('200909031602'),('200909031610'),('200909041527'),('200909101044'),('200909101051'),('200909151849'),('200909221603'),('200909231107'),('200909241014'),('200909261440'),('200909281241'),('200909281550'),('200909291414'),('200909291509'),('200909301358'),('200909301645'),('200909301722'),('200910011458'),('200910011541'),('200910021120'),('200910031516'),('200910031559'),('200910051340'),('200910051602'),('200910061318'),('200910061427'),('200910191151'),('200910210941'),('200910211300'),('200910211431'),('200910221449'),('200910231438'),('200910231441'),('200910270949'),('200911091143'),('200911101454'),('200911111810'),('200911131441'),('200911181025'),('200911181213'),('200911181221'),('200911181705'),('200911201255'),('200911251722'),('200912041007'),('200912041132'),('200912110953'),('200912151353'),('201001011601'),('201001070940'),('201001121206'),('201001121207'),('201001121208'),('201001121209'),('201001121433'),('201001131434'),('201001131506'),('201001131543'),('201001131726'),('201001151143'),('201001261663'),('201001261664'),('201001261730'),('201001281446'),('201002051058'),('201002081644'),('201002081651'),('201002091205'),('201002181144'),('201002181353'),('201002181427'),('201002191054'),('201002231132'),('201002251129'),('201002251651'),('201003041353'),('201003101614'),('201003151606');
/*!40000 ALTER TABLE `schema_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seenitstorageitem`
--

DROP TABLE IF EXISTS `seenitstorageitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seenitstorageitem` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `userid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_seenitstorageitem_on_userid` (`userid`),
  KEY `fk_seenitstorageitem_users` (`modifiedby`),
  CONSTRAINT `fk_seenitstorageitem_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seenitstorageitem`
--

LOCK TABLES `seenitstorageitem` WRITE;
/*!40000 ALTER TABLE `seenitstorageitem` DISABLE KEYS */;
INSERT INTO `seenitstorageitem` VALUES (1,'2010-02-01 16:33:22','2010-02-01 16:33:22',NULL,15512597),(2,'2010-03-30 20:35:25','2010-03-30 20:35:25',NULL,15512599),(3,'2010-03-31 14:50:01','2010-03-31 14:50:01',NULL,15512595);
/*!40000 ALTER TABLE `seenitstorageitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seenitstorageitem_itemsseen`
--

DROP TABLE IF EXISTS `seenitstorageitem_itemsseen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seenitstorageitem_itemsseen` (
  `seenitstorageitem_id` bigint(20) NOT NULL DEFAULT '0',
  `element` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`seenitstorageitem_id`,`element`),
  CONSTRAINT `fk_seenitstorageitem_itemsseen_seenitstorageitem` FOREIGN KEY (`seenitstorageitem_id`) REFERENCES `seenitstorageitem` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seenitstorageitem_itemsseen`
--

LOCK TABLES `seenitstorageitem_itemsseen` WRITE;
/*!40000 ALTER TABLE `seenitstorageitem_itemsseen` DISABLE KEYS */;
INSERT INTO `seenitstorageitem_itemsseen` VALUES (1,'SetupWizard'),(2,'SetupWizard'),(3,'SetupWizard');
/*!40000 ALTER TABLE `seenitstorageitem_itemsseen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serialnumbercounter`
--

DROP TABLE IF EXISTS `serialnumbercounter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serialnumbercounter` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `counter` bigint(20) DEFAULT NULL,
  `decimalformat` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `daystoreset` bigint(20) DEFAULT NULL,
  `lastreset` datetime DEFAULT NULL,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `one_counter_per_man` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=479 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serialnumbercounter`
--

LOCK TABLES `serialnumbercounter` WRITE;
/*!40000 ALTER TABLE `serialnumbercounter` DISABLE KEYS */;
INSERT INTO `serialnumbercounter` VALUES (476,15511595,1,'000000',365,'2010-01-01 00:00:00'),(477,15511596,1,'000000',365,'2010-01-01 00:00:00'),(478,15511597,1,'000000',365,'2010-01-01 00:00:00');
/*!40000 ALTER TABLE `serialnumbercounter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setupdatalastmoddates`
--

DROP TABLE IF EXISTS `setupdatalastmoddates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setupdatalastmoddates` (
  `tenant_id` bigint(20) DEFAULT NULL,
  `producttypes` datetime NOT NULL,
  `inspectiontypes` datetime NOT NULL,
  `autoattributes` datetime NOT NULL,
  `owners` datetime NOT NULL,
  `jobs` datetime DEFAULT NULL,
  UNIQUE KEY `idx_setupdatalastmoddates_tenantid` (`tenant_id`),
  KEY `index_setupdatalastmoddates_on_r_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setupdatalastmoddates`
--

LOCK TABLES `setupdatalastmoddates` WRITE;
/*!40000 ALTER TABLE `setupdatalastmoddates` DISABLE KEYS */;
INSERT INTO `setupdatalastmoddates` VALUES (15511550,'2010-01-30 15:48:02','2010-01-30 15:48:02','2010-01-30 15:48:02','2010-02-01 15:49:06','2010-01-30 15:48:02'),(15511595,'2010-03-31 14:55:21','2010-03-31 14:54:59','2010-02-01 16:05:46','2010-04-14 17:15:28','2010-02-01 16:05:46'),(15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11','2010-02-01 16:09:11','2010-04-14 17:15:01','2010-02-01 16:09:11'),(15511597,'2010-03-30 21:31:35','2010-03-30 21:30:07','2010-03-30 20:00:14','2010-03-30 20:23:25','2010-03-30 20:00:14');
/*!40000 ALTER TABLE `setupdatalastmoddates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `signupreferrals`
--

DROP TABLE IF EXISTS `signupreferrals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signupreferrals` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `signupdate` datetime NOT NULL,
  `referral_tenant_id` bigint(20) NOT NULL,
  `referred_tenant_id` bigint(20) NOT NULL,
  `referral_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_signupreferrals_on_referred_tenant_id` (`referred_tenant_id`),
  KEY `signupreferrals_referral_tenant` (`referral_tenant_id`),
  KEY `signupreferrals_referral_user` (`referral_user_id`),
  CONSTRAINT `signupreferrals_referral_tenant` FOREIGN KEY (`referral_tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `signupreferrals_referral_user` FOREIGN KEY (`referral_user_id`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `signupreferrals_referred_tenant` FOREIGN KEY (`referred_tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `signupreferrals`
--

LOCK TABLES `signupreferrals` WRITE;
/*!40000 ALTER TABLE `signupreferrals` DISABLE KEYS */;
/*!40000 ALTER TABLE `signupreferrals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `states`
--

DROP TABLE IF EXISTS `states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `states` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `displaytext` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `buttonname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `retired` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index_states_on_modifiedby` (`modifiedby`),
  KEY `index_states_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_states_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1109 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `states`
--

LOCK TABLES `states` WRITE;
/*!40000 ALTER TABLE `states` DISABLE KEYS */;
INSERT INTO `states` VALUES (1094,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'Pass','PASS','btn0',0),(1095,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'Fail','FAIL','btn1',0),(1096,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'NA','NA','btn2',0),(1097,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'Pass','PASS','btn0',0),(1098,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'Fail','FAIL','btn1',0),(1099,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'Pass','PASS','btn0',0),(1100,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'Fail','FAIL','btn1',0),(1101,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'NA','NA','btn2',0),(1102,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'Pass','PASS','btn0',0),(1103,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'Fail','FAIL','btn1',0),(1104,15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,'Pass','PASS','btn0',0),(1105,15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,'Fail','FAIL','btn1',0),(1106,15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,'NA','NA','btn2',0),(1107,15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,'Pass','PASS','btn0',0),(1108,15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,'Fail','FAIL','btn1',0);
/*!40000 ALTER TABLE `states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statesets`
--

DROP TABLE IF EXISTS `statesets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statesets` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `retired` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index_statesets_on_modifiedby` (`modifiedby`),
  KEY `index_statesets_on_r_tenant` (`tenant_id`),
  CONSTRAINT `fk_statesets_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=349 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statesets`
--

LOCK TABLES `statesets` WRITE;
/*!40000 ALTER TABLE `statesets` DISABLE KEYS */;
INSERT INTO `statesets` VALUES (343,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'Pass, Fail',0),(344,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'NA, Pass, Fail',0),(345,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'Pass, Fail',0),(346,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'NA, Pass, Fail',0),(347,15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,'Pass, Fail',0),(348,15511597,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,'NA, Pass, Fail',0);
/*!40000 ALTER TABLE `statesets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statesets_states`
--

DROP TABLE IF EXISTS `statesets_states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statesets_states` (
  `statesets_id` bigint(20) NOT NULL,
  `states_id` bigint(20) NOT NULL,
  `orderidx` bigint(20) NOT NULL,
  UNIQUE KEY `statesets_states_states_id_key` (`states_id`),
  KEY `index_statesets_states_on_states_id` (`states_id`),
  KEY `fk_statesets_states_statesets` (`statesets_id`),
  CONSTRAINT `fk_statesets_states_states` FOREIGN KEY (`states_id`) REFERENCES `states` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_statesets_states_statesets` FOREIGN KEY (`statesets_id`) REFERENCES `statesets` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statesets_states`
--

LOCK TABLES `statesets_states` WRITE;
/*!40000 ALTER TABLE `statesets_states` DISABLE KEYS */;
INSERT INTO `statesets_states` VALUES (343,1094,0),(343,1095,1),(344,1096,0),(344,1097,1),(344,1098,2),(345,1099,0),(345,1100,1),(346,1101,0),(346,1102,1),(346,1103,2),(347,1104,0),(347,1105,1),(348,1106,0),(348,1107,1),(348,1108,2);
/*!40000 ALTER TABLE `statesets_states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subproducts`
--

DROP TABLE IF EXISTS `subproducts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subproducts` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  `masterproduct_id` bigint(20) NOT NULL,
  `label` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weight` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_subproducts_on_masterproduct_id` (`masterproduct_id`),
  KEY `fk_subproducts_subproduct` (`product_id`),
  CONSTRAINT `fk_subproducts_masterproduct` FOREIGN KEY (`masterproduct_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subproducts_subproduct` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subproducts`
--

LOCK TABLES `subproducts` WRITE;
/*!40000 ALTER TABLE `subproducts` DISABLE KEYS */;
/*!40000 ALTER TABLE `subproducts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tagoptions`
--

DROP TABLE IF EXISTS `tagoptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tagoptions` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `optionkey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weight` bigint(20) NOT NULL,
  `text` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `resolverclassname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tagoptions_users` (`modifiedby`),
  KEY `fk_tagoptions_organization` (`tenant_id`),
  CONSTRAINT `fk_tagoptions_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tagoptions`
--

LOCK TABLES `tagoptions` WRITE;
/*!40000 ALTER TABLE `tagoptions` DISABLE KEYS */;
INSERT INTO `tagoptions` VALUES (1,1,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(2,28572,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(3,2,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(4,132385,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,'ReelId','com.n4systems.plugins.integration.impl.cglift.PlexusConnector'),(6,152715,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(7,216044,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(8,4,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(9,10802250,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(10,10802300,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(11,10802301,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(12,10802350,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(13,10802351,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(14,10802400,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(15,15511453,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(16,15511480,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(17,15511483,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,'Shop Order',NULL),(18,15511484,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(19,15511483,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'CUSTOMERORDER',1,NULL,NULL),(20,15511485,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(21,15511486,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(22,15511488,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(23,15511490,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(24,15511491,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(25,15511492,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(26,15511493,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(27,15511495,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(28,15511497,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(29,15511498,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(30,15511499,'2008-10-29 04:48:26','2008-10-29 04:48:26',NULL,'SHOPORDER',0,NULL,NULL),(31,15511500,'2008-11-04 16:27:19','2008-11-04 16:27:19',NULL,'SHOPORDER',0,NULL,NULL),(32,15511501,'2008-11-04 21:35:01','2008-11-04 21:35:01',NULL,'SHOPORDER',0,NULL,NULL),(33,15511503,'2008-11-07 16:02:02','2008-11-07 16:02:02',NULL,'SHOPORDER',0,NULL,NULL),(34,15511504,'2008-11-10 17:55:14','2008-11-10 17:55:14',NULL,'SHOPORDER',0,NULL,NULL),(35,15511505,'2008-12-05 14:23:42','2008-12-05 14:23:42',NULL,'SHOPORDER',0,NULL,NULL),(36,15511511,'2009-01-15 19:54:09','2009-01-15 19:54:09',NULL,'SHOPORDER',0,NULL,NULL),(37,15511513,'2009-02-25 20:07:18','2009-02-25 20:07:18',NULL,'SHOPORDER',0,NULL,NULL),(38,15511514,'2009-03-02 17:58:39','2009-03-02 17:58:39',NULL,'SHOPORDER',0,NULL,NULL),(39,15511515,'2009-03-04 20:42:44','2009-03-04 20:42:44',NULL,'SHOPORDER',0,NULL,NULL),(40,15511516,'2009-03-26 15:12:48','2009-03-26 15:12:48',NULL,'SHOPORDER',0,NULL,NULL),(41,15511518,'2009-04-24 20:23:21','2009-04-24 20:23:21',NULL,'SHOPORDER',0,NULL,NULL),(42,15511519,'2009-04-24 20:24:17','2009-04-24 20:24:17',NULL,'SHOPORDER',0,NULL,NULL),(43,15511520,'2009-04-24 20:24:52','2009-04-24 20:24:52',NULL,'SHOPORDER',0,NULL,NULL),(44,15511521,'2009-04-24 20:25:35','2009-04-24 20:25:35',NULL,'SHOPORDER',0,NULL,NULL),(45,15511522,'2009-04-24 20:26:03','2009-04-24 20:26:03',NULL,'SHOPORDER',0,NULL,NULL),(46,15511523,'2009-04-24 20:46:03','2009-04-24 20:46:03',NULL,'SHOPORDER',0,NULL,NULL),(47,15511524,'2009-04-24 20:46:40','2009-04-24 20:46:40',NULL,'SHOPORDER',0,NULL,NULL),(48,15511525,'2009-04-24 22:23:38','2009-04-24 22:23:38',NULL,'SHOPORDER',0,NULL,NULL),(49,15511532,'2009-05-19 14:35:31','2009-05-19 14:35:31',NULL,'SHOPORDER',0,NULL,NULL),(50,15511533,'2009-05-20 16:17:34','2009-05-20 16:17:34',NULL,'SHOPORDER',0,NULL,NULL),(51,15511537,'2009-06-16 19:17:20','2009-06-16 19:17:20',NULL,'SHOPORDER',0,NULL,NULL),(52,15511538,'2009-06-22 17:41:02','2009-06-22 17:41:02',NULL,'SHOPORDER',0,NULL,NULL),(53,15511540,'2009-06-26 13:48:09','2009-06-26 13:48:09',NULL,'SHOPORDER',0,NULL,NULL),(54,15511541,'2009-06-26 17:47:16','2009-06-26 17:47:16',NULL,'SHOPORDER',0,NULL,NULL),(55,15511544,'2009-07-15 18:00:55','2009-07-15 18:00:55',NULL,'SHOPORDER',0,NULL,NULL),(56,15511545,'2009-07-31 20:55:23','2009-07-31 20:55:23',NULL,'SHOPORDER',0,NULL,NULL),(57,15511549,'2009-08-04 20:20:05','2009-08-04 20:20:05',NULL,'SHOPORDER',0,NULL,NULL),(58,15511550,'2009-08-04 22:28:05','2009-08-04 22:28:05',NULL,'SHOPORDER',0,NULL,NULL),(59,15511552,'2009-08-20 03:30:16','2009-08-20 03:30:16',NULL,'SHOPORDER',0,NULL,NULL),(60,15511553,'2009-08-20 21:01:21','2009-08-20 21:01:21',NULL,'SHOPORDER',0,NULL,NULL),(61,15511554,'2009-08-26 22:55:09','2009-08-26 22:55:09',NULL,'SHOPORDER',0,NULL,NULL),(62,15511555,'2009-09-09 20:12:36','2009-09-09 20:12:36',NULL,'SHOPORDER',0,NULL,NULL),(63,15511558,'2009-09-16 17:57:34','2009-09-16 17:57:34',NULL,'SHOPORDER',0,NULL,NULL),(64,15511563,'2009-09-22 21:01:02','2009-09-22 21:01:02',NULL,'SHOPORDER',0,NULL,NULL),(65,15511564,'2009-09-30 14:06:20','2009-09-30 14:06:20',NULL,'SHOPORDER',0,NULL,NULL),(66,15511565,'2009-10-09 20:13:06','2009-10-09 20:13:06',NULL,'SHOPORDER',0,NULL,NULL),(67,15511566,'2009-10-14 14:14:12','2009-10-14 14:14:12',NULL,'SHOPORDER',0,NULL,NULL),(68,15511568,'2009-10-16 14:10:49','2009-10-16 14:10:49',NULL,'SHOPORDER',0,NULL,NULL),(69,15511573,'2009-10-25 20:29:07','2009-10-25 20:29:07',NULL,'SHOPORDER',0,NULL,NULL),(70,15511574,'2009-11-02 22:56:21','2009-11-02 22:56:21',NULL,'SHOPORDER',0,NULL,NULL),(71,15511575,'2009-11-03 19:12:51','2009-11-03 19:12:51',NULL,'SHOPORDER',0,NULL,NULL),(72,15511576,'2009-11-10 20:37:21','2009-11-10 20:37:21',NULL,'SHOPORDER',0,NULL,NULL),(73,15511577,'2009-11-17 14:21:54','2009-11-17 14:21:54',NULL,'SHOPORDER',0,NULL,NULL),(74,15511578,'2009-11-30 13:52:11','2009-11-30 13:52:11',NULL,'SHOPORDER',0,NULL,NULL),(75,15511579,'2009-12-02 23:09:44','2009-12-02 23:09:44',NULL,'SHOPORDER',0,NULL,NULL),(76,15511580,'2009-12-03 00:11:45','2009-12-03 00:11:45',NULL,'SHOPORDER',0,NULL,NULL),(77,15511581,'2009-12-07 14:15:44','2009-12-07 14:15:44',NULL,'SHOPORDER',0,NULL,NULL),(78,15511582,'2009-12-14 16:49:27','2009-12-14 16:49:27',NULL,'SHOPORDER',0,NULL,NULL),(79,15511583,'2009-12-14 22:13:47','2009-12-14 22:13:47',NULL,'SHOPORDER',0,NULL,NULL),(80,15511584,'2009-12-17 19:40:59','2009-12-17 19:40:59',NULL,'SHOPORDER',0,NULL,NULL),(81,15511585,'2009-12-22 22:41:23','2009-12-22 22:41:23',NULL,'SHOPORDER',0,NULL,NULL),(82,15511586,'2010-01-01 15:43:58','2010-01-01 15:43:58',NULL,'SHOPORDER',0,NULL,NULL),(83,15511587,'2010-01-03 19:37:14','2010-01-03 19:37:14',NULL,'SHOPORDER',0,NULL,NULL),(84,15511588,'2010-01-04 18:50:44','2010-01-04 18:50:44',NULL,'SHOPORDER',0,NULL,NULL),(85,15511589,'2010-01-05 15:31:37','2010-01-05 15:31:37',NULL,'SHOPORDER',0,NULL,NULL),(86,15511590,'2010-01-06 14:16:44','2010-01-06 14:16:44',NULL,'SHOPORDER',0,NULL,NULL),(87,15511591,'2010-01-08 15:14:13','2010-01-08 15:14:13',NULL,'SHOPORDER',0,NULL,NULL),(88,15511592,'2010-01-12 15:03:51','2010-01-12 15:03:51',NULL,'SHOPORDER',0,NULL,NULL),(89,15511593,'2010-01-13 02:15:05','2010-01-13 02:15:05',NULL,'SHOPORDER',0,NULL,NULL),(90,15511594,'2010-01-15 17:02:21','2010-01-15 17:02:21',NULL,'SHOPORDER',0,NULL,NULL),(91,15511595,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'SHOPORDER',0,NULL,NULL),(92,15511596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'SHOPORDER',0,NULL,NULL),(93,15511597,'2010-03-30 20:00:14','2010-03-30 20:00:14',NULL,'SHOPORDER',0,NULL,NULL);
/*!40000 ALTER TABLE `tagoptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `classname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `cronexpression` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `taskgroup` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES ('DiskUsage','2009-08-09 12:14:00','2009-08-09 12:14:00','com.n4systems.taskscheduling.task.DiskUsageTask','0 4 * * 6','default',1),('DownloadCleanup','2009-11-29 12:11:01','2009-11-29 12:11:01','com.n4systems.taskscheduling.task.DownloadCleanupTask','30 1 * * *','default',1),('InspectionScheduleNotifications','2009-06-14 13:07:03','2009-06-14 13:07:03','com.n4systems.taskscheduling.task.InspectionScheduleNotificationTask','0 7 * * *','default',1),('LimitUpdate','2009-08-09 12:14:14','2009-08-09 12:14:14','com.n4systems.taskscheduling.task.TenantLimitUpdaterTask','*/2 * * * *','default',1),('SerialNumberCounter','2009-06-14 13:07:03','2009-06-14 13:07:03','com.n4systems.taskscheduling.task.SerialNumberCounterTask','0 1 * * *','default',1),('SignUpPackageSync','2009-10-25 13:02:33','2009-10-25 13:02:33','com.n4systems.taskscheduling.task.SignUpPackageSyncTask','0 0 * * *','default',1);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenantlink`
--

DROP TABLE IF EXISTS `tenantlink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenantlink` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `r_manufacturer` bigint(20) NOT NULL,
  `r_linkedtenant` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_tenantlink_on_r_manufacturer_and_r_linkedtenant` (`r_linkedtenant`,`r_manufacturer`),
  KEY `fk_tenantlink_manufacturer` (`r_manufacturer`),
  CONSTRAINT `fk_tenantlink_linkedteant` FOREIGN KEY (`r_linkedtenant`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tenantlink_manufacturer` FOREIGN KEY (`r_manufacturer`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenantlink`
--

LOCK TABLES `tenantlink` WRITE;
/*!40000 ALTER TABLE `tenantlink` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenantlink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenants`
--

DROP TABLE IF EXISTS `tenants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenants` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15511598 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenants`
--

LOCK TABLES `tenants` WRITE;
/*!40000 ALTER TABLE `tenants` DISABLE KEYS */;
INSERT INTO `tenants` VALUES (15511550,'fieldid'),(15511595,'test-vendor'),(15511596,'test-distributor'),(15511597,'security-check');
/*!40000 ALTER TABLE `tenants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typedorgconnections`
--

DROP TABLE IF EXISTS `typedorgconnections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typedorgconnections` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `connectedorg_id` bigint(20) NOT NULL,
  `connectiontype` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `orgconnection_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_typedorgconnection_unique` (`owner_id`,`connectedorg_id`,`connectiontype`),
  KEY `fk_typedorgconnections_users` (`modifiedby`),
  KEY `fk_typedorgconnections_tenants` (`tenant_id`),
  KEY `fk_typedorgconnection_connectedorg` (`connectedorg_id`),
  KEY `fk_typedorgconnections_org_connections` (`orgconnection_id`),
  CONSTRAINT `fk_typedorgconnections_org_base` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_typedorgconnections_org_connections` FOREIGN KEY (`orgconnection_id`) REFERENCES `org_connections` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_typedorgconnections_tenants` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_typedorgconnections_users` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_typedorgconnection_connectedorg` FOREIGN KEY (`connectedorg_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typedorgconnections`
--

LOCK TABLES `typedorgconnections` WRITE;
/*!40000 ALTER TABLE `typedorgconnections` DISABLE KEYS */;
INSERT INTO `typedorgconnections` VALUES (115,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,15511595,15539065,15511550,'CATALOG_ONLY',NULL),(116,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,15511595,15539065,15539066,'CUSTOMER',14),(117,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,15511596,15539066,15539065,'VENDOR',14),(118,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,15511596,15539066,15511550,'CATALOG_ONLY',NULL),(119,'2010-03-30 20:00:15','2010-03-30 20:00:15',NULL,15511597,15539068,15511550,'CATALOG_ONLY',NULL);
/*!40000 ALTER TABLE `typedorgconnections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unitofmeasures`
--

DROP TABLE IF EXISTS `unitofmeasures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unitofmeasures` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shortname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `selectable` tinyint(1) DEFAULT NULL,
  `child_unitofmeasure_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_unitofmeasures_on_child_unitofmeasure_id` (`child_unitofmeasure_id`),
  CONSTRAINT `fk_unitofmeasures_unitofmeasures` FOREIGN KEY (`child_unitofmeasure_id`) REFERENCES `unitofmeasures` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unitofmeasures`
--

LOCK TABLES `unitofmeasures` WRITE;
/*!40000 ALTER TABLE `unitofmeasures` DISABLE KEYS */;
INSERT INTO `unitofmeasures` VALUES (1,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Inches','dimension','in',1,NULL),(2,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Centimeters','dimension','cm',1,NULL),(3,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Pounds','weight','lbs',1,NULL),(4,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Kilograms','weight','kg',1,NULL),(5,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Feet','dimension','ft',1,1),(6,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Meters','dimension','m',1,2),(7,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Tonne','weight','t',1,NULL),(8,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Ton','weight','ton',1,NULL),(9,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'kiloNewtons','weight','kN',1,NULL),(10,'2009-02-15 17:12:14','2009-02-15 17:12:14',NULL,'Millimetres','dimension','mm',1,NULL);
/*!40000 ALTER TABLE `unitofmeasures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userrequest`
--

DROP TABLE IF EXISTS `userrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userrequest` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `companyname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phonenumber` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `r_useraccount` bigint(20) NOT NULL,
  `comment` text COLLATE utf8_unicode_ci,
  `modified` datetime DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modifiedby` bigint(20) DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_userrequest_on_modifiedby` (`modifiedby`),
  KEY `index_userrequest_on_r_tenant` (`tenant_id`),
  KEY `index_userrequest_on_r_useraccount` (`r_useraccount`),
  CONSTRAINT `fk_userrequest_modifiedusers` FOREIGN KEY (`modifiedby`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_userrequest_users` FOREIGN KEY (`r_useraccount`) REFERENCES `users` (`uniqueid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userrequest`
--

LOCK TABLES `userrequest` WRITE;
/*!40000 ALTER TABLE `userrequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `userrequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `uniqueid` bigint(21) NOT NULL AUTO_INCREMENT,
  `datemodified` datetime DEFAULT NULL,
  `datecreated` datetime DEFAULT NULL,
  `modifiedby` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userid` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `firstname` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastname` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `emailaddress` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tenant_id` bigint(20) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  `hashpassword` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `timezoneid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `position` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `initials` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `resetpasswordkey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hashsecuritycardnumber` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `system` tinyint(1) NOT NULL,
  `admin` tinyint(1) DEFAULT NULL,
  `permissions` bigint(20) NOT NULL,
  `archiveduserid` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `externalid` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `referralkey` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`uniqueid`),
  UNIQUE KEY `index_users_on_referralkey` (`referralkey`),
  UNIQUE KEY `uniqueuseridrtenant` (`tenant_id`,`userid`),
  KEY `fieldiduser_idx` (`tenant_id`,`userid`),
  KEY `index_users_on_r_tenant` (`tenant_id`),
  KEY `fk_users_owner` (`owner_id`),
  CONSTRAINT `fk_users_owner` FOREIGN KEY (`owner_id`) REFERENCES `org_base` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15512622 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (15512593,'2010-02-01 15:45:51','2010-02-01 15:45:51',NULL,'n4systems','N4','Admin','dev@fieldid.com',15511550,0,'7c4fd0a53d8aa0d0f15f716cc6324e95223d9902','United States:New York - New York',NULL,1,NULL,NULL,NULL,1,0,2147483647,NULL,NULL,15511550,'sub0AHwMNo'),(15512594,'2010-02-01 16:05:46','2010-02-01 16:05:46',NULL,'n4systems','N4','Admin','dev@fieldid.com',15511595,0,'7c4fd0a53d8aa0d0f15f716cc6324e95223d9902','Canada:Ontario - Toronto',NULL,1,NULL,NULL,NULL,1,0,2147483647,NULL,NULL,15539065,'rjjsxcfp8k'),(15512595,'2010-02-01 16:05:47','2010-02-01 16:05:46',NULL,'admin','Test','Vendor','dev@fieldid.com',15511595,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto',NULL,1,NULL,NULL,NULL,0,1,2147483647,NULL,1942727390511000814,15539065,'A81Og3lQ6O'),(15512596,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'n4systems','N4','Admin','dev@fieldid.com',15511596,0,'7c4fd0a53d8aa0d0f15f716cc6324e95223d9902','Canada:Ontario - Toronto',NULL,1,NULL,NULL,NULL,1,0,2147483647,NULL,NULL,15539066,'vVjVOQ6MhU'),(15512597,'2010-02-01 16:09:11','2010-02-01 16:09:11',NULL,'admin','Test','Distributor','dev@fieldid.com',15511596,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto',NULL,1,NULL,NULL,NULL,0,1,2147483647,NULL,2149839928595329100,15539066,'MFZOM7kexg'),(15512598,'2010-03-30 20:00:14','2010-03-30 20:00:14',NULL,'n4systems','N4','Admin','admin@fieldid.com',15511597,0,'7c4fd0a53d8aa0d0f15f716cc6324e95223d9902','Canada:Ontario - Toronto',NULL,1,NULL,NULL,NULL,1,0,2147483647,NULL,NULL,15539068,'E9nQuBuFfV'),(15512599,'2010-03-30 20:00:15','2010-03-30 20:00:14',NULL,'admin','Security','Test','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto',NULL,1,NULL,NULL,NULL,0,1,2147483647,NULL,9015922753005980915,15539068,'acvxo22Z8t'),(15512600,'2010-03-30 20:25:03','2010-03-30 20:25:03',NULL,'primary','primary','org user','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,511,NULL,NULL,15539068,'N05GG8TBMs'),(15512601,'2010-03-30 20:37:13','2010-03-30 20:37:13',NULL,'wc_secondary','West Coast','Employee','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'we',NULL,NULL,0,0,511,NULL,NULL,15539069,'vChP3o0IJb'),(15512602,'2010-03-30 20:49:17','2010-03-30 20:49:17',NULL,'ec_secondary','East Coast','Employee','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,511,NULL,NULL,15539070,'xwOFmaRuVG'),(15512603,'2010-03-30 20:50:30','2010-03-30 20:50:30',NULL,'ns_customer','Norther State','Customer','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539071,'70e9mppChr'),(15512604,'2010-03-30 20:51:12','2010-03-30 20:51:12',NULL,'ss_customer','Southern State','Customer','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539072,'0UPP3tzVTd'),(15512605,'2010-03-30 20:52:02','2010-03-30 20:52:02',NULL,'relaxed_divsion','Relaxed NS','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539075,'2ChI0YnP8i'),(15512606,'2010-03-30 20:53:10','2010-03-30 20:53:10',NULL,'uptight_div','Uptight NS','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'ud',NULL,NULL,0,0,0,NULL,NULL,15539076,'8ahCArCumK'),(15512607,'2010-03-30 20:53:50','2010-03-30 20:53:50',NULL,'nw_customer','North Western','Customer','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539077,'jgiUAZkgMJ'),(15512608,'2010-03-30 20:54:43','2010-03-30 20:54:43',NULL,'sw_customer','Southern Western','Customer','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539080,'3yt6KuscSN'),(15512609,'2010-03-30 20:55:24','2010-03-30 20:55:24',NULL,'le_customer','Lower Eastern','Customer','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539086,'nxwrpIr9UZ'),(15512610,'2010-03-30 20:56:25','2010-03-30 20:56:25',NULL,'ue_customer','Upper Eastern','Customer','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'uc',NULL,NULL,0,0,0,NULL,NULL,15539083,'K1Kcry90e4'),(15512611,'2010-03-30 20:56:59','2010-03-30 20:56:59',NULL,'cold_division','cold NW','division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539079,'2Fg6VM4IiI'),(15512612,'2010-03-30 20:57:39','2010-03-30 20:57:39',NULL,'hot_division','hot NW','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539078,'vdaq9K13W4'),(15512613,'2010-03-30 20:58:19','2010-03-30 20:58:19',NULL,'blue_division','Blue SW','division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'d',NULL,NULL,0,0,0,NULL,NULL,15539082,'6blG81IXRj'),(15512614,'2010-03-30 20:58:54','2010-03-30 20:58:54',NULL,'red_division','Red SW','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539081,'488AAufKg4'),(15512615,'2010-03-30 20:59:24','2010-03-30 20:59:24',NULL,'back_division','back Le','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'bd',NULL,NULL,0,0,0,NULL,NULL,15539088,'gneTWwn6I5'),(15512616,'2010-03-30 20:59:56','2010-03-30 20:59:56',NULL,'front_division','Front LE','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'fd',NULL,NULL,0,0,0,NULL,NULL,15539087,'FMVynlDMJz'),(15512617,'2010-03-30 21:00:29','2010-03-30 21:00:29',NULL,'left_division','Left UE','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'ld',NULL,NULL,0,0,0,NULL,NULL,15539084,'xHWocqw7y0'),(15512618,'2010-03-30 21:00:59','2010-03-30 21:00:59',NULL,'right_division','Right UE','division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539085,'0YYrxRF5hE'),(15512619,'2010-03-30 22:41:30','2010-03-30 22:41:30',NULL,'bright_division','Bright SS','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539073,'JEUdyDpJ0a'),(15512620,'2010-03-30 22:41:59','2010-03-30 22:41:59',NULL,'dull_division','Dull SS','Division','dev@fieldid.com',15511597,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,0,NULL,NULL,15539074,'jtigwLpODB'),(15512621,'2010-04-14 17:19:40','2010-04-14 17:19:40',NULL,'secondary1','Secondary','Employee','dev@fieldid.com',15511596,0,'2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7','Canada:Ontario - Toronto','',1,'',NULL,NULL,0,0,511,NULL,NULL,15539091,'RNenoMec9W');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-04-14 14:47:33

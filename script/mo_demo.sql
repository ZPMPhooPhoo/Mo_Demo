-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: authorization-demo-mysql-os96.l.aivencloud.com    Database: mo_demo
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '2fe13197-e7dd-11f0-a82b-12fe324758d7:1-36,
a22d365e-e8bf-11f0-a60f-c662967e4e3e:1-85';

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (_binary '\—{PÉyI§ñ\÷r\ﬁ\∆y»æ','2026-01-04T12:23:43.524888','Sample Task 1',NULL,'APPROVED','Task 1','2026-01-04T21:00:12.395237100',_binary 'zUÖπù\ÀDI∫π*¸ø5C',_binary 'S\…e3¢èHêæÅ\…;J\◊\√'),(_binary '$ıß\÷\·BÄ\ÏE-úznw','2026-01-04T12:24:34.114017500','Sample Task 2',NULL,'APPROVED','Task 2','2026-01-04T12:26:49.225776700',_binary 'zUÖπù\ÀDI∫π*¸ø5C',_binary 'S\…e3¢èHêæÅ\…;J\◊\√'),(_binary 'S|ñIu\ÁMm±\Ê7Ò9\Ÿ\‰ˆ','2026-01-04T12:24:47.793064500','Sample Task 3','ttt','REJECTED','Task 3','2026-01-04T12:26:55.696132200',_binary 'zUÖπù\ÀDI∫π*¸ø5C',_binary 'S\…e3¢èHêæÅ\…;J\◊\√'),(_binary '®j∑øí°@w§ô6òÙ8','2026-01-04T21:02:03.320167500','Sample task 01',NULL,'DRAFT','Task 01','2026-01-04T21:02:03.320167500',NULL,_binary '(\"\◊~G–èÍÉì°∫Ñ'),(_binary 'ªM\r{~`@åôké!','2026-01-04T21:02:36.675664500','Sample task 02','task is not correct','REJECTED','task 02','2026-01-04T21:03:58.771021900',_binary 'zUÖπù\ÀDI∫π*¸ø5C',_binary '(\"\◊~G–èÍÉì°∫Ñ');
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary '(\"\◊~G–èÍÉì°∫Ñ','2026-01-04T21:01:23.836693100','user2@gmail','user 2','$2a$10$b.Jtu7zpu3g0OrgMG6cv3e5nb7NjqsQ/rF4WYiGXqOI4F0ZkiTyxS','USER','ACTIVE','2026-01-04T21:01:23.836693100'),(_binary 'S\…e3¢èHêæÅ\…;J\◊\√','2026-01-04T12:19:34.445381800','user@gmail.com','user','$2a$10$ISDQF3GKcQMoM0XjmGndx.2pszmvfMX5RmzvRTC4yfPpAu89bjMJy','USER','ACTIVE','2026-01-04T12:19:34.445381800'),(_binary 'zUÖπù\ÀDI∫π*¸ø5C','2026-01-04T12:19:11.391239300','admin@gmail.com','approver','$2a$10$CqzfEyR.5psXKofblFiC5eezWZHIlXJsuVYMaDampmjcsni.Mx7mW','APPROVER','ACTIVE','2026-01-04T12:19:11.391239300');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-04 21:58:32

-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: locadora
-- ------------------------------------------------------
-- Server version	8.0.45

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

--
-- Table structure for table `locacoes`
--

DROP TABLE IF EXISTS `locacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locacoes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_devolucao_prevista` datetime(6) DEFAULT NULL,
  `data_devolucao_real` datetime(6) DEFAULT NULL,
  `data_emprestimo` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `valor_total` decimal(38,2) DEFAULT NULL,
  `usuario_id` bigint NOT NULL,
  `veiculo_id` bigint NOT NULL,
  `valor_multa` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9x44qvkpyltev3ylhgu4rddo6` (`usuario_id`),
  KEY `FKry9641vt3k0xheml4fgrl1om9` (`veiculo_id`),
  CONSTRAINT `FK9x44qvkpyltev3ylhgu4rddo6` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKry9641vt3k0xheml4fgrl1om9` FOREIGN KEY (`veiculo_id`) REFERENCES `veiculos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locacoes`
--

LOCK TABLES `locacoes` WRITE;
/*!40000 ALTER TABLE `locacoes` DISABLE KEYS */;
INSERT INTO `locacoes` VALUES (2,'2026-05-12 13:00:00.000000','2026-05-13 13:00:00.000000','2026-05-05 13:00:00.000000','CONCLUIDA_COM_ATRASO',1008.00,8,2,168.00);
/*!40000 ALTER TABLE `locacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificacoes`
--

DROP TABLE IF EXISTS `notificacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacoes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_envio` datetime(6) DEFAULT NULL,
  `lida` bit(1) DEFAULT NULL,
  `mensagem` text,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3jcnk0ggmxklk5sjxm2plk5ml` (`usuario_id`),
  CONSTRAINT `FK3jcnk0ggmxklk5sjxm2plk5ml` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificacoes`
--

LOCK TABLES `notificacoes` WRITE;
/*!40000 ALTER TABLE `notificacoes` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagamentos`
--

DROP TABLE IF EXISTS `pagamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_pagamento` datetime(6) DEFAULT NULL,
  `metodo_pagamento` varchar(255) DEFAULT NULL,
  `status_pagamento` varchar(255) DEFAULT NULL,
  `token_transacao` varchar(255) DEFAULT NULL,
  `valor_pago` decimal(38,2) DEFAULT NULL,
  `locacao_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlqu6cv5mluyuixuikohv4i5qg` (`locacao_id`),
  CONSTRAINT `FKbk0nct3rwwk2hum51e2c6vyti` FOREIGN KEY (`locacao_id`) REFERENCES `locacoes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagamentos`
--

LOCK TABLES `pagamentos` WRITE;
/*!40000 ALTER TABLE `pagamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cnh` varchar(255) NOT NULL,
  `cpf` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `perfil` enum('ADMIN','CLIENTE') DEFAULT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt5vcxavfiqmg3i9b6do4h1hor` (`cnh`),
  UNIQUE KEY `UK2et2smpfrtsohr7w9fe1v8a5e` (`cpf`),
  UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (2,'556677889','123.456.789-10','samuel.it@email.com','Samuel Aprendiz','ADMIN','$2a$10$Ruywhwv1lK1HTSzDpm.xWeU8frWWijCMT9aHPuNLQF1zhYkkM8y/a'),(8,'1122334454','123.456.789-11','ana.santos@provedor.com','Ana Maria Santos','CLIENTE','password123');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `veiculos`
--

DROP TABLE IF EXISTS `veiculos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `veiculos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ano` int DEFAULT NULL,
  `cor` varchar(255) DEFAULT NULL,
  `disponivel` bit(1) DEFAULT NULL,
  `marca` varchar(255) DEFAULT NULL,
  `modelo` varchar(255) DEFAULT NULL,
  `placa` varchar(255) DEFAULT NULL,
  `valor_diaria` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `veiculos`
--

LOCK TABLES `veiculos` WRITE;
/*!40000 ALTER TABLE `veiculos` DISABLE KEYS */;
INSERT INTO `veiculos` VALUES (2,1996,'Preto',_binary '','Fiat','Uno','PAZ234',120),(3,2023,'Preto',_binary '','Honda','Civic','RUN5678',280),(4,2023,'Preto',_binary '','Honda','Civic','RUN5678',280);
/*!40000 ALTER TABLE `veiculos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-09  1:27:04

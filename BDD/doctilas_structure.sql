/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : doctilas

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 17/07/2023 04:34:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alumno
-- ----------------------------
DROP TABLE IF EXISTS `alumno`;
CREATE TABLE `alumno`  (
  `id_alumno` int NOT NULL AUTO_INCREMENT,
  `id_curso` int NOT NULL,
  `rut` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_alumno`) USING BTREE,
  INDEX `alumno_ibfk_1`(`id_curso` ASC) USING BTREE,
  INDEX `alumno_ibfk_2`(`rut` ASC) USING BTREE,
  CONSTRAINT `alumno_ibfk_1` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `alumno_ibfk_2` FOREIGN KEY (`rut`) REFERENCES `usuario` (`rut`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for archivo
-- ----------------------------
DROP TABLE IF EXISTS `archivo`;
CREATE TABLE `archivo`  (
  `id_archivo` int NOT NULL AUTO_INCREMENT,
  `enlace` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_asignatura` int NULL DEFAULT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_archivo`) USING BTREE,
  INDEX `archivo_ibfk_1`(`id_asignatura` ASC) USING BTREE,
  CONSTRAINT `archivo_ibfk_1` FOREIGN KEY (`id_asignatura`) REFERENCES `asignatura` (`id_asignatura`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for asignatura
-- ----------------------------
DROP TABLE IF EXISTS `asignatura`;
CREATE TABLE `asignatura`  (
  `id_asignatura` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_curso` int NOT NULL,
  PRIMARY KEY (`id_asignatura`) USING BTREE,
  INDEX `asignatura_ibfk_1`(`id_curso` ASC) USING BTREE,
  CONSTRAINT `asignatura_ibfk_1` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for carga_academica
-- ----------------------------
DROP TABLE IF EXISTS `carga_academica`;
CREATE TABLE `carga_academica`  (
  `id_carga_academica` int NOT NULL AUTO_INCREMENT,
  `id_asignatura` int NOT NULL,
  `rut` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_carga_academica`) USING BTREE,
  INDEX `carga_academica_ibfk_1`(`id_asignatura` ASC) USING BTREE,
  INDEX `carga_academica_ibfk_2`(`rut` ASC) USING BTREE,
  CONSTRAINT `carga_academica_ibfk_1` FOREIGN KEY (`id_asignatura`) REFERENCES `asignatura` (`id_asignatura`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `carga_academica_ibfk_2` FOREIGN KEY (`rut`) REFERENCES `usuario` (`rut`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ciudad
-- ----------------------------
DROP TABLE IF EXISTS `ciudad`;
CREATE TABLE `ciudad`  (
  `id_ciudad` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_pais` int NOT NULL,
  PRIMARY KEY (`id_ciudad`) USING BTREE,
  INDEX `ciudad_ibfk_1`(`id_pais` ASC) USING BTREE,
  CONSTRAINT `ciudad_ibfk_1` FOREIGN KEY (`id_pais`) REFERENCES `pais` (`id_pais`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comuna
-- ----------------------------
DROP TABLE IF EXISTS `comuna`;
CREATE TABLE `comuna`  (
  `id_comuna` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_ciudad` int NOT NULL,
  PRIMARY KEY (`id_comuna`) USING BTREE,
  INDEX `comuna_ibfk_1`(`id_ciudad` ASC) USING BTREE,
  CONSTRAINT `comuna_ibfk_1` FOREIGN KEY (`id_ciudad`) REFERENCES `ciudad` (`id_ciudad`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cuestionario
-- ----------------------------
DROP TABLE IF EXISTS `cuestionario`;
CREATE TABLE `cuestionario`  (
  `id_cuestionario` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `id_asignatura` int NOT NULL,
  `descripcion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_tipo_documento` int NULL DEFAULT NULL,
  PRIMARY KEY (`id_cuestionario`) USING BTREE,
  INDEX `cuestionario_ibfk_3`(`id_asignatura` ASC) USING BTREE,
  INDEX `cuestionario_ibfk_1`(`id_tipo_documento` ASC) USING BTREE,
  CONSTRAINT `cuestionario_ibfk_1` FOREIGN KEY (`id_tipo_documento`) REFERENCES `tipo_documento` (`id_tipo_documento`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cuestionario_ibfk_3` FOREIGN KEY (`id_asignatura`) REFERENCES `asignatura` (`id_asignatura`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cuestionario_pregunta
-- ----------------------------
DROP TABLE IF EXISTS `cuestionario_pregunta`;
CREATE TABLE `cuestionario_pregunta`  (
  `id_cuestionario_pregunta` int NOT NULL AUTO_INCREMENT,
  `id_pregunta` int NULL DEFAULT NULL,
  `id_cuestionario` int NULL DEFAULT NULL,
  PRIMARY KEY (`id_cuestionario_pregunta`) USING BTREE,
  INDEX `cuestionario_pregunta_fbk2`(`id_cuestionario` ASC) USING BTREE,
  INDEX `cuestionario_pregunta_fbk1`(`id_pregunta` ASC) USING BTREE,
  CONSTRAINT `cuestionario_pregunta_fbk1` FOREIGN KEY (`id_pregunta`) REFERENCES `pregunta` (`id_pregunta`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cuestionario_pregunta_fbk2` FOREIGN KEY (`id_cuestionario`) REFERENCES `cuestionario` (`id_cuestionario`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for curso
-- ----------------------------
DROP TABLE IF EXISTS `curso`;
CREATE TABLE `curso`  (
  `id_curso` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_curso`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for direccion
-- ----------------------------
DROP TABLE IF EXISTS `direccion`;
CREATE TABLE `direccion`  (
  `id_direccion` int NOT NULL AUTO_INCREMENT,
  `calle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `numeracion` int NOT NULL,
  `id_comuna` int NOT NULL,
  PRIMARY KEY (`id_direccion`) USING BTREE,
  INDEX `direccion_ibfk_1`(`id_comuna` ASC) USING BTREE,
  CONSTRAINT `direccion_ibfk_1` FOREIGN KEY (`id_comuna`) REFERENCES `comuna` (`id_comuna`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 AVG_ROW_LENGTH = 5461 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for nota
-- ----------------------------
DROP TABLE IF EXISTS `nota`;
CREATE TABLE `nota`  (
  `id_nota` int NOT NULL AUTO_INCREMENT,
  `rut` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nota` decimal(4, 2) NOT NULL,
  `id_cuestionario` int NULL DEFAULT NULL,
  PRIMARY KEY (`id_nota`) USING BTREE,
  INDEX `nota_ibfk_1`(`rut` ASC) USING BTREE,
  INDEX `nota_ibfk_2`(`id_cuestionario` ASC) USING BTREE,
  CONSTRAINT `nota_ibfk_1` FOREIGN KEY (`rut`) REFERENCES `usuario` (`rut`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `nota_ibfk_2` FOREIGN KEY (`id_cuestionario`) REFERENCES `cuestionario` (`id_cuestionario`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pais
-- ----------------------------
DROP TABLE IF EXISTS `pais`;
CREATE TABLE `pais`  (
  `id_pais` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_pais`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pregunta
-- ----------------------------
DROP TABLE IF EXISTS `pregunta`;
CREATE TABLE `pregunta`  (
  `id_pregunta` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_pregunta`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for respuesta
-- ----------------------------
DROP TABLE IF EXISTS `respuesta`;
CREATE TABLE `respuesta`  (
  `id_respuesta` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `estado` int NULL DEFAULT NULL,
  `id_pregunta` int NOT NULL,
  PRIMARY KEY (`id_respuesta`) USING BTREE,
  INDEX `respuesta_ibfk_1`(`id_pregunta` ASC) USING BTREE,
  CONSTRAINT `respuesta_ibfk_1` FOREIGN KEY (`id_pregunta`) REFERENCES `pregunta` (`id_pregunta`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tipo_documento
-- ----------------------------
DROP TABLE IF EXISTS `tipo_documento`;
CREATE TABLE `tipo_documento`  (
  `id_tipo_documento` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_tipo_documento`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tipo_usuario
-- ----------------------------
DROP TABLE IF EXISTS `tipo_usuario`;
CREATE TABLE `tipo_usuario`  (
  `id_tipo_usuario` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id_tipo_usuario`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 AVG_ROW_LENGTH = 5461 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for usuario
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario`  (
  `rut` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `apellido_paterno` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `apellido_materno` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_tipo_usuario` int NOT NULL,
  `usuario` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contrasena` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `correo_electronico` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_direccion` int NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `sexo` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`rut`) USING BTREE,
  INDEX `usuario_ibfk_1`(`id_tipo_usuario` ASC) USING BTREE,
  INDEX `usuario_ibfk_2`(`id_direccion` ASC) USING BTREE,
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_tipo_usuario`) REFERENCES `tipo_usuario` (`id_tipo_usuario`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usuario_ibfk_2` FOREIGN KEY (`id_direccion`) REFERENCES `direccion` (`id_direccion`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_ARCHIVO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_ARCHIVO`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_ARCHIVO`(penlace varchar(255))
BEGIN
	DELETE FROM archivo WHERE enlace=penlace;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_ASIGNATURA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_ASIGNATURA`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_ASIGNATURA`(pid int)
BEGIN
	DELETE FROM asignatura WHERE id_asignatura=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_CIUDAD
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_CIUDAD`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_CIUDAD`(pid int)
BEGIN
	DELETE FROM ciudad WHERE id_ciudad = pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_COMUNA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_COMUNA`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_COMUNA`(pid int)
BEGIN
	DELETE FROM comuna WHERE id_comuna = pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_CUESTIONARIO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_CUESTIONARIO`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_CUESTIONARIO`(pcuest INT)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE pregunta_id INT;
    DECLARE cur CURSOR FOR SELECT id_pregunta FROM cuestionario_pregunta WHERE id_cuestionario=pcuest;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    OPEN cur;

    procesar_loop: LOOP
        FETCH cur INTO pregunta_id;
        IF done THEN
            LEAVE procesar_loop;
        END IF;
				SELECT pregunta_id;
				DELETE FROM cuestionario_pregunta WHERE id_pregunta=pregunta_id and id_cuestionario=pcuest;
				DELETE FROM pregunta WHERE id_pregunta=pregunta_id;

    END LOOP;

    CLOSE cur;
		
		DELETE FROM cuestionario WHERE id_cuestionario=pcuest;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_CURSO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_CURSO`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_CURSO`(pid int)
BEGIN
  DELETE FROM curso WHERE id_curso=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_NOTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_NOTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_NOTA`(pid int)
BEGIN
	DELETE FROM nota WHERE id_nota = pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_PAIS
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_PAIS`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_PAIS`(pid int)
BEGIN
  DELETE FROM pais WHERE id_pais=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_RESPUESTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_RESPUESTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_RESPUESTA`(ppreg INT)
BEGIN
   DELETE FROM respuesta WHERE id_pregunta=ppreg;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_DELETE_TIPO_DOCUMENTO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_DELETE_TIPO_DOCUMENTO`;
delimiter ;;
CREATE PROCEDURE `spPRY_DELETE_TIPO_DOCUMENTO`(pid int)
BEGIN
  DELETE FROM tipo_documento WHERE id_tipo_documento=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ALUMNO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ALUMNO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ALUMNO`()
BEGIN
	SELECT
		a.rut,
		CONCAT( u.nombre, ' ', u.apellido_paterno, ' ', u.apellido_materno ) AS NombreCompleto,
		u.correo_electronico,
		concat(d.calle,' ',d.numeracion,', ',c.descripcion) as Direccion,
		u.sexo,
		cs.descripcion as CursoDescripcion,
		TIMESTAMPDIFF(YEAR, u.fecha_nacimiento, CURDATE()) AS Edad
	FROM
		alumno a
		INNER JOIN usuario u ON a.rut = u.rut
		INNER JOIN curso cs ON a.id_curso=cs.id_curso
		INNER JOIN direccion d ON u.id_direccion = d.id_direccion
		INNER JOIN comuna c ON d.id_comuna=c.id_comuna;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ALUMNOCUESTIONARIO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ALUMNOCUESTIONARIO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ALUMNOCUESTIONARIO`(pasig int, prut varchar(10))
BEGIN

	SELECT
		c.id_cuestionario,
		c.fecha,
		c.id_asignatura,
		c.descripcion,
		c.id_tipo_documento,
		td.descripcion as tipoDocumento,
    (SELECT id_nota FROM nota where rut=prut and id_cuestionario = (SELECT c.id_cuestionario)) as ids,
    IFNULL((SELECT ids),'') as id_nota,
    prut as rut,
		IFNULL((SELECT nota FROM nota where rut=prut and id_cuestionario = (SELECT c.id_cuestionario)),'') as nota,
		IF((SELECT ids) is null, 0, 1) AS est
	FROM cuestionario c LEFT JOIN nota n ON c.id_cuestionario = n.id_cuestionario 
	JOIN tipo_documento td ON c.id_tipo_documento = td.id_tipo_documento
	WHERE c.id_asignatura = pasig;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ALUMNODASH2
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ALUMNODASH2`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ALUMNODASH2`(pasig int, prut varchar(10))
BEGIN
  SELECT 
  COUNT(*) INTO @reprobadas
  FROM cuestionario c LEFT JOIN nota n ON c.id_cuestionario = n.id_cuestionario 
	JOIN tipo_documento td ON c.id_tipo_documento = td.id_tipo_documento
	WHERE c.id_asignatura = pasig  AND (SELECT nota FROM nota where rut=prut and id_cuestionario = (SELECT c.id_cuestionario)) BETWEEN 1.00 AND 4.00;

  SELECT 
  COUNT(*) INTO @aprobadas
  FROM cuestionario c LEFT JOIN nota n ON c.id_cuestionario = n.id_cuestionario 
	JOIN tipo_documento td ON c.id_tipo_documento = td.id_tipo_documento
	WHERE c.id_asignatura = pasig  AND (SELECT nota FROM nota where rut=prut and id_cuestionario = (SELECT c.id_cuestionario)) >4.00;

  SELECT
  COUNT(*) INTO @total
  FROM cuestionario c LEFT JOIN nota n ON c.id_cuestionario = n.id_cuestionario 
	JOIN tipo_documento td ON c.id_tipo_documento = td.id_tipo_documento
	WHERE c.id_asignatura = pasig;

	SET @pendientes = @total - (@aprobadas+@reprobadas);
			
	SELECT @pendientes AS pendientes, @aprobadas AS aprobadas, @reprobadas AS reprobadas;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ALUMNOxASIGNATURA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ALUMNOxASIGNATURA`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ALUMNOxASIGNATURA`(pidasig int)
BEGIN
SELECT
  rut INTO @rut
FROM carga_academica
WHERE id_asignatura = pidasig;

SELECT
  rut,
  CONCAT(nombre, ' ', apellido_paterno, ' ', apellido_materno) AS NombreCompleto
FROM usuario
WHERE rut = @rut;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ALUMNOxCURSO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ALUMNOxCURSO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ALUMNOxCURSO`(idc int)
BEGIN
	SELECT a.rut,CONCAT(nombre, ' ', apellido_paterno, ' ',apellido_materno) AS NombreCompleto FROM alumno a LEFT JOIN usuario u ON a.rut=u.rut WHERE a.id_curso=idc;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ARCHIVO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ARCHIVO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ARCHIVO`(pid int)
BEGIN
	SELECT id_archivo,enlace,nombre FROM archivo WHERE id_asignatura=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ARCHIVOCOUNT
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ARCHIVOCOUNT`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ARCHIVOCOUNT`(pid int)
BEGIN
	SELECT COUNT(*) AS cuenta FROM archivo WHERE id_asignatura=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ASIGCOUNT
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ASIGCOUNT`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ASIGCOUNT`(pid int)
BEGIN
	SELECT COUNT(*) AS cuenta FROM asignatura WHERE id_curso=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ASIGNATURA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ASIGNATURA`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ASIGNATURA`()
BEGIN
SELECT * FROM asignatura;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ASIGNATURAXID
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ASIGNATURAXID`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ASIGNATURAXID`(pidcurso int)
BEGIN
SELECT
  id_asignatura,
  descripcion
FROM asignatura
WHERE id_curso = pidcurso;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_ASIGNATURA_FULL
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_ASIGNATURA_FULL`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_ASIGNATURA_FULL`()
BEGIN
  SELECT a.id_asignatura, a.descripcion, c.id_curso, c.descripcion as curso_descripcion FROM asignatura a inner join curso c on a.id_curso=c.id_curso;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_CIUDAD
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_CIUDAD`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_CIUDAD`()
BEGIN
	SELECT c.id_ciudad,c.descripcion,p.id_pais,p.descripcion as paisDescripcion FROM ciudad c INNER JOIN pais p ON c.id_pais=p.id_pais;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_CIUDADxPAIS
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_CIUDADxPAIS`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_CIUDADxPAIS`(ppais int)
BEGIN
SELECT id_ciudad, descripcion,id_pais FROM ciudad WHERE id_pais=ppais;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_COMUNA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_COMUNA`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_COMUNA`()
BEGIN
	SELECT c.id_comuna,c.descripcion,ci.id_ciudad,ci.descripcion as ciudadDescripcion FROM comuna c INNER JOIN ciudad ci ON c.id_ciudad=ci.id_ciudad;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_COMUNAxCIUDAD
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_COMUNAxCIUDAD`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_COMUNAxCIUDAD`(pciudad int)
BEGIN
SELECT id_comuna, descripcion,id_ciudad FROM comuna WHERE id_ciudad=pciudad;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_COUNTNOTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_COUNTNOTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_COUNTNOTA`(pid int)
BEGIN
	SELECT COUNT(*) AS cuenta FROM nota WHERE id_cuestionario=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_CUESTIONARIO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_CUESTIONARIO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_CUESTIONARIO`()
BEGIN
SELECT
	c.id_cuestionario,
	c.fecha,
	c.id_asignatura,
	c.descripcion,
	c.id_tipo_documento,
	CONCAT(a.descripcion,'-',td.descripcion,'-',c.descripcion) AS detalle	
FROM cuestionario c LEFT JOIN asignatura a ON c.id_asignatura=a.id_asignatura
LEFT JOIN tipo_documento td ON c.id_tipo_documento=td.id_tipo_documento;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_CUESTIONARIOxASIG
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_CUESTIONARIOxASIG`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_CUESTIONARIOxASIG`(pasig int)
BEGIN
	
	SELECT c.id_cuestionario, CONCAT(td.descripcion, ' - ', c.descripcion) AS detalle FROM cuestionario c INNER JOIN tipo_documento td ON c.id_tipo_documento=td.id_tipo_documento WHERE id_asignatura=pasig;
	
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_CUESTIONARIOXID
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_CUESTIONARIOXID`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_CUESTIONARIOXID`(pcuest int)
BEGIN
SELECT
	c.id_cuestionario,
	c.fecha,
	c.id_asignatura,
	c.descripcion,
	c.id_tipo_documento,
	CONCAT(td.descripcion, ' - ', c.descripcion) AS detalle
FROM cuestionario c LEFT JOIN tipo_documento td ON c.id_tipo_documento = td.id_tipo_documento WHERE c.id_cuestionario = pcuest;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_CURSO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_CURSO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_CURSO`()
BEGIN
SELECT
  id_curso,
  descripcion
FROM curso;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_CURSOXASIG
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_CURSOXASIG`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_CURSOXASIG`(pid int)
BEGIN
  SELECT id_curso FROM asignatura WHERE id_asignatura = pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_DASHBOARD
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_DASHBOARD`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_DASHBOARD`(prut varchar(30))
BEGIN
  SELECT COUNT(*) INTO @tot FROM cuestionario c LEFT JOIN tipo_documento td ON c.id_tipo_documento=td.id_tipo_documento 
  LEFT JOIN asignatura a ON c.id_asignatura=a.id_asignatura RIGHT JOIN alumno al ON a.id_curso=al.id_curso WHERE al.rut=prut;

  SELECT c.id_tipo_documento, td.descripcion AS tipoDescripcion,COUNT(c.id_cuestionario) AS cuenta, @tot AS total, 
  CONCAT(ROUND(((count(c.id_cuestionario)*100)/@tot),2)) AS val,CONCAT(ROUND(((count(c.id_cuestionario)*100)/@tot),2),'%') AS porcentaje
  FROM cuestionario c LEFT JOIN tipo_documento td ON c.id_tipo_documento=td.id_tipo_documento LEFT JOIN asignatura a ON c.id_asignatura=a.id_asignatura RIGHT JOIN alumno al ON a.id_curso=al.id_curso WHERE al.rut=prut GROUP BY c.id_tipo_documento;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_DASHBOARDDOC
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_DASHBOARDDOC`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_DASHBOARDDOC`(pcurso int)
BEGIN
	IF pcurso = 0 THEN
		SELECT count(id_alumno) into @alumnos FROM alumno;
		SELECT @alumnos;
	ELSE
		SELECT count(id_alumno) into @alumnos FROM alumno WHERE id_curso=pcurso;
		SELECT @alumnos;
	END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_DOCUMENTO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_DOCUMENTO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_DOCUMENTO`()
BEGIN
	SELECT
		d.id_documento,
		d.descripcion,
		CONCAT(td.descripcion,' - ',d.descripcion) AS descripcionDetalle,
		d.fecha,
		d.id_tipo_documento,
		d.id_asignatura 
	FROM
		documento d
		LEFT JOIN tipo_documento td ON d.id_tipo_documento=td.id_tipo_documento;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_NOTAS
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_NOTAS`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_NOTAS`(prut varchar(10), pasig int)
BEGIN
	
	SELECT 
		id_nota,
		CONCAT(td.descripcion,' - ',c.descripcion) AS detalle,
		n.nota
	FROM nota n 
	INNER JOIN cuestionario c ON n.id_cuestionario=c.id_cuestionario INNER JOIN tipo_documento td ON c.id_tipo_documento=td.id_tipo_documento
	WHERE n.rut=prut AND c.id_asignatura=pasig;
	
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_PAIS
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_PAIS`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_PAIS`()
BEGIN
SELECT
  id_pais,
  descripcion
FROM pais;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_PREGUNTAXCUESTIONARIO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_PREGUNTAXCUESTIONARIO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_PREGUNTAXCUESTIONARIO`(pcuest INT)
BEGIN
   SELECT id_pregunta FROM cuestionario_pregunta WHERE id_cuestionario=pcuest;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_PREGUNTAXID
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_PREGUNTAXID`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_PREGUNTAXID`(pcuest INT)
BEGIN
	SELECT
		cp.id_cuestionario_pregunta,
		cp.id_cuestionario,
		cp.id_pregunta,
		p.descripcion
	FROM
		cuestionario_pregunta cp
	LEFT JOIN pregunta p ON cp.id_pregunta=p.id_pregunta
	WHERE
		cp.id_cuestionario = pcuest;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_RESPUESTAXID
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_RESPUESTAXID`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_RESPUESTAXID`(ppreg INT)
BEGIN
	SELECT
		id_respuesta,
		descripcion,
		estado,
		id_pregunta
	FROM
		respuesta 
	WHERE
		id_pregunta = ppreg;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_GET_TIPO_DOCUMENTO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_GET_TIPO_DOCUMENTO`;
delimiter ;;
CREATE PROCEDURE `spPRY_GET_TIPO_DOCUMENTO`()
BEGIN
	SELECT
		*
	FROM
		tipo_documento ;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_ARCHIVO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_ARCHIVO`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_ARCHIVO`(penlace varchar(255),pnom varchar(255),pid int)
BEGIN
	INSERT INTO archivo(enlace,nombre,id_asignatura) VALUES(penlace,pnom,pid);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_ASIGNATURA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_ASIGNATURA`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_ASIGNATURA`(pdesc varchar(50),pidc int)
BEGIN
	SELECT COUNT(*) INTO @existe FROM asignatura WHERE descripcion=pdesc AND id_curso=pidc;
	IF @existe<1 THEN
		INSERT INTO asignatura(descripcion,id_curso) values(pdesc,pidc);
    SELECT id_asignatura as rsp FROM asignatura WHERE descripcion=pdesc AND id_curso=pidc;
  ELSE
    SELECT 0 as rsp;
	END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_CIUDAD
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_CIUDAD`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_CIUDAD`(pdesc varchar(50),pidp int)
BEGIN
	SELECT COUNT(*) INTO @existe FROM ciudad WHERE descripcion=pdesc AND id_pais=pidp;
	IF @existe<1 THEN
		INSERT INTO ciudad(descripcion,id_pais) values(pdesc,pidp);
	END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_COMUNA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_COMUNA`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_COMUNA`(pdesc varchar(50),pidc int)
BEGIN
	SELECT COUNT(*) INTO @existe FROM comuna WHERE descripcion=pdesc AND id_ciudad=pidc;
	IF @existe<1 THEN
		INSERT INTO comuna(descripcion,id_ciudad) values(pdesc,pidc);
	END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_CUESTIONARIO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_CUESTIONARIO`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_CUESTIONARIO`(pasig int,pdesc varchar(100),ptipdoc int)
BEGIN
	set @hoy = CURDATE();
	INSERT INTO cuestionario(fecha,id_asignatura,descripcion,id_tipo_documento) values(@hoy,pasig,pdesc,ptipdoc);
	
	SELECT id_cuestionario FROM cuestionario WHERE fecha=@hoy and id_asignatura=pasig and descripcion=pdesc and id_tipo_documento=ptipdoc;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_CUESTIONARIOPREGUNTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_CUESTIONARIOPREGUNTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_CUESTIONARIOPREGUNTA`(ppreg int,pcuest int)
BEGIN
	
	INSERT INTO cuestionario_pregunta(id_pregunta,id_cuestionario) values(ppreg,pcuest);

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_CURSO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_CURSO`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_CURSO`(pdesc varchar(50))
BEGIN
	SELECT COUNT(*) INTO @existe FROM curso WHERE descripcion=pdesc;
	IF @existe<1 THEN
		INSERT INTO curso(descripcion) values(pdesc);
    SELECT id_curso AS rsp FROM curso WHERE descripcion=pdesc;
  ELSE
    SELECT 0 AS rsp;
	END IF;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_NOTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_NOTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_NOTA`(prut varchar(10), pnota float(4,2),pcuest int)
BEGIN

	INSERT INTO nota(rut,nota,id_cuestionario) VALUES(prut,pnota,pcuest);

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_PAIS
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_PAIS`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_PAIS`(pdesc varchar(40))
BEGIN
  SELECT COUNT(*) INTO @existe FROM pais WHERE descripcion=pdesc;
  IF @existe<1 THEN
    INSERT INTO pais(descripcion) VALUES(pdesc);
  END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_PREGUNTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_PREGUNTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_PREGUNTA`(pdesc varchar(100))
BEGIN
	SELECT COUNT(id_pregunta) into @existe from pregunta where descripcion=pdesc;
IF @existe=0 THEN
	INSERT INTO pregunta(descripcion) values(pdesc);
END IF;
	
	SELECT id_pregunta from pregunta where descripcion=pdesc;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_RESPUESTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_RESPUESTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_RESPUESTA`(pdesc varchar(100),pestado int,ppreg int)
BEGIN
	SELECT COUNT(*) INTO @existe FROM respuesta WHERE descripcion=pdesc AND id_pregunta=ppreg;
	IF @existe<1 THEN
		INSERT INTO respuesta(descripcion,estado,id_pregunta) values(pdesc,pestado,ppreg);
	END IF;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_INSERT_TIPO_DOCUMENTO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_INSERT_TIPO_DOCUMENTO`;
delimiter ;;
CREATE PROCEDURE `spPRY_INSERT_TIPO_DOCUMENTO`(pdesc varchar(40))
BEGIN
  SELECT COUNT(*) INTO @existe FROM tipo_documento WHERE descripcion=pdesc;
  IF @existe<1 THEN
    INSERT INTO tipo_documento(descripcion) VALUES(pdesc);
  END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_Login
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_Login`;
delimiter ;;
CREATE PROCEDURE `spPRY_Login`(puser varchar(255), pclave varchar(255))
BEGIN

DECLARE curs INT;
DECLARE rutt VARCHAR(10);

SELECT id_tipo_usuario, rut INTO @tipUs,rutt FROM usuario WHERE usuario=puser AND contrasena=MD5(pclave);

IF @tipUs = 3 THEN
	SELECT id_curso INTO curs FROM alumno WHERE rut=rutt;
ELSE
	SET curs=0;
END IF;

SELECT CONCAT(u.nombre, ' ', u.apellido_paterno) AS NombreCompleto, rut, tu.descripcion AS TipoUsuario,u.id_tipo_usuario, curs FROM usuario u JOIN tipo_usuario tu ON u.id_tipo_usuario = tu.id_tipo_usuario WHERE u.usuario=puser AND u.contrasena=MD5(pclave);
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_Register
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_Register`;
delimiter ;;
CREATE PROCEDURE `spPRY_Register`(pcalle varchar(200), pnum int, pcomuna int, prut varchar(10),
pnom varchar(50), papep varchar(50), papem varchar(50), pidtipouser int, ppass varchar(50), pcorreo varchar(100), pfecha date, curso int, psexo varchar(50))
BEGIN

DECLARE pdirec int;

SELECT
  COUNT(id_direccion) INTO @existe
FROM direccion
WHERE calle = pcalle
AND numeracion = pnum
AND id_comuna = pcomuna;

  IF @existe < 1 THEN
INSERT INTO direccion (calle, numeracion, id_comuna)
  VALUES (pcalle, pnum, pcomuna);
END IF;

SELECT
  d.id_direccion INTO pdirec FROM direccion d WHERE d.calle = pcalle AND d.numeracion = pnum AND d.id_comuna = pcomuna;

INSERT INTO usuario (rut,nombre,apellido_paterno,apellido_materno,id_tipo_usuario,usuario,contrasena,correo_electronico,id_direccion,fecha_nacimiento,sexo)
  VALUES (prut, pnom, papep, papem, pidtipouser, CONCAT(UPPER(pnom), '.', UPPER(papep)), MD5(ppass), pcorreo, pdirec, pfecha,psexo);
  IF pidtipouser = 3 THEN
INSERT INTO alumno (id_curso, Rut)
  VALUES (curso, prut);
END IF;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_ASIGNATURA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_ASIGNATURA`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_ASIGNATURA`(pid int, pdesc varchar(50),pidc int)
BEGIN
	UPDATE asignatura SET descripcion=pdesc, id_curso=pidc WHERE id_asignatura=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_CIUDAD
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_CIUDAD`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_CIUDAD`(pid int, pdesc varchar(50),pidp int)
BEGIN
	UPDATE ciudad SET descripcion=pdesc, id_pais=pidp WHERE id_ciudad=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_COMUNA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_COMUNA`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_COMUNA`(pid int, pdesc varchar(50),pidc int)
BEGIN
	UPDATE comuna SET descripcion=pdesc, id_ciudad=pidc WHERE id_comuna=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_CUESTIONARIO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_CUESTIONARIO`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_CUESTIONARIO`(pcuest int, pasig int,ptipdoc int, pdesc varchar(100))
BEGIN
	set @hoy = CURDATE();
	
	UPDATE cuestionario SET fecha=@hoy,id_asignatura=pasig,id_tipo_documento=ptipdoc,descripcion=pdesc WHERE id_cuestionario=pcuest;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_CURSO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_CURSO`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_CURSO`(pid int, pdesc varchar(50))
BEGIN
  UPDATE curso SET descripcion=pdesc WHERE id_curso=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_NOTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_NOTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_NOTA`(pid int, pnota decimal(4,2))
BEGIN
	
	UPDATE nota SET nota=pnota WHERE id_nota=pid;
	
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_PAIS
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_PAIS`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_PAIS`(pid int, pdesc varchar(40))
BEGIN
  UPDATE pais SET descripcion=pdesc WHERE id_pais=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_PREGUNTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_PREGUNTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_PREGUNTA`(ppreg int, pdesc varchar(100))
BEGIN
	
	UPDATE pregunta SET descripcion=pdesc WHERE id_pregunta=ppreg;
	
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_RESPUESTA
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_RESPUESTA`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_RESPUESTA`(presp int, pdesc varchar(100),pestado int)
BEGIN
	
	UPDATE respuesta SET descripcion = pdesc, estado = pestado WHERE id_respuesta = presp;

END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_UPDATE_TIPO_DOCUMENTO
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_UPDATE_TIPO_DOCUMENTO`;
delimiter ;;
CREATE PROCEDURE `spPRY_UPDATE_TIPO_DOCUMENTO`(pid int, pdesc varchar(40))
BEGIN
  UPDATE tipo_documento SET descripcion=pdesc WHERE id_tipo_documento=pid;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for spPRY_VALIDAR_RESPUESTAS
-- ----------------------------
DROP PROCEDURE IF EXISTS `spPRY_VALIDAR_RESPUESTAS`;
delimiter ;;
CREATE PROCEDURE `spPRY_VALIDAR_RESPUESTAS`(pid int)
BEGIN
	SELECT id_respuesta,estado,id_pregunta FROM respuesta WHERE estado=1 AND id_pregunta=pid;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;

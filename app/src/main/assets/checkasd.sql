BEGIN TRANSACTION;
CREATE TABLE USUARIOS_MONSTRUOS(
    id_user INTEGER,
    id_monster INTEGER,
    FOREIGN KEY(id_user) REFERENCES USUARIOS(id) ON DELETE CASCADE,
    FOREIGN KEY(id_monster) REFERENCES MONSTRUOS(id) ON DELETE CASCADE
);
INSERT INTO `USUARIOS_MONSTRUOS` VALUES ('1','1');
INSERT INTO `USUARIOS_MONSTRUOS` VALUES ('2','2');
INSERT INTO `USUARIOS_MONSTRUOS` VALUES ('1','2');
CREATE TABLE "USUARIOS" (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`nombre`	TEXT NOT NULL,
	`objectId`	TEXT NOT NULL,
	`monstruoactual`	INTEGER
);
INSERT INTO `USUARIOS` VALUES ('1','issam22','Ig1ihj48Gd','1');
INSERT INTO `USUARIOS` VALUES ('2','ISSAM2','MAIL2@MAIL.COM','1');
CREATE TABLE TEST (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,nombre varchar(40) ,
 casilla int, puntuacion float,monedas int,diamantes int,experiencia float,fecha DATE);
INSERT INTO `TEST` VALUES ('1','Test Matematicas 2+2','2','4.0','0','9','3.0','null');
INSERT INTO `TEST` VALUES ('2','Test Historia America','3','4.0','4','8','9.0','null');
CREATE TABLE RESPUESTAS (id_respuestas INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,respuesta varchar(120),
solucion boolean,id_preguntas int,FOREIGN KEY(id_preguntas) REFERENCES PREGUNTAS(id_preguntas));
INSERT INTO `RESPUESTAS` VALUES ('1','4','1','1');
INSERT INTO `RESPUESTAS` VALUES ('2','3','0','1');
INSERT INTO `RESPUESTAS` VALUES ('3','2','0','1');
INSERT INTO `RESPUESTAS` VALUES ('4','0','0','1');
INSERT INTO `RESPUESTAS` VALUES ('5','1500','0','2');
INSERT INTO `RESPUESTAS` VALUES ('6','1492','1','2');
INSERT INTO `RESPUESTAS` VALUES ('7','1491','0','2');
INSERT INTO `RESPUESTAS` VALUES ('8','ayer','0','2');
CREATE TABLE "PREGUNTAS" (
	`id_preguntas`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`titulo`	varchar(120) NOT NULL,
	`categoria`	varchar(25) NOT NULL,
	`imagen`	TEXT NOT NULL,
	`fid_preguntas`	int NOT NULL,
	`tipo`	TEXT,
	`Consejo`	TEXT
);
INSERT INTO `PREGUNTAS` VALUES ('1','¿Cuantos son 2+2?','Matematicas','mimonstruo1.png','1','texto','mas facil imposible joder
');
INSERT INTO `PREGUNTAS` VALUES ('2','¿En que año se descubrió America?','Historia','mimonstruo1','2','imagen','Ayer no fue, eso es seguro');
CREATE TABLE "MONSTRUOS_HABILIDADES" (
	`id_monstruo`	int,
	`id_habilidad`	int,
	`cantidad`	INTEGER
);
INSERT INTO `MONSTRUOS_HABILIDADES` VALUES ('1','1','1');
INSERT INTO `MONSTRUOS_HABILIDADES` VALUES ('2','1','3');
INSERT INTO `MONSTRUOS_HABILIDADES` VALUES ('1','2','3');
CREATE TABLE "MONSTRUOS" (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`nombre`	TEXT NOT NULL,
	`escomprado`	INTEGER NOT NULL,
	`monedas`	INTEGER NOT NULL,
	`diamantes`	INTEGER NOT NULL,
	`imagen`	TEXT
);
INSERT INTO `MONSTRUOS` VALUES ('1','monstruo1
','1','100','6','monstruito');
INSERT INTO `MONSTRUOS` VALUES ('2','monstruo2
','0','250','0','mimonstruo1');
INSERT INTO `MONSTRUOS` VALUES ('3','monstruo3','1','350','2','monstruodefault');
CREATE TABLE "HABILIDADES" (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`nombre`	TEXT NOT NULL,
	`imagen`	text NOT NULL
);
INSERT INTO `HABILIDADES` VALUES ('1','consejos','monstruito');
INSERT INTO `HABILIDADES` VALUES ('2','fiftyfifty','monstruito');
INSERT INTO `HABILIDADES` VALUES ('3','tiempo','monstruito');
COMMIT;

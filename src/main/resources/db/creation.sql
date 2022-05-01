CREATE TABLE recipes (
	id INT auto_increment NOT NULL,
	name varchar(255) NULL,
	image longblob NULL,
	insert_date DATETIME NOT NULL,
	update_date DATETIME NULL,
	CONSTRAINT recipes_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE ingredients (
	id INT auto_increment NOT NULL,
	name varchar(255) NOT NULL,
	insert_date DATETIME NOT NULL,
	update_date DATETIME NULL,
	CONSTRAINT ingredients_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE UNIQUE INDEX ingredients_name_IDX USING BTREE ON ingredients (name);

-- recipes_ingredients definition

CREATE TABLE `recipes_ingredients` (
  `recipe_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  PRIMARY KEY (`recipe_id`,`ingredient_id`),
  KEY `recipes_ingredients_FK_1` (`ingredient_id`),
  CONSTRAINT `recipes_ingredients_FK` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `recipes_ingredients_FK_1` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



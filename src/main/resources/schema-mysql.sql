CREATE TABLE `test`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(36) NOT NULL,
  `login` VARCHAR(39) NOT NULL,
  `full_name` VARCHAR(150) NULL,
  `birth_date` DATE NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE,
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE);
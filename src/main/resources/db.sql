SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema enterpriseDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `enterpriseDB` DEFAULT CHARACTER SET utf8 ;
USE `enterpriseDB` ;

-- -----------------------------------------------------
-- Table `enterpriseDB`.`role`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `enterpriseDB`.`role` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `role_status` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `role_status_UNIQUE` (`role_status` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `enterpriseDB`.`passport`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `enterpriseDB`.`passport` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `surname` VARCHAR(45) NOT NULL,
    `unique_num` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `passportId_UNIQUE` (`unique_num` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `enterpriseDB`.`user`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `enterpriseDB`.`user` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `password` VARCHAR(200) NOT NULL,
    `cash` DOUBLE NOT NULL,
    `affordable` TINYINT NOT NULL,
    `role_id` INT NOT NULL,
    `passport_id` INT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
    INDEX `fk_user_role_idx` (`role_id` ASC) VISIBLE,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
    INDEX `fk_user_passport1_idx` (`passport_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_role`
        FOREIGN KEY (`role_id`)
        REFERENCES `enterpriseDB`.`role` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `fk_user_passport1`
        FOREIGN KEY (`passport_id`)
        REFERENCES `enterpriseDB`.`passport` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `enterpriseDB`.`car`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `enterpriseDB`.`car` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `brand` VARCHAR(45) NOT NULL,
    `class` VARCHAR(45) NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `cost` DOUBLE NOT NULL,
    `used` TINYINT NOT NULL,
    `damaged` TINYINT NOT NULL,
    `repair_cost` DOUBLE NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `enterpriseDB`.`status`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `enterpriseDB`.`status` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `status_name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `status_name_UNIQUE` (`status_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `enterpriseDB`.`order`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `enterpriseDB`.`order` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `driver` TINYINT NOT NULL,
    `first_date` DATETIME NOT NULL,
    `last_date` DATETIME NOT NULL,
    `return_date` DATETIME NULL,
    `cost` DOUBLE NOT NULL,
    `car_id` INT NOT NULL,
    `status_id` INT NOT NULL,
    `passport_id` INT NOT NULL,
    `message` VARCHAR(200) NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_order_auto1_idx` (`car_id` ASC) VISIBLE,
    INDEX `fk_order_status1_idx` (`status_id` ASC) VISIBLE,
    INDEX `fk_order_passport1_idx` (`passport_id` ASC) VISIBLE,
    CONSTRAINT `fk_order_auto1`
        FOREIGN KEY (`car_id`)
        REFERENCES `enterpriseDB`.`car` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `fk_order_status1`
        FOREIGN KEY (`status_id`)
        REFERENCES `enterpriseDB`.`status` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `fk_order_passport1`
        FOREIGN KEY (`passport_id`)
        REFERENCES `enterpriseDB`.`passport` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `enterpriseDB`.`user_order`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `enterpriseDB`.`user_order` (
    `user_id` INT NOT NULL,
    `order_id` INT NOT NULL,
    PRIMARY KEY (`user_id`, `order_id`),
    INDEX `fk_user_has_order_order1_idx` (`order_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_has_order_user1`
        FOREIGN KEY (`user_id`)
        REFERENCES `enterpriseDB`.`user` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_user_has_order_order1`
        FOREIGN KEY (`order_id`)
        REFERENCES `enterpriseDB`.`order` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- Create role
-- INSERT INTO role (id, role_status) VALUES (default, "admin");
-- INSERT INTO role (id, role_status) VALUES (default, "manager");
-- INSERT INTO role (id, role_status) VALUES (default, "driver");
-- INSERT INTO role (id, role_status) VALUES (default, "client");

-- Create status
-- INSERT INTO status (id, status_name) VALUES (default, "in process");
-- INSERT INTO status (id, status_name) VALUES (default, "accepted");
-- INSERT INTO status (id, status_name) VALUES (default, "denied");
-- INSERT INTO status (id, status_name) VALUES (default, "finished");

-- Create admin
-- INSERT INTO user (id, login, email, password, cash, affordable, role_id) VALUES (DEFAULT, "admin", "admin@gmail.com", "�iv�A���M�߱g��s�K��o*�H�", 0, true, 1);



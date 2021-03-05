-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema cmpe172
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cmpe172` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `cmpe172` ;

-- -----------------------------------------------------
-- Table `cmpe172`.`person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmpe172`.`person` ;

CREATE TABLE IF NOT EXISTS `cmpe172`.`person` (
  `person_id` INT NOT NULL,
  `first_name` VARCHAR(80) NULL,
  `last_name` VARCHAR(80) NULL,
  `birth_date` DATETIME NULL,
  `death_date` DATETIME NULL,
  PRIMARY KEY (`person_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cmpe172`.`awards`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmpe172`.`awards` ;

CREATE TABLE IF NOT EXISTS `cmpe172`.`awards` (
  `award_id` INT NOT NULL,
  `award_name` VARCHAR(80) NULL,
  `awarded_by` VARCHAR(80) NULL,
  PRIMARY KEY (`award_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cmpe172`.`person_awards`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmpe172`.`person_awards` ;

CREATE TABLE IF NOT EXISTS `cmpe172`.`person_awards` (
  `person_id` INT NOT NULL,
  `award_id` INT NOT NULL,
  `awarded_year` DATETIME NULL,
  PRIMARY KEY (`person_id`, `award_id`),
  INDEX `fk_PERSON_AWARDS_AWARDS1_idx` (`award_id` ASC),
  CONSTRAINT `fk_PERSON_AWARDS_PERSON1`
    FOREIGN KEY (`person_id`)
    REFERENCES `cmpe172`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PERSON_AWARDS_AWARDS1`
    FOREIGN KEY (`award_id`)
    REFERENCES `cmpe172`.`awards` (`award_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cmpe172`.`contribs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cmpe172`.`contribs` ;

CREATE TABLE IF NOT EXISTS `cmpe172`.`contribs` (
  `contrib_id` INT NOT NULL,
  `person_id` INT NOT NULL,
  `contribution` VARCHAR(80) NULL,
  PRIMARY KEY (`contrib_id`, `person_id`),
  INDEX `fk_CONTRIBS_PERSON_idx` (`person_id` ASC),
  CONSTRAINT `fk_CONTRIBS_PERSON`
    FOREIGN KEY (`person_id`)
    REFERENCES `cmpe172`.`person` (`person_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
